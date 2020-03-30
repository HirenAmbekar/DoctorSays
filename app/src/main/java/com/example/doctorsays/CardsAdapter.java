package com.example.doctorsays;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardsViewHolder> {

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onContinueClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    private Context mCtx;
    private List<PublicUser> usersList;
    private ConstraintLayout constraintLayout;


    public CardsAdapter(Context mCtx, ArrayList<PublicUser> usersList) {
        this.mCtx = mCtx;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public CardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.cards, null);
        return new CardsViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardsViewHolder holder, int position) {
        PublicUser users = usersList.get(position);

        constraintLayout.setMinWidth((mCtx.getResources().getDisplayMetrics().widthPixels)-2);

        holder.name.setText(users.getName());
        holder.age.setText(users.getAge());
        holder.sex.setText(users.getSex());
        Glide.with(mCtx.getApplicationContext()).load(Uri.parse(users.getPhotoUrl())).apply(RequestOptions.circleCropTransform()).into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class CardsViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView name, age, sex;
        Button deleteButton, continueButton;

        public CardsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profilePicture);
            name = itemView.findViewById(R.id.nameCardTextView);
            age = itemView.findViewById(R.id.textView13);
            sex = itemView.findViewById(R.id.sexTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            continueButton = itemView.findViewById(R.id.continueButton);
            constraintLayout = itemView.findViewById(R.id.cards_constrained_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onContinueClick(position);
                        }
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
