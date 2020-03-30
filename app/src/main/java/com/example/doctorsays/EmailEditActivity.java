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

public class EmailEditActivity extends AppCompatActivity {
    FirebaseUser user;
    DatabaseReference databaseReference;
    EditText emailEditText;
    Button saveButton;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_edit);
        getSupportActionBar().setTitle("Edit Email");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = dataSnapshot.getValue(Users.class);

                emailEditText = findViewById(R.id.emailEditText);
                emailEditText.setText(users.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailEditText.getText().toString().isEmpty()) {
                    if (emailEditText.getText().toString().toLowerCase().matches("^[a-zA-Z0-9_+&*-]+(?:\\."+
                            "[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                            "A-Z]{2,7}$")) {
                        databaseReference.child(user.getUid()).child("email").setValue(emailEditText.getText().toString());
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("public_user_data");
                        reference.child(user.getUid()).child("email").setValue(emailEditText.getText().toString());
                        Toast.makeText(EmailEditActivity.this, "Updated Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(EmailEditActivity.this, "Email not Valid. Please Verify", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(EmailEditActivity.this, "Email not Valid. Please Verify", Toast.LENGTH_LONG).show();
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
        EmailEditActivity.this.finish();
    }
}
