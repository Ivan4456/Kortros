package ru.mysmartflat.kortros;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yandex.mapkit.MapKitFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import io.realm.RealmResults;

public class FlatsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ListView mList;
    private ArrayList<Realm_Flats> arrayFlats = new ArrayList<Realm_Flats>();
    private AdapterFlats mAdapter;

    private SharedPreferences sPref;
    String token;

    public String Complex_ID;
    public Integer Type_Apartment;
    public String Select_UID;

    private View view;

    AlertDialog dialog;
    int SortingCheckedItem = 0; // cow

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
        public void onFlatsClickEvent(String Complex_ID, Integer typeApartment); // Complex_ID,typeApartment
    }

    public FlatsFragment() {
        // Required empty public constructor
    }

    public static FlatsFragment newInstance(String complex_id, Integer typeApartment) {
        FlatsFragment fragment = new FlatsFragment();

        Bundle bundle = new Bundle();
        bundle.putString("Complex_ID", complex_id);
        bundle.putInt("Type_Apartment", typeApartment);
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

        } else {
            trasection.show(newFragment);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_flats, container, false);



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme2);
        builder.setTitle("Сортировать по");

        String[] animals = {"Возврастанию цены", "Убыванию цены", "Увеличению площади", "Уменьшению площади"};

        builder.setSingleChoiceItems(animals, SortingCheckedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
                SortingCheckedItem = which;
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
            }
        });

        dialog = builder.create();

        RelativeLayout Sorting = (RelativeLayout) view.findViewById(R.id.relativeLayout_sort_ID);
        Sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();

            }
        });




        mList = (ListView) view.findViewById(R.id.ListView_flats_ID);
        mAdapter = new AdapterFlats(view.getContext(), arrayFlats);
        mList.setAdapter(mAdapter);

        sPref = getActivity().getSharedPreferences("City_Settings", Context.MODE_PRIVATE);
        token = sPref.getString("ghost_token", "");

        Complex_ID = getArguments().getString("Complex_ID");
        Type_Apartment = getArguments().getInt("Type_Apartment");

        TextView TextView_GK_Name = (TextView) view.findViewById(R.id.textView_title_ID);
        TextView_GK_Name.setText(SearchGkFragment.GK_name);

        RelativeLayout Back = (RelativeLayout) view.findViewById(R.id.linerlayout_back_ID);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(SearchGkFragment.newInstance(SearchGkFragment.posTown, SearchGkFragment.Complex_ID, SearchGkFragment.GK_name));

            }
        });

/*
        arrayNews.clear();

        ModelNews objNews = new ModelNews();

        objNews.setNewsID("1");
        objNews.setStrTitle("title");
        objNews.setStrText("text");
        objNews.setImg("https://pro100.media/api/ihome/images/kortros/img/dce8916aa0affd08653e6c39dc8b30d6_375.jpg");

        arrayNews.add(objNews);
        mAdapter.notifyDataSetChanged();
*/


        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Select_UID = arrayFlats.get(position).flats_UID;
                ((FlatFragment.Listener) getActivity()).onFlatClickEvent(Complex_ID,Type_Apartment,Select_UID);

            }
        });

        refresh_Data_Flats();

        requestFlatsList("https://pro100.media/api/kortros/get-flats/?token="+token+"&complex_id="+Complex_ID+"&type_apartment="+String.valueOf(Type_Apartment)+"&app=1");

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void requestFlatsList(String url) {

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

                        //User_ghost_token = jsonObjectsData.getString("ghost_token");
                        //User_token = jsonObjectsData.getString("token");

                        Main.mRealm.beginTransaction();

                        for (int i = 0; i < jsonArray_Flats.length(); i++) {

                            JSONObject jsonObject = jsonArray_Flats.getJSONObject(i);

                            Realm_Flats flats = Main.mRealm.where(Realm_Flats.class).equalTo("flats_UID", jsonObject.getString("uid")).findFirst();

                            if (flats == null) {
                                flats = Main.mRealm.createObject(Realm_Flats.class);
                            }

                            flats.setComplexID(Complex_ID);
                            flats.setTypeApartment(Type_Apartment);
                            flats.setFlatsUID(jsonObject.getString("uid"));
                            flats.setDateEnd(jsonObject.getString("date_end"));
                            flats.setNumber(jsonObject.getString("number"));
                            flats.setRooms(jsonObject.getString("rooms"));

                            flats.setPrice(jsonObject.getString("price"));

                            try {
                                flats.setSquare(Integer.parseInt(jsonObject.getString("square")));
                            } catch (Exception e) {
                                Log.e("Error", e.toString());
                                flats.setSquare(0);
                            }

                            try {
                                flats.setFloor(Integer.parseInt(jsonObject.getString("floor")));
                            } catch (Exception e) {
                                Log.e("Error", e.toString());
                                flats.setFloor(0);
                            }


                            flats.setFloorTotal(jsonObject.getString("floor_total"));
                            flats.setDescriptionSmall(jsonObject.getString("description_small"));

                            flats.setFavorites(jsonObject.getInt("favorites"));

                            flats.setImgSmall(jsonObject.getString("img_small"));
                            flats.setImgBig(jsonObject.getString("img_big"));

                        }

                        Main.mRealm.commitTransaction();

                        refresh_Data_Flats();

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

    private void refresh_Data_Flats() {

        RealmResults<Realm_Flats> array_flats = Main.mRealm.where(Realm_Flats.class).equalTo("complex_ID", Complex_ID).equalTo( "type_Apartament", Type_Apartment).findAll();

        if (array_flats != null) {

            arrayFlats.clear();

            for(int i=0; i < array_flats.size(); i++) {

                arrayFlats.add(array_flats.get(i));

            }
            mAdapter.notifyDataSetChanged();
        }
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



