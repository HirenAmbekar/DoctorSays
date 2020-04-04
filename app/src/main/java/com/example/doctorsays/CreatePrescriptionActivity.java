package com.example.doctorsays;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class CreatePrescriptionActivity extends AppCompatActivity {

    private static final int RECOGNISER_RESULT = 1;

    FloatingActionButton startSpeechRecognizerButton, createPDFButton;
    TextView nameTextView, ageTextView, sexTextView, bloodGroupTextView;
    EditText symptomsEditText, diagnosisEditText, prescriptionEditText, adviceEditText;
    EditText selectedEditText;
    Spinner languageSpinner;
    FirebaseUser firebaseUser;
    PublicUser users;
    DatabaseReference databaseReference;
    String publicUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_prescription);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create Prescription");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        publicUserID = Objects.requireNonNull(getIntent().getExtras()).getString("id");


        createPDFButton = findViewById(R.id.createPDFButton);
        startSpeechRecognizerButton = findViewById(R.id.startSpeechRecognizerButton);
        nameTextView = findViewById(R.id.nameTextView);
        ageTextView = findViewById(R.id.ageTextView);
        sexTextView = findViewById(R.id.sexTextView);
        bloodGroupTextView = findViewById(R.id.bloodGroupTextView);
        symptomsEditText = findViewById(R.id.symptomsEditText);
        diagnosisEditText = findViewById(R.id.diagnosisEditText);
        prescriptionEditText = findViewById(R.id.prescriptionEditText);
        adviceEditText = findViewById(R.id.adviceEditText);
        languageSpinner = findViewById(R.id.languageSpinner);



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("public_user_data");
        databaseReference.child(publicUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = dataSnapshot.getValue(PublicUser.class);
                assert users != null;
                nameTextView.setText(users.getName());
                ageTextView.setText(users.getAge());
                sexTextView.setText(users.getSex());
                bloodGroupTextView.setText(users.getBloodGroup());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(CreatePrescriptionActivity.this,
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.Languages));
        //spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        languageSpinner.setAdapter(spinnerAdapter);

        startSpeechRecognizerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedLang;

                if (languageSpinner.getSelectedItemPosition() == 0) {
                    selectedLang = "en-US";
                }
                else if (languageSpinner.getSelectedItemPosition() == 1) {
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


        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        createPDFButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createPDFFile(Common.getAppPath(CreatePrescriptionActivity.this) + "test_pdf.pdf");
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        symptomsEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedEditText = symptomsEditText;
            }
        });

        diagnosisEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedEditText = diagnosisEditText;
            }
        });

        prescriptionEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedEditText = prescriptionEditText;
            }
        });

        adviceEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedEditText = adviceEditText;
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
                selectedEditText.append(matches.get(0));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createPDFFile(String path) {
        if (new File(path).exists()) {
            new File(path).delete();
        }

        try {
            Document document = new Document();
            //save
            PdfWriter.getInstance(document, new FileOutputStream(path));
            //open to write
            document.open();
            //setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("DoctorSaysApp");
            document.addCreator("UserDoctor");

            //Font Setting
            BaseColor colorAccent = new BaseColor(0, 153, 204,255);
            float fontSize = 20.0f;
            float valueFontSize = 26.0f;

            //Custom Font
            BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);

            //Create Title of Document
            Font titleFont = new Font(fontName, 36.0f, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "Order Details", Element.ALIGN_CENTER, titleFont);

            //Add more
            Font orderNumberFont = new Font(fontName, fontSize, Font.NORMAL, colorAccent);
            addNewItem(document, "Order No: ", Element.ALIGN_LEFT, orderNumberFont);

            Font orderNumberValueFont = new Font(fontName, valueFontSize, Font.NORMAL, BaseColor.BLACK);
            addNewItem(document, "#717171", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeparator(document);

            addNewItem(document, "Order Date", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, "3/8/2019", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeparator(document);

            addNewItem(document, "Account Name", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, "Hiren", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeparator(document);

            //Add Product order detail
            addLineSpace(document);
            addNewItem(document, "Product Detail", Element.ALIGN_CENTER, titleFont);
            addLineSeparator(document);

            //Item 1
            addNewItemWithLeftAndRight(document, "Pizza 25", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleFont, orderNumberValueFont);

            addLineSeparator(document);

            //Item 2
            addNewItemWithLeftAndRight(document, "Pizza 26", "(0.0%)", titleFont, orderNumberValueFont);
            addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleFont, orderNumberValueFont);

            addLineSeparator(document);

            //Total
            addLineSpace(document);
            addLineSpace(document);

            addNewItemWithLeftAndRight(document, "Total", "24000", titleFont, orderNumberValueFont);

            document.close();

            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
            
            printPDF();



        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(Common.getAppPath(CreatePrescriptionActivity.this) + "test_pdf.pdf");
            assert printManager != null;
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
        }catch (Exception ex) {
            Log.e("DoctorSays", ""+ex.getMessage());
        }
    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk(textLeft, textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight, textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);
    }

    private void addLineSeparator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
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
        CreatePrescriptionActivity.this.finish();
    }
}
