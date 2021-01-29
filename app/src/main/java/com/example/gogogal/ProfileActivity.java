package com.example.gogogal;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity  {
    //public List<Todo>  upDate(String title,int ex_all_count,int ex_set,int count_check,int progr ,int id_value);
    int progr = 0, count_check = 0, ex_all_count = 0, ex_set = 0, id_value = 0;
    String plan_name;
    private ProgressBar progressBar;
    private TextView progressText;
    private Button bt_plus, bt_dsc, bt_clear;
    private EditText et_exertcise_count, et_set_count, et_exercise_name;
    private TextView text_all_count, text_check_count, tv_one_set;
    String ex_count = "0";
    private AdView mAdView;
    private AppDatabase db;
    private DayDatabase db_day;
    private SumDatabase db_sum;
    Toolbar toolbar;
    int curday, year, month, day,one_set;
    String id ,first_1set,language;
    private ArrayList<Todo> arrayList;

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

        //현재 시스템 언어가 어떤값인지 알아내는 코드
        Locale locale =getResources().getConfiguration().locale;
        language =locale.getLanguage();

        if(language.equals("ko")){
            first_1set="1세트";
        }else{
            first_1set="1set:";
        }


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
        tv_one_set = findViewById(R.id.tv_one_set);

        //db = Room.databaseBuilder(this,AppDatabase.class,"todo-db").allowMainThreadQueries().build();
        db = AppDatabase.getInstance(this);
        db_day = DayDatabase.getInstance(this);
        db_sum = SumDatabase.getInstance(this);

        //recycleAdaper에서 인테트하기전에 값 넘긴거 받는 코딩
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            plan_name = extras.getString("plan_name");
            id = extras.getString("id");
        }

        System.out.println("====파일ProfileActivtity=====값넘어오는지 확인:" + plan_name + "/ID=" + id);


        List<Todo> item = db.todoDao().getDate(plan_name);
        item.get(0).getEx_all_count();
        System.out.println("========item에 들어온 값 확인:" + item.toString());

        //tv_one_set.setText("1세트:" + item.get(0).getEx_all_count() / item.get(0).getEx_set());

        //이름
        et_exercise_name.setText(item.get(0).getTitle());
        et_exertcise_count.setText(String.valueOf(item.get(0).getEx_all_count()));
        et_set_count.setText(String.valueOf(item.get(0).getEx_set()));
        //프로그래스바 퍼센트 텍스트뷰
        progressText.setText(String.valueOf(item.get(0).getProgr()) + "%");

        if (item.get(0).getProgr() == 0) {
            progressBar.setProgress(0);
        } else {
            progressBar.setProgress(item.get(0).getProgr());
        }

        progr = item.get(0).getProgr();


        text_all_count.setText(String.valueOf(item.get(0).getEx_all_count()));
        text_check_count.setText(String.valueOf(item.get(0).getCount_check()));
        count_check = item.get(0).getCount_check();

        one_set = Integer.valueOf(String.valueOf(et_exertcise_count.getText())) / Integer.valueOf(String.valueOf(et_set_count.getText()));
        tv_one_set.setText(tv_one_set.getText()+""+String.valueOf(one_set));

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
        et_exertcise_count.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_exertcise_count.setText("");
                } else {
                    if(et_exertcise_count.length() ==0 || et_exertcise_count.getText().toString().equals("0")) {
                        et_exertcise_count.setText("1");
                         one_set = Integer.valueOf(String.valueOf(et_exertcise_count.getText())) / Integer.valueOf(String.valueOf(et_set_count.getText()));
                        System.out.println("===one_set:"+one_set);
                        tv_one_set.setText(tv_one_set.getText()+""+String.valueOf(one_set));
                    }else{
                        et_exertcise_count.getText();
                        int one_set = Integer.valueOf(String.valueOf(et_exertcise_count.getText())) / Integer.valueOf(String.valueOf(et_set_count.getText()));
                        System.out.println("===one_set:"+one_set);
                        tv_one_set.setText(tv_one_set.getText()+""+String.valueOf(one_set));
                    }

                }

            }
        });
        et_set_count.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    et_set_count.setText("");
                } else {
                    if(et_set_count.length() ==0 || et_set_count.getText().toString().equals("0")) {
                        et_set_count.setText("1");
                        int one_set = Integer.valueOf(String.valueOf(et_exertcise_count.getText())) / Integer.valueOf(String.valueOf(et_set_count.getText()));
                        System.out.println("===one_set:"+one_set);
                        tv_one_set.setText(tv_one_set.getText()+""+String.valueOf(one_set));
                    }else{
                        int one_set = Integer.valueOf(String.valueOf(et_exertcise_count.getText())) / Integer.valueOf(String.valueOf(et_set_count.getText()));
                        System.out.println("===one_set:"+one_set);
                        tv_one_set.setText(tv_one_set.getText()+""+String.valueOf(one_set));
                    }



                }

            }
        });
        et_exertcise_count.addTextChangedListener(textWatcher);
        et_set_count.addTextChangedListener(textWatcher);


        //현재 날짜 구하기
        Calendar calendar = Calendar.getInstance();
        curday = calendar.get(Calendar.DAY_OF_WEEK);

          year = calendar.get(Calendar.YEAR);
          month = calendar.get(Calendar.MONTH)+1;
          day  = calendar.get(Calendar.DATE);

        //날짜 임의로 변경 TEST
         //curday=5;
         //day=27;


        //키보드 inputype 마지막 엔터시 남아있는 깜빡거리는 문제를 키보드 내리고 포커스를 초기화시켜주면서 해결
        et_set_count.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    et_set_count.requestFocus();
                    InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    et_set_count.clearFocus();



                    return  true;
                }
                return false;
            }
        });



    }


    /*프로그램 종료됬을때 실행된다.*/
    @Override
    protected void onPause() {
        super.onPause();
        if (et_exertcise_count.getText().toString() == "") {
            et_exertcise_count.setText("0");
        }
        if (et_set_count.getText().toString() == "") {
            et_set_count.setText("0");
        }
        //public List<Todo>  upDate(String title,///int ex_all_count,///int ex_set,///int count_check,///int progr,///int id_value);
        if (et_exertcise_count.getText().toString().equals("")) {
            et_exertcise_count.setText("0");
        }
        if (et_set_count.getText().toString().equals("")) {
            et_set_count.setText("0");
        }
        List<Todo> item = db.todoDao().getDate(plan_name);
        List<Sum> item3 = db_sum.sumDao().getDate(plan_name);

        //업데이트 이름,총 개수,세트수,체크수,퍼센트값,아이디,day
        db.todoDao().upDate(et_exercise_name.getText().toString(), Integer.valueOf(et_exertcise_count.getText().toString()), Integer.valueOf(et_set_count.getText().toString()),
                Integer.valueOf(text_check_count.getText().toString()), progr, item.get(0).getId(), curday,year,month,day);

        System.out.println("======Todo db가update되었습니다.======");
        System.out.println("===" + db.todoDao().getDate(et_exercise_name.getText().toString()));
        List<Todo_Day> item2 = db_day.todo_dayDao().getDate(plan_name);
        if (item2.size() > 0) {
            System.out.println("===item2 getID=" + item2.get(item2.size() - 1).getId());
            System.out.println("===item2 에서 가져온 값=" + item2.toString());
            db_day.todo_dayDao().UPDATE(et_exercise_name.getText().toString(), progr, item2.get(item2.size() - 1).getId());
        }

        if(item3.size()>0) {
            db_sum.sumDao().upDate_name(et_exercise_name.getText().toString(), plan_name);
        }


        System.out.println("======Todo db_Day가update되었습니다.======");
        System.out.println("===" + db_day.todo_dayDao().getDate(et_exercise_name.getText().toString()));
        System.out.println("===SUM update =>" + db_sum.sumDao().getDate(et_exercise_name.getText().toString()));

    }

    public void btn_all_clean(View view) {
        progressText.setText("0%");
        progressBar.setProgress(0);
        text_check_count.setText("0");
        progr = 0;
        count_check = 0;
    }

    public void btn_plus_click(View view) {
        if (count_check >= Integer.parseInt(et_exertcise_count.getText().toString())) {
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

    //1. TouchEvent가 들어올때마다 최근 포커스를 받은 View가 무엇인지 파악한다.
    //2. 만약 최근 포커스를 받은 View가 EditText고 Touch된 위치가 EditText가 아니라면 EditText의 Foucs를 clearFoucs() 한다.
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            View v = getCurrentFocus();
            if(v instanceof EditText){
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if(!outRect.contains((int)ev.getRawX(),(int)ev.getRawY())){
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);


                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

//    public void bt_set_info(View v) {
//        Toast.makeText(this.getApplicationContext(), "세트는 개수는 한번에 완료할지 분활해서 완료할지 정한느거랍니당~ \n ex)개수 100 세트 10은 1세트+시 10개  \nex2)개수100 세트1은 1세트시 100 \n한번에 완료하고싶으면 세트0,1을 주며됩니다.", Toast.LENGTH_LONG).show();
//    }

    private final TextWatcher textWatcher = new TextWatcher() {
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
            tv_one_set.setText(first_1set);
        }
    };


}
