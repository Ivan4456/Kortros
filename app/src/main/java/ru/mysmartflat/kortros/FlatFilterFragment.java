package ru.mysmartflat.kortros;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yandex.mapkit.MapKitFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import cz.msebera.android.httpclient.Header;

public class FlatFilterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public String Complex_ID;
    public String Select_UID;
    public Integer Type_Apartment;
    private View view;

    private RelativeLayout relativeLayout_Status_Heart;
    private ImageView imageView_Status_Heart;
    private Boolean Bool_Heart = false;
    private Boolean Bool_Request_Heart = false;
    Realm_Flats flat;

    String Filter_Town_ID;
    String Filter_GK_complex_ID;
    Integer Filter_button_studia;
    Integer Filter_button_1;
    Integer Filter_button_2;
    Integer Filter_button_3;
    Integer Filter_button_4;
    String Filter_Bilding_ID;
    String Filter_Section_ID;
    Integer Filter_squareMin;
    Integer Filter_squareMax;
    Integer Filter_floorMin;
    Integer Filter_floorMax;
    Long Filter_priceMin;
    Long Filter_priceMax;
    String Filter_sales;
    String Filter_finish;

    private SharedPreferences sPref;
    private String token;

/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "!!! must implement OnFragmentInteractionListener");
        }
    }
*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    // This makes sure that the container activity has implemented
    // the callback interface. If not, it throws an exception
        /*
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "!! must implement OnHeadlineSelectedListener");
        }
    */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    interface Listener {
        public void onFlatFilterClickEvent(String Select_UID, String Town_ID, String GK_complex_ID, Integer button_studia, Integer button_1, Integer button_2, Integer button_3, Integer button_4, String Bilding_ID, String Section_ID, Integer squareMin, Integer squareMax, Integer floorMin, Integer floorMax, Long priceMin, Long priceMax, String sales, String finish); // Complex_ID,typeApartment
    }

    public FlatFilterFragment() {
        // Required empty public constructor
    }

    public static FlatFilterFragment newInstance(String Select_UID, String Town_ID, String GK_complex_ID, Integer button_studia, Integer button_1, Integer button_2, Integer button_3, Integer button_4, String Bilding_ID, String Section_ID, Integer squareMin, Integer squareMax, Integer floorMin, Integer floorMax, Long priceMin, Long priceMax, String sales, String finish) {
        FlatFilterFragment fragment = new FlatFilterFragment();

        Bundle bundle = new Bundle();

        bundle.putString("Filter_Town_ID", Town_ID);
        bundle.putString("Filter_GK_complex_ID", GK_complex_ID);

        bundle.putInt("Filter_button_studia", button_studia);
        bundle.putInt("Filter_button_1", button_1);
        bundle.putInt("Filter_button_2", button_2);
        bundle.putInt("Filter_button_3", button_3);
        bundle.putInt("Filter_button_4", button_4);

        bundle.putString("Filter_Bilding_ID", Bilding_ID);
        bundle.putString("Filter_Section_ID", Section_ID);

        bundle.putInt("Filter_squareMin", squareMin);
        bundle.putInt("Filter_squareMax", squareMax);
        bundle.putInt("Filter_floorMin", floorMin);
        bundle.putInt("Filter_floorMax", floorMax);
        bundle.putLong("Filter_priceMin", priceMin);
        bundle.putLong("Filter_priceMax", priceMax);

        bundle.putString("Filter_sales", sales);
        bundle.putString("Filter_finish", finish);

        bundle.putString("Filter_Select_UID", Select_UID);
        fragment.setArguments(bundle);

        return fragment;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    @Override
    public void onStop() {
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void replaceFragment(Fragment newFragment) {

        FragmentTransaction trasection = getFragmentManager().beginTransaction();

        if (!newFragment.isAdded()) {

            try{

                getFragmentManager().beginTransaction();
                trasection.replace(R.id.main_container, newFragment);
                trasection.addToBackStack(null);
                trasection.commit();

            }catch (Exception e) {
                // TODO: handle exception
                // AppConstants.printLog(e.getMessage());
            }

        }else

            trasection.show(newFragment);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_flat, container, false);

//        Complex_ID = getArguments().getString("Complex_ID");
        Select_UID = getArguments().getString("Filter_Select_UID");
//        Type_Apartment = getArguments().getInt("Type_Apartment");

        Filter_Town_ID = getArguments().getString("Filter_Town_ID");
        Filter_GK_complex_ID = getArguments().getString("Filter_GK_complex_ID");

        Filter_button_studia = getArguments().getInt("Filter_button_studia");
        Filter_button_1 = getArguments().getInt("Filter_button_1");
        Filter_button_2 = getArguments().getInt("Filter_button_2");
        Filter_button_3 = getArguments().getInt("Filter_button_3");
        Filter_button_4 = getArguments().getInt("Filter_button_4");

        Filter_Bilding_ID = getArguments().getString("Filter_Bilding_ID");
        Filter_Section_ID = getArguments().getString("Filter_Section_ID");

        Filter_squareMin = getArguments().getInt("Filter_squareMin");
        Filter_squareMax = getArguments().getInt("Filter_squareMax");
        Filter_floorMin = getArguments().getInt("Filter_floorMin");
        Filter_floorMax = getArguments().getInt("Filter_floorMax");
        Filter_priceMin = getArguments().getLong("Filter_priceMin");
        Filter_priceMax = getArguments().getLong("Filter_priceMax");

        Filter_sales = getArguments().getString("Filter_sales");
        Filter_finish = getArguments().getString("Filter_finish");


        imageView_Status_Heart = (ImageView) view.findViewById(R.id.imageView_Status_Heart_ID);
        relativeLayout_Status_Heart = (RelativeLayout) view.findViewById(R.id.relativeLayout_Status_Heart_ID);

        sPref = getActivity().getSharedPreferences("City_Settings", Context.MODE_PRIVATE);                        // MODE_WORLD_READABLE);
        token = sPref.getString("ghost_token", "");

        relativeLayout_Status_Heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Bool_Request_Heart == false) {

                    Bool_Request_Heart = true;

                    if (Bool_Heart) {

                        Bool_Heart = false;

                        requestSetFavorites("https://pro100.media/api/kortros/update-favorites/?token=" + token + "&uid="+Select_UID+"&favorites=0&app=1");

                    } else {

                        Bool_Heart = true;

                        requestSetFavorites("https://pro100.media/api/kortros/update-favorites/?token=" + token + "&uid="+Select_UID+"&favorites=1&app=1");
                    }
                }
            }
        });

        flat = Main.mRealm.where(Realm_Flats.class).equalTo("flats_UID", Select_UID).findFirst();

        if (flat != null) {

            TextView textView_flats_rooms = (TextView) view.findViewById(R.id.textView_flats_rooms_ID);
            TextView textView_flat_text = (TextView) view.findViewById(R.id.textView_flat_text_ID);
            TextView textView_flat_price = (TextView) view.findViewById(R.id.textView_flat_price_ID);

            textView_flats_rooms.setText(flat.getRooms()+" ком. \u2022 "+flat.getSquare()+" м\u00B2 \u2022 этаж "+flat.getFloor()+"/"+flat.getFloorTotal());
            textView_flat_text.setText(flat.getDescriptionSmall());

            if (flat.getFavorites() == 1) {
                imageView_Status_Heart.setImageResource(R.drawable.heart_red);
            }

            StringBuffer sb = new StringBuffer(flat.getPrice());

            int i = sb.length();
            if (i > 3) {
                sb.insert(i - 3, " ");
            }
            if (i > 6) {
                sb.insert(i - 6, " ");
            }

            textView_flat_price.setText(sb.toString()+ " \u20BD");

            ImageView imageView_flat_big = (ImageView) view.findViewById(R.id.imageView_flat_big_ID);

            if (flat.getImgSmall().indexOf("clear.png") > 0) {

                imageView_flat_big.setImageResource(R.drawable.empty_image);

            } else {

                if (flat.img_big_bitmap != null) {
                    try {

                        Bitmap bitmap = BitmapFactory.decodeByteArray(flat.getImgBigBitmap(), 0, flat.getImgBigBitmap().length);
                        imageView_flat_big.setImageBitmap(bitmap);

                    } catch (Exception e) {

                    }
                }

                FlatFilterFragment.information info = new FlatFilterFragment.information(imageView_flat_big);
                info.execute(flat.getImgBig());
            }

            TextView textView_flat_count_room = (TextView) view.findViewById(R.id.textView_flat_count_room_ID);
            textView_flat_count_room.setText(flat.getRooms());

            TextView textView_flat_room_n = (TextView) view.findViewById(R.id.textView_flat_room_n_ID);
            textView_flat_room_n.setText(flat.getNumber());



        } else {
            replaceFragment(FlatsFragment.newInstance(Complex_ID, Type_Apartment));
        }

        RelativeLayout Back = (RelativeLayout) view.findViewById(R.id.linerlayout_back_ID);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(FlatsFilterFragment.newInstance(Filter_Town_ID,Filter_GK_complex_ID,Filter_button_studia,Filter_button_1,Filter_button_2,Filter_button_3,Filter_button_4,Filter_Bilding_ID,Filter_Section_ID,Filter_squareMin,Filter_squareMax,Filter_floorMin,Filter_floorMax,Filter_priceMin,Filter_priceMax,Filter_sales,Filter_finish));
            }
        });

        return view;
    }

    public class information extends AsyncTask<String, String, String> {

        Bitmap b;
        ImageView img;

        public information(ImageView img) {
            this.img = img;
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

                    flat.setImgBigBitmap(baos.toByteArray());

                    Main.mRealm.commitTransaction();

                }

            } catch (Exception e){
                Main.mRealm.commitTransaction();
            }
        }
    }

    private void requestSetFavorites(String url) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get(url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Response", "Messsage: " + response.toString());

                Bool_Request_Heart = false;

                try {

                    JSONObject jsonObjects = null;

                    try {

                        jsonObjects = new JSONObject(response.toString());
                        int error = jsonObjects.getInt("error");

                        if (error == 0){

                            Realm_Flats flat = Main.mRealm.where(Realm_Flats.class).equalTo("flats_UID", Select_UID).findFirst();

                            try{

                                Main.mRealm.beginTransaction();
                                if (Bool_Heart == false) {
                                    flat.setFavorites(0);
                                    imageView_Status_Heart.setImageResource(R.drawable.heart_black);
                                } else {
                                    flat.setFavorites(1);
                                    imageView_Status_Heart.setImageResource(R.drawable.heart_red);
                                }
                                Main.mRealm.commitTransaction();

                            } catch (Exception e){
                                Main.mRealm.commitTransaction();
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                Log.e("ERROR", e.toString());
            }

        });
    }



/*
https://pro100.media/api/kortros/get-flats/?token=ust-358645-c72f3f44ef332beb809d04176aefc1f3&complex_id=13&type_apartment=1&app=1

{
"command":"kortros->get-flats",
"error":0,
"message":"",
"data":{
        "token":"ust-358645-c72f3f44ef332beb809d04176aefc1f3",
        "count":69,
        "flat":[{
                 "date_end":"4 \u043a\u0432\u0430\u0440\u0442\u0430\u043b 2019 \u0433",
                 "img_big":"https://pro100.media/api/ihome/images/plan/big/37688.png",
                 "img_small":"https://pro100.media/api/ihome/images/plan/small/37688.png",
                 "number":"145",
                 "rooms":"1",
                 "square":"25.96",
                 "price":4524725,
                 "floor":"10",
                 "floor_total":"15",
                 "description_small":"\u0431\u0435\u0437 \u043e\u0442\u0434\u0435\u043b\u043a\u0438",
                 "favorites":0,
                 "uid":"88521761-ca1a-e711-80d1-005056010696"
                },{

               }],
        "ending":""
       },
"token":"ust-358645-c72f3f44ef332beb809d04176aefc1f3",
"fromdomain":""
}

*/
}



