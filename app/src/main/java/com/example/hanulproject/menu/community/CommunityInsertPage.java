package com.example.hanulproject.menu.community;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hanulproject.R;
import com.example.hanulproject.menu.complain.ComplainInsertPage;
import com.example.hanulproject.task.common.CommonMethod;
import com.example.hanulproject.task.task.Insert;
import com.example.hanulproject.vo.CommunityVO;

import java.io.File;
import java.util.Date;

import static com.example.hanulproject.task.common.CommonMethod.ipConfig;
import static com.example.hanulproject.task.common.CommonMethod.isNetworkConnected;

public class CommunityInsertPage extends AppCompatActivity {
    ImageView cmiImageView;
    Button insert, cancel, cmiPhotonLoad, btnPhotoDelete;
    EditText cmititle, cmicontent;
    CommunityVO vo;
    String title = "", content = "";

    String uploadType, imageFilePathA, imageUploadPathA, uploadFileName;

    long now;
    Date date;
    java.text.SimpleDateFormat tmpDateFormat;

    final int CAMERA_REQUEST = 1000;
    final int LOAD_IMAGE = 1001;
    final int VIDEO_REQUEST = 1003;
    final int LOAD_VIDEO = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_insert);

        now = System.currentTimeMillis();
        date = new Date(now);
        tmpDateFormat = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss");

        insert = findViewById(R.id.insert);
        cancel =findViewById(R.id.cancel);
        cmititle = findViewById(R.id.cmititle);
        cmicontent = findViewById(R.id.cmicontent);
        cmiPhotonLoad = findViewById(R.id.cmiPhotonLoad);
        cmiImageView = findViewById(R.id.cmiImageView);
        btnPhotoDelete = findViewById(R.id.btnPhotoDelete);

        cmiImageView.setVisibility(View.GONE);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cmititle.getText().toString().length()==0){
                    Toast.makeText(CommunityInsertPage.this, "제목을 입력하세요.", Toast.LENGTH_SHORT).show();
                    cmititle.requestFocus();
                    return;
                }
                if(cmicontent.getText().toString().length()==0){
                    Toast.makeText(CommunityInsertPage.this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                    cmicontent.requestFocus();
                    return;
                }

                if (isNetworkConnected(getApplication())==true){
                    title = cmititle.getText().toString();
                    content = cmicontent.getText().toString();
                    Insert insert = new Insert(title, content, 3,uploadType, imageFilePathA, imageUploadPathA, uploadFileName);
                    insert.execute();
                    reset();
                }else{
                    Toast.makeText(getApplication(), "인터넷이 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cmiPhotonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmiImageView.setVisibility(View.VISIBLE);

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE);
            }
        });

        btnPhotoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadType = "";
                imageFilePathA = "";
                imageUploadPathA = "";
                uploadFileName = "";
                cmiImageView.setVisibility(View.GONE);
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
                    cmiImageView.setImageBitmap(newBitmap);
                } else {
                   Toast.makeText(this, "이미지가 null 입니다...", Toast.LENGTH_SHORT).show();
                }
                imageFilePathA = path;
                int communityInsert = Log.d("CommunityInsert", "imageFilePathA Path : " + imageFilePathA);
                uploadFileName = tmpDateFormat.format(date)+(imageFilePathA.split("/")[imageFilePathA.split("/").length - 1]);
                imageUploadPathA = ipConfig + "/AA/resources/images/community/" + uploadFileName;
                Log.d("CommunityInsert",uploadFileName);

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

    void reset(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("result","result");
        setResult(RESULT_OK,resultIntent);
        finish();
    }

}
