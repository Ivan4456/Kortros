package ru.mysmartflat.kortros;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import io.realm.RealmList;
import io.realm.RealmResults;

public class LoveFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView TextView_loveFlat;

    private ListView mList;

    private ArrayList<Realm_Favorites> arrayFavorites = new ArrayList<Realm_Favorites>();
    private AdapterFavorites mAdapter;

    private SharedPreferences sPref;
    String token;

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
        public void onFavoriteClickEvent(String FlatUID);
    }

    public LoveFragment() {
        // Required empty public constructor
    }

    public static LoveFragment newInstance() {
        LoveFragment fragment = new LoveFragment();
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

        View view = inflater.inflate(R.layout.fragment_love, container, false);

        mList = (ListView) view.findViewById(R.id.ListView_love_flats_ID);
        mAdapter = new AdapterFavorites(view.getContext(), arrayFavorites);
        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Realm_Favorites flatsFavorite = (Realm_Favorites) arrayFavorites.get(position);

                String flat_UID = flatsFavorite.getFlatsUID();
                ((LoveFragment.Listener) getActivity()).onFavoriteClickEvent(flat_UID);

            }
        });

        sPref = getActivity().getSharedPreferences("City_Settings", Context.MODE_PRIVATE);
        token = sPref.getString("ghost_token", "");

        TextView_loveFlat = (TextView) view.findViewById(R.id.textView_loveFlat_ID);
        TextView_loveFlat.setText("В избранном ещё ничего нет");

        refresh_Favorites();

        requestGetFavorites("https://pro100.media/api/kortros/get-favorites/?token="+token+"&app=1");

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void refresh_Favorites() {

        RealmResults<Realm_Favorites> array_Favorites = Main.mRealm.where(Realm_Favorites.class).findAll();

        if (array_Favorites.size() > 0) {

            TextView_loveFlat.setVisibility(View.INVISIBLE);
            mList.setVisibility(View.VISIBLE);

            arrayFavorites.clear();

            for (int i = 0; i < array_Favorites.size(); i++) {

                arrayFavorites.add(array_Favorites.get(i));

            }

            mAdapter.notifyDataSetChanged();

        } else {
            TextView_loveFlat.setVisibility(View.VISIBLE);
            mList.setVisibility(View.INVISIBLE);
        }
    }

    private void requestGetFavorites(String url) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get(url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Response", "Messsage: " + response.toString());

                try {

                    JSONObject jsonObjects = null;
                    JSONObject jsonObjectsData = null;
                    JSONArray jsonArray_Flats = null;

                    try {

                        jsonObjects = new JSONObject(response.toString());
                        jsonObjectsData = jsonObjects.getJSONObject("data");
                        jsonArray_Flats = jsonObjectsData.getJSONArray("flat");

                        Main.mRealm.beginTransaction();

                        Main.mRealm.delete(Realm_Favorites.class);

                        RealmList<Realm_Favorites> Favorites = new RealmList<Realm_Favorites>();

                        for (int i = 0; i < jsonArray_Flats.length(); i++) {

                            JSONObject jsonObject = jsonArray_Flats.getJSONObject(i);

/*
                                    "date_end":"4 квартал 2018 г",
                                    "img_big":"https:\/\/test.mysmartflat.ru\/img\/v1\/clear.png",
                                    "img_small":"https:\/\/test.mysmartflat.ru\/img\/v1\/clear.png",
                                    "number":51,
                                    "rooms":"2",
                                    "square":"54.2",
                                    "price":8416389,
                                    "floor":"2",
                                    "floor_total":"18",
                                    "description_small":"без отделки",
                                    "favorites":1,
                                    "uid":"ed4d503e-1a84-e711-80d1-005056010696"
*/

                            Realm_Favorites Favorite = Main.mRealm.createObject(Realm_Favorites.class);

                            Favorite.setDateEnd(jsonObject.getString("date_end"));
                            Favorite.setImgBig(jsonObject.getString("img_big"));
                            Favorite.setImgSmall(jsonObject.getString("img_small"));
                            Favorite.setNumber(jsonObject.getString("number"));
                            Favorite.setRooms(jsonObject.getString("rooms"));
                            Favorite.setSquare(jsonObject.getString("square"));
                            Favorite.setPrice(jsonObject.getString("price"));
                            Favorite.setFloor(jsonObject.getString("floor"));
                            Favorite.setFloorTotal(jsonObject.getString("floor_total"));
                            Favorite.setDescriptionSmall(jsonObject.getString("description_small"));
                            Favorite.setFavorites(jsonObject.getInt("favorites"));
                            Favorite.setFlatsUID(jsonObject.getString("uid"));

                            Favorites.add(Favorite);
                        }

                        Main.mRealm.commitTransaction();

                        refresh_Favorites();

                    } catch (JSONException e) {
                        e.printStackTrace();

                        Main.mRealm.beginTransaction();

                        Main.mRealm.delete(Realm_Favorites.class);

                        Main.mRealm.commitTransaction();

                        refresh_Favorites();

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

}

/*
"command":"kortros->get-favorites",
"error":0,
"message":"",
"data":{"token":"ust-483929-7e9a52e107fe30e5675dde293e64dcdb",
        "flat":[
                {
                 "date_end":"4 квартал 2018 г",
                 "img_big":"https:\/\/test.mysmartflat.ru\/img\/v1\/clear.png",
                 "img_small":"https:\/\/test.mysmartflat.ru\/img\/v1\/clear.png",
                 "number":51,
                 "rooms":"2",
                 "square":"54.2",
                 "price":8416389,
                 "floor":"2",
                 "floor_total":"18",
                 "description_small":"без отделки",
                 "favorites":1,
                 "uid":"ed4d503e-1a84-e711-80d1-005056010696"
                }
               ],
        "ending":"а"
       },
"token":"ust-483929-7e9a52e107fe30e5675dde293e64dcdb",
"fromdomain":""
*/