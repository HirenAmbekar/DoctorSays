package com.example.doctorsays.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorsays.Cards;
import com.example.doctorsays.CardsAdapter;
import com.example.doctorsays.Home;
import com.example.doctorsays.NewQR;
import com.example.doctorsays.PatientProfileActivity;
import com.example.doctorsays.PublicUser;
import com.example.doctorsays.R;
import com.example.doctorsays.Users;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CardsAdapter cardsAdapter;
    FirebaseUser user;
    View root;

    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;

    private ArrayList<PublicUser> cardsList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton button;
        button = root.findViewById(R.id.addNewPatientButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateQRCodeScanner();
            }
        });

        FloatingActionButton refreshButton;
        refreshButton = root.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshRecyclerView();
            }
        });


        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference1 = FirebaseDatabase.getInstance().getReference("users");
        databaseReference1.child(user.getUid()).child("patients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refreshRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return root;
    }

    private void refreshRecyclerView() {
        databaseReference1 = FirebaseDatabase.getInstance().getReference("users");
        databaseReference1.child(user.getUid()).child("patients").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardsList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String userID = postSnapshot.getValue(String.class);
                    databaseReference2 = FirebaseDatabase.getInstance().getReference("public_user_data");
                    databaseReference2.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            PublicUser publicUser = dataSnapshot.getValue(PublicUser.class);
                            cardsList.add(publicUser);
                            buildRecyclerView(root);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
//    private void createCardsList() {
//        cardsList = new ArrayList<>();
//        cardsList.add(new Cards("12345","12345", "90"));
//        cardsList.add(new Cards("12345","12345", "90"));
//        cardsList.add(new Cards("12345","12345", "90"));
//        cardsList.add(new Cards("12345","12345", "90"));
//        cardsList.add(new Cards("12345","12345", "90"));
//        cardsList.add(new Cards("12345","12345", "90"));
//        cardsList.add(new Cards("12345","12345", "90"));
//        cardsList.add(new Cards("12345","12345", "90"));
//        cardsList.add(new Cards("12345","12345", "90"));
//        cardsList.add(new Cards("12345","12345", "90"));
//    }

    private void buildRecyclerView(View root) {
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        cardsAdapter = new CardsAdapter(root.getContext(), cardsList);
        recyclerView.setAdapter(cardsAdapter);

        cardsAdapter.setOnItemClickListener(new CardsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //changeItem(position, "Changed");
                //Statistics
            }

            @Override
            public void onDeleteClick(int position) {
                removeCardItem(position);
            }

            @Override
            public void onContinueClick(int position) {
                startPatientProfileActivity(position);

            }
        });
    }

    private void changeItem(int position, String text) {
        cardsList.get(position).changeText(text);
        cardsAdapter.notifyItemChanged(position);
    }

    public void startPatientProfileActivity (int position) {
        String id = cardsList.get(position).getId();
        startActivity(new Intent(getActivity(), PatientProfileActivity.class).putExtra("id", id));
    }

    public void removeCardItem (final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("Confirmation");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id = cardsList.get(position).getId();
                cardsList.remove(position);
                cardsAdapter.notifyItemRemoved(position);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                reference.child(user.getUid()).child("patients").child(id).removeValue();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setMessage("Are you sure, you want to remove the patient??");
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void initiateQRCodeScanner() {
        Intent intent = new Intent(getActivity(), NewQR.class);
        startActivity(intent);
    }
}