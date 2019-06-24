package com.example.hanulproject.login;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanulproject.R;
import com.example.hanulproject.join.Join_main;
import com.example.hanulproject.task.common.CommonMethod;
import com.example.hanulproject.task.task.Update;
import com.example.hanulproject.vo.UserVO;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Date;

import static com.example.hanulproject.task.common.CommonMethod.ipConfig;

public class Member_modify extends AppCompatActivity {

    ImageView modify_profile;
    Button modify_photoLoad, memberModify_update, back, modify_photoDelete;
    EditText modify_name, modify_pw, modify_change_pw, modify_check_pw;
    UserVO vo = LoginRequest.vo;
    TextView rpwcheck, mpwc, mpwf;

    final int LOAD_IMAGE = 1001;

    String uploadType, imageFilePathA, imageUploadPathA, uploadFileName;
    boolean is_check = false;

    long now;
    Date date;
    java.text.SimpleDateFormat tmpDateFormat;

    @Override
    protected void onResume() {
        super.onResume();
        loadImage();
        if(is_check){
            loadImage();
            is_check = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_modify_activity);
        uploadType="";
        rpwcheck=findViewById(R.id.rpwcheck);
        now = System.currentTimeMillis();
        date = new Date(now);
        tmpDateFormat = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss");
        mpwc=findViewById(R.id.mpwcomplete);
        mpwf=findViewById(R.id.mpwfail);
        modify_profile = findViewById(R.id.modify_profile);
        modify_photoLoad = findViewById(R.id.modify_photoLoad);
        modify_photoDelete = findViewById(R.id.modify_photoDelete);
        modify_name = findViewById(R.id.modify_name);
        modify_pw = findViewById(R.id.modify_pw);
        modify_change_pw = findViewById(R.id.modify_change_pw);
        modify_check_pw = findViewById(R.id.modify_check_pw);
        memberModify_update = findViewById(R.id.memberModify_update);
        back = findViewById(R.id.back);

        modify_name.setText(vo.getName());
        modify_profile.setImageResource(R.drawable.emptyprofile);
        
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        modify_photoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modify_profile.setImageResource(R.drawable.emptyprofile);
                LoginRequest.vo.setProfile("");
                LoginRequest.vo.setProfileName("");
                uploadType = "delete";
            }
        });

        modify_photoLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE);
            }
        });
        //두개 비교시 일치확인
        //비밀번호 확인 일치
        modify_check_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password=modify_change_pw.getText().toString();
                String confirm=modify_check_pw.getText().toString();
                if(password.equals(confirm)){
                    mpwc.setVisibility(View.VISIBLE);
                    mpwf.setVisibility(View.GONE);
                }else{
                    mpwc.setVisibility(View.GONE);
                    mpwf.setVisibility(View.VISIBLE);
                }
                if(confirm.equals(password)){
                    mpwc.setVisibility(View.VISIBLE);
                    mpwf.setVisibility(View.GONE);
                }else{
                    mpwc.setVisibility(View.GONE);
                    mpwf.setVisibility(View.VISIBLE);
                }
                if(modify_change_pw.getText().toString().length()==0 || modify_check_pw.getText().toString().length()==0){
                    mpwc.setVisibility(View.GONE);
                    mpwf.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        //비밀번호 일치
        modify_change_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password=modify_change_pw.getText().toString();
                String confirm=modify_check_pw.getText().toString();
                if(password.equals(confirm)){
                    mpwc.setVisibility(View.VISIBLE);
                    mpwf.setVisibility(View.GONE);
                }else{
                    mpwc.setVisibility(View.GONE);
                    mpwf.setVisibility(View.VISIBLE);
                }
                if(confirm.equals(password)){
                    mpwc.setVisibility(View.VISIBLE);
                    mpwf.setVisibility(View.GONE);
                }else{
                    mpwc.setVisibility(View.GONE);
                    mpwf.setVisibility(View.VISIBLE);
                }
                if(modify_change_pw.getText().toString().length()==0 || modify_check_pw.getText().toString().length()==0){
                    mpwc.setVisibility(View.GONE);
                    mpwf.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        memberModify_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modify_name.getText().toString().length()==0){
                    Toast.makeText(Member_modify.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    modify_name.requestFocus();
                    return;
                }
                if(!(modify_pw.getText().toString().equals(vo.getPw()))){
                    //Toast.makeText(Member_modify.this, "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    rpwcheck.setVisibility(View.VISIBLE);
                    modify_pw.requestFocus();
                    return;
                }
                rpwcheck.setVisibility(View.GONE);
                if(modify_change_pw.getText().toString().length()==0){
                    Toast.makeText(Member_modify.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    modify_change_pw.requestFocus();
                    return;
                }
                if(modify_check_pw.getText().toString().length()==0){
                    Toast.makeText(Member_modify.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    modify_check_pw.requestFocus();
                    return;
                }
                if(!(modify_change_pw.getText().toString().equals(modify_check_pw.getText().toString()))){
                    Toast.makeText(Member_modify.this, "새로운 비밀번호와 비밀번호 확인이 서로 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    modify_change_pw.requestFocus();
                    return;
                }
                vo.setName(modify_name.getText().toString());
                vo.setPw(modify_change_pw.getText().toString());

                Log.d("PWCHECK", vo.getPw());
                Update update = new Update(vo);
                update.setFileInfo(uploadType, imageFilePathA, imageUploadPathA, uploadFileName);
                update.execute();
                is_check = true;
                LoginRequest.vo.setName(modify_name.getText().toString());
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_IMAGE && resultCode == RESULT_OK) {
            uploadType = "image";

            try {
                String path = "";
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    // Get the path from the Uri
                    path = getPathFromURI(selectedImageUri);
                }
                // 이미지 돌리기 및 리사이즈
                Bitmap newBitmap = CommonMethod.imageRotateAndResize(path);
                if (newBitmap != null) {
                    modify_profile.setImageBitmap(newBitmap);
                } else {
                    Toast.makeText(this, "이미지가 null 입니다...", Toast.LENGTH_SHORT).show();
                }
                if(vo.getProfileName() == null || vo.getProfileName().equals("")){
                    imageFilePathA = path;
                    int ProfileUpdate = Log.d("ProfileUpdate", "imageFilePathA Path : " + imageFilePathA);
                    uploadFileName = tmpDateFormat.format(date)+(imageFilePathA.split("/")[imageFilePathA.split("/").length - 1]);
                    imageUploadPathA = ipConfig + "/AA/resources/images/profile/" + uploadFileName;
                    Log.d("ProfileUpdate",uploadFileName);
                }else {
                    imageFilePathA = path;
                    Log.d("ProfileUpdate", "imageFilePathA Path : " + imageFilePathA);
                    uploadFileName = vo.getProfileName();
                    imageUploadPathA = ipConfig + "/AA/resources/images/profile/" + vo.getProfileName();
                    Log.d("ProfileUpdate",uploadFileName);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d("ComplainInsert => ", "imagepath is null, whatever something is wrong!!");
        }
    }

    // Get the real path from the URI
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void loadImage(){
        //로그인 프로필 받아오기
        if(vo.getProfile() != null && !(vo.getProfile().equals(""))){
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .discCacheFileNameGenerator(new Md5FileNameGenerator())
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .writeDebugLogs() // Remove for release app
                    .build();
            ImageLoader.getInstance().init(config);

            modify_profile.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(LoginRequest.vo.getProfile(),
                    modify_profile, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {
//                            viewHolder.progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
//                            viewHolder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                            viewHolder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {
//                            viewHolder.progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }
}
