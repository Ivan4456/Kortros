package ru.mysmartflat.kortros;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.location.LocationListener;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.AnimatedIcon;
import com.yandex.mapkit.map.Callback;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapLoadedListener;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectCollectionListener;
import com.yandex.mapkit.map.MapObjectDragListener;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.ModelStyle;
import com.yandex.mapkit.map.PlacemarkMapObject;
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

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.yandex.runtime.Runtime.getApplicationContext;

public class SearchTownFragment extends Fragment implements UserLocationObjectListener { //, LinearLayout.OnTouchListener , View.OnTouchListener

    private OnFragmentInteractionListener mListener;
    private ImageView imageView_searchCity;
    private MapView mapview2;

    private UserLocationLayer userLocationLayer;
    private View view;
    private Spinner TownSpinner;
    private SharedPreferences sPref;

    String token;
   // private Realm mRealm;
    private RealmResults<Realm_City> Cities;
    private RealmQuery<Realm_City> query_Cities;
    private RealmResults<Realm_Complex> realm_complexes;


    int posTown = 0;
    boolean first_GK_view = false;
    static int first_GK_remap = 0;

    //String[] Town;// = new String[10];
    ArrayList<String> Town = new ArrayList<String>();

    public SearchTownFragment() {
        // Required empty public constructor
    }

    public static SearchTownFragment newInstance(Integer Pos) {
        SearchTownFragment fragment = new SearchTownFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("Town_pos", Pos);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onStop() {
        //mapview2.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        //mapview2.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //MapKitFactory.setApiKey("6748b84e-0e7c-49eb-afb3-6e2ee2bf2a52");//ac7ff396-c67b-4e3e-bd68-d0f4c88385a3
        //MapKitFactory.initialize(this.getActivity());
        super.onCreate(savedInstanceState);
    }

   // private final CameraListener cameraListener = new CameraListener() {

   // }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_seach_town, container, false);

        first_GK_remap = 0;

        final MapView mapview = (MapView) view.findViewById(R.id.mapview_ID);
        mapview2 = mapview;

        sPref = getActivity().getSharedPreferences("City_Settings", Context.MODE_PRIVATE);                        // MODE_WORLD_READABLE);

        //mapview = (MapView) view.findViewById(R.id.mapview_ID);
        //mapview.getMap().addCameraListener(cameraListener);

        //MapController mMapController = mapview.getMapController();
        //OverlayManager mOverlayManager = mMapController.getOverlayManager();
        //mOverlayManager.addOverlay(overlay);

        posTown = getArguments().getInt("Town_pos",0);

        TownSpinner = (Spinner) view.findViewById(R.id.town_spinner_ID);

        //Creating the ArrayAdapter instance having the country list
        //Realm.init(getApplicationContext());
        //getActivity().mRealm = Realm.getDefaultInstance();

        query_Cities = Main.mRealm.where(Realm_City.class);
        Cities = query_Cities.findAll();

        int i = 0;
        while(i < query_Cities.count()){
            Town.add(Cities.get(i).getTitle());
            i++;
        }

        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,Town);

        aa.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

        TownSpinner.setAdapter(aa);


        TownSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                mapview.getMap().move(
                        new CameraPosition(new com.yandex.mapkit.geometry.Point(Float.parseFloat(Cities.get(pos).getCenter_x()), Float.parseFloat(Cities.get(pos).getCenter_y())), Cities.get(pos).getZoom(), 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 0),
                        null);

                SearchTownFragment.first_GK_remap = 0;

                refreshComplexList();

/*
                switch (pos) {
                    case 0:

                        Integer city_number = 0;
                        try {
                            city_number = sPref.getInt("city_", 0);
                        } catch (Exception e) {
                            Log.e("Error", e.toString());
                        }

                        int i = 0;
                        while(i < query_Cities.count()){
                            Town.add(Cities.get(i).getTitle());
                            i++;
                        }

                        mapview.getMap().move(
                                new CameraPosition(new com.yandex.mapkit.geometry.Point(Float.parseFloat(Cities.get(i).getCenter_x()), Float.parseFloat(Cities.get(i).getCenter_y())), Cities.get(i).getZoom(), 0.0f, 0.0f),
                                new Animation(Animation.Type.SMOOTH, 0),
                                null);

                        mapview.getMap().getMapObjects().addPlacemark(new com.yandex.mapkit.geometry.Point(55.73222, 37.61556), ImageProvider.fromResource(view.getContext(),R.drawable.gk)).addTapListener(new MapObjectTapListener() {
                            @Override
                            public boolean onMapObjectTap(MapObject mapObject, com.yandex.mapkit.geometry.Point point) {

                                ((Listener) getActivity()).onGkClickEvent(0,"Любенкий1");

                                return false;
                            }
                        });

                        mapview.getMap().getMapObjects().addPlacemark(new com.yandex.mapkit.geometry.Point(55.76222, 37.69556), ImageProvider.fromResource(view.getContext(),R.drawable.gk)).addTapListener(new MapObjectTapListener() {
                            @Override
                            public boolean onMapObjectTap(MapObject mapObject, com.yandex.mapkit.geometry.Point point) {

                                ((Listener) getActivity()).onGkClickEvent(0,"Любенкий2");

                                return false;
                            }
                        });

                        mapview.getMap().getMapObjects().addPlacemark(new com.yandex.mapkit.geometry.Point(55.795222, 37.64556), ImageProvider.fromResource(view.getContext(),R.drawable.gk)).addTapListener(new MapObjectTapListener() {
                            @Override
                            public boolean onMapObjectTap(MapObject mapObject, com.yandex.mapkit.geometry.Point point) {

                                ((Listener) getActivity()).onGkClickEvent(0,"Любенкий3");

                                return false;
                            }
                        });

                        break;
                    case 1:
                        mapview.getMap().move(
                                new CameraPosition(new com.yandex.mapkit.geometry.Point(Float.parseFloat(Cities.get(i).getCenter_x()), Float.parseFloat(Cities.get(i).getCenter_y())), Cities.get(i).getZoom(), 0.0f, 0.0f),
                                new Animation(Animation.Type.SMOOTH, 0),
                                null);
                        break;
                    case 2:
                        mapview.getMap().move(
                                new CameraPosition(new com.yandex.mapkit.geometry.Point(Float.parseFloat(Cities.get(i).getCenter_x()), Float.parseFloat(Cities.get(i).getCenter_y())), Cities.get(i).getZoom(), 0.0f, 0.0f),
                                new Animation(Animation.Type.SMOOTH, 0),
                                null);
                        break;
                    case 3:
                        mapview.getMap().move(
                                new CameraPosition(new com.yandex.mapkit.geometry.Point(59.9386300, 30.3141300), 11.0f, 0.0f, 0.0f),
                                new Animation(Animation.Type.SMOOTH, 0),
                                null);
                        break;
                }
 */
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        TownSpinner.setSelection(posTown);

        userLocationLayer = mapview.getMap().getUserLocationLayer();

        userLocationLayer.setEnabled(true);

        userLocationLayer.setHeadingEnabled(true);

        userLocationLayer.setObjectListener(this);

        ImageProvider Ip = new ImageProvider() {
            @Override
            public String getId() {
                return null;
            }

            @Override
            public Bitmap getImage() {
                return null;
            }
        };

        //com.yandex.mapkit.geometry.Circle;
        //while (!mapview.isValid()){}

        /*
        mapview.getMap().setMapLoadedListener(new MapLoadedListener() {
            public void onMapActionEvent(MapEvent event) {
                if(event==MapEvent.MSG_LONG_PRESS){//обработка Вашего события}
            }
        });
        */

        ImageView transparentImageView = (ImageView) view.findViewById(R.id.transparent_image_ID);

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                         // Disallow ScrollView to intercept touch events.
                         if (SearchTownFragment.first_GK_remap < 2) {
                             SearchTownFragment.first_GK_remap++;
                             refreshComplexList();
                         }

/*
                         mapview2.getMap().getMapObjects().addPlacemark(new com.yandex.mapkit.geometry.Point(55.73222, 37.61556), ImageProvider.fromResource(view.getContext(),R.drawable.gk)).addTapListener(new MapObjectTapListener() {
                             @Override
                             public boolean onMapObjectTap(MapObject mapObject, com.yandex.mapkit.geometry.Point point) {

                                 ((Listener) getActivity()).onGkClickEvent(0,"Любенкий1");

                                 return false;
                             }
                         });

                         mapview2.getMap().getMapObjects().addPlacemark(new com.yandex.mapkit.geometry.Point(55.76222, 37.69556), ImageProvider.fromResource(view.getContext(),R.drawable.gk)).addTapListener(new MapObjectTapListener() {
                             @Override
                             public boolean onMapObjectTap(MapObject mapObject, com.yandex.mapkit.geometry.Point point) {

                                ((Listener) getActivity()).onGkClickEvent(0,"Любенкий2");

                                return false;
                             }
                         });

                         mapview2.getMap().getMapObjects().addPlacemark(new com.yandex.mapkit.geometry.Point(55.795222, 37.64556), ImageProvider.fromResource(view.getContext(),R.drawable.gk)).addTapListener(new MapObjectTapListener() {
                             @Override
                             public boolean onMapObjectTap(MapObject mapObject, com.yandex.mapkit.geometry.Point point) {

                                ((Listener) getActivity()).onGkClickEvent(0,"Любенкий3");

                                return false;
                             }
                         });
*/
                         return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.

                        return false;

                    case MotionEvent.ACTION_MOVE:

                        return false;

                    default:
                        return false;
                }
            }
        });


        token = sPref.getString("ghost_token", "");

        requestComplexList("https://pro100.media/api/kortros/get-complex/?token="+token+"&city_id=50&app=1");

        return view;
    }

    private void requestComplexList(String url) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get(url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Response", "Messsage: " + response.toString());

                try {

                    JSONObject jsonObjects = null;
                    JSONObject jsonObjectsData = null;
                    JSONArray jsonArray_Complex = null;
                    JSONArray jsonArray_Sliders = null;

                    try {

                        jsonObjects = new JSONObject(response.toString());
                        jsonObjectsData = jsonObjects.getJSONObject("data");
                        jsonArray_Complex = jsonObjectsData.getJSONArray("complex");

                        SharedPreferences.Editor ed = sPref.edit();

                        Main.mRealm.beginTransaction();
                        ///Main.mRealm.delete(Realm_Complex.class);

                        for (int i = 0; i < jsonArray_Complex.length(); i++) {

                            JSONObject jsonObject_Complex = jsonArray_Complex.getJSONObject(i);

                            //Realm_Complex Complexes = Main.mRealm.createObject(Realm_Complex.class);
                            Realm_Complex Complexes = Main.mRealm.where(Realm_Complex.class).equalTo("id", jsonObject_Complex.getString("id").toString().trim()).findFirst();

                            if (Complexes == null) {
                                Complexes = Main.mRealm.createObject(Realm_Complex.class);
                            }

                            Complexes.setTitle(jsonObject_Complex.getString("title").toString().trim());
                            Complexes.setId(jsonObject_Complex.getString("id").toString().trim());
                            Complexes.setCityId(jsonObject_Complex.getString("city_id").toString().trim());

                            Complexes.setDescription(jsonObject_Complex.getString("description").toString().trim());
                            Complexes.setAddress(jsonObject_Complex.getString("address").toString().trim());
                            Complexes.setDate_end(jsonObject_Complex.getString("date_end").toString().trim());


                            jsonArray_Sliders = jsonObject_Complex.getJSONArray("img");

                            RealmList<String> img = new RealmList<String>();

                            for (int j=0; j < jsonArray_Sliders.length(); j++) {

                                img.add(jsonArray_Sliders.getString(j).toString());

                            }

                            Complexes.setImgSliders(img);

                            JSONObject jsonArray_Coord_Center = jsonObject_Complex.getJSONObject("center");

                            Complexes.setCenter_x(String.valueOf(jsonArray_Coord_Center.getDouble("x")));
                            Complexes.setCenter_y(String.valueOf(jsonArray_Coord_Center.getDouble("y")));
                        }

                        Main.mRealm.commitTransaction();

                        ed.commit();

                        //if (!first_GK_view){

                            refreshComplexList();

                        //}

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

    private void refreshComplexList() {
        RealmQuery<Realm_Complex> query_Complex = Main.mRealm.where(Realm_Complex.class);

        if (query_Complex.count() > 0) {

            first_GK_view = true;

            realm_complexes = query_Complex.findAll();

            mapview2.getMap().getMapObjects().clear();

            int i = 0;
            while (i < query_Complex.count()) {

                final Integer position_Town = posTown;
                final String title = realm_complexes.get(i).getTitle();
                final String complex_id = realm_complexes.get(i).getId();

                mapview2.getMap().getMapObjects().addPlacemark(new com.yandex.mapkit.geometry.Point(Float.parseFloat(realm_complexes.get(i).getCenter_x()), Float.parseFloat(realm_complexes.get(i).getCenter_y())), ImageProvider.fromResource(view.getContext(), R.drawable.gk)).addTapListener(new MapObjectTapListener() {
                    @Override
                    public boolean onMapObjectTap(MapObject mapObject, com.yandex.mapkit.geometry.Point point) {

                        ((Listener) getActivity()).onGkClickEvent(position_Town, complex_id , title);

                        return false;
                    }
                });

                i++;
            }
        }
    }

    @Override
    public void onObjectRemoved(UserLocationView view) {
    }

    @Override
    public void onObjectUpdated(UserLocationView view, ObjectEvent event) {
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
       // userLocationLayer.setAnchor(
       //         new PointF((float)(mapview2.getWidth() * 0.5), (float)(mapview2.getHeight() * 0.5)),
       //         new PointF((float)(mapview2.getWidth() * 0.5), (float)(mapview2.getHeight() * 0.83)));

       // userLocationView.getPin().setIcon(ImageProvider.fromResource(getActivity(), R.drawable.));
       // userLocationView.getArrow().setIcon(ImageProvider.fromResource(getActivity(), R.drawable.arrow));
       // userLocationView.getAccuracyCircle().setFillColor(Color.RED);
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

    interface Listener {
        public void onGkClickEvent(Integer Pos, String complex_id, String name);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
       // void openHome(View view);
    }

}

/*
{"command":"kortros->get-complex",
 "error":0,
 "message":"",
 "data":{
         "token":"ust-377134-4b15e56857aa9e3dc43f7c3bfb3fe133",
         "complex":[{
                     "id":"12",
                     "city_id":"50",
                     "title":"\u0416\u041a Headliner",
                     "description":"\u0416\u041a Headliner",
                     "img":[
                            "https://pro100.media/api/ihome/images/kortros/img/small-slider/slider01_375.jpg",
                            "https://pro100.media/api/ihome/images/kortros/img/small-slider/slider02_375.jpg",
                            "https://pro100.media/api/ihome/images/kortros/img/small-slider/slider03_375.jpg",
                            "https://pro100.media/api/ihome/images/kortros/img/small-slider/slider04_375.jpg",
                            "https://pro100.media/api/ihome/images/kortros/img/small-slider/slider05_375.jpg",
                            "https://pro100.media/api/ihome/images/kortros/img/small-slider/slider06_375.jpg"
                           ],
                     "address":"\u041c\u043e\u0441\u043a\u0432\u0430,\u0428\u043c\u0438\u0442\u043e\u0432\u0441\u043a\u0438\u0439 \u043f\u0440\u043e\u0435\u0437\u0434, \u0434. 39",
                     "center":{
                               "x":"55.7551133807672",
                               "y":"37.545453489882"
                              },
                     "date_end":"\u0421\u0440\u043e\u043a \u0441\u0434\u0430\u0447\u0438: 4 \u043a\u0432\u0430\u0440\u0442\u0430\u043b 2019"
                    },{

                    "id":"18","city_id":"50","title":"\u0410\u043f\u0430\u0440\u0442\u0430\u043c\u0435\u043d\u0442\u044b \u0414\u043e\u043c 128","description":"\u0410\u043f\u0430\u0440\u0442\u0430\u043c\u0435\u043d\u0442\u044b \u0414\u043e\u043c 128","img":["https://pro100.media/api/ihome/images/kortros/img/small-slider/128_1.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/128_2.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/128_3.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/128_4.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/128_5.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/128_6.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/128_7.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/128_8.jpg"],"address":"\u0433. \u041c\u043e\u0441\u043a\u0432\u0430, \u0443\u043b. \u041f\u0440\u043e\u0444\u0441\u043e\u044e\u0437\u043d\u0430\u044f, \u0432\u043b. 128","center":{"x":"55.630401","y":"37.515102"},"date_end":"\u0421\u0440\u043e\u043a \u0441\u0434\u0430\u0447\u0438: 4 \u043a\u0432\u0430\u0440\u0442\u0430\u043b 2018"},{"id":"11","city_id":"50","title":"\u0416\u041a \u0414\u043e\u043c \u0441\u0435\u0440\u0435\u0431\u0440\u044f\u043d\u044b\u0439 \u0431\u043e\u0440","description":"\u0416\u041a \u0414\u043e\u043c \u0441\u0435\u0440\u0435\u0431\u0440\u044f\u043d\u044b\u0439 \u0431\u043e\u0440","img":["https://pro100.media/api/ihome/images/kortros/img/small-slider/silver_1.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/silver_2.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/silver_3.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/silver_4.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/silver_5.jpg"],"address":"\u0433. \u041c\u043e\u0441\u043a\u0432\u0430, \u0443\u043b. \u0416\u0438\u0432\u043e\u043f\u0438\u0441\u043d\u0430\u044f, \u0432\u043b. 21","center":{"x":"55.793065","y":"37.448509"},"date_end":"\u0421\u0440\u043e\u043a \u0441\u0434\u0430\u0447\u0438: 1 \u043a\u0432\u0430\u0440\u0442\u0430\u043b 2019"},{"id":"13","city_id":"50","title":"\u0416\u041a \u041b\u044e\u0431\u043b\u0438\u043d\u0441\u043a\u0438\u0439","description":"\u0416\u041a \u041b\u044e\u0431\u043b\u0438\u043d\u0441\u043a\u0438\u0439","img":["https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_1.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_2.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_3.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_4.jpg"],"address":"\u041c\u043e\u0441\u043a\u0432\u0430, \u043f\u0440. 40 \u043b\u0435\u0442 \u041e\u043a\u0442\u044f\u0431\u0440\u044f, \u0432\u043b. 36","center":{"x":"55.673803","y":"37.745546"},"date_end":"\u0421\u0440\u043e\u043a \u0441\u0434\u0430\u0447\u0438: 4 \u043a\u0432\u0430\u0440\u0442\u0430\u043b 2018"}

                    ]},
         "token":"ust-377134-4b15e56857aa9e3dc43f7c3bfb3fe133",
         "fromdomain":""
        }
*/