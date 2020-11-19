package com.example.background.activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.background.R;
import com.example.background.module.User;

public class SignInActivity extends AppCompatActivity {
    private EditText name;
    private EditText pass;
    private Button sign;
    private User user;
    private static final int RESULTCODE = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        name = findViewById(R.id.name);
        pass = findViewById(R.id.pass);
        sign = findViewById(R.id.sign);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = name.getText().toString();
                String p = pass.getText().toString();
                if(!n.equals("")||!p.equals("")){
                    User user = new User();
                    user.name = n;
                    user.pass = p;
                    user.save();
                    Intent intent = new Intent();
                    intent.putExtra("name", n); //将计算的值回传回去
                    intent.putExtra("pass", p);
                    // 通过intent对象返回结果，必须要调用一个setResult方法，
                    // setResult(888, data);第一个参数表示结果返回码，一般只要大于1就可以
                    setResult(RESULTCODE, intent);
                    finish(); //结束当前的activity的生命周期
                }
            }
        });

    }
}