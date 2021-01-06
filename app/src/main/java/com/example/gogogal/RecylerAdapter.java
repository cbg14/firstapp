package com.example.gogogal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Room;

import java.util.ArrayList;

public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.CustomViewHolder> {

    private  ArrayList<RecyclerData> arrayList;
    private Context context;
    private  AppDatabase db;
    String curName;

    public RecylerAdapter(ArrayList<RecyclerData> arrayList, Context context) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    //처음으로 생성될떄
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        db = Room.databaseBuilder(parent.getContext(),AppDatabase.class,"todo-db").allowMainThreadQueries().build();
        return holder;
    }

    @Override
    //뷰가 실제로 보여질때
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.textView.setText(arrayList.get(position).getPlan_name());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 curName = holder.textView.getText().toString();
                Toast.makeText(v.getContext(),curName,Toast.LENGTH_SHORT).show();
                //아이템 클릭시 인테트 해보기
                 context = v.getContext();
                Intent intent =new Intent(v.getContext(),ProfileActivity.class);
                //화면넘기면서 값 넘기기
                intent.putExtra("plan_name",curName);
                System.out.println("==RecycleAdapter==값을 보내기전 잘 담기는지 확인:"+curName);
                context.startActivity(intent);


            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                curName = holder.textView.getText().toString();
                remove(holder.getAdapterPosition());
                return true;
            }
        });
    }
    public void remove(int position) { //position 값이 리스트 순서대로 0부터 시작함 ex) 리스트개수가4면 0,1,2,3 맨위는 항상0
        try{

            System.out.println("==========REMOVE==============");
            db.todoDao().getDelete_title(curName);//클릭한 아이템 이름을 DB에서 삭제한다.
            System.out.println(curName+"DB에서 삭제");
            System.out.println(db.todoDao().getAll().toString());
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return (null != arrayList? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected  TextView textView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_rc_itemlist_name);
        }
    }
}
