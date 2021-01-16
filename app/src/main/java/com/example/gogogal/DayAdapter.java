package com.example.gogogal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.CustomViewHolder> {

    private ArrayList<dayData> arrayList;


    public DayAdapter(ArrayList<dayData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    //리스트뷰가 처음으로 생성될떄 생성주기
    public DayAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    //실제로 추가될때
    @Override
    public void onBindViewHolder(@NonNull DayAdapter.CustomViewHolder holder, int position) {
        holder.day_todo_name.setText(arrayList.get(position).getDay_todo_name());
        holder.day_todo_percent.setText(arrayList.get(position).getDay_todo_percent());

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != arrayList? arrayList.size() :0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView day_todo_name;
        protected TextView day_todo_percent;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.day_todo_name =itemView.findViewById(R.id.tv_day_todo_name);
            this.day_todo_percent= itemView.findViewById(R.id.tv_day_todo_percent);
        }
    }
}
