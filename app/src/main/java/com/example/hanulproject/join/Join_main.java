package com.example.hanulproject.join;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.hanulproject.R;
import com.example.hanulproject.login.Login;
import com.example.hanulproject.login.Login_menu;
import com.example.hanulproject.login.Login_page;

import org.w3c.dom.Text;

import static com.example.hanulproject.task.common.CommonMethod.isNetworkConnected;


public class Join_main extends AppCompatActivity {

    ToggleButton checkBtn;
    Button joinBtn;
    int check = 0;
    EditText name, id, pw, pwc, email;
    TextView jpwcompl, jpwfail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_main);

        checkBtn = findViewById(R.id.checkBtn);
        joinBtn = findViewById(R.id.realJoin);
        name =findViewById(R.id.joinName);
        id=findViewById(R.id.joinId);
        pw=findViewById(R.id.joinPw);
        pwc=findViewById(R.id.joinPwr);
        email=findViewById(R.id.joinEmail);
        jpwcompl=findViewById(R.id.jpwcompl);
        jpwfail=findViewById(R.id.jpwfail);



        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBtn.isChecked()) {
                    check = 1;
                    checkBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.check));
                    joinBtn.setBackground(getResources().getDrawable(R.drawable.btn_on));
                    joinBtn.setTextColor(getResources().getColor(R.color.titlecolor));
                }else{
                    check = 0;
                    checkBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.noncheck));
                    joinBtn.setBackground(getResources().getDrawable(R.drawable.btn_gray));
                    joinBtn.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        //비밀번호 두개 비교해서 일치
        pwc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password=pw.getText().toString();
                String confirm=pwc.getText().toString();
                if(password.equals(confirm)){
                    jpwcompl.setVisibility(View.VISIBLE);
                    jpwfail.setVisibility(View.GONE);
                }else{
                    jpwcompl.setVisibility(View.GONE);
                    jpwfail.setVisibility(View.VISIBLE);
                }
                if(pw.getText().toString().length()==0 || pwc.getText().toString().length()==0){
                    jpwcompl.setVisibility(View.GONE);
                    jpwfail.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().length()==0){
                    Toast.makeText(Join_main.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                    return;
                }
                if(id.getText().toString().length()==0){
                    Toast.makeText(Join_main.this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                    id.requestFocus();
                    return;
                }
                if(email.getText().toString().length()==0){
                    Toast.makeText(Join_main.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                    id.requestFocus();
                    return;
                }
                if(pw.getText().toString().length()==0){
                    Toast.makeText(Join_main.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    pw.requestFocus();
                    return;
                }
                if(pwc.getText().toString().length()==0){
                    Toast.makeText(Join_main.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    pwc.requestFocus();
                    return;
                }

                if (isNetworkConnected(Join_main.this)==true){
                    /* 회원가입 처리할 부분 */
                    String jname=name.getText().toString();
                    String jid=id.getText().toString();
                    String jpw=pw.getText().toString();
                    String jemail=email.getText().toString();


                    Intent intent = new Intent(Join_main.this, Login_page.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    JoinInsert insert=new JoinInsert(jname, jid, jpw, jemail);
                    insert.execute();
                    Toast.makeText(Join_main.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    startActivity(intent);

                    finish();

                }else{
                    Toast.makeText(Join_main.this, "내용을 확인해 주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Join_main.this, Login.class);
        startActivity(intent);
        finish();
    }
}

