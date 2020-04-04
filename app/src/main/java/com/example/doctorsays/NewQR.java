package com.example.doctorsays;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class NewQR extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    DatabaseReference databaseReference;
    PublicUser publicUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        if (ContextCompat.checkSelfPermission(NewQR.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(NewQR.this, "Permission is granted", Toast.LENGTH_LONG).show();
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This and that")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(NewQR.this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(NewQR.this, "Permission Granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(NewQR.this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(NewQR.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (scannerView == null) {
                scannerView = new ZXingScannerView(this);
                setContentView(scannerView);
            }
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }else {
            requestPermission();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        final String scanResult = result.getText();

        databaseReference = FirebaseDatabase.getInstance().getReference("public_user_data");
        databaseReference.child(scanResult).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                publicUser = dataSnapshot.getValue(PublicUser.class);

                final AlertDialog.Builder builder = new AlertDialog.Builder(NewQR.this);
                builder.setTitle("Scan Result");
                builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                        reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("patients").child(scanResult).setValue(scanResult);
                        startActivity(new Intent(NewQR.this, Home.class));
                        NewQR.this.finishAffinity();
                    }
                });
                builder.setNeutralButton("END", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(NewQR.this, Home.class));
                        NewQR.this.finishAffinity();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scannerView.resumeCameraPreview(NewQR.this);
                    }
                });
                builder.setMessage(publicUser.getName() + "\n" + publicUser.getAge() + "\n" + publicUser.getSex());
                AlertDialog alert = builder.create();
                alert.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Scan Result");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                scannerView.resumeCameraPreview(NewQR.this);
//            }
//        });
//        builder.setNeutralButton("Add", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                databaseReference = FirebaseDatabase.getInstance().getReference("users");
//                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("patients").child(scanResult).setValue(users);
//                NewQR.this.finish();
//            }
//        });
//        builder.setMessage(users.getName());
//        AlertDialog alert = builder.create();
//        alert.show();
    }
}
