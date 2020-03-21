package com.example.doctorsays.ui.gallery;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.doctorsays.QRDisplayActivity;
import com.example.doctorsays.R;
import com.example.doctorsays.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GalleryFragment extends Fragment {

    ImageView profilePicture;

    FirebaseAuth mAuth;
    FirebaseUser user;
    Button showQRButton;
    Users userDatabase;
    DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        mAuth = FirebaseAuth.getInstance();
        showQRButton = root.findViewById(R.id.showQRButton);

        showQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), QRDisplayActivity.class));
            }
        });

        Toast.makeText(getContext(), "Im outside", Toast.LENGTH_LONG).show();

        //User details
        user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Toast.makeText(getContext(), "Im here done first", Toast.LENGTH_LONG).show();
                userDatabase = dataSnapshot.getValue(Users.class);

                if (userDatabase != null) {
                    Toast.makeText(getContext(), "Im here", Toast.LENGTH_LONG).show();
                    String name = userDatabase.getName();
                    String email = userDatabase.getEmail();
                    String id = userDatabase.getId();
                    Uri profilePic = Uri.parse(userDatabase.getPhotoUrl());
                    String phone = userDatabase.getPhoneNumber();
                    String address = userDatabase.getAddress();
                    String age = userDatabase.getAge();
                    String sex = userDatabase.getSex();
                    String bloodGroup = userDatabase.getBloodGroup();


                    TextView emailText = root.findViewById(R.id.emailTextView);
                    TextView displayText = root.findViewById(R.id.displayNameTextView);
                    TextView idText = root.findViewById(R.id.idTextView);
                    ImageView profilePicImage = root.findViewById(R.id.profilePictureImageView);

                    emailText.setText(email);
                    displayText.setText(name);
                    idText.setText(id);
                    Glide.with(root.getContext()).load(profilePic).apply(RequestOptions.circleCropTransform()).into(profilePicImage);

                    TextView nameCard = root.findViewById(R.id.nameCardTextView);
                    TextView emailCard = root.findViewById(R.id.emailCardTextView);
                    TextView phoneCard = root.findViewById(R.id.phoneCardTextView);
                    TextView addressCard = root.findViewById(R.id.addressCardTextView);
                    TextView ageCard = root.findViewById(R.id.ageCardTextView);
                    TextView sexCard = root.findViewById(R.id.sexCardTextView);
                    TextView bloodGroupCard = root.findViewById(R.id.bloodGroupCardTextView);

                    nameCard.setText(name);
                    emailCard.setText(email);
                    phoneCard.setText(phone);
                    addressCard.setText(address);
                    ageCard.setText(age);
                    sexCard.setText(sex);
                    bloodGroupCard.setText(bloodGroup);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Not completed", Toast.LENGTH_LONG).show();
            }
        });



        return root;
    }
}