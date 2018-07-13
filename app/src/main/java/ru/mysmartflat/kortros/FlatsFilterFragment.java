package ru.mysmartflat.kortros;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
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

public class FlatsFilterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ListView mList;
    private ArrayList<Realm_Flats> arrayFlats = new ArrayList<Realm_Flats>();
    private AdapterFlats mAdapter;

    private SharedPreferences sPref;
    String token;


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


    public String Select_UID;

    private View view;
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
        public void onFlatsFilterClickEvent(String Town_ID, String GK_complex_ID, Integer button_studia, Integer button_1, Integer button_2, Integer button_3, Integer button_4, String Bilding_ID, String Section_ID, Integer squareMin, Integer squareMax, Integer floorMin, Integer floorMax, Long priceMin, Long priceMax, String sales, String finish); // Complex_ID,typeApartment
    }

    public FlatsFilterFragment() {
        // Required empty public constructor
    }

    public static FlatsFilterFragment newInstance(String Town_ID, String GK_complex_ID, Integer button_studia, Integer button_1, Integer button_2, Integer button_3, Integer button_4, String Bilding_ID, String Section_ID, Integer squareMin, Integer squareMax, Integer floorMin, Integer floorMax, Long priceMin, Long priceMax, String sales, String finish) {
        FlatsFilterFragment fragment = new FlatsFilterFragment();

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

        mList = (ListView) view.findViewById(R.id.ListView_flats_ID);
        mAdapter = new AdapterFlats(view.getContext(), arrayFlats);
        mList.setAdapter(mAdapter);

        sPref = getActivity().getSharedPreferences("City_Settings", Context.MODE_PRIVATE);
        token = sPref.getString("ghost_token", "");

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


        TextView TextView_GK_Name = (TextView) view.findViewById(R.id.textView_title_ID);
        TextView_GK_Name.setText(SearchGkFragment.GK_name);

        RelativeLayout Back = (RelativeLayout) view.findViewById(R.id.linerlayout_back_ID);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //SearchFilterFragment.Filter_GK_complex_ID = Filter_GK_complex_ID;

                ((SearchFragment.Listener) getActivity()).onFilterClickEvent();

                //replaceFragment(SearchGkFragment.newInstance(SearchGkFragment.posTown, SearchGkFragment.Complex_ID, SearchGkFragment.GK_name));

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

                ((FlatFilterFragment.Listener) getActivity()).onFlatFilterClickEvent(arrayFlats.get(position).flats_UID,Filter_Town_ID,Filter_GK_complex_ID,Filter_button_studia,Filter_button_1,Filter_button_2,Filter_button_3,Filter_button_4,Filter_Bilding_ID,Filter_Section_ID,Filter_squareMin,Filter_squareMax,Filter_floorMin,Filter_floorMax,Filter_priceMin,Filter_priceMax,Filter_sales,Filter_finish);

            }
        });

        refresh_Data_Flats();
        requestFlatsList("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + Filter_GK_complex_ID + "&limit=100500&offset=0&room0=" + String.valueOf(Filter_button_studia) + "&room1=" + String.valueOf(Filter_button_1) + "&room2=" + String.valueOf(Filter_button_2) + "&room3=" + String.valueOf(Filter_button_3) + "&room4=" + String.valueOf(Filter_button_4) + "&building_id=" + Filter_Bilding_ID + "&section_id=" + Filter_Section_ID + "&square_min=" + String.valueOf(Filter_squareMin) + "&square_max=" + String.valueOf(Filter_squareMax) + "&floor_min=" + String.valueOf(Filter_floorMin) + "&floor_max=" + String.valueOf(Filter_floorMax) + "&price_min=" + String.valueOf(Filter_priceMin) + "&price_max=" + String.valueOf(Filter_priceMax) + "&sales=" + Filter_sales + "&finish=" + Filter_finish + "&only_count=0&app=1");

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

                            flats.setComplexID(Filter_GK_complex_ID);
                            flats.setTypeApartment(0);//Type_Apartment);
                            flats.setFlatsUID(jsonObject.getString("uid"));
                            flats.setDateEnd(jsonObject.getString("date_end"));
                            flats.setNumber(jsonObject.getString("number"));
                            flats.setRooms(jsonObject.getString("rooms"));

                            flats.setPrice(jsonObject.getString("price"));

                            try {
                                flats.setSquare((int)Float.parseFloat(jsonObject.getString("square")));
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
/*
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


        String complex_ID;

        String number;
        String rooms;

        String square;
        String price;

        String floor;
        String floor_total;
*/


        //String[] array_floor = new String[]{};

        String[] array_rooms = new String[]{"9","9","9","9","9"};

        if (Filter_button_studia == 1){
            array_rooms[0] = "0";
        }
        if (Filter_button_1 == 1){
            array_rooms[1] = "1";
        }
        if (Filter_button_2 == 1){
            array_rooms[2] = "2";
        }
        if (Filter_button_3 == 1){
            array_rooms[3] = "3";
        }
        if (Filter_button_4 == 1){
            array_rooms[4] = "4";
        }

        if((Filter_button_studia == 0) && (Filter_button_1 == 0) && (Filter_button_2 == 0) && (Filter_button_3 == 0) && (Filter_button_4 == 0)) {
            array_rooms = new String[]{"0","1","2","3","4"};
        }

        RealmResults<Realm_Flats> array_flats = Main.mRealm.where(Realm_Flats.class).equalTo("complex_ID", Filter_GK_complex_ID).in("rooms",array_rooms).greaterThan("floor", Filter_floorMin).lessThan("floor", Filter_floorMax).greaterThan("square", Filter_squareMin).lessThan("square", Filter_squareMax).findAll(); //greaterThan("price", Filter_priceMin).lessThan("price", Filter_priceMax).

        if (array_flats != null) {

            arrayFlats.clear();

            for(int i=0; i < array_flats.size(); i++) {

                int price = 0;

                try {
                    price = (int)Float.parseFloat(array_flats.get(i).getPrice());
                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }

                if((price >= Filter_priceMin)&&(price <= Filter_priceMax )){
                    arrayFlats.add(array_flats.get(i));
                }

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



