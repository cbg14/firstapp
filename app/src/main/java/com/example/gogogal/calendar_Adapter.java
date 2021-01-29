package com.example.gogogal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class calendar_Adapter extends RecyclerView.Adapter<calendar_Adapter.CustomViewHolder> {
    private ArrayList<dayData> arrayList;

    public calendar_Adapter(ArrayList<dayData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getDay_todo_name());
        holder.percent.setText(arrayList.get(position).getDay_todo_percent());

        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return (null!= arrayList? arrayList.size() :0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView percent;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.calendarView_name);
            this.percent = itemView.findViewById(R.id.calendarView_percent);
        }
    }
}
