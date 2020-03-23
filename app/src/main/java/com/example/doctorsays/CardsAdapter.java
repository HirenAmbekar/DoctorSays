package com.example.doctorsays;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardsViewHolder> {

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    private Context mCtx;
    private List<Users> usersList;
    private ConstraintLayout constraintLayout;


    public CardsAdapter(Context mCtx, ArrayList<Users> usersList) {
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
        Users users = usersList.get(position);

        constraintLayout.setMinWidth((mCtx.getResources().getDisplayMetrics().widthPixels)-2);

        holder.name.setText(users.getName());
        holder.age.setText(users.getAge());
        holder.sex.setText(users.getSex());
        //holder.levelImage.setImageDrawable(mCtx.getResources().getDrawable(cards.getLevelImage(), null));
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class CardsViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView name, age, sex;

        public CardsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profilePicture);
            name = itemView.findViewById(R.id.nameCardTextView);
            age = itemView.findViewById(R.id.ageTextView);
            sex = itemView.findViewById(R.id.sexTextView);
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
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
