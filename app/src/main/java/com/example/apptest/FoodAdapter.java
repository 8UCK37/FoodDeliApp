package com.example.apptest;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptest.utils.model.FoodItem;

import java.util.List;

public class  FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private FoodItem food;
    List<FoodItem> list;
    Context context;

    public FoodAdapter(List<FoodItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_layout, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {

        holder.food_image.setImageResource(list.get(position).getFoodImage());
        holder.food_name.setText(list.get(position).getFoodName());
        holder.food_price.setText("Rs "+list.get(position).getFoodPrice());


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FoodDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("food_name", list.get(holder.getAdapterPosition()).getFoodName());
                intent.putExtra("food_price", String.valueOf(list.get(holder.getAdapterPosition()).getFoodPrice()));
                intent.putExtra("food_image", list.get(holder.getAdapterPosition()).getFoodImage());
                intent.putExtra("food_desc",list.get(holder.getAdapterPosition()).getFoodDesc());
                intent.putExtra("origin",list.get(holder.getAdapterPosition()).getFoodBrandName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        ImageView food_image;
        TextView food_name, food_price;
        View view;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            food_image = itemView.findViewById(R.id.food_image);
            food_name = itemView.findViewById(R.id.food_name);
            food_price = itemView.findViewById(R.id.food_price);
        }
    }
}
