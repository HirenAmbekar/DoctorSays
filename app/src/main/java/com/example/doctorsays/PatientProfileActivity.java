package com.example.doctorsays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientProfileActivity extends AppCompatActivity {
    ImageView profileImageView;
    TextView nameTextView, nameCardTextView, ageTextView, ageCardTextView, sexTextView, sexCardTextView, addressCardTextView, phoneCardTextView, emailCardTextView, bloodGroupCardTextView;
    Button phoneCallButton, emailSendButton;
    PublicUser publicUser;
    String publicUserID;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        publicUserID = getIntent().getExtras().getString("id");


        nameTextView = findViewById(R.id.nameTextView);
        ageTextView = findViewById(R.id.ageTextView);
        sexTextView = findViewById(R.id.sexTextView);
        nameCardTextView = findViewById(R.id.nameCardTextView);
        addressCardTextView = findViewById(R.id.addressCardTextView);
        phoneCardTextView = findViewById(R.id.phoneCardTextView);
        emailCardTextView = findViewById(R.id.emailCardTextView);
        sexCardTextView = findViewById(R.id.sexCardTextView);
        ageCardTextView = findViewById(R.id.ageCardTextView);
        bloodGroupCardTextView = findViewById(R.id.bloodGroupCardTextView);
        profileImageView = findViewById(R.id.profilePictureImageView);
        phoneCallButton = findViewById(R.id.phoneCallButton);
        emailSendButton = findViewById(R.id.emailSendButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("public_user_data");
        databaseReference.child(publicUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                publicUser = dataSnapshot.getValue(PublicUser.class);

                nameTextView.setText(publicUser.getName());
                ageTextView.setText(publicUser.getAge());
                sexTextView.setText(publicUser.getSex());
                nameCardTextView.setText(publicUser.getName());
                addressCardTextView.setText(publicUser.getAddress());
                phoneCardTextView.setText(publicUser.getPhoneNumber());
                emailCardTextView.setText(publicUser.getEmail());
                sexCardTextView.setText(publicUser.getSex());
                ageCardTextView.setText(publicUser.getAge());
                bloodGroupCardTextView.setText(publicUser.getBloodGroup());
                Glide.with(getApplicationContext()).load(Uri.parse(publicUser.getPhotoUrl())).apply(RequestOptions.circleCropTransform()).into(profileImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        phoneCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + publicUser.getPhoneNumber()));
                startActivity(intent);
            }
        });

        emailSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                /* Fill it with Data */
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{publicUser.getEmail()});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mail from your Doctor");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "This is to notify you that.....");

                /* Send it off to the Activity-Chooser */
                PatientProfileActivity.this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
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
        PatientProfileActivity.this.finish();
    }


}
