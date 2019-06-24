package com.example.hanulproject.task.task.detail;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.JsonReader;

import com.example.hanulproject.vo.UserVO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.example.hanulproject.task.common.CommonMethod.ipConfig;

public class MemberListDetailCall extends AsyncTask<Void,Void, UserVO> {

    String id;
    public static UserVO vo= new UserVO();


    public MemberListDetailCall(String id){
        this.id=id;
    }

    String postURL=null;

    @Override
    protected UserVO doInBackground(Void... voids) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        HttpResponse httpResponse = null;
        HttpEntity httpEntity = null;

        postURL = ipConfig + "/AA/udetail?id="+id;

        try {
            //MultipartEntityBuild  생성
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            //전송
            InputStream inputStream = null;
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(postURL);
            httpPost.setEntity(builder.build());

            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

            return readJsonStream(inputStream);

        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if(httpEntity != null){
                httpEntity = null;
            }
            if(httpResponse != null){
                httpResponse = null;
            }
            if(httpPost != null){
                httpPost = null;
            }
            ((AndroidHttpClient) httpClient).close();
        }
        return null;
    }
    @Override
    protected void onPostExecute(UserVO userVO) {
        super.onPostExecute(userVO);
    }

    private UserVO readJsonStream(InputStream inputStream) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        UserVO vo = null;
        reader.beginObject();
        try {
            vo = readMessage(reader);
        }finally {
            reader.close();
        }
        return vo;
    }


    private UserVO readMessage(JsonReader reader) throws IOException {
        String name = "", phone="", addr="", id="", pw="", email="",  result="", profile="";

        while (reader.hasNext()){
            String readStr = reader.nextName();
            if(readStr.equals("result")){
                result = reader.nextString();
                vo.setResult(result);
            }else if(readStr.equals("name")){
                name = reader.nextString();
                vo.setName(name);
                vo.setResult("success");
            }else if (readStr.equals("phone")){
                phone = reader.nextString();
                vo.setPhone(phone);
            }else if (readStr.equals("addrs")){
                addr = reader.nextString();
                vo.setAddr(addr);
            }else if (readStr.equals("id")){
                id = reader.nextString();
                vo.setId(id);
            }else if (readStr.equals("pw")){
                pw = reader.nextString();
                vo.setPw(pw);
            }else if (readStr.equals("email")){
                email = reader.nextString();
                vo.setEmail(email);
            }else if(readStr.equals("profile")){
                profile = reader.nextString();
                vo.setProfile(profile);
            }else{
                reader.skipValue();
            }
        }
        reader.endObject();

        return new UserVO(id,pw,name,email,addr,profile);
    }
}
