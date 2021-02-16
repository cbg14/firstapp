package com.example.gogogal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class calendar extends AppCompatActivity {
    //광고
    private AdView mAdView;
    Toolbar toolbar;
    CalendarView calendarView;
    TextView tv_calendar_date;
    TextView tv_percent;
    private Calendar_Database db;
    private  DayDatabase db_day;

    private calendar_Adapter calendar_adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<dayData> arrayList;
    int day_date1, year1, month1, progr;
    String language;
    int Day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //현재 시스템 언어가 어떤값인지 알아내는 코드
        Locale locale =getResources().getConfiguration().locale;
        language =locale.getLanguage();

        //툴바 뒤로가기 만들기
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        db = Calendar_Database.getINSTANCE(this);
        db_day = DayDatabase.getInstance(this);

        tv_percent = findViewById(R.id.tv_calendar_percent);
        tv_calendar_date = findViewById(R.id.tv_calendar_date);
        //광고 삽입
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //리스트 뿌리기
        recyclerView = findViewById(R.id.calendar_rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();
        calendar_adapter = new calendar_Adapter(arrayList);
        recyclerView.setAdapter(calendar_adapter);


        //현재 날짜 구하기
        Calendar calendar = Calendar.getInstance();
        year1 = calendar.get(Calendar.YEAR);
        month1 = calendar.get(Calendar.MONTH) + 1;
        day_date1 = calendar.get(Calendar.DATE);
        System.out.println("===현재 년월일 " + year1 + "년" + (month1) + "월" + day_date1 + "일");
        if(language.equals("ko")){
            tv_calendar_date.setText(String.valueOf(year1) + "년 " + String.valueOf(month1) + "월 " + String.valueOf(day_date1) + "일");
        }else{{
            tv_calendar_date.setText(String.valueOf(year1) + ". " + String.valueOf(month1) + ". " + String.valueOf(day_date1));
        }}

        //인테트하면서 값넘긴거 받기
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Day = extras.getInt("Day");
        }
        System.out.println("=========mainactivty에서 값 넘겨오는지 확인====");
        System.out.println("===각 요일에 따른 키값="+Day);

        List<Todo_calendar> item = db.todo_calendarDao().calendar_find_date(year1, month1, day_date1);
        System.out.println("===달력db:" +db.todo_calendarDao().getAll());
        List<Todo_Day> item2 = db_day.todo_dayDao().getAll();
        System.out.println("===오늘할일"+db_day.todo_dayDao().getAll().toString());



        //현재날짜 리스트 보여줌
        if (item2.size() > 0) {
            for (int i = 0; i < item2.size(); i++) {
                dayData dayData = new dayData(item2.get(i).getTitle(), String.valueOf(item2.get(i).getProgr() + "%"));
                if(Day == item2.get(i).getDay()){
                    progr += Math.ceil((float)item2.get(i).getProgr()/db_day.todo_dayDao().getCount(Day));
                    System.out.println("===item2.size:"+db_day.todo_dayDao().getCount(Day));
                    System.out.println("===item2.get(i).getprogr:"+item2.get(i).getProgr());
                    System.out.println("===progr:"+progr);
                    arrayList.add(dayData);
                }
            }
            calendar_adapter.notifyDataSetChanged();
        }
        if(progr>100){
            progr=100;
        }
        if(language.equals("ko")){
            tv_percent.setText("진행률:"+String.valueOf(progr) + "%");
        }else{
            tv_percent.setText("Progress:"+String.valueOf(progr) + "%");
        }

        progr=0;

        //달력 클릭시 해당하는 날짜 넘어온다
        calendarView = findViewById(R.id.calendarView);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                arrayList.clear();
                calendar_adapter.notifyDataSetChanged();
                int click_progr = 0;
                List<Todo_calendar> item1 = db.todo_calendarDao().calendar_find_date(year, month+1, dayOfMonth);

                System.out.println("===클릭한 년월일 " + year + "년" + (month + 1) + "월" + dayOfMonth + "일");
                System.out.println("==="+item1.toString());
                if(language.equals("ko")){
                    tv_percent.setText("진행률:0%");
                }else{
                    System.out.println("===progr:"+progr);
                    tv_percent.setText("Progress:0%");
                }

                if(year==year1 && (month+1) ==month1 && dayOfMonth ==day_date1 ){
                    progr=0;
                    if (item2.size() > 0) {
                        for (int i = 0; i < item2.size(); i++) {
                            dayData dayData = new dayData(item2.get(i).getTitle(), String.valueOf(item2.get(i).getProgr() + "%"));
                            if(Day == item2.get(i).getDay()){
                                progr += Math.ceil((float)item2.get(i).getProgr()/db_day.todo_dayDao().getCount(Day));
                                System.out.println("===item2.size:"+db_day.todo_dayDao().getCount(Day));
                                System.out.println("===item2.get(i).getprogr:"+item2.get(i).getProgr());
                                System.out.println("===progr:"+progr);
                                arrayList.add(dayData);
                            }
                        }
                        calendar_adapter.notifyDataSetChanged();
                        if(progr>100){
                            progr=100;
                        }
                        if(language.equals("ko")){
                            tv_percent.setText("진행률:"+String.valueOf(progr) + "%");
                        }else{
                            System.out.println("===progr:"+progr);
                            String name = String.valueOf(progr);
                            tv_percent.setText("Progress:"+name + "%");
                        }
                        progr=0;
                    }

                }
                else if (item1.size() > 0) {
                    click_progr=0;

                    for (int i = 0; i < item1.size(); i++) {
                        dayData recyclerData = new dayData(item1.get(i).getTitle(), String.valueOf(item1.get(i).getProgr()) + "%");
                        click_progr += Math.ceil((float)item1.get(i).getProgr() / item1.size());
                        arrayList.add(recyclerData);
                    }
                    System.out.println("===진행률 값:"+(float)click_progr);
                    calendar_adapter.notifyDataSetChanged();
                    if(click_progr>100){
                        click_progr=100;
                    }
                    if(language.equals("ko")){
                        tv_calendar_date.setText(String.valueOf(year) + "년 " + String.valueOf(month + 1) + "월 " + String.valueOf(dayOfMonth) + "일");
                        tv_percent.setText("진행률:"+String.valueOf(click_progr) + "%");
                    }else{
                        tv_calendar_date.setText(String.valueOf(year) + ". " + String.valueOf(month + 1) + ". " + String.valueOf(dayOfMonth) + "");
                        tv_percent.setText("Progress:"+String.valueOf(click_progr) + "%");
                    }

                }



            }
        });

    }

}