package com.example.gogogal;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private Button bt_week_day, bt_Mon, bt_Tue, bt_Wed, bt_Thu, bt_Fri, bt_Sat, bt_Sun;
    private AdView mAdView;
    ProgressBar progressBar;
    TextView tv_percent, tv_check_count, tv_all_count,nav_hearder;
    private AppDatabase db;
    private  DayDatabase db_day;
    private SumDatabase db_sum;
    private Calendar_Database db_calendar;
    TextView tv_day, tv_Mon, tv_Tue, tv_Wed, tv_Thu, tv_Fri, tv_Sat, tv_Sun;
    String shared = "0",language;
    Toolbar toolbar;
    int connect_date=0;//접속일
    int done=0; //목표달성

    //네이게이션바
    private DrawerLayout drawerLayout;
    private Context context = this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start__view);

        //현재 시스템 언어가 어떤값인지 알아내는 코드
        Locale locale =getResources().getConfiguration().locale;
        language =locale.getLanguage();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        //광고
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        db=AppDatabase.getInstance(this);
        db_day= DayDatabase.getInstance(this);
        db_sum = SumDatabase.getInstance(this);
        db_calendar = Calendar_Database.getINSTANCE(this);


//        db_sum.sumDao().upDate("push up100",300,1);
//        db_sum.sumDao().upDate("Drink water10",100,2);
//        //db_calendar.todo_calendarDao().insert(new Todo_calendar(2021,1,28,"push up100",100));-
//        db_calendar.todo_calendarDao().UPDATE("Drink water10",4);

        bt_week_day = findViewById(R.id.bt_week_day);
        progressBar = findViewById(R.id.progress_bar);
        tv_percent = findViewById(R.id.text_progress);
        tv_check_count = findViewById(R.id.tv_check_count);
        tv_all_count = findViewById(R.id.tv_all_count);

        //네비 헤더바
        nav_hearder = findViewById(R.id.nav_hearder);

        //////////
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView =findViewById(R.id.nav_view);

        //현재 날짜 구하기
        Calendar calendar = Calendar.getInstance();
        int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK); // 1=일 2=월 3=화 4=수 5=목 6=금 7=토
        System.out.println("===dayofweek값:"+dayOfweek);
        int year = calendar.get(Calendar.YEAR); //년도 가져오기
        int month = calendar.get(Calendar.MONTH)+1; //월 가져오기
        int day = calendar.get(Calendar.DAY_OF_MONTH); // 일 가져오기
        //요일 임이로test //날자변경 테스트
        // dayOfweek = 1;
        //day =27;
        //지난 요일에 값 넣기 간단하게 값저장하기


        //네빙게이션바 아이템 클릭시 화면이동과 이벤트
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                int id = item.getItemId();
                String title = item.getTitle().toString();


                if(id == R.id.Todo_calendar){
                    Intent intent= new Intent(getApplicationContext(),calendar.class);
                    intent.putExtra("Day",dayOfweek);
                    startActivity(intent);
                }
                if(id == R.id.Todo_SUM) {
                    Intent intent  = new Intent(getApplicationContext(),Todo_Sum.class);
                    startActivity(intent);
                }

                return true;
            }
        });
        View nav_header_view = navigationView.getHeaderView(0);
        nav_hearder = nav_header_view.findViewById(R.id.nav_hearder);


        /////////

        //버튼 각요일 //텍스트
        bt_Mon = findViewById(R.id.bt_mon);
        bt_Tue = findViewById(R.id.bt_tue);
        bt_Wed = findViewById(R.id.bt_wed);
        bt_Thu = findViewById(R.id.bt_thu);
        bt_Fri = findViewById(R.id.bt_fri);
        bt_Sat = findViewById(R.id.bt_sat);
        bt_Sun = findViewById(R.id.bt_sun);
        //일주일 확인
        tv_day = findViewById(R.id.tv_day);
        tv_Mon = findViewById(R.id.tv_mon);
        tv_Tue = findViewById(R.id.tv_tue);
        tv_Wed = findViewById(R.id.tv_wed);
        tv_Thu = findViewById(R.id.tv_thu);
        tv_Fri = findViewById(R.id.tv_fri);
        tv_Sat = findViewById(R.id.tv_sat);
        tv_Sun = findViewById(R.id.tv_sun);


        List<Todo> item = db.todoDao().getAll();
        List<Todo_Day> item2= db_day.todo_dayDao().getAll();
        List<Sum> item_sum =db_sum.sumDao().getAll();
        List<Todo_calendar> calendarList =db_calendar.todo_calendarDao().getAll();
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

        //목표설정 이벤트
        bt_week_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Every_day_Activity.class);
                startActivity(intent);
            }
        });

        //일주일 버튼 클릭시 각 인텐트 설정
        bt_Sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),Day_info.class);
                intent.putExtra("Day",1);
                startActivity(intent);
            }
        });
        bt_Mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),Day_info.class);
                intent.putExtra("Day",2);
                startActivity(intent);
            }
        });
        bt_Tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),Day_info.class);
                intent.putExtra("Day",3);
                startActivity(intent);
            }
        });
        bt_Wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),Day_info.class);
                intent.putExtra("Day",4);
                startActivity(intent);
            }
        });
        bt_Thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),Day_info.class);
                intent.putExtra("Day",5);
                startActivity(intent);
            }
        });
        bt_Fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),Day_info.class);
                intent.putExtra("Day",6);
                startActivity(intent);
            }
        });
        bt_Sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),Day_info.class);
                intent.putExtra("Day",7);
                startActivity(intent);
            }
        });



        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        String Mon_value = sharedPreferences.getString("Mon", "");
        String Tue_value = sharedPreferences.getString("Tue", "");
        String Wed_value = sharedPreferences.getString("Wed", "");
        String Thu_value = sharedPreferences.getString("Thu", "");
        String Fri_value = sharedPreferences.getString("Fri", "");
        String Sat_value = sharedPreferences.getString("Sat", "");
        String Sun_value = sharedPreferences.getString("Sun", "");
        //connect_date = sharedPreferences.getInt("connect_date",0);
        done = sharedPreferences.getInt("done",0);

        //dayofweek(현재요일)가 저장된 날짜보다 뒷 난날짜면 초기화 시켜야한다.
        //시작일인 일요일이 되면 초기화 하고 시작
        System.out.println("===todo_day의 size:"+item2.size());
        if(item2.size() != 0) {
            if (dayOfweek<item2.get(0).getDay() || dayOfweek<item2.get(item2.size()-1).getDay() ) {
                System.out.println("====일요일이거나/ 시작날보다 현재요일이 작으면 초기화");
                db_day.todo_dayDao().deleteAll();
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
                tv_Sun.setText(re_Sun_value);
                tv_Mon.setText(re_Mon_value);
                tv_Tue.setText(re_Tue_value);
                tv_Wed.setText(re_Wed_value);
                tv_Thu.setText(re_Thu_value);
                tv_Fri.setText(re_Fri_value);
                tv_Sat.setText(re_Sat_value);
                tv_Sun.setText(re_Sun_value);
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
        }

//        if (item.size() > 0) {
//            System.out.println("========dakofweek값:" + dayOfweek + "/ last_Day값:" + item.get(0).getDay());
//        }
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
                System.out.println("==="+db.todoDao().getAll().toString());
                System.out.println("===업데이트하기전 전에 값으로 통계 더하기");
                for (int i = 0; i < item.size(); i++) {
                    for(int a=0; a<item_sum.size(); a++){
                        //만약 통계에 있는 이름이 있을시 초기화 전에 데이터를 합친다.
                        if(item_sum.get(a).getTitle().equals(item.get(i).getTitle())){
                            db_sum.sumDao().upDate(item_sum.get(a).getTitle(),(item_sum.get(a).getSum()+item.get(i).getCount_check()),item_sum.get(a).getId());
                        }
                    }
                    if(item.get(i).getProgr() ==100){
                        done++;
                    }
                    db_calendar.todo_calendarDao().insert(new Todo_calendar(item.get(i).getYear(),item.get(i).getMonth(),item.get(i).getDay_date(),item.get(i).getTitle(),item.get(i).getProgr()));
                    db.todoDao().upDate(item.get(i).getTitle(), item.get(i).getEx_all_count(), item.get(i).getEx_set(), 0, 0, item.get(i).getId(), dayOfweek,year,month,day);
                    db_day.todo_dayDao().insert(new Todo_Day(item.get(i).getTitle(),0,dayOfweek));
                }

                System.out.println("============업데이트후==========");
                System.out.println("==="+db.todoDao().getAll().toString());
                System.out.println("==="+db_day.todo_dayDao().getAll().toString());
                System.out.println("==="+db_sum.sumDao().getAll().toString());
                System.out.println("==="+db_calendar.todo_calendarDao().getAll().toString());
            }
        }

        //몇일동안 실행했는지 계산
        //현재 날짜를 초로 계산 그리고 실행했을때 처음 날짜를 초로 계산해서
        //현재날짜-실행날자 계산
        Date d = new Date();
        if(calendarList.size()>0){
            List<Todo_calendar> test123 = db_calendar.todo_calendarDao().select(1);
            System.out.println("==="+test123.toString());
            long time1 = getTimeInMillis(calendarList.get(0).getYear(),calendarList.get(0).getMonth()-1,calendarList.get(0).getWeek_day()); //처음 등록된날짜
            long time2 = calendar.getTimeInMillis();//현재날짜 가져오기

            d.setTime(time1);
            System.out.println("===처음등록날짜:"+d);
            d.setTime(time2);
            System.out.println("===현재날짜:"+d);
            //임의로 날짜 변경 테스트
            //long test = getTimeInMillis(2021,1,25); //현재날짜  month 0부터 1월임
            //d.setTime(test);
            //System.out.println("==="+d);
            //System.out.println("===오늘날짜:"+test/86400000);

            long result = (int)(time2/86400000) - (int)(time1/86400000);
           // long result = (int)(test-time1)/86400000;
           // System.out.println("===처음등록날짜:"+(int)(time1/86400000) +"/ 현재날짜 :"+(int)(time2/86400000));
         //   System.out.println("===현재날짜-등록날자:"+ (time2-time1));
            //18,652 18,666
            //long result = time2-test;
            //d.setTime(result);
            System.out.println("===현재날짜 -실행날짜 계산 값은:"+((int)(result)));
            //네비 헤드바
            if(language.equals("ko")){
                nav_hearder.setText(String.valueOf((int)result)+"일동안 "+String.valueOf(done)+" 목표달성");
            }else{
                nav_hearder.setText("For "+String.valueOf((int)result)+"days "+String.valueOf(done)+" Achieve the goal");
                            }

        }else{{
            if (language.equals("ko")){
                nav_hearder.setText("0일동안 "+"0목표달성");
            }else {
                nav_hearder.setText("For 0days "+" 0Achieve the goal");
            }

        }}





        System.out.println("===현제 Todo데이터 리스트===");
        System.out.println("==="+db.todoDao().getAll().toString());
        System.out.println("===현제 Todo_Day데이터 리스트===");
        System.out.println("==="+db_day.todo_dayDao().getAll().toString());



        //해당하는 요일에 실행되도록
        switch (dayOfweek) {
            case 1:
                if(language.equals("ko")){
                    tv_day.setText("일요일"); //제목
                }else{
                    tv_day.setText("Sunday"); //제목
                }

                bt_Sun.setBackgroundColor(Color.rgb(153, 000, 255));//배경색
                tv_Sun.setText(tv_percent.getText()); //현제 퍼센트가져오기
                break;
            case 2: //월
                if(language.equals("ko")){
                    tv_day.setText("월요일");
                }else{
                    tv_day.setText("Monday");
                }
                bt_Mon.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Mon.setText(tv_percent.getText());
                break;
            case 3://화
                if(language.equals("ko")){
                    tv_day.setText("화요일");
                }else{
                    tv_day.setText("Tuesday");
                }
                bt_Tue.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Tue.setText(tv_percent.getText());
                break;
            case 4:
                if(language.equals("ko")){
                    tv_day.setText("수요일");
                }else{
                    tv_day.setText("Wednesday");
                }
                bt_Wed.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Wed.setText(tv_percent.getText());
                break;
            case 5:
                if(language.equals("ko")){
                    tv_day.setText("목요일");
                }else{
                    tv_day.setText("Thursday");
                }
                bt_Thu.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Thu.setText(tv_percent.getText());
                break;
            case 6:
                if(language.equals("ko")){
                    tv_day.setText("금요일");
                }else{
                    tv_day.setText("Friday");
                }
                bt_Fri.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Fri.setText(tv_percent.getText());
                break;
            case 7:
                if(language.equals("ko")){
                    tv_day.setText("토요일");
                }else{
                    tv_day.setText("Saturday");
                }
                bt_Sat.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Sat.setText(tv_percent.getText());
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        done = done;
        connect_date = connect_date;
        System.out.println("=====종료됩니다.=====");
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String Mon_value = tv_Mon.getText().toString();
        String Tue_value = tv_Tue.getText().toString();
        String Wed_value = tv_Wed.getText().toString();
        String Thu_value = tv_Thu.getText().toString();
        String Fri_value = tv_Fri.getText().toString();
        String Sat_value = tv_Sat.getText().toString();
        String Sun_value = tv_Sun.getText().toString();
//        connect_date = connect_date;
        done = done;
        editor.putString("Mon", Mon_value);
        editor.putString("Tue", Tue_value);
        editor.putString("Wed", Wed_value);
        editor.putString("Thu", Thu_value);
        editor.putString("Fri", Fri_value);
        editor.putString("Sat", Sat_value);
        editor.putString("Sun", Sun_value);
//        editor.putInt("connect_date",connect_date);
        editor.putInt("done",done);
        System.out.println("========SharedPreferences종료시점 각 요일 퍼센트 확인========");
        System.out.println("====SUN=" + Sun_value+"/ Mon="+ Mon_value + "/ Tue=" + Tue_value + "/Wed=" + Wed_value + "/Thu=" + Thu_value + "/FRi=" + Fri_value + "/SAT=" + Sat_value +"=======");
        editor.commit();


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
        //dayOfweek =1;

        switch (dayOfweek) {
            case 1:
                if(language.equals("ko")){
                    tv_day.setText("일요일"); //제목
                }else{
                    tv_day.setText("Sunday"); //제목
                }

                bt_Sun.setBackgroundColor(Color.rgb(153, 000, 255));//배경색
                tv_Sun.setText(tv_percent.getText()); //현제 퍼센트가져오기
                break;
            case 2: //월
                if(language.equals("ko")){
                    tv_day.setText("월요일");
                }else{
                    tv_day.setText("Monday");
                }
                bt_Mon.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Mon.setText(tv_percent.getText());
                break;
            case 3://화
                if(language.equals("ko")){
                    tv_day.setText("화요일");
                }else{
                    tv_day.setText("Tuesday");
                }
                bt_Tue.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Tue.setText(tv_percent.getText());
                break;
            case 4:
                if(language.equals("ko")){
                    tv_day.setText("수요일");
                }else{
                    tv_day.setText("Wednesday");
                }
                bt_Wed.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Wed.setText(tv_percent.getText());
                break;
            case 5:
                if(language.equals("ko")){
                    tv_day.setText("목요일");
                }else{
                    tv_day.setText("Thursday");
                }
                bt_Thu.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Thu.setText(tv_percent.getText());
                break;
            case 6:
                if(language.equals("ko")){
                    tv_day.setText("금요일");
                }else{
                    tv_day.setText("Friday");
                }
                bt_Fri.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Fri.setText(tv_percent.getText());
                break;
            case 7:
                if(language.equals("ko")){
                    tv_day.setText("토요일");
                }else{
                    tv_day.setText("Saturday");
                }
                bt_Sat.setBackgroundColor(Color.rgb(153, 000, 255));
                tv_Sat.setText(tv_percent.getText());
                break;
        }

    }
    public static long getTimeInMillis(int year ,int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        return calendar.getTimeInMillis();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        System.out.println("--------눌린다");
        //return super.onOptionsItemSelected(item);
        drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }


}



