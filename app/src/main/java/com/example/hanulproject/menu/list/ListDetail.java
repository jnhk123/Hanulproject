package com.example.hanulproject.menu.list;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hanulproject.R;
import com.example.hanulproject.task.task.Delete;
import com.example.hanulproject.task.task.detail.MemberListDetailCall;
import com.example.hanulproject.vo.UserVO;

public class ListDetail extends AppCompatActivity {

    Button back, delete;
    UserVO vo;
    TextView id, pw, name, addr, email;
    ImageView profile;
    LinearLayout pw_layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_detail);

        profile=findViewById(R.id.profile);
        id=findViewById(R.id.uid);
        pw=findViewById(R.id.upw);
        name=findViewById(R.id.uname);
        addr=findViewById(R.id.uaddr);
        email=findViewById(R.id.uemail);
        pw_layout= findViewById(R.id.pw_layout);

        delete=findViewById(R.id.udelete);
        back=findViewById(R.id.back);
        // 뒤로가기
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        vo= (UserVO) getIntent().getSerializableExtra("vo");
        //수정화면 넘어가는 곳

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(ListDetail.this);
                builder.setTitle("탈퇴여부");
                builder.setMessage("정말 탈퇴시키시겠습니까?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Delete delete=new Delete(vo.getId(), 5);
                        delete.execute();
                        reset();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }});
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
//화면 가져와서 뿌려주는 곳
    @Override
    protected void onResume(){
        super.onResume();
        MemberListDetailCall detail=new MemberListDetailCall(vo.getId());
        try{
            vo=detail.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        id.setText(vo.getId());
        pw.setText(vo.getPw());
        name.setText(vo.getName());
        addr.setText(vo.getAddr());
        email.setText(vo.getEmail());
        pw_layout.setVisibility(View.GONE);
    }
//리셋 메소드
    void reset(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("result","result");
        setResult(RESULT_OK,resultIntent);
        finish();
    }
}
