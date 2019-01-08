package com.example.q.cs496_2.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.q.cs496_2.views.Listviewitem;
import com.example.q.cs496_2.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<Listviewitem> listViewItemList = new ArrayList<Listviewitem>();

    public ListViewAdapter(){

    }

    @Override
    public int getCount() {return listViewItemList.size();}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem, parent, false);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.nameItem);
        TextView addressTextView = (TextView) convertView.findViewById(R.id.adressItem);

        Listviewitem listViewItem = listViewItemList.get(position);

        nameTextView.setText(listViewItem.getName());
        addressTextView.setText(listViewItem.getaddress());

        return convertView;
    }

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public Listviewitem getItem(int position) {return listViewItemList.get(position);}

    public void addItem(String name, String address, String check){
        Listviewitem item = new Listviewitem();

        item.setAddress(address);
        item.setName(name);
        item.setContacts_check(check);
        Log.d("ada@",item.getName() + " / " + item.getaddress() + " / " + item.getContacts_check());

        listViewItemList.add(item);
    }
    // Contacts에서 받은 것은 check 변수가 무조건 a type
    public void addItemContacts(String name, String address){
        Listviewitem item = new Listviewitem();

        item.setAddress(address);
        item.setName(name);
        Log.d("ada@",item.getName() + " / " + item.getaddress() + " / " + item.getContacts_check());

        listViewItemList.add(item);
    }

    public void remove(int position){
        listViewItemList.remove(position);
    }
}
