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
import com.example.hanulproject.main.statusTask.Boiler_on_off;
import com.example.hanulproject.main.statusTask.StatusSelect;
import com.example.hanulproject.vo.StatusVO;

public class HomeBoilerView extends Fragment {
    LinearLayout onBoiler;
    LinearLayout offBoiler;
    StatusVO vo = Second_fragment.statusVO;

    @Override
    public void onResume() {
        super.onResume();
        status_refresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.home_boiler_view_fragment, container, false);
        onBoiler = rootview.findViewById(R.id.onboiler);
        offBoiler = rootview.findViewById(R.id.offboiler);

        offBoiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("statusTest",vo.getAutoWindow());
                if(vo.getAutoWindow().equals("N")) {
                    Boiler_on_off boiler_on_off = new Boiler_on_off(Second_fragment.dialog);
                    boiler_on_off.execute();

                    status_refresh();
                }else{
                    Toast.makeText(getContext(), "집 감지모드를 Off 해주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });

        onBoiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("statusTest",vo.getAutoWindow());
                if(vo.getAutoWindow().equals("N")) {
                    Boiler_on_off boiler_on_off = new Boiler_on_off(Second_fragment.dialog);
                    boiler_on_off.execute();

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

        onBoiler.setVisibility(View.GONE);
        offBoiler.setVisibility(View.GONE);

        if (vo.getBoiler().equals("Y")){
            onBoiler.setVisibility(View.VISIBLE);
            offBoiler.setVisibility(View.GONE);
        }else{
            offBoiler.setVisibility(View.VISIBLE);
            onBoiler.setVisibility(View.GONE);
        }
    }
}
