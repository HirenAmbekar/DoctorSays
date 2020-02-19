package com.example.doctorsays;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageButton button;
    EditText editText;
    Spinner spinner;
    EditText symptoms, diagnosis, prescription, advice;
    EditText selectedEditText;

    private static final int RECOGNISER_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button1);
        editText = findViewById(R.id.editText1);
        spinner = findViewById(R.id.spinner1);
        symptoms = findViewById(R.id.symptomsText);
        diagnosis = findViewById(R.id.diagnosisText);
        prescription = findViewById(R.id.prescriptionText);
        advice = findViewById(R.id.adviceText);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.Languages));
        //spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedLang;

                if (spinner.getSelectedItemPosition() == 0) {
                    selectedLang = "en-US";
                }
                else if (spinner.getSelectedItemPosition() == 1) {
                    selectedLang = "hi-IN";
                }
                else selectedLang = "mr-IN";

                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, selectedLang);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak");
                startActivityForResult(speechIntent, RECOGNISER_RESULT);
            }
        });

        symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedEditText = symptoms;
            }
        });

        diagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedEditText = diagnosis;
            }
        });

        prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedEditText = prescription;
            }
        });

        advice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedEditText = advice;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RECOGNISER_RESULT) {
            ArrayList<String> matches = null;
            if (data != null) {
                matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            }
            if (matches != null) {
                selectedEditText.setText(matches.get(0));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //abcd


}
