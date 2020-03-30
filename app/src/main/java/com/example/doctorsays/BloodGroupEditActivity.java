package com.example.doctorsays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BloodGroupEditActivity extends AppCompatActivity {
    FirebaseUser user;
    DatabaseReference databaseReference;
    Spinner bloodSpinner;
    Button saveButton;
    Users users;
    String[] bloodList = {"Not Selected", "AB+", "AB-", "A+", "A-", "B+", "B-", "O+", "0-", "Not Known"};
    String selectedBloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_group_edit);
        getSupportActionBar().setTitle("Edit Blood Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bloodSpinner = findViewById(R.id.bloodGroupSpinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, bloodList);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        bloodSpinner.setAdapter(arrayAdapter);

        bloodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBloodGroup = bloodList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = dataSnapshot.getValue(Users.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedBloodGroup != null && !selectedBloodGroup.equals("Not Selected")) {
                    databaseReference.child(user.getUid()).child("bloodGroup").setValue(selectedBloodGroup);
                    if (users.isBloodGroupVisible()) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("public_user_data");
                        reference.child(user.getUid()).child("bloodGroup").setValue(selectedBloodGroup);
                    }
                    Toast.makeText(BloodGroupEditActivity.this, "Updated Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(BloodGroupEditActivity.this, "Please select you Blood Group", Toast.LENGTH_LONG).show();
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
        BloodGroupEditActivity.this.finish();
    }
}
