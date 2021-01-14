package com.example.gogogal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Every_day_Activity extends AppCompatActivity implements View.OnClickListener{
    /*private List<String> data;*/
    //광고
    private AdView mAdView;
    //아이템추가 +버튼
    private View item_add;

    //리사이크러뷰
    private  RecylerAdapter recylerAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<RecyclerData> arrayList;
    private  AppDatabase db;
    private TextView tv_progs, text_done;
    int progs=0;
    int cur_day;


    private  Context context;
    String name ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_day_);

        //현재 날짜 구하기
        Calendar calendar  =Calendar.getInstance();
         cur_day = calendar.get(Calendar.DAY_OF_WEEK); // 1=일 2=월 3=화 4=수 5=목 6=금 7=토

        //룸 DB 사용해보기
        //데이터 베이스 객체 생성
        db = Room.databaseBuilder(this,AppDatabase.class,"todo-db").allowMainThreadQueries().build();


        //광고 삽입
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        tv_progs = findViewById(R.id.tv_percent);
        //리사이클러 뷰
        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();
        recylerAdapter = new RecylerAdapter(arrayList, context);
        recyclerView.setAdapter(recylerAdapter);

        List<Todo> item = db.todoDao().getAll();

        System.out.println("=======item============");
        System.out.println(item.toString());
        for(int i=0; i<item.size();i++){
            RecyclerData recyclerData = new RecyclerData(item.get(i).getTitle(),item.get(i).getProgr());
            arrayList.add(recyclerData);
            if(item.get(i).getProgr() > 100){
            
                //text_done.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            }
        }


        //아이템 추가버튼 클릭
        item_add = findViewById(R.id.item_add);
        item_add.setOnClickListener(this::onClick);

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onstart실행");
        System.out.println(db.todoDao().getAll().toString());
        arrayList.clear();
        recylerAdapter.notifyDataSetChanged();
        List<Todo> item = db.todoDao().getAll();
        for(int i=0; i<item.size();i++){
            RecyclerData recyclerData = new RecyclerData(item.get(i).getTitle(),item.get(i).getProgr());
            arrayList.add(recyclerData);

        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_add:
                CustomDialog dialog = new CustomDialog(this);
                dialog.setDialogListener(new CustomDialog.CustomDialogListener() {
                    @Override
                    public void okClicked(String plan_name) {
                      RecyclerData recyclerData = new RecyclerData(plan_name,0);
                      arrayList.add(recyclerData);
                        System.out.println("========Evert_Day_Activity===========");
                        db.todoDao().insert(new Todo(plan_name,0,0,0,0,cur_day));
                        System.out.println(db.todoDao().getAll().toString());
                      recylerAdapter.notifyDataSetChanged();



                    }

                    @Override
                    public void noClicked() {

                    }
                });
                dialog.show();
                break;
        }
    }

}