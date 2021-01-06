package com.example.gogogal;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class CustomDialog  extends Dialog  implements  View.OnClickListener{

    private Context context;
    private  Button saveButton,cancelButton;
    private EditText et_text;

    AppDatabase db;

    private  CustomDialogListener customDialogListener;
    public CustomDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    interface  CustomDialogListener{
        void okClicked(String plan_name);
        void noClicked();
    }
    public void setDialogListener(CustomDialogListener customDialogListener){
        this.customDialogListener = customDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customdialog);

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        et_text = findViewById(R.id.et_plan_name);
         saveButton = findViewById(R.id.bt_ok);
         cancelButton = findViewById(R.id.bt_no);

        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_ok:
                String name = et_text.getText().toString();
                customDialogListener.okClicked(name);
                dismiss();
                break;
            case R.id.bt_no:
                cancel();
                break;
        }

    }
}