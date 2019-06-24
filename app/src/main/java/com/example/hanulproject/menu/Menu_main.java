package com.example.hanulproject.menu;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hanulproject.R;
import com.example.hanulproject.menu.community.Community_main;
import com.example.hanulproject.menu.complain.Complain_main;
import com.example.hanulproject.menu.list.List_main;
import com.example.hanulproject.menu.notice.Notice_main;
import com.example.hanulproject.menu.settings.Settings_main;

public class Menu_main extends AppCompatActivity {

    Notice_main notice = new Notice_main();
    Community_main community = new Community_main();
    Complain_main complain = new Complain_main();
    List_main list = new List_main();
    Settings_main settings = new Settings_main();
    Button back;
    TextView bartext;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        bartext = findViewById(R.id.bartext);

        //백버튼
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // 페이지 전환 처리
        Intent intent = getIntent();
        int selectKey = intent.getIntExtra("selectKey",99999);

        if(selectKey == 1){ onFragmentChange(1); }
        if(selectKey == 2){ onFragmentChange(2); }
        if(selectKey == 3){ onFragmentChange(3); }
        if(selectKey == 4){ onFragmentChange(4); }
        if(selectKey == 5){ onFragmentChange(5); }
        if(selectKey == 6){ onFragmentChange(6); }

        Log.d("abc" , "" + selectKey);
    }

    public void onFragmentChange(int i) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();

        if(i==1){
            bartext.setText("공지사항");
            ft.addToBackStack(null);
            ft.replace(R.id.container,notice).commit();
        }else if(i==2){
            bartext.setText("고객센터");
            ft.addToBackStack(null);
            ft.replace(R.id.container,complain).commit();
        } else if(i==3){
            bartext.setText("커뮤니티");
            ft.addToBackStack(null);
            ft.replace(R.id.container,community).commit();
        } else if(i==4){
            bartext.setText("설정하기");
            ft.addToBackStack(null);
            ft.replace(R.id.container,settings).commit();
        } else if(i==5){
            bartext.setText("회원목록");
            ft.addToBackStack(null);
            ft.replace(R.id.container,list).commit();
        }
    }
}
