package com.example.hanulproject.menu.notice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hanulproject.R;
import com.example.hanulproject.menu.Menu_main;
import com.example.hanulproject.task.adapter.NoticeAdapter;
import com.example.hanulproject.task.task.Select;
import com.example.hanulproject.vo.NoticeVO;

import java.io.Serializable;
import java.util.ArrayList;

public class Notice_main extends Fragment implements Serializable {

    Menu_main activity;
    ListView listView;
    Select select;
    ArrayList<NoticeVO> nlist;
    NoticeAdapter adapter;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.notice_main, container, false);
        listView=rootView.findViewById(R.id.nlist_view);

        nlist = new ArrayList<>();
        adapter = new NoticeAdapter(getActivity(), R.layout.notice_list_view, nlist);
        listView.setAdapter(adapter);

        select = new Select(nlist, adapter);
        select.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoticeVO vo = (NoticeVO) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), NoticeDetail.class);
                intent.putExtra("vo", vo);
                startActivity(intent);
            }
        });

        return rootView;
    }

}
