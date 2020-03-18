package com.example.doctorsays.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.doctorsays.QRDisplayActivity;
import com.example.doctorsays.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GalleryFragment extends Fragment {

    ImageView profilePicture;

    FirebaseAuth mAuth;
    FirebaseUser user;
    Button showQRButton;

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

        //User details
        user = mAuth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String id = user.getUid();
            Uri profilePic = user.getPhotoUrl();

            TextView emailText = root.findViewById(R.id.emailTextView);
            TextView displayText = root.findViewById(R.id.displayNameTextView);
            TextView idText = root.findViewById(R.id.idTextView);
            ImageView profilePicImage = root.findViewById(R.id.profilePictureImageView);

            emailText.setText(email);
            displayText.setText(name);
            idText.setText(id);
            Glide.with(root.getContext()).load(profilePic).apply(RequestOptions.circleCropTransform()).into(profilePicImage);
        }

        return root;
    }
}