package ru.mysmartflat.kortros;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.renderscript.Sampler;
import android.support.constraint.ConstraintLayout;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.yandex.runtime.Runtime.getApplicationContext;

public class SearchFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageView imageView_searchCity;
    private ImageView imageView_searchFilter;

    private SharedPreferences sPref;
    String token;

    //private Realm mRealm;

/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "!!! must implement OnFragmentInteractionListener");
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
        //mRealm.close();
    }

    interface Listener {
        public void onTownClickEvent(Integer Pos);
        public void onFilterClickEvent();
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        // void openHome(View view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_seach, container, false);

        sPref = getActivity().getSharedPreferences("City_Settings", Context.MODE_PRIVATE);
        token = sPref.getString("ghost_token", "");

        imageView_searchCity = (ImageView) view.findViewById(R.id.imageView_searchCity_ID);
        imageView_searchFilter = (ImageView) view.findViewById(R.id.imageView_searchFilter_ID);

        imageView_searchCity.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Integer city_number = 0;
                try {
                    city_number = sPref.getInt("city_number", 0);
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
                */

                RealmQuery<Realm_City> query_Cities = Main.mRealm.where(Realm_City.class);

                if(query_Cities.count() > 0){
                    ((Listener) getActivity()).onTownClickEvent(0);
                }

            }
        });

        imageView_searchFilter.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Listener) getActivity()).onFilterClickEvent();
            }
        });

        requestTownList("https://pro100.media/api/kortros/get-city/?token="+token+"&app=1");

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void requestTownList(String url) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get(url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Response", "Messsage: " + response.toString());

                try {

                    JSONObject jsonObjects = null;
                    JSONObject jsonObjectsData = null;
                    JSONArray jsonArray_City = null;

                    try {

                        jsonObjects = new JSONObject(response.toString());
                        jsonObjectsData = jsonObjects.getJSONObject("data");
                        jsonArray_City = jsonObjectsData.getJSONArray("city");

                        SharedPreferences.Editor ed = sPref.edit();

                        ed.putString("ghost_token", jsonObjectsData.getString("ghost_token").toString().trim());
                        ed.putString("token", jsonObjectsData.getString("token").toString().trim());
                        ed.putInt("city_number", jsonArray_City.length());

                        Main.mRealm.beginTransaction();
                        //Main.mRealm.delete(Realm_City.class);

                        for (int i = 0; i < jsonArray_City.length(); i++) {

/*
                            JSONObject jsonObject_City = jsonArray_City.getJSONObject(i);

                            ed.putString("city_title_"+ String.valueOf(i+1), jsonObject_City.getString("title").toString().trim());
                            ed.putString("city_id_"+ String.valueOf(i+1), jsonObject_City.getString("id").toString().trim());
                            ed.putInt("city_zoom_"+ String.valueOf(i+1), jsonObject_City.getInt("zoom"));

                            JSONObject jsonArray_Coord_Center = jsonObject_City.getJSONObject("center");

                            ed.putString("city_x_"+ String.valueOf(i+1), String.valueOf(jsonArray_Coord_Center.getDouble("x")));
                            ed.putString("city_y_"+ String.valueOf(i+1), String.valueOf(jsonArray_Coord_Center.getDouble("y")));
*/

                            JSONObject jsonObject_City = jsonArray_City.getJSONObject(i);

                            Realm_City Cities = Main.mRealm.where(Realm_City.class).equalTo("id", jsonObject_City.getString("id").toString().trim()).findFirst();

                            if (Cities == null) {
                                Cities = Main.mRealm.createObject(Realm_City.class);
                            }

                            Cities.setTitle(jsonObject_City.getString("title").toString().trim());
                            Cities.setId(jsonObject_City.getString("id").toString().trim());
                            Cities.setZoom(jsonObject_City.getInt("zoom"));

                            JSONObject jsonArray_Coord_Center = jsonObject_City.getJSONObject("center");

                            Cities.setCenter_x(String.valueOf(jsonArray_Coord_Center.getDouble("x")));
                            Cities.setCenter_y(String.valueOf(jsonArray_Coord_Center.getDouble("y")));
                        }

                        Main.mRealm.commitTransaction();

                        ed.commit();

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
https://pro100.media/api/kortros/get-city/?token=&app=1
{
 "command":"kortros->get-city",
 "error":0,
 "message":"",
 "data":{
         "ghost_token":"ust-377150-c8ee62f534c75cb49196800cd3f21eb9",
         "token":"ust-377150-c8ee62f534c75cb49196800cd3f21eb9",
         "city":[{
                  "center":{
                            "x":55.713095595192,
                            "y":37.56365262247
                           },
                  "zoom":9,
                  "id":"50",
                  "title":"\u041c\u043e\u0441\u043a\u0432\u0430"}
                ]},
         "token":"",
         "ghost_token":"ust-377150-c8ee62f534c75cb49196800cd3f21eb9",
         "fromdomain":""
}
*/

}
