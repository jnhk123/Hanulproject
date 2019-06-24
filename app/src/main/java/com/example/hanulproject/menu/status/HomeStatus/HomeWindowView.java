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
import com.example.hanulproject.main.statusTask.StatusSelect;
import com.example.hanulproject.main.statusTask.Window_on_off;
import com.example.hanulproject.vo.StatusVO;


public class HomeWindowView extends Fragment {
    private LinearLayout onWindow;
    private LinearLayout offWindow;
    StatusVO vo = Second_fragment.statusVO;

    @Override
    public void onResume() {

        status_refresh();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.home_window_view_fragment, container, false);


        onWindow = rootview.findViewById(R.id.onwindow);
        offWindow = rootview.findViewById(R.id.offwindow);

        onWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("statusTest",vo.getAutoWindow());
                if(vo.getAutoWindow().equals("N")) {
                    Window_on_off window_on_off = new Window_on_off(Second_fragment.dialog,1 );
                    window_on_off.execute();

                    status_refresh();
                }else{
                    Toast.makeText(getContext(), "집 감지모드를 Off 해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        offWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("statusTest",vo.getAutoWindow());
                if(vo.getAutoWindow().equals("N")) {
                    Window_on_off window_on_off = new Window_on_off(Second_fragment.dialog,2 );
                    window_on_off.execute();

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

        onWindow.setVisibility(View.GONE);
        offWindow.setVisibility(View.GONE);

        if (vo.getWindow().equals("O")){
            onWindow.setVisibility(View.VISIBLE);
            offWindow.setVisibility(View.GONE);
        }else{
            offWindow.setVisibility(View.VISIBLE);
            onWindow.setVisibility(View.GONE);
        }
    }
}
