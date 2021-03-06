package com.example.gogogal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Day_info extends AppCompatActivity {

    //광고
    private AdView mAdView;
    //리사이클러뷰
    private ArrayList<dayData> arrayList;
    private DayAdapter dayAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    Toolbar toolbar;
    int Day;
    String language;

    //룸
    private  DayDatabase db_day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_info);

        //현재 시스템 언어가 어떤값인지 알아내는 코드
        Locale locale =getResources().getConfiguration().locale;
        language =locale.getLanguage();

        //툴바 뒤로가기 만들기 //매니패스트에 등록을 해줘야함  parentActivityName 돌아갈 부보 액티비티 정해줘야한다!!
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //광고 삽입
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //룸 DB 사용해보기
        //데이터 베이스 객체 생성
        db_day = DayDatabase.getInstance(this);


        recyclerView = findViewById(R.id.day_info_rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();
        dayAdapter = new DayAdapter(arrayList);
        recyclerView.setAdapter(dayAdapter);
        //인테트하면서 값넘긴거 받기
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Day = extras.getInt("Day");
        }
        System.out.println("=========mainactivty에서 값 넘겨오는지 확인====");
        System.out.println("===각 요일에 따른 키값="+Day);



        List<Todo_Day> item = db_day.todo_dayDao().getAll();
        System.out.println("==="+item.toString());
        for(int i =0; i<item.size();i++) {
            dayData dayData = new dayData(item.get(i).getTitle(), String.valueOf(item.get(i).getProgr() + "%"));
            if(Day == item.get(i).getDay()){
                arrayList.add(dayData);
            }

        }

            switch (Day) {
                case 1:
                    if(language.equals("ko")) {
                        toolbar.setTitle("일요일 목표 리스트");
                    }else{
                        toolbar.setTitle("Sunday goal list");
                    }
                    break;
                case 2:
                    if(language.equals("ko")) {
                        toolbar.setTitle("월요일 목표리스트");
                    }else{
                        toolbar.setTitle("Monday goal list");
                    }
                    break;
                case 3:
                    if (language.equals("ko")) {
                        toolbar.setTitle("화요일 목표리스트");
                    }else{
                        toolbar.setTitle("Tuesday target list");
                    }
                    break;
                case 4:
                    if (language.equals("ko")) {
                        toolbar.setTitle("수요일 목표리스트");
                    }else{
                        toolbar.setTitle("Wednesday goal list");
                    }
                    break;
                case 5:
                    if (language.equals("ko")) {
                        toolbar.setTitle("목요일 목표리스트");
                    }else{
                        toolbar.setTitle("Thursday goal list");
                    }
                    break;
                case 6:
                    if (language.equals("ko")) {
                        toolbar.setTitle("금요일 목표리스트");
                    }else{
                        toolbar.setTitle("Friday goal list");
                    }
                    break;
                case 7:
                    if (language.equals("ko")) {
                        toolbar.setTitle("토요일 목표리스트");
                    }else{
                        toolbar.setTitle("Saturday goal list");
                    }
                    break;
            }




    }
}
