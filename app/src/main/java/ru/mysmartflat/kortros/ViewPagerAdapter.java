package ru.mysmartflat.kortros;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import io.realm.RealmList;

public class ViewPagerAdapter extends PagerAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    //private Integer [] images = {R.drawable.city,R.drawable.city,R.drawable.city};

    RealmList<byte[]> storage_bitmap = new RealmList<byte[]>();


    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return SearchGkFragment.images_Complex.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2 = layoutInflater.inflate(R.layout.layout_pager,null);

        ImageView imageView = (ImageView) view2.findViewById(R.id.imageView2);

        RealmList<String> imges = SearchGkFragment.images_Complex;

        Realm_Complex complex = Main.mRealm.where(Realm_Complex.class).equalTo("id", SearchGkFragment.Complex_ID).findFirst();

        if (complex != null) {

            RealmList<byte []> bb = complex.getImgesBitmap();

            try {

                Bitmap bitmap = BitmapFactory.decodeByteArray(bb.get(position), 0, bb.get(position).length);
                imageView.setImageBitmap(bitmap);

            } catch (Exception e){

            }
        } //else {

            ViewPagerAdapter.img_loader info = new ViewPagerAdapter.img_loader(imageView, position);
            info.execute(imges.get(position));

        //}

        ViewPager vp = (ViewPager) container;
        vp.addView(view2,0);
        return view2;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

    public class img_loader extends AsyncTask<String, String, String> {

        Bitmap b;
        ImageView img;
        Integer position;

        public img_loader(ImageView img, Integer position) {
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

                    if (!Main.mRealm.isInTransaction()) {

                        Main.mRealm.beginTransaction();

                        Realm_Complex complex = Main.mRealm.where(Realm_Complex.class).equalTo("id", SearchGkFragment.Complex_ID).findFirst();

                        storage_bitmap.add(position,baos.toByteArray());

                        if (position == (SearchGkFragment.images_Complex.size()-1)) {
                            complex.setImgesBitmap(storage_bitmap);                                 // сохраняется тока весь
                        }

                        Main.mRealm.commitTransaction();
                    }
                }

            } catch (Exception e){
                Main.mRealm.commitTransaction();
            }
        }
    }
}
