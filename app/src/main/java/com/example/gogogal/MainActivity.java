package com.example.gogogal;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
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

    private Button bt_week_day, bt_Mon, bt_Tue, bt_Wed, bt_Thu, bt_Fri, bt_Sat, bt_Sun;
    private AdView mAdView;
    ProgressBar progressBar;
    TextView tv_percent, tv_check_count, tv_all_count;
    private AppDatabase db;
    TextView tv_day, tv_Mon, tv_Tue, tv_Wed, tv_Thu, tv_Fri, tv_Sat, tv_Sun;
    String shared = "0";

    //매일24시가되면 데이터 초기화
    private AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start__view);

        db = Room.databaseBuilder(this, AppDatabase.class, "todo-db").allowMainThreadQueries().build();

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

        tv_day = findViewById(R.id.tv_day);
        tv_Mon = findViewById(R.id.tv_mon);
        tv_Tue = findViewById(R.id.tv_tue);
        tv_Wed = findViewById(R.id.tv_wed);
        tv_Thu = findViewById(R.id.tv_thu);
        tv_Fri = findViewById(R.id.tv_fri);
        tv_Sat = findViewById(R.id.tv_sat);
        tv_Sun = findViewById(R.id.tv_sun);


        List<Todo> item = db.todoDao().getAll();
        tv_all_count.setText(String.valueOf(item.size()));
        int check = 0, progr = 0;
        for (int i = 0; i < item.size(); i++) {

            progr += Math.ceil(item.get(i).getProgr() / item.size());
            if (item.get(i).getProgr() >= 100) {
                check++;
            }
        }
        if (progr > 100) {
            progr = 100;
        }
        tv_check_count.setText(String.valueOf(check));
        progressBar.setProgress(progr);

        tv_percent.setText(String.valueOf(progr) + "%");


        bt_week_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Every_day_Activity.class);
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
        Calendar calendar = Calendar.getInstance();

        int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK); // 1=일 2=월 3=화 4=수 5=목 6=금 7=토
        //요일 임이로test
        // int dayOfweek = 1;

        if (item.size() > 0) {
            System.out.println("========dakofweek값:" + dayOfweek + "/ last_Day값:" + item.get(0).getDay());
        }
        //날이변경 되어 데이터 초기화 시키기
        if (item.size() > 0) {
            if (dayOfweek != item.get(0).getDay()) {
                System.out.println("Day가 바뀌여서 초기화 합니다.~~!");
                //ex)Todo{id=1, title='one', ex_all_count=0, ex_set=0, progr=0, count_check=0, day=3}]
                //업데이트 순서
                // 1.title
                // 2.ex_all_count
                // 3.ex_set
                // 4.count_check
                // 5.progr
                // 6.id_value
                // 7.int day)
                //progr값이랑 count_check값을 0으로 바꿔주고 day값을 dayofweek값으로 변경해준다.
                System.out.println("============업데이트전================");
                System.out.println(db.todoDao().getAll().toString());
                for (int i = 0; i < item.size(); i++) {
                    db.todoDao().upDate(item.get(i).getTitle(), item.get(i).getEx_all_count(), item.get(i).getEx_set(), 0, 0, item.get(i).getId(), dayOfweek);
                }
                System.out.println("============업데이트후==========");
                System.out.println(db.todoDao().getAll().toString());
            }
        }

        //지난 요일에 값 넣기
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        String Mon_value = sharedPreferences.getString("Mon", "");
        String Tue_value = sharedPreferences.getString("Tue", "");
        String Wed_value = sharedPreferences.getString("Wed", "");
        String Thu_value = sharedPreferences.getString("Thu", "");
        String Fri_value = sharedPreferences.getString("Fri", "");
        String Sat_value = sharedPreferences.getString("Sat", "");
        String Sun_value = sharedPreferences.getString("Sun", "");

        if (dayOfweek == 1) {
            //일요일이 되면 초기화 하고 시작
            SharedPreferences sharedPreferences1 = getSharedPreferences(shared, 0);
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            String re_Mon_value = "0%";
            String re_Tue_value = "0%";
            String re_Wed_value = "0%";
            String re_Thu_value = "0%";
            String re_Fri_value = "0%";
            String re_Sat_value = "0%";
            String re_Sun_value = "0%";
            editor.putString("Mon", re_Mon_value);
            editor.putString("Tue", re_Tue_value);
            editor.putString("Wed", re_Wed_value);
            editor.putString("Thu", re_Thu_value);
            editor.putString("Fri", re_Fri_value);
            editor.putString("Sat", re_Sat_value);
            editor.putString("Sun", re_Sun_value);
            System.out.println("========SharedPreferences일주일이 끝날때  각 요일 퍼센트 리셋후 확인========");
            System.out.println("====Mon=" + re_Mon_value + "/ Tue=" + re_Tue_value + "/Wed=" + re_Wed_value + "/Thu=" + re_Thu_value + "/FRi=" + re_Fri_value + "/SAT=" + re_Sat_value + "/ SUN=" + re_Sun_value + "========");
            editor.commit();
        } else {
            System.out.println("====Mon=" + Mon_value + "/ Tue=" + Tue_value + "/Wed=" + Wed_value + "/Thu=" + Thu_value + "/FRi=" + Fri_value + "/SAT=" + Sat_value + "/ SUN=" + Sun_value + "========");
            if (Mon_value.equals("")) {
                tv_Mon.setText("0%");
            } else {
                tv_Mon.setText(Mon_value);
            }

            if (Tue_value.equals("")) {
                tv_Tue.setText("0%");
            } else {
                tv_Tue.setText(Tue_value);
            }
            if (Wed_value.equals("")) {
                tv_Wed.setText("0%");
            } else {
                tv_Wed.setText(Wed_value);
            }
            if (Thu_value.equals("")) {
                tv_Thu.setText("0%");
            } else {
                tv_Thu.setText(Thu_value);
            }
            if (Fri_value.equals("")) {
                tv_Fri.setText("0%");
            } else {
                tv_Fri.setText(Fri_value);
            }
            if (Sat_value.equals("")) {
                tv_Sat.setText("0%");
            } else {
                tv_Sat.setText(Sat_value);
            }
            if (Sun_value.equals("")) {
                tv_Sun.setText("0%");
            } else {
                tv_Sun.setText(Sun_value);
            }

        }

        switch (dayOfweek) {
            case 1:
                tv_day.setText("일요일"); //제목
                bt_Sun.setBackgroundColor(Color.rgb(153, 000, 255));//배경색
                tv_Sun.setText(tv_percent.getText()); //현제 퍼센트가져오기
                break;
            case 2: //월
                tv_day.setText("월요일");
                bt_Mon.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Mon.setText(tv_percent.getText());
                break;
            case 3://화
                tv_day.setText("화요일");
                bt_Tue.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Tue.setText(tv_percent.getText());
                break;
            case 4:
                tv_day.setText("수요일");
                bt_Wed.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Wed.setText(tv_percent.getText());
                break;
            case 5:
                tv_day.setText("목요일");
                bt_Thu.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Thu.setText(tv_percent.getText());
                break;
            case 6:
                tv_day.setText("금요일");
                bt_Fri.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Fri.setText(tv_percent.getText());
                break;
            case 7:
                tv_day.setText("토요일");
                bt_Sat.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Sat.setText(tv_percent.getText());
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String Mon_value = tv_Mon.getText().toString();
        String Tue_value = tv_Tue.getText().toString();
        String Wed_value = tv_Wed.getText().toString();
        String Thu_value = tv_Thu.getText().toString();
        String Fri_value = tv_Fri.getText().toString();
        String Sat_value = tv_Sat.getText().toString();
        String Sun_value = tv_Sun.getText().toString();
        editor.putString("Mon", Mon_value);
        editor.putString("Tue", Tue_value);
        editor.putString("Wed", Wed_value);
        editor.putString("Thu", Thu_value);
        editor.putString("Fri", Fri_value);
        editor.putString("Sat", Sat_value);
        editor.putString("Sun", Sun_value);
        System.out.println("========SharedPreferences종료시점 각 요일 퍼센트 확인========");
        System.out.println("====Mon=" + Mon_value + "/ Tue=" + Tue_value + "/Wed=" + Wed_value + "/Thu=" + Thu_value + "/FRi=" + Fri_value + "/SAT=" + Sat_value + "/ SUN=" + Sun_value + "========");
        editor.commit();
        System.exit(0);// 현재 액티비티를 종료한다

    }

    @Override
    protected void onStart() {
        super.onStart();
        List<Todo> item = db.todoDao().getAll();
        tv_all_count.setText(String.valueOf(item.size()));
        int check = 0, progr = 0;
        for (int i = 0; i < item.size(); i++) {
            progr += Math.ceil(item.get(i).getProgr() / item.size());
            if (item.get(i).getProgr() >= 100) {
                check++;
            }
        }
        if (progr > 100) {
            progr = 100;
        }
        if (check == item.size()) {
            progr = 100;
        }
        if (item.size() == 0) {
            progr = 0;
        }
        tv_check_count.setText(String.valueOf(check));
        progressBar.setProgress(progr);
        tv_percent.setText(String.valueOf(progr) + "%");

        //현재 날짜 구하기
        Calendar calendar = Calendar.getInstance();
        int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);
        //날짜 임이로test 확인후 삭제
        //int dayOfweek = 1;
        switch (dayOfweek) {
            case 1:
                //일
                tv_day.setText("일요일"); //제목
                bt_Sun.setBackgroundColor(Color.rgb(153, 000, 255));//배경색
                tv_Sun.setText(tv_percent.getText()); //현제 퍼센트가져오기
                break;
            case 2: //월
                tv_day.setText("월요일");
                bt_Mon.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Mon.setText(tv_percent.getText());
                break;
            case 3://화
                tv_day.setText("화요일");
                bt_Tue.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Tue.setText(tv_percent.getText());
                break;
            case 4:
                tv_day.setText("수요일");
                bt_Wed.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Wed.setText(tv_percent.getText());
                break;
            case 5:
                tv_day.setText("목요일");
                bt_Thu.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Thu.setText(tv_percent.getText());
                break;
            case 6:
                tv_day.setText("금요일");
                bt_Fri.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Fri.setText(tv_percent.getText());
                break;
            case 7:
                tv_day.setText("토요일");
                bt_Sat.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Sat.setText(tv_percent.getText());
                break;
        }

    }
}



