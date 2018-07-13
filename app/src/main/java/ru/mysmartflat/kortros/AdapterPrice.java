package ru.mysmartflat.kortros;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmResults;

public class AdapterPrice extends BaseAdapter {

    private ArrayList<ModelPrice> mListItems;
    private LayoutInflater mLayoutInflater;

    public AdapterPrice(Context context, ArrayList<ModelPrice> arrayList) {
        mListItems = arrayList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        view = mLayoutInflater.inflate(R.layout.adapter_price, null);

        ModelPrice objPrice = (ModelPrice) mListItems.get(position);

        TextView textView_Title = (TextView) view.findViewById(R.id.TextView_Title_ID);
        textView_Title.setText(objPrice.getTitle());

        TextView textView_Text = (TextView) view.findViewById(R.id.TextView_Text_ID);
        textView_Text.setText(objPrice.getText());

        TextView textView_Flats_Count = (TextView) view.findViewById(R.id.textView_Count_ID);
        textView_Flats_Count.setText(objPrice.getCountText());

        return view;
    }

}