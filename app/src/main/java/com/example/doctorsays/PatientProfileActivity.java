package com.example.doctorsays;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class PatientProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        String id = getIntent().getExtras().getString("id");

        TextView textView = findViewById(R.id.textView12);
        textView.setText(id);
    }
}
