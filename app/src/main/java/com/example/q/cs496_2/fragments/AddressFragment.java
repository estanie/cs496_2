package com.example.q.cs496_2.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.q.cs496_2.R;
import com.example.q.cs496_2.adapters.ListViewAdapter;
import com.example.q.cs496_2.asynctasks.JSONGet;
import com.google.gson.JsonIOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class AddressFragment extends Fragment {
    ListView listView;
    String result;
    ListViewAdapter adapter = new ListViewAdapter();
    EditText insertName;
    EditText insertAddress;

    public AddressFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_address, container, false);

        insertName = (EditText) view.findViewById(R.id.InsertName);
        insertAddress= (EditText) view.findViewById(R.id.InsertAddress);
        Button btn_up = (Button) view.findViewById(R.id.httpPOST);
        Button btn_down = (Button) view.findViewById(R.id.httpGET);
        listView = (ListView) view.findViewById(R.id.Listview);

        btn_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                new com.example.q.cs496_2.asynctasks.JSONPost(insertName.getText().toString(), insertAddress.getText().toString()).execute("http://socrip4.kaist.ac.kr:2780/api/books");//AsyncTask시작

            }
        });

        btn_down.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    result = new com.example.q.cs496_2.asynctasks.JSONGet().execute("http://socrip4.kaist.ac.kr:2780/api/books").get();

                    adapter = new ListViewAdapter();
                    Log.d("%%%%", result);
                    JSONArray dataArrange = new JSONArray(result);
                    String numint = String.valueOf(dataArrange.length());
                    Log.d("%%%%", numint);
                    String[] name = new String[dataArrange.length()];
                    String[] address = new String[dataArrange.length()];
                    for (int i = 0; i < dataArrange.length(); i++) {
                        JSONObject jsonObject = dataArrange.getJSONObject(i);
                        name[i] = jsonObject.getString("user_name");
                        address[i] = jsonObject.getString("user_Address");
                        adapter.addItem(name[i], address[i], "a");
                    }
                    //adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
}
