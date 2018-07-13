package ru.mysmartflat.kortros;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class AdapterNews extends BaseAdapter {

    private ArrayList<Realm_News> mListItems;
    private LayoutInflater mLayoutInflater;

    public AdapterNews(Context context, ArrayList<Realm_News> arrayList) {
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

        view = mLayoutInflater.inflate(R.layout.adapter_news, null);

        Realm_News objNews = (Realm_News) mListItems.get(position);

        TextView textView_TitleNews = (TextView) view.findViewById(R.id.TextView_title_news_ID);
        TextView textView_TextNews = (TextView) view.findViewById(R.id.textView_body_news_ID);
        ImageView Image_News = (ImageView) view.findViewById(R.id.imageView_news_ID);
        TextView textView_dt_news = (TextView) view.findViewById(R.id.textView_dt_news_ID);

        textView_TitleNews.setText(objNews.getStrTitle());
        textView_TextNews.setText(objNews.getStrText());
        textView_dt_news.setText(objNews.getDTMessage());


        if (objNews.img_bitmap != null) {

            try {

                Bitmap bitmap = BitmapFactory.decodeByteArray(objNews.getImgBitmap(), 0, objNews.getImgBitmap().length);
                Image_News.setImageBitmap(bitmap);

            } catch (Exception e){

            }

        }

        information info = new information(Image_News, position);
        info.execute(objNews.getImg());

        // if ((objChat.getLastDTMessage().length() > 0) && (objChat.getLastDTMessage().charAt(0) != '0') && (objChat.getStrMessage().trim() != "")){
        //     textView_LastMessage_Chats.setText(objChat.getStrMessage() + "\n" + getDate(Long.parseLong(objChat.getLastDTMessage()), "dd:MM:yyyy hh:mm"));
        // } else {
        //     textView_LastMessage_Chats.setText(objChat.getStrMessage());
        // }

        return view;
    }


    public class information extends AsyncTask<String, String, String> {

        Bitmap b;
        ImageView img;
        int position;

        public information(ImageView img, int position) {
            this.img = img;
            this.position = position;
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {

                URL url = new URL(arg0[0].toString());
                InputStream is = new BufferedInputStream(url.openStream());
                b = BitmapFactory.decodeStream(is);

            } catch(Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            try{

                if (b != null) {

                    img.setImageBitmap(b);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.WEBP, 100, baos);

                    Main.mRealm.beginTransaction();

                    Realm_News modelNews = (Realm_News) mListItems.get(position);
                    modelNews.setImgBitmap(baos.toByteArray());

                    Main.mRealm.commitTransaction();
                }

            } catch (Exception e){
                Main.mRealm.commitTransaction();
            }
        }
    }
}