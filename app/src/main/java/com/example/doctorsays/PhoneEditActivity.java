package com.example.doctorsays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PhoneEditActivity extends AppCompatActivity {
    FirebaseUser user;
    DatabaseReference databaseReference;
    EditText phoneEditText;
    Button saveButton;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_edit);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Phone Number");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = dataSnapshot.getValue(Users.class);

                phoneEditText = findViewById(R.id.phoneEditText);
                phoneEditText.setText(users.getPhoneNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phoneEditText.getText().toString().isEmpty()) {
                    if (phoneEditText.getText().toString().matches("[0-9]*") && phoneEditText.getText().toString().length() == 10) {
                        databaseReference.child(user.getUid()).child("phoneNumber").setValue(phoneEditText.getText().toString());
                        if (users.isPhoneVisible()) {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("public_user_data");
                            reference.child(user.getUid()).child("phoneNumber").setValue(phoneEditText.getText().toString());
                        }
                        Toast.makeText(PhoneEditActivity.this, "Updated Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PhoneEditActivity.this, "Phone Number not Valid. Please Verify", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(PhoneEditActivity.this, "Phone Number not Valid. Please Verify", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// Respond to the action bar's Up/Home button
            //NavUtils.navigateUpFromSameTask(this);
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PhoneEditActivity.this.finish();
    }
}
