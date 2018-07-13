package ru.mysmartflat.kortros;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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

public class AdapterFlats extends BaseAdapter {

    private ArrayList<Realm_Flats> mListItems;
    private LayoutInflater mLayoutInflater;

    public AdapterFlats(Context context, ArrayList<Realm_Flats> arrayList) {
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

        view = mLayoutInflater.inflate(R.layout.adapter_flats, null);

        Realm_Flats objFlats = (Realm_Flats) mListItems.get(position);

        TextView textView_flats_rooms = (TextView) view.findViewById(R.id.textView_flats_rooms_ID);
        textView_flats_rooms.setText(objFlats.getRooms()+" ком. \u2022 "+objFlats.getSquare().toString()+" м\u00B2 \u2022");

        TextView textView_flats_floors = (TextView) view.findViewById(R.id.textView_flats_floors_ID);
        textView_flats_floors.setText("этаж "+objFlats.getFloor().toString()+"/"+objFlats.getFloorTotal());

        TextView textView_TextSmall = (TextView) view.findViewById(R.id.textView_flats_text_ID);
        textView_TextSmall.setText(objFlats.getDescriptionSmall());

        StringBuffer sb = new StringBuffer(objFlats.getPrice());

        int i = sb.length();
        if (i > 3) {
            sb.insert(i - 3, " ");
        }
        if (i > 6) {
            sb.insert(i - 6, " ");
        }

        TextView textView_flats_price = (TextView) view.findViewById(R.id.textView_flats_price_ID);
        textView_flats_price.setText(sb.toString()+ " \u20BD");

        ImageView imageView_Flats_Heart = (ImageView) view.findViewById(R.id.imageView_Flats_Heart_ID);

        if (objFlats.getFavorites() == 1) {
            imageView_Flats_Heart.setImageResource(R.drawable.heart_red);
            imageView_Flats_Heart.setVisibility(View.VISIBLE);
        } else {
            imageView_Flats_Heart.setImageResource(R.drawable.heart_black);
            imageView_Flats_Heart.setVisibility(View.INVISIBLE);
        }

        ImageView Image_Flats = (ImageView) view.findViewById(R.id.imageView_flats_ID);

        if (objFlats.getImgSmall().indexOf("clear.png") > 0) {

            Image_Flats.setImageResource(R.drawable.empty_image);

        } else {

            if (objFlats.img_small_bitmap != null) {
                try {

                    Bitmap bitmap = BitmapFactory.decodeByteArray(objFlats.getImgSmallBitmap(), 0, objFlats.getImgSmallBitmap().length);
                    Image_Flats.setImageBitmap(bitmap);

                } catch (Exception e){

                }
            }
            AdapterFlats.information info = new AdapterFlats.information(Image_Flats, objFlats.getFlatsUID());
            info.execute(objFlats.getImgSmall());
        }

        return view;
    }

    public class information extends AsyncTask<String, String, String> {

        Bitmap b;
        ImageView img;
        String uid;

        public information(ImageView img, String uid) {
            this.img = img;
            this.uid = uid;
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

                    Realm_Flats modelFlats = Main.mRealm.where(Realm_Flats.class).equalTo("flats_UID", uid).findFirst();
                    modelFlats.setImgSmallBitmap(baos.toByteArray());

                    Main.mRealm.commitTransaction();

                }

            } catch (Exception e){
                Main.mRealm.commitTransaction();
            }
        }
    }
}