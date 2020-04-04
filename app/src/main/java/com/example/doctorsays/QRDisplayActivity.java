package com.example.doctorsays;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRDisplayActivity extends AppCompatActivity {

    FirebaseUser user;
    DatabaseReference databaseReference;

    ImageView qrImageView, profilePictureImageView, profilePictureBackground;
    TextView nameTextView, idTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_q_r_display);

        user = FirebaseAuth.getInstance().getCurrentUser();
        qrImageView = findViewById(R.id.qrImageView);


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(user.getUid(), BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);

                profilePictureImageView = findViewById(R.id.profilePictureImageView);
                profilePictureBackground = findViewById(R.id.profilePictureBackground);
                nameTextView = findViewById(R.id.nameTextView);
                idTextView = findViewById(R.id.idTextView);

                assert users != null;
                nameTextView.setText(users.getName());
                idTextView.setText(users.getId());
                Glide.with(QRDisplayActivity.this).load(Uri.parse(users.getPhotoUrl())).apply(RequestOptions.circleCropTransform()).into(profilePictureImageView);
                profilePictureBackground.setAlpha(0.65f);
                profilePictureImageView.setAlpha(0.65f);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 1;
        getWindow().setAttributes(layoutParams);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 255;
        getWindow().setAttributes(layoutParams);
        QRDisplayActivity.this.finish();
    }
}
