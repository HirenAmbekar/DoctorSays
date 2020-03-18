package com.example.doctorsays.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorsays.Cards;
import com.example.doctorsays.CardsAdapter;
import com.example.doctorsays.NewQR;
import com.example.doctorsays.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CardsAdapter cardsAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private ArrayList<Cards> cardsList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton button;
        button = root.findViewById(R.id.addNewPatientButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateQRCodeScanner();
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        createCardsList();
        buildRecyclerView(root);

        return root;
    }

    private void createCardsList() {
        cardsList = new ArrayList<>();
        cardsList.add(new Cards("12345","12345", "90"));
        cardsList.add(new Cards("12345","12345", "90"));
        cardsList.add(new Cards("12345","12345", "90"));
        cardsList.add(new Cards("12345","12345", "90"));
        cardsList.add(new Cards("12345","12345", "90"));
        cardsList.add(new Cards("12345","12345", "90"));
        cardsList.add(new Cards("12345","12345", "90"));
        cardsList.add(new Cards("12345","12345", "90"));
        cardsList.add(new Cards("12345","12345", "90"));
        cardsList.add(new Cards("12345","12345", "90"));
    }

    private void buildRecyclerView(View root) {
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        cardsAdapter = new CardsAdapter(root.getContext(), cardsList);
        recyclerView.setAdapter(cardsAdapter);

        cardsAdapter.setOnItemClickListener(new CardsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position, "Changed");
            }
        });
    }

    private void changeItem(int position, String text) {
        cardsList.get(position).changeText(text);
        cardsAdapter.notifyItemChanged(position);
    }

    public void initiateQRCodeScanner() {
        Intent intent = new Intent(getActivity(), NewQR.class);
        startActivity(intent);
    }
}