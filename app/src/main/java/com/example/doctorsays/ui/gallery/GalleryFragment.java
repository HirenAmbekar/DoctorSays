package com.example.doctorsays.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.doctorsays.AddressEditActivity;
import com.example.doctorsays.AgeEditActivity;
import com.example.doctorsays.BloodGroupEditActivity;
import com.example.doctorsays.EmailEditActivity;
import com.example.doctorsays.NameEditActivity;
import com.example.doctorsays.PhoneEditActivity;
import com.example.doctorsays.QRDisplayActivity;
import com.example.doctorsays.R;
import com.example.doctorsays.SexEditActivity;
import com.example.doctorsays.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GalleryFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Button showQRButton;
    private Users userDatabase;
    private DatabaseReference databaseReference;
    private TextView emailText, displayText, idText, nameCard, emailCard, phoneCard, addressCard, ageCard, sexCard, bloodGroupCard;
    private ImageView profilePicImage;
    private Switch phoneSwitch, addressSwitch, ageSwitch, sexSwitch, bloodGroupSwitch;
    private Button nameEditButton, emailEditButton, addressEditButton, phoneEditButton, ageEditButton, sexEditButton, bloodGroupEditButton;
    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        emailText = root.findViewById(R.id.textView13);
        displayText = root.findViewById(R.id.displayNameTextView);
        idText = root.findViewById(R.id.idTextView);
        profilePicImage = root.findViewById(R.id.profilePictureImageView);
        showQRButton = root.findViewById(R.id.showQRButton);

        nameCard = root.findViewById(R.id.nameCardTextView);
        emailCard = root.findViewById(R.id.emailCardTextView);
        phoneCard = root.findViewById(R.id.phoneCardTextView);
        addressCard = root.findViewById(R.id.addressCardTextView);
        ageCard = root.findViewById(R.id.ageCardTextView);
        sexCard = root.findViewById(R.id.sexCardTextView);
        bloodGroupCard = root.findViewById(R.id.bloodGroupCardTextView);

        phoneSwitch = root.findViewById(R.id.phoneNumberSwitch);
        addressSwitch = root.findViewById(R.id.addressSwitch);
        ageSwitch = root.findViewById(R.id.ageSwitch);
        sexSwitch = root.findViewById(R.id.sexSwitch);
        bloodGroupSwitch = root.findViewById(R.id.bloodGroupSwitch);

        nameEditButton = root.findViewById(R.id.nameEditButton);
        emailEditButton = root.findViewById(R.id.emailSendButton);
        addressEditButton = root.findViewById(R.id.addressEditButton);
        phoneEditButton = root.findViewById(R.id.phoneCallButton);
        ageEditButton = root.findViewById(R.id.ageEditButton);
        sexEditButton = root.findViewById(R.id.sexEditButton);
        bloodGroupEditButton = root.findViewById(R.id.bloodGroupEditButton);

        progressBar = root.findViewById(R.id.galleryProgressBar);

        mAuth = FirebaseAuth.getInstance();

        //User details
        user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);

                userDatabase = dataSnapshot.getValue(Users.class);

                if (userDatabase != null) {
                    String name = userDatabase.getName();
                    String email = userDatabase.getEmail();
                    String id = userDatabase.getId();
                    Uri profilePic = Uri.parse(userDatabase.getPhotoUrl());
                    String phone = userDatabase.getPhoneNumber();
                    String address = userDatabase.getAddress();
                    String age = userDatabase.getAge();
                    String sex = userDatabase.getSex();
                    String bloodGroup = userDatabase.getBloodGroup();
                    boolean phoneVisible = userDatabase.isPhoneVisible();
                    boolean addressVisible = userDatabase.isAddressVisible();
                    boolean ageVisible = userDatabase.isAgeVisible();
                    boolean sexVisible = userDatabase.isSexVisible();
                    boolean bloodGroupVisible = userDatabase.isBloodGroupVisible();

                    emailText.setText(email);
                    displayText.setText(name);
                    idText.setText(id);
                    Glide.with(root.getContext()).load(profilePic).apply(RequestOptions.circleCropTransform()).into(profilePicImage);

                    nameCard.setText(name);
                    emailCard.setText(email);
                    phoneCard.setText(phone);
                    addressCard.setText(address);
                    ageCard.setText(age);
                    sexCard.setText(sex);
                    bloodGroupCard.setText(bloodGroup);

                    phoneSwitch.setChecked(phoneVisible);
                    addressSwitch.setChecked(addressVisible);
                    ageSwitch.setChecked(ageVisible);
                    sexSwitch.setChecked(sexVisible);
                    bloodGroupSwitch.setChecked(bloodGroupVisible);
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Not completed", Toast.LENGTH_LONG).show();
            }
        });


        showQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), QRDisplayActivity.class));
            }
        });


        phoneSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("public_user_data");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                if (phoneSwitch.isChecked()) {
                    reference.child(user.getUid()).child("phoneNumber").setValue(userDatabase.getPhoneNumber());
                    ref.child(user.getUid()).child("phoneVisible").setValue(true);
                } else {
                    reference.child(user.getUid()).child("phoneNumber").setValue("Not Visible");
                    ref.child(user.getUid()).child("phoneVisible").setValue(false);
                }
            }
        });

        addressSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("public_user_data");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                if (addressSwitch.isChecked()) {
                    reference.child(user.getUid()).child("address").setValue(userDatabase.getAddress());
                    ref.child(user.getUid()).child("addressVisible").setValue(true);
                } else {
                    reference.child(user.getUid()).child("address").setValue("Not Visible");
                    ref.child(user.getUid()).child("addressVisible").setValue(false);
                }
            }
        });

        ageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("public_user_data");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                if (ageSwitch.isChecked()) {
                    reference.child(user.getUid()).child("age").setValue(userDatabase.getAge());
                    ref.child(user.getUid()).child("ageVisible").setValue(true);
                } else {
                    reference.child(user.getUid()).child("age").setValue("Not Visible");
                    ref.child(user.getUid()).child("ageVisible").setValue(false);
                }
            }
        });

        sexSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("public_user_data");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                if (sexSwitch.isChecked()) {
                    reference.child(user.getUid()).child("sex").setValue(userDatabase.getSex());
                    ref.child(user.getUid()).child("sexVisible").setValue(true);
                } else {
                    reference.child(user.getUid()).child("sex").setValue("Not Visible");
                    ref.child(user.getUid()).child("sexVisible").setValue(false);
                }
            }
        });

        bloodGroupSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("public_user_data");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                if (bloodGroupSwitch.isChecked()) {
                    reference.child(user.getUid()).child("bloodGroup").setValue(userDatabase.getBloodGroup());
                    ref.child(user.getUid()).child("bloodGroupVisible").setValue(true);
                } else {
                    reference.child(user.getUid()).child("bloodGroup").setValue("Not Visible");
                    ref.child(user.getUid()).child("bloodGroupVisible").setValue(false);
                }
            }
        });


        //Edit section
        nameEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NameEditActivity.class));
            }
        });

        emailEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EmailEditActivity.class));
            }
        });

        phoneEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PhoneEditActivity.class));
            }
        });

        addressEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddressEditActivity.class));
            }
        });

        ageEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AgeEditActivity.class));
            }
        });

        sexEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SexEditActivity.class));
            }
        });

        bloodGroupEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BloodGroupEditActivity.class));
            }
        });

        return root;
    }
}