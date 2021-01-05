package com.example.gogogal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_day_);

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

        //리사이클러 뷰
        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();
        recylerAdapter = new RecylerAdapter(arrayList);
        recyclerView.setAdapter(recylerAdapter);



        //
        item_add = findViewById(R.id.item_add);
        item_add.setOnClickListener(this::onClick);

        //setUserInfo();


    }

    /*private void setUserInfo() {
        arrayList.add(new RecyclerData("감자"));
        arrayList.add(new RecyclerData("kim"));
        arrayList.add(new RecyclerData("nick"));
    }*/

//

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_add:
                CustomDialog dialog = new CustomDialog(this);
                dialog.setDialogListener(new CustomDialog.CustomDialogListener() {
                    @Override
                    public void okClicked(String plan_name) {

                      RecyclerData recyclerData = new RecyclerData(plan_name);
                      arrayList.add(recyclerData);
                      recylerAdapter.notifyDataSetChanged();

                        System.out.println("=========================");
                        System.out.println(db.todoDao().getAll().toString());

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