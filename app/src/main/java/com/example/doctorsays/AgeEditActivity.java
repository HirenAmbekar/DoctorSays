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

public class AgeEditActivity extends AppCompatActivity {
    FirebaseUser user;
    DatabaseReference databaseReference;
    EditText ageEditText;
    Button saveButton;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_edit);
        getSupportActionBar().setTitle("Edit Age");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = dataSnapshot.getValue(Users.class);

                ageEditText = findViewById(R.id.ageEditText);
                ageEditText.setText(users.getAge());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ageEditText.getText().toString().isEmpty()) {
                    if (ageEditText.getText().toString().matches("[0-9]*")) {
                        databaseReference.child(user.getUid()).child("age").setValue(ageEditText.getText().toString());
                        if (users.isAgeVisible()) {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("public_user_data");
                            reference.child(user.getUid()).child("age").setValue(ageEditText.getText().toString());
                        }
                        Toast.makeText(AgeEditActivity.this, "Updated Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AgeEditActivity.this, "Age not Valid. Please Verify", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AgeEditActivity.this, "Age not Valid. Please Verify", Toast.LENGTH_LONG).show();
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
        AgeEditActivity.this.finish();
    }
}
