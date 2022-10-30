package com.example.apptest.utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptest.R;
import com.example.apptest.utils.model.FoodItem;

import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder> {



    private List<FoodItem> foodItemList;

    private FoodClickedListeners foodClickedListeners;
    public FoodItemAdapter(FoodClickedListeners foodClickedListeners){
        this.foodClickedListeners = foodClickedListeners;
    }
    
    public void setFoodItemList(List<FoodItem> foodItemList){
        this.foodItemList = foodItemList;
    }
    @NonNull
    @Override
    public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_food_item, parent , false);
        return new FoodItemViewHolder(view);
    }

    public void setFilteredList(List<FoodItem> filteredList){
        this.foodItemList=filteredList;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
        FoodItem foodItem = foodItemList.get(position);
        holder.foodNameTv.setText(foodItem.getFoodName());
        holder.foodBrandNameTv.setText(foodItem.getFoodBrandName());
        holder.foodPriceTv.setText(String.valueOf(foodItem.getFoodPrice()));
        holder.foodImageView.setImageResource(foodItem.getFoodImage());



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodClickedListeners.onCardClicked(foodItem);
            }
        });
        holder.favTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {foodClickedListeners.onFavBtnClicked(foodItem);}
        });
        holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodClickedListeners.onAddToCartBtnClicked(foodItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (foodItemList == null){
            return 0;
        }else{
            return foodItemList.size();
        }
    }

    public class FoodItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView foodImageView, addToCartBtn,favTap;
        private TextView foodNameTv, foodBrandNameTv, foodPriceTv;
        private CardView cardView;
        public FoodItemViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.eachFoodCardView);
            addToCartBtn = itemView.findViewById(R.id.eachFoodAddToCartBtn);
            foodNameTv = itemView.findViewById(R.id.eachFoodName);
            foodImageView = itemView.findViewById(R.id.eachFoodIv);
            favTap=itemView.findViewById(R.id.favtap);
            foodBrandNameTv = itemView.findViewById(R.id.eachFoodBrandNameTv);
            foodPriceTv = itemView.findViewById(R.id.eachFoodPriceTv);
        }
    }

    public interface FoodClickedListeners {
        void onCardClicked(FoodItem foodItem);
        void onAddToCartBtnClicked(FoodItem foodItem);
        void onFavBtnClicked(FoodItem foodItem);
    }

}
