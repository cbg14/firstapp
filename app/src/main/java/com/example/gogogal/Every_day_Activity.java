package com.example.gogogal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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
    private DayDatabase db_day;
    private SumDatabase db_sum;
    private TextView tv_progs, text_done;
    Toolbar toolbar;
    int progs=0;
    int cur_day,day_date,year,month;


    private  Context context;
    String name ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_day_);

        //툴바 뒤로가기 만들기 //매니패스트에 등록을 해줘야함  parentActivityName 돌아갈 부보 액티비티 정해줘야한다!!
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //룸 DB 사용해보기
        //데이터 베이스 객체 생성 기존방식
        //db = Room.databaseBuilder(this,AppDatabase.class,"todo-db").allowMainThreadQueries().build();

        //싱글톤으로 db생성
        db=AppDatabase.getInstance(this);
        db_day=DayDatabase.getInstance(this);
        db_sum=SumDatabase.getInstance(this);

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
        recylerAdapter = new RecylerAdapter(arrayList, this);
        recyclerView.setAdapter(recylerAdapter);

        List<Todo> item = db.todoDao().getAll();
        List<Todo_Day> item2 = db_day.todo_dayDao().getAll();

        System.out.println("=====Everyday=====");
        System.out.println("======Todo에 담긴 데이터=======");
        System.out.println("===="+item.toString());
        System.out.println("======Todo_Day에 담긴 데이터=======");
        System.out.println("===="+item2.toString());

        for(int i=0; i<item.size();i++){
            RecyclerData recyclerData = new RecyclerData(item.get(i).getTitle(),item.get(i).getProgr());
            arrayList.add(recyclerData);
        }

        //현재 날짜 구하기
        Calendar calendar  =Calendar.getInstance();
        cur_day = calendar.get(Calendar.DAY_OF_WEEK); // 1=일 2=월 3=화 4=수 5=목 6=금 7=토
        year = calendar.get(Calendar.YEAR);
        month =calendar.get(Calendar.MONTH)+1;
        day_date=calendar.get(Calendar.DATE);
        //임이로 날짜 테스트
        //cur_day =5;
        //day_date=27;

        System.out.println("===현재날짜는"+year+"년"+month+"월"+day_date+"일");

        //아이템 추가버튼 클릭
        item_add = findViewById(R.id.item_add);
        item_add.setOnClickListener(this::onClick);

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onstart실행");
        System.out.println("===="+db.todoDao().getAll().toString());
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
                        System.out.println("========Evert_Day_Activity===========");
                        //만약 동일한 아이디가 저장될경우 못하도록
                        List<Todo> item = db.todoDao().getDate(plan_name);
                        List<Sum> item_sum= db_sum.sumDao().getDate(plan_name);
                        if(item.size()>0){
                            System.out.println("===이미 동일한 이름이 저장되어 있습니다.");
                            Toast.makeText(getApplicationContext(),plan_name+"의 동일한 이름이 등록되어있습니다.",Toast.LENGTH_LONG).show();
                        }else {
                            System.out.println("===동일한 이름이 없으므로 새로 추가됩니다.");
                            db.todoDao().insert(new Todo(plan_name,1,1,0,0,cur_day,year,month,day_date));
                            db_day.todo_dayDao().insert(new Todo_Day(plan_name,0,cur_day));
                            arrayList.add(recyclerData);
                        }

                        if(item_sum.size()>0){

                        }else{
                            db_sum.sumDao().insert(new Sum(plan_name,0));
                            System.out.println("===db_sum추가"+db_sum.sumDao().getDate(plan_name));
                        }



                        System.out.println("=======Todo db에 삽입되었습니다.");
                        System.out.println("==="+db.todoDao().getAll().toString());
                        System.out.println("=======Todo_day db에 삽입되었습니다.");
                        System.out.println("==="+db_day.todo_dayDao().getAll().toString());
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