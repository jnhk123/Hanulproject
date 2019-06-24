package com.example.hanulproject.menu.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.hanulproject.R;
import com.example.hanulproject.firebase.CheckAllPushState;
import com.example.hanulproject.firebase.CheckCommentPushState;
import com.example.hanulproject.firebase.TokenSetting;
import com.example.hanulproject.firebase.TokenSettingAll;
import com.example.hanulproject.login.LoginRequest;
import com.example.hanulproject.login.Member_modify;
import com.example.hanulproject.menu.Menu_main;
import com.example.hanulproject.menu.status.CheckStatusWindowAuto;
import com.example.hanulproject.menu.status.StatusWindowAuto;

public class Settings_main extends Fragment {
    Menu_main activity;
    TextView memberModify;
    Switch commentPush;
    Switch allPush;
    Switch autoWindow;

    boolean on = false; // 최초 실행시 버튼 체인지 막음

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Menu_main) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        autoWindow();
        commentCheck();
        allCheck();
        on = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.settings_main, container, false);
        memberModify = rootView.findViewById(R.id.memberModify);
        commentPush = rootView.findViewById(R.id.commentPush);
        allPush = rootView.findViewById(R.id.allPush);
        autoWindow = rootView.findViewById(R.id.autoWindow);

        if(LoginRequest.vo.isLogintype()){
            memberModify.setVisibility(View.VISIBLE);
        }else{
            memberModify.setVisibility(View.GONE);
        }

        memberModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Member_modify.class);
                startActivity(intent);
            }
        });

        commentPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(on) {
                    TokenSetting tokenSetting = new TokenSetting(LoginRequest.vo.getId());
                    tokenSetting.execute();
                }
            }
        });

        allPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(on){
                    TokenSettingAll tokenSettingAll = new TokenSettingAll(LoginRequest.vo.getId());
                    tokenSettingAll.execute();
                }
            }
        });

        autoWindow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(on){
                    StatusWindowAuto statusWindowAuto = new StatusWindowAuto(LoginRequest.vo.getId());
                    statusWindowAuto.execute();
                }
            }
        });

        return rootView;
    }

    //댓글알림기능 on/off 체크
    private void commentCheck(){
        boolean check = true;

        CheckCommentPushState checkCommentPushState = new CheckCommentPushState();
        try {
            check = checkCommentPushState.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (check){
            commentPush.setChecked(true);
        }else{
            commentPush.setChecked(false);
        }
    }

    //전체알림기능 on/off 체크
    private void allCheck(){
        boolean check = true;

        CheckAllPushState checkAllPushState = new CheckAllPushState();
        try {
            check = checkAllPushState.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (check){
            allPush.setChecked(true);
        }else{
            allPush.setChecked(false);
        }
    }

    private void autoWindow(){
        boolean check = true;

        CheckStatusWindowAuto checkStatusWindowAuto = new CheckStatusWindowAuto();
        try {
            check = checkStatusWindowAuto.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(check){
            autoWindow.setChecked(true);
        }else{
            autoWindow.setChecked(false);
        }
    }
}
