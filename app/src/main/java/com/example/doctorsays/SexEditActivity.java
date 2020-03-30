package com.example.doctorsays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SexEditActivity extends AppCompatActivity {
    FirebaseUser user;
    DatabaseReference databaseReference;
    Spinner sexSpinner;
    Button saveButton;
    Users users;
    String[] sexList = {"Not Selected", "Male", "Female", "Other"};
    String selectedSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex_edit);
        getSupportActionBar().setTitle("Edit Sex");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sexSpinner = findViewById(R.id.sexSpinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, sexList);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sexSpinner.setAdapter(arrayAdapter);

        sexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSex = sexList[position];
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
                if (selectedSex != null && !selectedSex.equals("Not Selected")) {
                    databaseReference.child(user.getUid()).child("sex").setValue(selectedSex);
                    if (users.isSexVisible()) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("public_user_data");
                        reference.child(user.getUid()).child("sex").setValue(selectedSex);
                    }
                    Toast.makeText(SexEditActivity.this, "Updated Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SexEditActivity.this, "Please select you sex", Toast.LENGTH_LONG).show();
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
        SexEditActivity.this.finish();
    }
}
