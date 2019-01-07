package com.example.q.cs496_2.asynctasks;

import android.os.AsyncTask;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONPost extends AsyncTask<String, String, String> {
    EditText insertName;
    EditText insertAddress;

    @Override
    protected String doInBackground(String... urls){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("user_name",insertName.getText());
            jsonObject.accumulate("user_Address",insertAddress.getText());

            HttpURLConnection con = null;
            BufferedReader reader = null;

            try{
                URL url = new URL(urls[0]);
                con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("Cache-Control", "no-cache");//캐시설정
                con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                con.setRequestProperty("Accept", "text/html");//서버에 response데이터를 html로 받음
                con.setDoOutput(true);// OutStream으로 post데이터를 넘겨주겠다는 의미
                con.setDoInput(true);// InputStream으로 서버로부터의 응답을 받겠다는 의미
                con.connect();

                //서버로 보내기 위한 stream생성
                OutputStream outStream = con.getOutputStream();
                //버퍼를 생성하고 넣음
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();//버퍼를 닫아줌

                //서버로 부터 데이터를 받음
                InputStream stream = con.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();//서버로부터 받은 값을 return해줌
            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                if(con != null){
                    con.disconnect();
                }
                try{
                    if(reader !=null){
                        reader.close();
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        //tvData.setText(result);//서버로부터 받은 값을 출력
    }
}