package com.example.expensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeekSpendingAdapter extends  RecyclerView.Adapter<WeekSpendingAdapter.viewHolder> {

    private Context mContext;
    private List<Data> myDataList;

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.retrieve_layout,parent,false);
        return  new WeekSpendingAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Data data = myDataList.get(position);
        holder.item.setText("Item :"+data.getItem());
        holder.amount.setText("Amount :"+data.getAmount());
        holder.date.setText("Item :"+data.getDate());
        holder.notes.setText("Note :"+data.getNotes());

        switch(data.getItem()){
            case "Transport":
                holder.imageView.setImageResource(R.drawable.ic_transport);
                break;
            case "Food":
                holder.imageView.setImageResource(R.drawable.ic_transport);
                break;
            case "Entertainment":
                holder.imageView.setImageResource(R.drawable.ic_entertainment);
                break;
            case "House":
                holder.imageView.setImageResource(R.drawable.ic_house);
                break;
            case "Health":
                holder.imageView.setImageResource(R.drawable.ic_health);
                break;
            case "Charity":
                holder.imageView.setImageResource(R.drawable.ic_consultancy);
                break;
            case "Personal":
                holder.imageView.setImageResource(R.drawable.ic_personalcare);
                break;
            case "Other":
                holder.imageView.setImageResource(R.drawable.ic_other);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public WeekSpendingAdapter(Context mContext, List<Data> myDataList) {
        this.mContext = mContext;
        this.myDataList = myDataList;
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        public TextView item,amount,date,notes;
        public ImageView imageView;
        View mView;


        public viewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            notes = itemView.findViewById(R.id.note);
            imageView = itemView.findViewById(R.id.imageView);


        }
        public void setItemName(String itemName) {
            TextView item = mView.findViewById(R.id.item);
            item.setText(itemName);
        }
        public void setItemAmount(String itemAmount) {
            TextView amount = mView.findViewById(R.id.amount);
            amount.setText(itemAmount);
        }
        public void setDate(String itemDate) {
            TextView date = mView.findViewById(R.id.date);
            date.setText(itemDate);
        }
        public void setNotes(String itemNote) {
            TextView note = mView.findViewById(R.id.note);
            date.setText(itemNote);
        }

    }

}
