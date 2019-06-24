package com.example.hanulproject.menu.status;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.JsonReader;

import com.example.hanulproject.login.LoginRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import static com.example.hanulproject.task.common.CommonMethod.ipConfig;

public class CheckStatusWindowAuto extends AsyncTask<Void,Void,Boolean> {

    @Override
    protected Boolean doInBackground(Void... voids) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        HttpResponse httpResponse = null;
        HttpEntity httpEntity = null;
        String check = "";

        String postURL = ipConfig + "/AA/autoWindowSetting?id="+ LoginRequest.vo.getId();

        try {
            //MultipartEntityBuilder 생성
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

            check = readJsonStream(inputStream);

            if(check.equals("Y")){
                return true;
            }else if(check.equals("N")){
                return false;
            }

        }catch (Exception e){

        }finally {
            ((AndroidHttpClient) httpClient).close();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }


    private String readJsonStream(InputStream inputStream) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        String check = "";

        try {
            reader.beginObject();
            while (reader.hasNext()){
                String readStr = reader.nextName();
                if(readStr.equals("result")){
                    check = reader.nextString();
                }else{
                    reader.skipValue();
                }
            }
            reader.endObject();

        }finally {
            reader.close();
        }

        return check;
    }
}
