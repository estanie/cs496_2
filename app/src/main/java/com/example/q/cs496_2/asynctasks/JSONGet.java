package com.example.q.cs496_2.asynctasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import com.example.q.cs496_2.adapters.ListViewAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONGet extends AsyncTask<String, String, String> {

    ListView listView;
    private ListViewAdapter adapter = new ListViewAdapter();
    String []dataFromMongo = new String[1];

    @Override
    protected String doInBackground(String... urls) {
        try {
            HttpURLConnection con = null;
            BufferedReader reader = null;

            try{
                //URL url = new URL("http://192.168.25.16:3000/users");
                URL url = new URL(urls[0]);
                con = (HttpURLConnection) url.openConnection();
                con.connect();

                InputStream stream = con.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(con != null){
                    con.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        dataFromMongo[0] = result;
        //GetData.setText(result);
        try {
            adapter = new ListViewAdapter();
            Log.d("%%%%", dataFromMongo[0]);
            JSONArray dataArrange = new JSONArray(dataFromMongo[0]);
            String numint = String.valueOf(dataArrange.length());
            Log.d("%%%%", numint);
            String [] name = new String[dataArrange.length()];
            String [] address = new String[dataArrange.length()];
            for(int i =0; i<dataArrange.length(); i++){
                JSONObject jsonObject = dataArrange.getJSONObject(i);
                name[i] = jsonObject.getString("user_name");
                address[i] = jsonObject.getString("user_Address");
                adapter.addItem(name[i],address[i],"a");
            }
            //adapter.notifyDataSetChanged();
            adapter.addItem("abcd","0101010","a");
            listView.setAdapter(adapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("%%%%", dataFromMongo[0]);
    }
}