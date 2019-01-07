package com.example.q.cs496_2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.q.cs496_2.R;

public class AddressFragment extends Fragment {
    ListView listView;

    public AddressFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_address,container,false);
        Button btn_up = (Button) view.findViewById(R.id.httpPOST);
        //GetData = (TextView)findViewById(R.id.GetView);
        Button btn_down = (Button) view.findViewById(R.id.httpGET);

        listView = (ListView) view.findViewById(R.id.Listview);

        btn_up.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                new com.example.q.cs496_2.asynctasks.JSONPost().execute("http://socrip4.kaist.ac.kr:2780/api/books");//AsyncTask시작
            }
        });

        btn_down.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                new com.example.q.cs496_2.asynctasks.JSONGet().execute("http://socrip4.kaist.ac.kr:2780/api/books");
            }
        });

        return view;
    }

    private void refresh(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }
}
