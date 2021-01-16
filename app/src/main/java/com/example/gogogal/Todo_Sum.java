package com.example.gogogal;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class Todo_Sum extends AppCompatActivity {

    //광고
    private AdView mAdView;
    Toolbar toolbar;

    private RecyclerView recyclerView;
    private ArrayList<Sum> arrayList;
    private LinearLayoutManager linearLayoutManager;
    private SumAdapter sumAdapter;
    private SumDatabase db_sum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo__sum);

        //툴바 뒤로가기 만들기
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //광고 삽입
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //
        recyclerView = findViewById(R.id.todo_sum_rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();
        sumAdapter = new SumAdapter(arrayList);
        recyclerView.setAdapter(sumAdapter);

        db_sum = SumDatabase.getInstance(this);
        List<Sum> item = db_sum.sumDao().getAll();
        if(item.size()>0) {
            for (int i = 0; i < item.size(); i++) {
                Sum sum = new Sum(item.get(i).getTitle(),item.get(i).getSum());
                arrayList.add(sum);
            }
        }





    }
}
