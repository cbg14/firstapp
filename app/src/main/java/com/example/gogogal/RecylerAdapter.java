package com.example.gogogal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.CustomViewHolder> {

    private  ArrayList<RecyclerData> arrayList;
    private Context context;
    private  AppDatabase db;
    String curName;
    int curpercent;

    public RecylerAdapter(ArrayList<RecyclerData> arrayList, Context context) {
        this.context = context;
        System.out.println(context+"=============");
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
        holder.tv_progr.setText(String.valueOf(arrayList.get(position).getProgr())+"%");
        if(arrayList.get(position).getProgr()>=100){
            holder.textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        }else{
            holder.textView.setPaintFlags(0);
        }

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 curName = holder.textView.getText().toString();


               // Toast.makeText(v.getContext(),curName,Toast.LENGTH_SHORT).show();
                //아이템 클릭시 인테트 해보기
                 context = v.getContext();
                Intent intent =new Intent(v.getContext(),ProfileActivity.class);
                //화면넘기면서 값 넘기기
                intent.putExtra("plan_name",curName);
                System.out.println("==RecycleAdapter==값을 보내기전 잘 담기는지 curname/curpercent 확인:"+curName+"/"+curpercent);
                context.startActivity(intent);


            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                curName = holder.textView.getText().toString();
                System.out.println("롱클릭감지=========");
                AlertDialog.Builder ad = new AlertDialog.Builder(v.getRootView().getContext());
                ad.setTitle("삭제");
                ad.setMessage(curName+"를 삭제하시 겠습니까?");
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        curName = holder.textView.getText().toString();
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
        protected TextView tv_progr;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_rc_itemlist_name);
            tv_progr = itemView.findViewById(R.id.tv_percent);

        }
    }
}
