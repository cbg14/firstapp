package com.example.gogogal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Button bt_week_day;
    private AdView mAdView;
    ProgressBar progressBar;
    TextView tv_percent,tv_check_count,tv_all_count;
    private AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start__view);

        db= Room.databaseBuilder(this,AppDatabase.class,"todo-db").allowMainThreadQueries().build();

        bt_week_day = findViewById(R.id.bt_week_day);
        progressBar = findViewById(R.id.progress_bar);
        tv_percent = findViewById(R.id.text_progress);
        tv_check_count = findViewById(R.id.tv_check_count);
        tv_all_count = findViewById(R.id.tv_all_count);

        List<Todo> item = db.todoDao().getAll();
        tv_all_count.setText(String.valueOf(item.size()));
        int check =0, progr =0;
        for(int i=0; i<item.size();i++){
            if(item.get(i).getProgr()>=100){
                check++;
                progr +=Math.ceil(100/(float)item.size());
            }
        }
        if (progr >100){
            progr =100;
        }
        tv_check_count.setText(String.valueOf(check));
        progressBar.setProgress(progr);
        tv_percent.setText(String.valueOf(progr)+"%");

        bt_week_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Every_day_Activity.class);
                startActivity(intent);
            }
        });
        //광고
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    @Override
    protected void onStart() {
        super.onStart();
        List<Todo> item = db.todoDao().getAll();
        tv_all_count.setText(String.valueOf(item.size()));
        int check =0, progr =0;
        for(int i=0; i<item.size();i++){
            if(item.get(i).getProgr()>=100){
                check++;
                progr +=Math.ceil(100/(float)item.size());
            }
        }
        if (progr >100){
            progr =100;
        }
        tv_check_count.setText(String.valueOf(check));
        progressBar.setProgress(progr);
        tv_percent.setText(String.valueOf(progr)+"%");
    }
}



