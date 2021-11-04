package com.example.diary;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class login extends AppCompatActivity {
    private TextView tv1;
    private TextView tv2;
    private EditText et1;
    private Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);
        et1=findViewById(R.id.et1);
        bt1=findViewById(R.id.bt1);
    }
    /**
    登录
     */
    public void login(View view){
        //实例化SharedPreferences对象（第一步）
        SharedPreferences mySharedPreferences= getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据,得到作者名
        editor.putString("author", et1.getText().toString());
        //提交当前数据
        editor.commit();
        Intent intent = new Intent(login.this, MainActivity.class);
        startActivity(intent);
    }
}
