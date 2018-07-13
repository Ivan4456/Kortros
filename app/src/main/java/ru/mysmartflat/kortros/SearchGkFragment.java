package ru.mysmartflat.kortros;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.yandex.runtime.Runtime.getApplicationContext;

public class SearchGkFragment extends Fragment implements View.OnTouchListener {

    private ListView mList;
    private ListView mList2;

    private ScrollView GK_ScrollView;

    BottomNavigationView BottomNavigation;

    //private RealmList<ModelPrice> arrayPrice = new RealmList<ModelPrice>();
    //private RealmResults<ModelPrice> arrayPrice = new RealmResults<ModelPrice>();

    private AdapterPrice mAdapterPrice;
    private AdapterIpoteka mAdapterIpoteka;

    private OnFragmentInteractionListener mListener;
    private ImageView imageView_searchCity;
   // private MapView mapview2;


    static public String GK_name;
    static public String Complex_ID;
    static public int posTown = 0;

    View divider_add;

    private TextView TextView_GK_Name;
    private UserLocationLayer userLocationLayer;
    private View view;

    private ProgressBar progressBar;

    ViewPager viewPager;

    Timer timer;

    int heightFrame;

    int _yDelta;
    boolean closeMap = false;
    RelativeLayout rootLayout;
    ViewGroup.MarginLayoutParams rootLayout_param_map;
    ViewGroup.MarginLayoutParams rootLayout_param_mapview;

    RelativeLayout relativeLayout_map;
    ViewGroup.LayoutParams relativeLayout_param_map;

    //public Integer [] images = {R.drawable.city,R.drawable.city,R.drawable.city};

    static public RealmList<String> images_Complex = new RealmList<String>();

    ArrayList<ModelPrice> List_Price;
    ArrayList<ModelIpoteka> List_Ipoteka = new ArrayList<ModelIpoteka>();

    private SharedPreferences sPref;
    String token;
    private RealmQuery<Realm_Complex> query_Complex;
    private RealmResults<Realm_Complex> realm_Complexes;

    private Button button_action;
    private String Action_Number;

    public SearchGkFragment() {
        //Required empty public constructor
    }

    public static SearchGkFragment newInstance(Integer pos,String complex_id, String name) {
        SearchGkFragment fragment = new SearchGkFragment();

        Bundle bundle = new Bundle();
        bundle.putString("GK_name", name);
        bundle.putInt("Town_pos", pos);
        bundle.putString("GK_ID", complex_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStop() {
    //    mapview2.onStop();
        MapKitFactory.getInstance().onStop();
        timer.cancel();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();

     //   mapview2.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //MapKitFactory.setApiKey("6748b84e-0e7c-49eb-afb3-6e2ee2bf2a52");//ac7ff396-c67b-4e3e-bd68-d0f4c88385a3
        //MapKitFactory.initialize(this.getActivity());
        super.onCreate(savedInstanceState);
    }

   // private final CameraListener cameraListener = new CameraListener() {

   // }

    private void replaceFragment(Fragment newFragment) {

        FragmentTransaction trasection = getFragmentManager().beginTransaction();
        if(!newFragment.isAdded()){
            try{
               // FragmentTransaction trasection =
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

    public boolean onTouch(View view, MotionEvent event) {

        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                 //RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

                 //RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) relativeLayout_map.getLayoutParams();

                 view.setBackgroundColor(Color.LTGRAY);

                 _yDelta = Y - rootLayout_param_map.topMargin;
                 //_yDelta = Y - relativeLayout_param_map.height;
                 break;

            case MotionEvent.ACTION_UP:

                 view.setBackgroundColor(Color.WHITE);

                 if (rootLayout_param_map.topMargin < 5) {
                     rootLayout_param_map.topMargin = 0;
                 } else {
                     if (closeMap) {
                       // TownSpinner.setSelection(posTown);
                     }
                 }

                 if (rootLayout_param_map.topMargin > (relativeLayout_map.getHeight() - BottomNavigation.getHeight()*2)) {
                     rootLayout_param_map.topMargin = relativeLayout_map.getHeight() - BottomNavigation.getHeight() - divider_add.getHeight();
                 }

                 break;

            case MotionEvent.ACTION_POINTER_DOWN:
                 break;

            case MotionEvent.ACTION_POINTER_UP:
                 break;

            case MotionEvent.ACTION_MOVE:

                 rootLayout_param_map.topMargin = Y - _yDelta;
                 rootLayout.setLayoutParams(rootLayout_param_map);

                 if(rootLayout_param_map.topMargin < 5) {//&& (Y > 60)){
                     rootLayout_param_map.topMargin = 0;
                 }

                 if (rootLayout_param_map.topMargin > (relativeLayout_map.getHeight() - BottomNavigation.getHeight()*2)) {
                     rootLayout_param_map.topMargin = relativeLayout_map.getHeight() - BottomNavigation.getHeight() - divider_add.getHeight();
                 }

            break;
        }
        relativeLayout_map.invalidate();
        //rootLayout.invalidate();

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_seach_gk, container, false);

        mList2 = (ListView) view.findViewById(R.id.ListView_Ipoteka_ID);

        divider_add = (View) view.findViewById(R.id.divider_add_ID);

        BottomNavigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);

        GK_ScrollView = (ScrollView) view.findViewById(R.id.ScrollView_ID);
        //GK_ScrollView.requestDisallowInterceptTouchEvent(true);

        //LinearLayout linerLayout_GK = (LinearLayout) view.findViewById(R.id.linerLayout_GK_ID);
        //linerLayout_GK.requestDisallowInterceptTouchEvent(true);

        GK_ScrollView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {

                final int Y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:

                        //view.setBackgroundColor(Color.LTGRAY);

                        _yDelta = Y - rootLayout_param_map.topMargin;
                        rootLayout_param_map.topMargin = _yDelta;
                        break;

                    case MotionEvent.ACTION_UP:

                        //view.setBackgroundColor(Color.WHITE);

                        if (rootLayout_param_map.topMargin < 5) {
                            rootLayout_param_map.topMargin = 0;
                            return false;
                        } else {
                            if (closeMap) {

                            }
                        }
                        if (rootLayout_param_map.topMargin > (relativeLayout_map.getHeight() - BottomNavigation.getHeight()*2)) {
                            rootLayout_param_map.topMargin = relativeLayout_map.getHeight() - BottomNavigation.getHeight() - divider_add.getHeight();
                        }

                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        break;

                    case MotionEvent.ACTION_MOVE:

                        if (rootLayout_param_map.topMargin < (Y - _yDelta)) { // вниз

                            if (GK_ScrollView.getScrollY() == 0) { // верхнее состояние

                                rootLayout_param_map.topMargin = Y - _yDelta;
                                rootLayout.setLayoutParams(rootLayout_param_map);

                                if (rootLayout_param_map.topMargin < 15){// && (Y > 60)) {
                                    rootLayout_param_map.topMargin = 0;
                                    return false;
                                }

                            } else {

                                return false;

                            }

                        } else {

                            rootLayout_param_map.topMargin = Y - _yDelta;
                            rootLayout.setLayoutParams(rootLayout_param_map);

                            if (rootLayout_param_map.topMargin < 15){// && (Y > 60)) {
                                rootLayout_param_map.topMargin = 0;
                                return false;
                            }

                        }

                        if (rootLayout_param_map.topMargin > (relativeLayout_map.getHeight() - BottomNavigation.getHeight()*2)) {
                            rootLayout_param_map.topMargin = relativeLayout_map.getHeight() - BottomNavigation.getHeight() - divider_add.getHeight();
                        }

                        break;
                }
                relativeLayout_map.invalidate();

                return true;
            }
        });

        sPref = getActivity().getSharedPreferences("City_Settings", Context.MODE_PRIVATE);

        token = sPref.getString("ghost_token", "");

        Display display = getActivity().getWindowManager().getDefaultDisplay();

        heightFrame = display.getHeight();

        GK_name = getArguments().getString("GK_name");

        Complex_ID = getArguments().getString("GK_ID");

        posTown = getArguments().getInt("Town_pos",0);

        final MapView mapview = (MapView) view.findViewById(R.id.mapview_GK_ID);

        RelativeLayout Back = (RelativeLayout) view.findViewById(R.id.linerlayout_back_ID);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(SearchTownFragment.newInstance(posTown));
            }
        });

        RelativeLayout Filter = (RelativeLayout) view.findViewById(R.id.RelativeLayout_Filter_ID);
        Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SearchFilterFragment.Filter_GK_complex_ID = Complex_ID;

                SearchFilterFragment.Filter_GK_building_ID = "";

                SearchFilterFragment.Filter_GK_section_ID = "";

                ((SearchFragment.Listener) getActivity()).onFilterClickEvent();

            }
        });

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        button_action = (Button) view.findViewById(R.id.button_action_ID);

        rootLayout = (RelativeLayout) view.findViewById(R.id.relativeRoot_ID);

        relativeLayout_map = (RelativeLayout) view.findViewById(R.id.relativeLayout_map_ID); //relativeLayout_map.getLayoutParams();//     view.findViewById(R.id.relativeLayout_map_ID); //  relativeLayout_map.getLayoutParams();

        relativeLayout_param_map = relativeLayout_map.getLayoutParams();

        rootLayout_param_map = (ViewGroup.MarginLayoutParams) rootLayout.getLayoutParams();

        rootLayout_param_mapview =  (ViewGroup.MarginLayoutParams) mapview.getLayoutParams();

        if (heightFrame > 1200) {
            rootLayout_param_map.topMargin = rootLayout_param_map.topMargin + 100;
            rootLayout.setLayoutParams(rootLayout_param_map);
            rootLayout_param_mapview.topMargin = 150 + 500 - heightFrame;
        } else {
            rootLayout_param_mapview.topMargin = 150 + 200 - heightFrame;
        }

        mapview.setLayoutParams(rootLayout_param_mapview);

        //View DividerView = (View) view.findViewById(R.id.divider_ID);

        //DividerView.setOnTouchListener(this);
        rootLayout.setOnTouchListener(this);

        TextView_GK_Name = (TextView) view.findViewById(R.id.textView_GK_name_ID);

        TextView_GK_Name.setText(GK_name);

        Realm_City city = Main.mRealm.where(Realm_City.class).equalTo("id", "50").findFirst();
        Realm_Complex complex = Main.mRealm.where(Realm_Complex.class).equalTo("id", Complex_ID).findFirst();

        if((city != null)&&(complex != null)) {

            mapview.getMap().move(
                    new CameraPosition(new com.yandex.mapkit.geometry.Point(Float.parseFloat(complex.getCenter_x()), Float.parseFloat(complex.getCenter_y())), city.getZoom(), 0.0f, 0.0f),
                    new Animation(Animation.Type.SMOOTH, 0),
                    null);

            mapview.getMap().getMapObjects().addPlacemark(new com.yandex.mapkit.geometry.Point(Float.parseFloat(complex.getCenter_x()), Float.parseFloat(complex.getCenter_y())), ImageProvider.fromResource(view.getContext(), R.drawable.gk_selected));

            images_Complex = complex.getImgSliders();

            TextView textView_GK_Head = (TextView) view.findViewById(R.id.textView_GK_Head);
            textView_GK_Head.setText(complex.getTitle());
            TextView textView_GK_adress = (TextView) view.findViewById(R.id.textView_GK_adress);
            textView_GK_adress.setText(complex.getAddress());
            TextView textView_GK_date_end = (TextView) view.findViewById(R.id.textView_GK_date_end);
            textView_GK_date_end.setText(complex.getDate_end());

            viewPager = (ViewPager) view.findViewById(R.id.viewPager_ID);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity());
            viewPager.setAdapter(viewPagerAdapter);

            viewPager.setOnTouchListener(this);

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask(), 2000, 4000);

            token = sPref.getString("ghost_token", "");

            //Realm_Complex_Info complex_info = Main.mRealm.where(Realm_Complex_Info.class).equalTo("title", GK_name).findFirst();

            refresh_Data_Complex_Info();

            requestComplex("https://pro100.media/api/kortros/get-complex-info/?token=" + token + "&id=" + complex.getId() + "&app=1");

        } else {

            replaceFragment(SearchTownFragment.newInstance(posTown)); // откатываемся назад к городам

        }

        return view;
    }

    private void requestComplex(String url) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get(url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Response", "Messsage: " + response.toString());

                try {

                    JSONObject jsonObjects = null;
                    JSONObject jsonObjectsData = null;
                    JSONObject jsonObjects_Complex_Info = null;
                    JSONObject jsonObjects_Complex_Action = null;

                    JSONArray jsonArray_Price = null;
                    JSONArray jsonArray_Ipoteka = null;

                    try {

                        jsonObjects = new JSONObject(response.toString());
                        jsonObjectsData = jsonObjects.getJSONObject("data");
                        jsonObjects_Complex_Info = jsonObjectsData.getJSONObject("info");

                        SharedPreferences.Editor ed = sPref.edit();

                        Main.mRealm.beginTransaction();

//                        Main.mRealm.delete(Realm_Complex_Info.class);

                        Realm_Complex_Info Complex_Info = Main.mRealm.where(Realm_Complex_Info.class).equalTo("title", GK_name).findFirst();

                        if (Complex_Info == null) {
                            Complex_Info = Main.mRealm.createObject(Realm_Complex_Info.class);
                        }

                        Complex_Info.setTitle(jsonObjects_Complex_Info.getString("title").toString().trim());

                        jsonArray_Price = jsonObjects_Complex_Info.getJSONArray("price");
                        RealmList<ModelPrice> price = new RealmList<ModelPrice>();
                        for (int i = 0; i < jsonArray_Price.length(); i++) {

                            /*
                                "title":      "1-\u043a\u043e\u043c\u043d\u0430\u0442\u043d\u0430\u044f",
                                "count_text": "69 \u043a\u0432\u0430\u0440\u0442\u0438\u0440",
                                "text":       "\u041e\u0442 4.52 \u0434\u043e 7.62 \u043c\u043b\u043d \u0440\u0443\u0431",
                                "type_apartment": 1
                            */

                            JSONObject jsonObject_Price = jsonArray_Price.getJSONObject(i);
                            ModelPrice Complex_Price = Main.mRealm.createObject(ModelPrice.class);

                            Complex_Price.title = jsonObject_Price.getString("title").toString().trim();
                            Complex_Price.count_text = jsonObject_Price.getString("count_text").toString().trim();
                            Complex_Price.text = jsonObject_Price.getString("text").toString().trim();
                            Complex_Price.type_apartment = jsonObject_Price.getInt("type_apartment");

                            price.add(Complex_Price);
                        }

                        Complex_Info.setPrice(price);

                        Complex_Info.setBuildProgress(jsonObjects_Complex_Info.getDouble("build_progress"));

                        jsonArray_Ipoteka = jsonObjects_Complex_Info.getJSONArray("ipoteka");
                        RealmList<ModelIpoteka> ipoteka = new RealmList<ModelIpoteka>();
                        for (int i = 0; i < jsonArray_Ipoteka.length(); i++) {

                            /*
                                "logo":  "https://test.mysmartflat.ru/img/v1/sber.png",
                                "title": "\u0421\u0431\u0435\u0440\u0431\u0430\u043d\u043a \u0420\u043e\u0441\u0441\u0438\u0438",
                                "text": [
                                         "\u0421\u0440\u043e\u043a \u043a\u0440\u0435\u0434\u0438\u0442\u0430 \u043e\u0442 1 \u0433\u043e\u0434\u0430 \u0434\u043e 30 \u043b\u0435\u0442",
                                         "\u041f\u0435\u0440\u0432\u043e\u043d\u0430\u0447\u0430\u043b\u044c\u043d\u044b\u0439 \u0432\u0437\u043d\u043e\u0441: \u043e\u0442 15%",
                                         "\u0421\u0440\u043e\u043a \u043a\u0440\u0435\u0434\u0438\u0442\u0430 \u043e\u0442 1 \u0433\u043e\u0434\u0430 \u0434\u043e 30 \u043b\u0435\u0442"
                                        ]
                            */

                            JSONObject jsonObject_Ipoteka = jsonArray_Ipoteka.getJSONObject(i);
                            ModelIpoteka Complex_Ipoteka = Main.mRealm.createObject(ModelIpoteka.class);

                            Complex_Ipoteka.logo = jsonObject_Ipoteka.getString("logo").toString().trim();
                            Complex_Ipoteka.title = jsonObject_Ipoteka.getString("title").toString().trim();

                            JSONArray jsonArray_Texts = jsonObject_Ipoteka.getJSONArray("text");
                            RealmList<String> texts = new RealmList<String>();
                            for (int j=0; j < jsonArray_Texts.length(); j++) {
                                texts.add(jsonArray_Texts.getString(j).toString());
                            }
                            Complex_Ipoteka.text = texts;

                            ipoteka.add(Complex_Ipoteka);

                        }
                        Complex_Info.setIpoteka(ipoteka);

                        jsonObjects_Complex_Action = jsonObjects_Complex_Info.getJSONObject("action");

                        Complex_Info.setActionType(jsonObjects_Complex_Action.getString("type").toString().trim());
                        Complex_Info.setActionTitle(jsonObjects_Complex_Action.getString("title").toString().trim());
                        Complex_Info.setActionNumber(jsonObjects_Complex_Action.getString("number").toString().trim());

                        Main.mRealm.commitTransaction();

                        ed.commit();

                        refresh_Data_Complex_Info();

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

    private void refresh_Data_Complex_Info() {

        Realm_Complex_Info complex_info = Main.mRealm.where(Realm_Complex_Info.class).equalTo("title", GK_name).findFirst();

        if (complex_info != null) {

            progressBar.setProgress((int)(complex_info.getBuildProgress()*100));

            button_action.setText(complex_info.getActionTitle());
            Action_Number = complex_info.getActionNumber();

            button_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+"+Action_Number));
                    startActivity(intent);
                }
            });

            List_Price = new ArrayList(complex_info.getPrice());
            mList = (ListView) view.findViewById(R.id.ListView_price_ID);
            mAdapterPrice = new AdapterPrice(view.getContext(), List_Price);
            mList.setAdapter(mAdapterPrice);

            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ModelPrice objPrice = (ModelPrice) List_Price.get(position);
                    Integer typeApartment = objPrice.getTypeApartment();
                    ((FlatsFragment.Listener) getActivity()).onFlatsClickEvent(Complex_ID,typeApartment);

                }
            });

            ViewGroup.LayoutParams params = mList.getLayoutParams();
            params.height = convertDpToPy(70)*List_Price.size();
            mList.setLayoutParams(params);

            //if (mAdapterIpoteka == null) {

                List_Ipoteka = new ArrayList(complex_info.getIpoteka());
                mAdapterIpoteka = new AdapterIpoteka(view.getContext(), List_Ipoteka);
                mList2.setAdapter(mAdapterIpoteka);

            //} else {

            //    List_Ipoteka.clear();
            //    List_Ipoteka = new ArrayList(complex_info.getIpoteka());
            //    mAdapterIpoteka.notifyDataSetInvalidated();

            //}

            params = mList2.getLayoutParams();
            params.height = convertDpToPy(140)*List_Ipoteka.size();
            mList2.setLayoutParams(params);

        }
    }


    private int convertDpToPy(int dp){
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            return Math.round(dp * (getResources().getDisplayMetrics().ydpi / DisplayMetrics.DENSITY_MEDIUM));
        } else {
            return Math.round(dp*(getResources().getDisplayMetrics().ydpi/ DisplayMetrics.DENSITY_DEFAULT));
        }
    }

    public class TimerTask extends java.util.TimerTask{
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(viewPager.getCurrentItem() == (SearchGkFragment.images_Complex.size()-1)){
                       viewPager.setCurrentItem(0);
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    }
                }
            });
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        // void openHome(View view);
    }

}

/*
{
    "command":"kortros->get-complex-info",
    "error":0,
    "message":"",
    "data":{
            "token":"ust-377134-4b15e56857aa9e3dc43f7c3bfb3fe133",
            "info":{
                    "img":[
                           "https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_1.jpg",
                           "https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_2.jpg",
                           "https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_3.jpg",
                           "https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_4.jpg"],

                    "title":"\u0416\u041a \u041b\u044e\u0431\u043b\u0438\u043d\u0441\u043a\u0438\u0439",
                    "date_end":"\u0421\u0440\u043e\u043a \u0441\u0434\u0430\u0447\u0438: 4 \u043a\u0432\u0430\u0440\u0442\u0430\u043b 2018",
                    "price":[
                             {
                              "title":"1-\u043a\u043e\u043c\u043d\u0430\u0442\u043d\u0430\u044f",
                              "count_text":"69 \u043a\u0432\u0430\u0440\u0442\u0438\u0440",
                              "text":"\u041e\u0442 4.52 \u0434\u043e 7.62 \u043c\u043b\u043d \u0440\u0443\u0431",
                              "type_apartment":1
                             },{
                              "title":"2-\u043a\u043e\u043c\u043d\u0430\u0442\u043d\u0430\u044f",
                              "count_text":"46 \u043a\u0432\u0430\u0440\u0442\u0438\u0440","text":
                              "\u041e\u0442 8.25 \u0434\u043e 11.26 \u043c\u043b\u043d \u0440\u0443\u0431",
                              "type_apartment":2
                             },{
                              "title":"3-\u043a\u043e\u043c\u043d\u0430\u0442\u043d\u0430\u044f",
                              "count_text":"5 \u043a\u0432\u0430\u0440\u0442\u0438\u0440",
                              "text":"\u041e\u0442 11.62 \u0434\u043e 15.02 \u043c\u043b\u043d \u0440\u0443\u0431",
                              "type_apartment":3
                             }
                            ],
                    "description":null,
                    "description_more":"",
                    "build_progress":0.75,
                    "ipoteka":[
                               {
                                "logo":"https://test.mysmartflat.ru/img/v1/sber.png",
                                "title":"\u0421\u0431\u0435\u0440\u0431\u0430\u043d\u043a \u0420\u043e\u0441\u0441\u0438\u0438",
                                "text":["\u0421\u0440\u043e\u043a \u043a\u0440\u0435\u0434\u0438\u0442\u0430 \u043e\u0442 1 \u0433\u043e\u0434\u0430 \u0434\u043e 30 \u043b\u0435\u0442",
                                        "\u041f\u0435\u0440\u0432\u043e\u043d\u0430\u0447\u0430\u043b\u044c\u043d\u044b\u0439 \u0432\u0437\u043d\u043e\u0441: \u043e\u0442 15%",
                                        "\u0421\u0440\u043e\u043a \u043a\u0440\u0435\u0434\u0438\u0442\u0430 \u043e\u0442 1 \u0433\u043e\u0434\u0430 \u0434\u043e 30 \u043b\u0435\u0442"
                                       ]
                               },{
                                "logo":"https://test.mysmartflat.ru/img/v1/bopened.png",
                                "title":"\u0411\u0430\u043d\u043a \u041e\u0442\u043a\u0440\u044b\u0442\u0438\u0435",
                                "text":["\u0421\u0440\u043e\u043a \u043a\u0440\u0435\u0434\u0438\u0442\u0430 \u043e\u0442 1 \u0433\u043e\u0434\u0430 \u0434\u043e 30 \u043b\u0435\u0442",
                                        "\u041f\u0435\u0440\u0432\u043e\u043d\u0430\u0447\u0430\u043b\u044c\u043d\u044b\u0439 \u0432\u0437\u043d\u043e\u0441: \u043e\u0442 15%",
                                        "\u0421\u0440\u043e\u043a \u043a\u0440\u0435\u0434\u0438\u0442\u0430 \u043e\u0442 1 \u0433\u043e\u0434\u0430 \u0434\u043e 30 \u043b\u0435\u0442"
                                       ]
                               }
                              ],
                     "action":{
                               "type":"call",
                               "title":"\u041f\u043e\u0437\u0432\u043e\u043d\u0438\u0442\u044c",
                               "number":"74951043431"
                              }
                   }
                   },
             "token":"ust-377134-4b15e56857aa9e3dc43f7c3bfb3fe133","fromdomain":""
}
*/