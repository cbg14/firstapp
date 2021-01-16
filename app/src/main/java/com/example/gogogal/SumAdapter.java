package com.example.gogogal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SumAdapter extends RecyclerView.Adapter<SumAdapter.CustomViewHolder> {

    private ArrayList<Sum> arrayList;
    String curName;
    private SumDatabase db;
    int sum;
    public SumAdapter(ArrayList<Sum> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    //리스트뷰가 처음으로 생성될떄 생성주기
    public SumAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        db=SumDatabase.getInstance(parent.getContext());
        return holder;
    }

    //실제로 추가될때
    @Override
    public void onBindViewHolder(@NonNull SumAdapter.CustomViewHolder holder, int position) {
        holder.day_todo_name.setText(arrayList.get(position).getTitle());
        holder.day_todo_percent.setText(String.valueOf(arrayList.get(position).getSum()));
        holder.itemView.setTag(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                curName = holder.day_todo_name.getText().toString();
                sum = Integer.valueOf(holder.day_todo_percent.getText().toString());
                AlertDialog.Builder ad = new AlertDialog.Builder(v.getRootView().getContext());
                ad.setTitle("삭제");
                ad.setMessage(curName+"를 삭제하시 겠습니까?");
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        curName = holder.day_todo_name.getText().toString();
                        remove(holder.getAdapterPosition());
                    }
                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
                return true;
            }
        });
    }

    private void remove(int position) {
        List<Sum> item = db.sumDao().getDate(curName);
        try{
            db.sumDao().getDelete_title(curName,sum);
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
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
