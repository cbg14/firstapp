package com.example.gogogal;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;

public class ProfileActivity  extends AppCompatActivity {
//public List<Todo>  upDate(String title,int ex_all_count,int ex_set,int count_check,int progr ,int id_value);
    int progr = 0, count_check = 0 , ex_all_count=0, ex_set=0,id_value=0;
    String plan_name;
    private ProgressBar progressBar;
    private TextView progressText;
    private Button bt_plus, bt_dsc, bt_clear;
    private EditText et_exertcise_count, et_set_count, et_exercise_name;
    private TextView text_all_count, text_check_count;
    String ex_count = "0";
    private AdView mAdView;
    private  AppDatabase db;
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ring_probressbar);

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

        mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        progressBar = findViewById(R.id.progress_bar);
        progressText = findViewById(R.id.text_progress);
        text_all_count = findViewById(R.id.tv_all_count);
        text_check_count = findViewById(R.id.tv_check_count);
        et_exercise_name = findViewById(R.id.et_exercise_name);
        et_exertcise_count = findViewById(R.id.et_exercise_count);
        et_set_count = findViewById(R.id.et_set_count);
        bt_plus = findViewById(R.id.button_plus);
        bt_dsc = findViewById(R.id.button_dsc);
        bt_clear = findViewById(R.id.bt_clear);




        //db = Room.databaseBuilder(this,AppDatabase.class,"todo-db").allowMainThreadQueries().build();
        db= Room.databaseBuilder(this,AppDatabase.class,"todo-db").allowMainThreadQueries().build();



        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            plan_name  = extras.getString("plan_name");
        }

        System.out.println("====파일ProfileActivtity=====값넘어오는지 확인:"+plan_name);


        List<Todo> item = db.todoDao().getDate(plan_name);
        item.get(0).getEx_all_count();
        System.out.println("========item에 들어온 값 확인:"+item.toString());


        et_exercise_name.setText(item.get(0).getTitle());
        et_exertcise_count.setText(String.valueOf(item.get(0).getEx_all_count()));
        et_set_count.setText(String.valueOf(item.get(0).getEx_set()));
        progressText.setText(String.valueOf(item.get(0).getProgr())+"%");

        if( item.get(0).getProgr() ==0){
            progressBar.setProgress(0);
        }else{
            progressBar.setProgress(item.get(0).getProgr());
        }

        progr = item.get(0).getProgr();


        text_all_count.setText(et_exertcise_count.getText());
        text_check_count.setText(String.valueOf(item.get(0).getCount_check()));
        count_check = item.get(0).getCount_check();

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(100);
                progressText.setText("100%");
                text_check_count.setText(text_all_count.getText().toString());
                progr = 100;
                count_check = Integer.parseInt(text_all_count.getText().toString());
            }
        });
        /*텍스트 값 변경 감지 운동갯수나 세트수가 변경되면 차트 초기화시키기*/
        et_exertcise_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                progressBar.setProgress(0);
                progressText.setText("0%");
                progr = 0;
                text_all_count.setText(et_exertcise_count.getText());
                text_check_count.setText("0");
                count_check = 0;
            }
        });
        et_set_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                progressBar.setProgress(0);
                progressText.setText("0%");
                progr = 0;
                text_all_count.setText(et_exertcise_count.getText());
                text_check_count.setText("0");
                count_check = 0;
            }
        });



    }



    /*프로그램 종료됬을때 실행된다.*/
    @Override
    protected void onPause() {
        super.onPause();
        //public List<Todo>  upDate(String title,///int ex_all_count,///int ex_set,///int count_check,///int progr,///int id_value);
        List<Todo> item = db.todoDao().getDate(plan_name);
        db.todoDao().upDate(et_exercise_name.getText().toString(),Integer.valueOf(et_exertcise_count.getText().toString()),Integer.valueOf(et_set_count.getText().toString()),Integer.valueOf(text_check_count.getText().toString()),progr,item.get(0).getId());
        System.out.println("=======update되었습니다.======");
        System.out.println(db.todoDao().getDate(et_exercise_name.getText().toString()));

    }

    public void btn_all_clean(View view) {
        progressText.setText("0%");
        progressBar.setProgress(0);
        text_check_count.setText("0");
        progr = 0;
        count_check = 0;
    }

    public void btn_plus_click(View view) {
        if (count_check>=Integer.parseInt(et_exertcise_count.getText().toString())){
            count_check = Integer.parseInt(et_exertcise_count.getText().toString());
        }
        if (progr <= 100) {
            int A = Integer.parseInt(et_exertcise_count.getText().toString());//총 운동량
            float B = Integer.parseInt(et_set_count.getText().toString());//세트
            progr += Math.ceil(100 / B);  //총운동량/세트 값을 추가해주면된다0.
            progressBar.setProgress(progr);

            if (progr >= 100) {
                progressText.setText("100%");
                text_check_count.setText(String.valueOf(A));
                progr = 100;
                count_check = A;
            } else {
                progressText.setText(progr + "%");
                count_check += A / B;
                text_check_count.setText(String.valueOf(count_check));
            }

        }
    }

    public void btn_dsc_click(View view) {
        if (count_check <= 0) {
            count_check = 0;
        }
        if (progr >= 0) {
            int A = Integer.parseInt(et_exertcise_count.getText().toString());//총 운동량
            float B = Integer.parseInt(et_set_count.getText().toString());//세트
            progr -= Math.ceil(100 / B);

            progressBar.setProgress(progr);
            if (progr <= 0) {
                progr = 0;
                progressText.setText("0%");
                text_check_count.setText("0");
                count_check = 0;
            } else {
                progressText.setText(progr + "%");
                count_check = (int) (count_check - (A / B));
                text_check_count.setText(String.valueOf(count_check));
            }

        }
    }
}
