package com.example.background.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.background.R;
import com.example.background.module.User;
import com.example.background.module.User_Table;
import com.raizlabs.android.dbflow.sql.language.Select;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private EditText name;
    private EditText pass;
    private Button sign;
    private Button login;
    private User user;

    private static final int REQUESTCODE = 111;
    private static final int RESULTCODE = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.name);
        pass = findViewById(R.id.pass);
        sign = findViewById(R.id.new_user);
        login = findViewById(R.id.login);
        sp = this.getSharedPreferences("login",MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = name.getText().toString();
                String p = pass.getText().toString();
                if(!n.equals("")||!p.equals("")){
                    user = new Select()
                            .from(User.class)
                            .where(User_Table.name.is(n))
                            .querySingle();
                    if(p.equals(user.pass)){
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putString("name" , n);
                        ed.putString("portrait",user.portrait);
                        ed.putBoolean("isLogged",true);
                        ed.commit();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                    else Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(LoginActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(LoginActivity.this,SignInActivity.class),REQUESTCODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (resultCode == RESULTCODE) {
            if (requestCode == REQUESTCODE) {
                //设置结果显示框的显示数值
                name.setText(data.getStringExtra("name"));
                pass.setText(data.getStringExtra("pass"));
            }
        }
    }
}