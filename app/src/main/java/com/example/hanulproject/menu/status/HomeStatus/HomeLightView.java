package com.example.hanulproject.menu.status.HomeStatus;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hanulproject.R;
import com.example.hanulproject.main.Second_fragment;
import com.example.hanulproject.main.statusTask.Light_on_off;
import com.example.hanulproject.main.statusTask.StatusSelect;
import com.example.hanulproject.vo.StatusVO;

public class HomeLightView extends Fragment {

    LinearLayout on;
    LinearLayout off;
    StatusVO vo = Second_fragment.statusVO;

    @Override
    public void onResume() {
        super.onResume();
        status_refresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.home_light_view_fragment, container, false);
        on = rootview.findViewById(R.id.onlight);
        off = rootview.findViewById(R.id.offlight);


        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("statusTest",vo.getAutoWindow());
                if(vo.getAutoWindow().equals("N")) {
                    Light_on_off light_on_off = new Light_on_off(Second_fragment.dialog);
                    light_on_off.execute();

                    status_refresh();
                }else{
                    Toast.makeText(getContext(), "집 감지모드를 Off 해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("statusTest",vo.getAutoWindow());
                if(vo.getAutoWindow().equals("N")) {
                    Light_on_off light_on_off = new Light_on_off(Second_fragment.dialog);
                    light_on_off.execute();

                    status_refresh();
                }else{
                    Toast.makeText(getContext(), "집 감지모드를 Off 해주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return rootview;
    }

    private void status_refresh(){

        StatusSelect statusSelect = new StatusSelect();
        statusSelect.setProgressDialog(Second_fragment.dialog);
        try {
            vo = statusSelect.execute().get();
        }catch (Exception e) {
            e.printStackTrace();
        }

        on.setVisibility(View.GONE);
        off.setVisibility(View.GONE);

        if (vo.getLight().equals("Y")){
            on.setVisibility(View.VISIBLE);
            off.setVisibility(View.GONE);
        }else{
            off.setVisibility(View.VISIBLE);
            on.setVisibility(View.GONE);
        }
    }
}
