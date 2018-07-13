package ru.mysmartflat.kortros;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yandex.mapkit.MapKitFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import io.realm.RealmResults;

public class FlatFragment extends Fragment {

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

    private Button button_action;

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
        public void onFlatClickEvent(String Complex_ID, Integer typeApartment, String Select_UID); // Complex_ID,typeApartment
    }

    public FlatFragment() {
        // Required empty public constructor
    }

    public static FlatFragment newInstance(String complex_id, Integer typeApartment, String select_UID) {
        FlatFragment fragment = new FlatFragment();

        Bundle bundle = new Bundle();
        bundle.putString("Complex_ID", complex_id);
        bundle.putInt("Type_Apartment", typeApartment);
        bundle.putString("Select_UID", select_UID);
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

        Complex_ID = getArguments().getString("Complex_ID");
        Select_UID = getArguments().getString("Select_UID");
        Type_Apartment = getArguments().getInt("Type_Apartment");

        imageView_Status_Heart = (ImageView) view.findViewById(R.id.imageView_Status_Heart_ID);
        relativeLayout_Status_Heart = (RelativeLayout) view.findViewById(R.id.relativeLayout_Status_Heart_ID);

        sPref = getActivity().getSharedPreferences("City_Settings", Context.MODE_PRIVATE);                        // MODE_WORLD_READABLE);
        token = sPref.getString("ghost_token", "");

        button_action = (Button) view.findViewById(R.id.button_action_ID);
        button_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String Action_Number = "+790812333444";


                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+"+Action_Number));
                startActivity(intent);
            }
        });

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

                FlatFragment.information info = new FlatFragment.information(imageView_flat_big);
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
                replaceFragment(FlatsFragment.newInstance(Complex_ID, Type_Apartment));
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



