package ru.mysmartflat.kortros;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class AdapterIpoteka extends BaseAdapter {

    private ArrayList<ModelIpoteka> mListItems;
    private LayoutInflater mLayoutInflater;

    public AdapterIpoteka(Context context, ArrayList<ModelIpoteka> arrayList) {
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

        view = mLayoutInflater.inflate(R.layout.adapter_ipoteka, null);

        ModelIpoteka objIpoteka = (ModelIpoteka) mListItems.get(position);

        TextView textView_Title = (TextView) view.findViewById(R.id.textView_ipoteka_title_ID);

        TextView textView_Text = (TextView) view.findViewById(R.id.textView_ipoteka_text_ID);

        ImageView Image_News = (ImageView) view.findViewById(R.id.imageView_ipoteka_ID);


        textView_Title.setText(objIpoteka.getTitle());

        ArrayList<String> text = new ArrayList<String>(objIpoteka.getText());

        String s="";

        for(int i =0; i < text.size(); i++){

            s += text.get(i)+"\n";

        }

        textView_Text.setText(s);

        if (objIpoteka.img_logo != null) {

            try{

                Bitmap bitmap = BitmapFactory.decodeByteArray(objIpoteka.getImgLogo(), 0, objIpoteka.getImgLogo().length);
                Image_News.setImageBitmap(bitmap);

            } catch (Exception e){

            }

        } //else {

            AdapterIpoteka.information info = new AdapterIpoteka.information(Image_News, position);
            info.execute(objIpoteka.getLogo());

            //}

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

                    ModelIpoteka objIpoteka = (ModelIpoteka) mListItems.get(position);

                    //if (objIpoteka.getImgLogo().length > 100) {

                        Main.mRealm.beginTransaction();
                        objIpoteka.setImgLogo(baos.toByteArray());
                        Main.mRealm.commitTransaction();

                    //}
                }

            } catch (Exception e){

            }
        }
    }
}