package com.example.hanulproject.menu.status;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.InputStream;
import java.nio.charset.Charset;

import static com.example.hanulproject.task.common.CommonMethod.ipConfig;

public class StatusWindowAuto extends AsyncTask<Void,Void,Void> {
    String id;

    public StatusWindowAuto(String id){
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String postURL;

        HttpClient httpClient = null;
        HttpPost httpPost = null;
        HttpResponse httpResponse = null;
        HttpEntity httpEntity = null;

        postURL = ipConfig + "/AA/controll_train?id=" + id;

        try {
            //MultipartEntityBuilder 생성
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setCharset(Charset.forName("UTF-8"));

            InputStream inputStream = null;
            httpClient = AndroidHttpClient.newInstance("Android");

            httpPost = new HttpPost(postURL);
            httpPost.setEntity(builder.build());
            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

        }catch (Exception e){

        }finally {
            ((AndroidHttpClient) httpClient).close();
        }

        return null;
    }
}
