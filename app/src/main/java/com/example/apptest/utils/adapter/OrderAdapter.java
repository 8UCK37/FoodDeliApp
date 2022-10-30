package com.example.apptest.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptest.R;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private ArrayList keyList,name, res, img,price,quant,date;

    public OrderAdapter(Context context, ArrayList name, ArrayList res, ArrayList img,ArrayList price,ArrayList quant,ArrayList keyList,ArrayList date) {
        this.context = context;
        this.name = name;
        this.res = res;
        this.img= img;
        this.price=price;
        this.quant=quant;
        this.keyList=keyList;
        this.date=date;
    }
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_order_item,parent,false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.name.setText(String.valueOf(name.get(position)));
        holder.res.setText(String.valueOf(res.get(position)));
        holder.price.setText(String.valueOf(price.get(position)));
        holder.img.setImageResource(Integer.valueOf(String.valueOf(img.get(position))));
        holder.quant.setText(String.valueOf(quant.get(position)));
        holder.order_uid.setText(String.valueOf(keyList.get(position)));
        holder.order_date.setText(String.valueOf(date.get(position)));

    }

    @Override
    public int getItemCount() {
        return name.size();
    }
    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView name, res, price,quant,order_uid,order_date;
        ImageView img;
        public OrderViewHolder(@NonNull View itemView) {

            super(itemView);
            name =itemView.findViewById(R.id.eachOrderItemName);
            res =itemView.findViewById(R.id.eachOrderItemOrigin);
            img=itemView.findViewById(R.id.eachOrderItemTv);
            price =itemView.findViewById(R.id.eachOrderItemPriceTv);
            quant=itemView.findViewById(R.id.eachOrderItemQuantityTV);
            order_uid=itemView.findViewById(R.id.title);
            order_date=itemView.findViewById(R.id.title1);

        }
    }

}
