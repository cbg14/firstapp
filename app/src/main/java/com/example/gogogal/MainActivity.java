package com.example.gogogal;

import android.content.Intent;
import android.graphics.Color;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Button bt_week_day,bt_Mon,bt_Tue,bt_Wed,bt_Thu,bt_Fri,bt_Sat,bt_Sun;
    private AdView mAdView;
    ProgressBar progressBar;
    TextView tv_percent,tv_check_count,tv_all_count;
    private AppDatabase db;
    TextView tv_Mon,tv_Tue,tv_Wed,tv_Thu,tv_Fri,tv_Sat,tv_Sun;


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


        //버튼 각요일 //텍스트
        bt_Mon = findViewById(R.id.bt_mon);
        bt_Tue = findViewById(R.id.bt_tue);
        bt_Wed = findViewById(R.id.bt_wed);
        bt_Thu = findViewById(R.id.bt_thu);
        bt_Fri = findViewById(R.id.bt_fri);
        bt_Sat = findViewById(R.id.bt_sat);
        bt_Sun = findViewById(R.id.bt_sun);

        tv_Mon = findViewById(R.id.tv_mon);
        tv_Tue = findViewById(R.id.tv_tue);
        tv_Wed = findViewById(R.id.tv_wed);
        tv_Thu = findViewById(R.id.tv_thu);
        tv_Fri = findViewById(R.id.tv_fri);
        tv_Sat = findViewById(R.id.tv_sat);
        tv_Sun = findViewById(R.id.tv_sun);



        List<Todo> item = db.todoDao().getAll();
        tv_all_count.setText(String.valueOf(item.size()));
        int check =0, progr =0;
        for(int i=0; i<item.size();i++){

            progr += Math.ceil(item.get(i).getProgr()/item.size());
            if(item.get(i).getProgr()>=100){
                check++;
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

        //현재 날짜 구하기
        Calendar calendar  =Calendar.getInstance();
        int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfweek){
            case 1:
                //일
                bt_Sun.setBackgroundColor(Color.rgb(153,000,255));
                break;
            case 2: //월
                bt_Mon.setBackgroundColor(Color.rgb(153,000,255));
                break;
            case 3://화
                bt_Tue.setBackgroundColor(Color.rgb(153,000,255));
                tv_Tue.setText(tv_percent.getText());
                break;
            case 4:
                bt_Wed.setBackgroundColor(Color.rgb(153,000,255));
                break;
            case 5:
                bt_Thu.setBackgroundColor(Color.rgb(153,000,255));
                break;
            case 6:
                bt_Fri.setBackgroundColor(Color.rgb(153,000,255));
                break;
            case 7:
                bt_Sat.setBackgroundColor(Color.rgb(153,000,255));
                break;

        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        List<Todo> item = db.todoDao().getAll();
        tv_all_count.setText(String.valueOf(item.size()));
        int check =0, progr =0;
        for(int i=0; i<item.size();i++){
            progr += Math.ceil(item.get(i).getProgr()/item.size());
            if(item.get(i).getProgr()>=100){
                check++;
            }
        }
        if (progr >100){
            progr =100;
        }
        if(check == item.size()){
            progr=100;
        }
        if(item.size() ==0) {
            progr=0;
        }
        tv_check_count.setText(String.valueOf(check));
        progressBar.setProgress(progr);
        tv_percent.setText(String.valueOf(progr)+"%");

        //현재 날짜 구하기
        Calendar calendar  =Calendar.getInstance();
        int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfweek){
            case 1:
                //일
                bt_Sun.setBackgroundColor(Color.rgb(153,000,255));
                break;
            case 2: //월
                bt_Mon.setBackgroundColor(Color.rgb(153,000,255));
                break;
            case 3://화
                bt_Tue.setBackgroundColor(Color.rgb(153,000,255));
                tv_Tue.setText(tv_percent.getText());
                break;
            case 4:
                bt_Wed.setBackgroundColor(Color.rgb(153,000,255));
                break;
            case 5:
                bt_Thu.setBackgroundColor(Color.rgb(153,000,255));
                break;
            case 6:
                bt_Fri.setBackgroundColor(Color.rgb(153,000,255));
                break;
            case 7:
                bt_Sat.setBackgroundColor(Color.rgb(153,000,255));
                break;

        }

    }
}



