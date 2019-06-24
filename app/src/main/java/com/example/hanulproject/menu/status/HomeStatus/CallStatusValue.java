package com.example.hanulproject.menu.status.HomeStatus;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

import com.example.hanulproject.login.LoginRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.InputStream;
import java.nio.charset.Charset;

import static com.example.hanulproject.task.common.CommonMethod.ipConfig;

public class CallStatusValue extends AsyncTask<Void,Void,Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        HttpResponse httpResponse = null;
        HttpEntity httpEntity = null;

        String postURL = ipConfig + "/AA/readerCurValue?id="+ LoginRequest.vo.getId();

        try {
            //MultipartEntityBuild  생성
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setCharset(Charset.forName("UTF-8"));

            //전송
            InputStream inputStream = null;
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(postURL);
            httpPost.setEntity(builder.build());

            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

//            vo = readJsonStream(inputStream);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ((AndroidHttpClient) httpClient).close();
        }
        return null;
    }
}
