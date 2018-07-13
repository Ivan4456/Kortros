package ru.mysmartflat.kortros;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import io.realm.RealmList;

import static com.yandex.runtime.Runtime.getApplicationContext;

public class SearchFilterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private SharedPreferences sPref;

    static boolean finish_load = false;
    boolean priceMinEditing = true;
    boolean setPrice = false;
    String token;

    Button button_call;

    View view;

    CheckBox checkBox_sales;
    CheckBox checkBox_finish;

    String sales = "0";
    String finish = "0";

    Integer count_flats = 0;

    Button button_studia;
    Button button_1;
    Button button_2;
    Button button_3;
    Button button_4;

    byte button_studia_Set = 0;
    byte button_1_Set = 0;
    byte button_2_Set = 0;
    byte button_3_Set = 0;
    byte button_4_Set = 0;

    int squareMin = 0;
    int squareMax = 0;

    int floorMin = 0;
    int floorMax = 0;

    Spinner spinner_Town;
    Integer townCurrentPos = 0;
    ArrayAdapter adapterSpinerTown;
    ArrayList<String> Town_title = new ArrayList<String>();
    ArrayList<String> Town_ID = new ArrayList<String>();

    static String Filter_GK_complex_ID = "";
    static String Filter_GK_building_ID = "";
    static String Filter_GK_section_ID = "";


    Spinner spinner_GK;
    Integer GKCurrentPos = 0;
    ArrayAdapter adapterSpinerGK;
    ArrayList<String> GK_complex = new ArrayList<String>();
    ArrayList<String> GK_complex_ID = new ArrayList<String>();

    Spinner spinner_Building;
    Integer BuildingCurrentPos = 0;
    ArrayAdapter adapterSpinerBuilding;
    ArrayList<String> Building_Number = new ArrayList<String>();
    ArrayList<String> Building_ID = new ArrayList<String>();

    Spinner spinner_Section;
    Integer SectionCurrentPos = 0;
    ArrayAdapter adapterSpinerSection;
    ArrayList<String> Section_Number = new ArrayList<String>();
    ArrayList<String> Section_ID = new ArrayList<String>();

    Spinner spinner_SquareMin;
    ArrayAdapter adapterSpinerSquareMin;

    ArrayList<String> Square = new ArrayList<String>();

    Spinner spinner_SquareMax;
    ArrayAdapter adapterSpinerSquareMax;

    Spinner spinner_FloorMin;
    ArrayAdapter adapterSpinerFloorMin;

    ArrayList<String> Floor = new ArrayList<String>();

    Spinner spinner_FloorMax;
    ArrayAdapter adapterSpinerFloorMax;

    EditText GK_editText_price_min;
    EditText GK_editText_price_max;

    long priceMin = 1;
    long priceMax = 100000000;

    public SearchFilterFragment() {
        // Required empty public constructor
    }

    public static SearchFilterFragment newInstance() {
        SearchFilterFragment fragment = new SearchFilterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    private void replaceFragment(Fragment newFragment) {

        FragmentTransaction trasection = getFragmentManager().beginTransaction();
        if(!newFragment.isAdded()){
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

        view = inflater.inflate(R.layout.fragment_seach_filter, container, false);

        finish_load = false;

        sPref = getActivity().getSharedPreferences("City_Settings", Context.MODE_PRIVATE);
        token = sPref.getString("ghost_token", "");

        RelativeLayout Back = (RelativeLayout) view.findViewById(R.id.linerlayout_back2_ID);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(SearchTownFragment.newInstance(0));
            }
        });


        RelativeLayout Clear = (RelativeLayout) view.findViewById(R.id.RelativeLayout_clear_ID);
        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button_studia_Set = 0;
                button_1_Set = 0;
                button_2_Set = 0;
                button_3_Set = 0;
                button_4_Set = 0;

                String sales = "0";
                String finish = "0";

                SearchFilterFragment.Filter_GK_complex_ID = "";
                SearchFilterFragment.Filter_GK_building_ID = "";
                SearchFilterFragment.Filter_GK_section_ID = "";

                checkBox_sales.setChecked(false);
                checkBox_finish.setChecked(false);

                button_call.setText("Поиск квартир...");
                button_call.setBackgroundResource(R.drawable.corner5);

                requestFilterComplex("https://pro100.media/api/kortros/get-complex/?token="+ token +"&city_id="+ Town_ID.get(0)+"&app=1");

            }
        });

        checkBox_sales = (CheckBox) view.findViewById(R.id.checkBox_seler_ID);
        checkBox_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBox_sales.isChecked()) {

                    sales = "1";

                } else {

                    sales = "0";

                }

                if (finish_load == true) {

                    button_call.setText("Поиск квартир...");
                    button_call.setBackgroundResource(R.drawable.corner5);

                    requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0=" + String.valueOf(button_studia_Set) + "&room1=" + String.valueOf(button_1_Set) + "&room2=" + String.valueOf(button_2_Set) + "&room3=" + String.valueOf(button_3_Set) + "&room4=" + String.valueOf(button_4_Set) + "&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id=" + Section_ID.get(SectionCurrentPos) + "&square_min=" + String.valueOf(squareMin) + "&square_max=" + String.valueOf(squareMax) + "&floor_min=" + String.valueOf(floorMin) + "&floor_max=" + String.valueOf(floorMax) + "&price_min=" + String.valueOf(priceMin) + "&price_max=" + String.valueOf(priceMax) + "&sales=" + sales + "&finish=" + finish + "&only_count=1&app=1");
                }
            }
        });

        checkBox_finish = (CheckBox) view.findViewById(R.id.checkBox_finish_ID);
        checkBox_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBox_finish.isChecked()) {

                    finish = "1";

                } else {

                    finish = "0";

                }

                if (finish_load == true) {

                    button_call.setText("Поиск квартир...");
                    button_call.setBackgroundResource(R.drawable.corner5);

                    requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0=" + String.valueOf(button_studia_Set) + "&room1=" + String.valueOf(button_1_Set) + "&room2=" + String.valueOf(button_2_Set) + "&room3=" + String.valueOf(button_3_Set) + "&room4=" + String.valueOf(button_4_Set) + "&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id=" + Section_ID.get(SectionCurrentPos) + "&square_min=" + String.valueOf(squareMin) + "&square_max=" + String.valueOf(squareMax) + "&floor_min=" + String.valueOf(floorMin) + "&floor_max=" + String.valueOf(floorMax) + "&price_min=" + String.valueOf(priceMin) + "&price_max=" + String.valueOf(priceMax) + "&sales=" + sales + "&finish=" + finish + "&only_count=1&app=1");
                }
            }
        });

        button_call = (Button) view.findViewById(R.id.button_call_ID);
        button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((count_flats > 0)&&(finish_load == true)&&(!button_call.getText().toString().equalsIgnoreCase("Поиск квартир..."))) {

                    ((FlatsFilterFragment.Listener) getActivity()).onFlatsFilterClickEvent(Town_ID.get(townCurrentPos),GK_complex_ID.get(GKCurrentPos),(int)button_studia_Set,(int)button_1_Set,(int)button_2_Set,(int)button_3_Set,(int)button_4_Set,Building_ID.get(BuildingCurrentPos),Section_ID.get(SectionCurrentPos),squareMin,squareMax,floorMin,floorMax,priceMin,priceMax,sales,finish);

                }
            }
        });

        button_studia = (Button) view.findViewById(R.id.button_studia_ID);
        button_1 = (Button) view.findViewById(R.id.button_1_ID);
        button_2 = (Button) view.findViewById(R.id.button_2_ID);
        button_3 = (Button) view.findViewById(R.id.button_3_ID);
        button_4 = (Button) view.findViewById(R.id.button_4_ID);

        button_studia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((button_studia.isEnabled() == true)&&(finish_load == true)) {

                    if (button_studia_Set != 0) {
                        button_studia_Set = 0;
                        button_studia.setBackgroundResource(R.drawable.corner6);

                    } else {
                        button_studia_Set = 1;
                        button_studia.setBackgroundResource(R.drawable.corner8);
                    }

                    //if (finish_load == true) {

                        button_call.setText("Поиск квартир...");
                        button_call.setBackgroundResource(R.drawable.corner5);

                        requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0=" + String.valueOf(button_studia_Set) + "&room1=" + String.valueOf(button_1_Set) + "&room2=" + String.valueOf(button_2_Set) + "&room3=" + String.valueOf(button_3_Set) + "&room4=" + String.valueOf(button_4_Set) + "&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id=" + Section_ID.get(SectionCurrentPos) + "&square_min=" + String.valueOf(squareMin) + "&square_max=" + String.valueOf(squareMax) + "&floor_min=" + String.valueOf(floorMin) + "&floor_max=" + String.valueOf(floorMax) + "&price_min=" + String.valueOf(priceMin) + "&price_max=" + String.valueOf(priceMax) + "&sales=" + sales + "&finish=" + finish + "&only_count=1&app=1");

                    //}
                }
            }
        });

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((button_1.isEnabled() == true)&&(finish_load == true)) {

                    if (button_1_Set != 0) {
                        button_1_Set = 0;
                        button_1.setBackgroundResource(R.drawable.corner6);

                    } else {
                        button_1_Set = 1;
                        button_1.setBackgroundResource(R.drawable.corner8);
                    }

                    //if (finish_load == true) {

                        button_call.setText("Поиск квартир...");
                        button_call.setBackgroundResource(R.drawable.corner5);

                        requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0=" + String.valueOf(button_studia_Set) + "&room1=" + String.valueOf(button_1_Set) + "&room2=" + String.valueOf(button_2_Set) + "&room3=" + String.valueOf(button_3_Set) + "&room4=" + String.valueOf(button_4_Set) + "&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id=" + Section_ID.get(SectionCurrentPos) + "&square_min=" + String.valueOf(squareMin) + "&square_max=" + String.valueOf(squareMax) + "&floor_min=" + String.valueOf(floorMin) + "&floor_max=" + String.valueOf(floorMax) + "&price_min=" + String.valueOf(priceMin) + "&price_max=" + String.valueOf(priceMax) + "&sales=" + sales + "&finish=" + finish + "&only_count=1&app=1");
                    //}
                }
            }
        });

        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((button_2.isEnabled() == true)&&(finish_load == true)) {

                    if (button_2_Set != 0) {
                        button_2_Set = 0;
                        button_2.setBackgroundResource(R.drawable.corner6);

                    } else {
                        button_2_Set = 1;
                        button_2.setBackgroundResource(R.drawable.corner8);
                    }

                    //if (finish_load == true) {

                        button_call.setText("Поиск квартир...");
                        button_call.setBackgroundResource(R.drawable.corner5);

                        requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0=" + String.valueOf(button_studia_Set) + "&room1=" + String.valueOf(button_1_Set) + "&room2=" + String.valueOf(button_2_Set) + "&room3=" + String.valueOf(button_3_Set) + "&room4=" + String.valueOf(button_4_Set) + "&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id=" + Section_ID.get(SectionCurrentPos) + "&square_min=" + String.valueOf(squareMin) + "&square_max=" + String.valueOf(squareMax) + "&floor_min=" + String.valueOf(floorMin) + "&floor_max=" + String.valueOf(floorMax) + "&price_min=" + String.valueOf(priceMin) + "&price_max=" + String.valueOf(priceMax) + "&sales=" + sales + "&finish=" + finish + "&only_count=1&app=1");
                    //}
                }
            }
        });

        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((button_3.isEnabled() == true)&&(finish_load == true)) {

                    if (button_3_Set != 0) {
                        button_3_Set = 0;
                        button_3.setBackgroundResource(R.drawable.corner6);

                    } else {
                        button_3_Set = 1;
                        button_3.setBackgroundResource(R.drawable.corner8);
                    }

                    //if (finish_load == true) {

                        button_call.setText("Поиск квартир...");
                        button_call.setBackgroundResource(R.drawable.corner5);

                        requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0=" + String.valueOf(button_studia_Set) + "&room1=" + String.valueOf(button_1_Set) + "&room2=" + String.valueOf(button_2_Set) + "&room3=" + String.valueOf(button_3_Set) + "&room4=" + String.valueOf(button_4_Set) + "&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id=" + Section_ID.get(SectionCurrentPos) + "&square_min=" + String.valueOf(squareMin) + "&square_max=" + String.valueOf(squareMax) + "&floor_min=" + String.valueOf(floorMin) + "&floor_max=" + String.valueOf(floorMax) + "&price_min=" + String.valueOf(priceMin) + "&price_max=" + String.valueOf(priceMax) + "&sales=" + sales + "&finish=" + finish + "&only_count=1&app=1");
                    //}
                }
            }
        });

        button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((button_4.isEnabled() == true)&&(finish_load == true)) {

                    if (button_4_Set != 0) {
                        button_4_Set = 0;
                        button_4.setBackgroundResource(R.drawable.corner6);

                    } else {
                        button_4_Set = 1;
                        button_4.setBackgroundResource(R.drawable.corner8);
                    }

                    //if (finish_load == true) {
                        button_call.setText("Поиск квартир...");
                        button_call.setBackgroundResource(R.drawable.corner5);

                        requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0=" + String.valueOf(button_studia_Set) + "&room1=" + String.valueOf(button_1_Set) + "&room2=" + String.valueOf(button_2_Set) + "&room3=" + String.valueOf(button_3_Set) + "&room4=" + String.valueOf(button_4_Set) + "&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id=" + Section_ID.get(SectionCurrentPos) + "&square_min=" + String.valueOf(squareMin) + "&square_max=" + String.valueOf(squareMax) + "&floor_min=" + String.valueOf(floorMin) + "&floor_max=" + String.valueOf(floorMax) + "&price_min=" + String.valueOf(priceMin) + "&price_max=" + String.valueOf(priceMax) + "&sales=" + sales + "&finish=" + finish + "&only_count=1&app=1");
                    //}
                }
            }
        });


        spinner_Town = (Spinner) view.findViewById(R.id.Town_spinner_ID);
        adapterSpinerTown = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, Town_title);
        adapterSpinerTown.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner_Town.setAdapter(adapterSpinerTown);
        spinner_Town.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (finish_load == true) {

                    finish_load = false;
                    button_call.setText("Поиск квартир...");
                    button_call.setBackgroundResource(R.drawable.corner5);

                    requestFilterComplex("https://pro100.media/api/kortros/get-complex/?token="+ token +"&city_id="+ Town_ID.get(townCurrentPos)+"&app=1");

                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_GK = (Spinner) view.findViewById(R.id.GK_spinner_ID);
        adapterSpinerGK = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, GK_complex);
        adapterSpinerGK.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner_GK.setAdapter(adapterSpinerGK);

        spinner_GK.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                //if (finish_load == true) {

                    //finish_load = false;
                    button_call.setText("Поиск квартир...");
                    button_call.setBackgroundResource(R.drawable.corner5);

                    GKCurrentPos = pos;

                    SearchFilterFragment.Filter_GK_complex_ID = GK_complex_ID.get(GKCurrentPos);

                    if (finish_load == true) {
                        SearchFilterFragment.Filter_GK_building_ID = "";
                        SearchFilterFragment.Filter_GK_section_ID = "";
                    }
                    requestFilterBilding("https://pro100.media/api/kortros/get-buildings/?token="+ token +"&complex_id="+ GK_complex_ID.get(GKCurrentPos) +"&app=1");

                //}
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_Building = (Spinner) view.findViewById(R.id.Building_spinner_ID);
        adapterSpinerBuilding = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, Building_Number);
        adapterSpinerBuilding.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner_Building.setAdapter(adapterSpinerBuilding);
        spinner_Building.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                //if (finish_load == true) {

                    //finish_load = false;
                    button_call.setText("Поиск квартир...");
                    button_call.setBackgroundResource(R.drawable.corner5);

                    BuildingCurrentPos = pos;

                    SearchFilterFragment.Filter_GK_building_ID = Building_ID.get(BuildingCurrentPos);

                    if (finish_load == true) {
                        SearchFilterFragment.Filter_GK_section_ID = "";
                    }

                    requestFilterSection("https://pro100.media/api/kortros/get-sections/?token="+ token +"&building_id="+ Building_ID.get(BuildingCurrentPos) +"&app=1");
                //}
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_Section = (Spinner) view.findViewById(R.id.Section_spinner_ID);
        adapterSpinerSection = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, Section_Number);
        adapterSpinerSection.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner_Section.setAdapter(adapterSpinerSection);
        spinner_Section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                //if (finish_load == true) {

                    //finish_load = false;
                    button_call.setText("Поиск квартир...");
                    button_call.setBackgroundResource(R.drawable.corner5);

                    SectionCurrentPos = pos;

                    SearchFilterFragment.Filter_GK_section_ID = Section_ID.get(SectionCurrentPos);

                    requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0="+String.valueOf(button_studia_Set)+"&room1="+String.valueOf(button_1_Set)+"&room2="+String.valueOf(button_2_Set)+"&room3="+String.valueOf(button_3_Set)+"&room4="+String.valueOf(button_4_Set)+"&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id="+Section_ID.get(SectionCurrentPos)+"&square_min="+String.valueOf(squareMin)+"&square_max="+String.valueOf(squareMax)+"&floor_min="+String.valueOf(floorMin)+"&floor_max="+String.valueOf(floorMax)+"&price_min="+String.valueOf(priceMin)+"&price_max="+String.valueOf(priceMax)+"&sales="+sales+"&finish="+finish+"&only_count=1&app=1");
                //}
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_SquareMin = (Spinner) view.findViewById(R.id.GK_spinner_square_min_ID);
        adapterSpinerSquareMin = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, Square);
        adapterSpinerSquareMin.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner_SquareMin.setAdapter(adapterSpinerSquareMin);
        spinner_SquareMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {

                try {
                    squareMin = Integer.parseInt(spinner_SquareMin.getSelectedItem().toString());
                    squareMax = Integer.parseInt(spinner_SquareMax.getSelectedItem().toString());

                    if (squareMin > squareMax){
                        adapterSpinerSquareMin.notifyDataSetChanged();
                        spinner_SquareMax.setSelection(spinner_SquareMin.getSelectedItemPosition());
                    }

                } catch(NumberFormatException nfe) {

                }

                //if (finish_load == true) {

                    button_call.setText("Поиск квартир...");
                    button_call.setBackgroundResource(R.drawable.corner5);

                    requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0=" + String.valueOf(button_studia_Set) + "&room1=" + String.valueOf(button_1_Set) + "&room2=" + String.valueOf(button_2_Set) + "&room3=" + String.valueOf(button_3_Set) + "&room4=" + String.valueOf(button_4_Set) + "&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id=" + Section_ID.get(SectionCurrentPos) + "&square_min=" + String.valueOf(squareMin) + "&square_max=" + String.valueOf(squareMax) + "&floor_min=" + String.valueOf(floorMin) + "&floor_max=" + String.valueOf(floorMax) + "&price_min=" + String.valueOf(priceMin) + "&price_max=" + String.valueOf(priceMax) + "&sales=" + sales + "&finish=" + finish + "&only_count=1&app=1");
                //}
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_SquareMax = (Spinner) view.findViewById(R.id.GK_spinner_square_max_ID);
        adapterSpinerSquareMax = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, Square);
        adapterSpinerSquareMax.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner_SquareMax.setAdapter(adapterSpinerSquareMax);
        spinner_SquareMax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {

                try {
                    squareMin = Integer.parseInt(spinner_SquareMin.getSelectedItem().toString());
                    squareMax = Integer.parseInt(spinner_SquareMax.getSelectedItem().toString());

                    if (squareMin > squareMax){
                        adapterSpinerSquareMin.notifyDataSetChanged();
                        spinner_SquareMin.setSelection(spinner_SquareMax.getSelectedItemPosition());
                    }

                } catch(NumberFormatException nfe) {

                }

                //if (finish_load == true) {
                    button_call.setText("Поиск квартир...");
                    button_call.setBackgroundResource(R.drawable.corner5);
                    requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0=" + String.valueOf(button_studia_Set) + "&room1=" + String.valueOf(button_1_Set) + "&room2=" + String.valueOf(button_2_Set) + "&room3=" + String.valueOf(button_3_Set) + "&room4=" + String.valueOf(button_4_Set) + "&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id=" + Section_ID.get(SectionCurrentPos) + "&square_min=" + String.valueOf(squareMin) + "&square_max=" + String.valueOf(squareMax) + "&floor_min=" + String.valueOf(floorMin) + "&floor_max=" + String.valueOf(floorMax) + "&price_min=" + String.valueOf(priceMin) + "&price_max=" + String.valueOf(priceMax) + "&sales=" + sales + "&finish=" + finish + "&only_count=1&app=1");
                //}
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_FloorMin = (Spinner) view.findViewById(R.id.GK_spinner_floor_min_ID);
        adapterSpinerFloorMin = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, Floor);
        adapterSpinerFloorMin.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner_FloorMin.setAdapter(adapterSpinerFloorMin);
        spinner_FloorMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {

                try {
                    floorMin = Integer.parseInt(spinner_FloorMin.getSelectedItem().toString());
                    floorMax = Integer.parseInt(spinner_FloorMax.getSelectedItem().toString());

                    if (floorMin > floorMax){
                        adapterSpinerFloorMin.notifyDataSetChanged();
                        spinner_FloorMax.setSelection(spinner_FloorMin.getSelectedItemPosition());
                    }

                } catch(NumberFormatException nfe) {

                }
                //if (finish_load == true) {

                    button_call.setText("Поиск квартир...");
                    button_call.setBackgroundResource(R.drawable.corner5);
                    requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0=" + String.valueOf(button_studia_Set) + "&room1=" + String.valueOf(button_1_Set) + "&room2=" + String.valueOf(button_2_Set) + "&room3=" + String.valueOf(button_3_Set) + "&room4=" + String.valueOf(button_4_Set) + "&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id=" + Section_ID.get(SectionCurrentPos) + "&square_min=" + String.valueOf(squareMin) + "&square_max=" + String.valueOf(squareMax) + "&floor_min=" + String.valueOf(floorMin) + "&floor_max=" + String.valueOf(floorMax) + "&price_min=" + String.valueOf(priceMin) + "&price_max=" + String.valueOf(priceMax) + "&sales=" + sales + "&finish=" + finish + "&only_count=1&app=1");
                //}
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_FloorMax = (Spinner) view.findViewById(R.id.GK_spinner_floor_max_ID);
        adapterSpinerFloorMax = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, Floor);
        adapterSpinerFloorMax.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner_FloorMax.setAdapter(adapterSpinerFloorMax);
        spinner_FloorMax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {

                try {
                    floorMin = Integer.parseInt(spinner_FloorMin.getSelectedItem().toString());
                    floorMax = Integer.parseInt(spinner_FloorMax.getSelectedItem().toString());

                    if (floorMin > floorMax){
                        adapterSpinerFloorMin.notifyDataSetChanged();
                        spinner_FloorMin.setSelection(spinner_FloorMax.getSelectedItemPosition());
                    }

                } catch(NumberFormatException nfe) {

                }
                //if (finish_load == true) {

                    button_call.setText("Поиск квартир...");
                    button_call.setBackgroundResource(R.drawable.corner5);

                    requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0=" + String.valueOf(button_studia_Set) + "&room1=" + String.valueOf(button_1_Set) + "&room2=" + String.valueOf(button_2_Set) + "&room3=" + String.valueOf(button_3_Set) + "&room4=" + String.valueOf(button_4_Set) + "&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id=" + Section_ID.get(SectionCurrentPos) + "&square_min=" + String.valueOf(squareMin) + "&square_max=" + String.valueOf(squareMax) + "&floor_min=" + String.valueOf(floorMin) + "&floor_max=" + String.valueOf(floorMax) + "&price_min=" + String.valueOf(priceMin) + "&price_max=" + String.valueOf(priceMax) + "&sales=" + sales + "&finish=" + finish + "&only_count=1&app=1");
                //}
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        LinearLayout relativeclic1 =(LinearLayout) view.findViewById(R.id.parent_focus_ID);
        relativeclic1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                hideKeyboard(v);

                if (setPrice) {

                    GK_editText_price_min.setCursorVisible(false);
                    GK_editText_price_max.setCursorVisible(false);

                    try {
                        priceMin = Long.parseLong(GK_editText_price_min.getText().toString());
                    } catch (NumberFormatException nfe) {
                        GK_editText_price_min.setText(String.valueOf("1"));
                        priceMin = 1;
                    }

                    try {
                        priceMax = Long.parseLong(GK_editText_price_max.getText().toString());
                    } catch (NumberFormatException nfe) {
                        GK_editText_price_max.setText(String.valueOf("100000000"));
                        priceMax = 100000000;
                    }

                    //if (finish_load == true) {

                        button_call.setText("Поиск квартир...");
                        button_call.setBackgroundResource(R.drawable.corner5);

                        requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0="+String.valueOf(button_studia_Set)+"&room1="+String.valueOf(button_1_Set)+"&room2="+String.valueOf(button_2_Set)+"&room3="+String.valueOf(button_3_Set)+"&room4="+String.valueOf(button_4_Set)+"&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id="+Section_ID.get(SectionCurrentPos)+"&square_min="+String.valueOf(squareMin)+"&square_max="+String.valueOf(squareMax)+"&floor_min="+String.valueOf(floorMin)+"&floor_max="+String.valueOf(floorMax)+"&price_min="+String.valueOf(priceMin)+"&price_max="+String.valueOf(priceMax)+"&sales="+sales+"&finish="+finish+"&only_count=1&app=1");
                    //}

                    setPrice = false;
                }
            }
        });


        GK_editText_price_min = (EditText) view.findViewById(R.id.GK_editText_price_min_ID);
        GK_editText_price_min.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                GK_editText_price_min.setCursorVisible(true);

                try {

                    long priceMin1 = Long.parseLong(GK_editText_price_min.getText().toString());
                    long priceMax1 = Long.parseLong(GK_editText_price_max.getText().toString());

                    if (priceMin > priceMax) {
                        GK_editText_price_max.setText(String.valueOf(priceMin));
                    }

                    if ((priceMin1 != priceMin)||(priceMax1 != priceMax)) {
                        setPrice = true;
                        priceMin = priceMin1;
                        priceMax = priceMax1;
                        if (finish_load == true) {
                            button_call.setText("Поиск квартир...");
                            button_call.setBackgroundResource(R.drawable.corner5);
                            requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0=" + String.valueOf(button_studia_Set) + "&room1=" + String.valueOf(button_1_Set) + "&room2=" + String.valueOf(button_2_Set) + "&room3=" + String.valueOf(button_3_Set) + "&room4=" + String.valueOf(button_4_Set) + "&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id=" + Section_ID.get(SectionCurrentPos) + "&square_min=" + String.valueOf(squareMin) + "&square_max=" + String.valueOf(squareMax) + "&floor_min=" + String.valueOf(floorMin) + "&floor_max=" + String.valueOf(floorMax) + "&price_min=" + String.valueOf(priceMin) + "&price_max=" + String.valueOf(priceMax) + "&sales=" + sales + "&finish=" + finish + "&only_count=1&app=1");
                        }
                    }

                } catch(NumberFormatException nfe) {

                }
            }
        });

        GK_editText_price_min.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    GK_editText_price_min.setCursorVisible(true);
                }
            }
        });

        GK_editText_price_max = (EditText) view.findViewById(R.id.GK_editText_price_max_ID);
        GK_editText_price_max.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                GK_editText_price_max.setCursorVisible(true);

                try {

                    long priceMin2 = Long.parseLong(GK_editText_price_min.getText().toString());
                    long priceMax2 = Long.parseLong(GK_editText_price_max.getText().toString());

                    if (priceMin2 > priceMax2) {

                        GK_editText_price_min.setText(String.valueOf(priceMax));
                    }

                    if ((priceMin2 != priceMin)||(priceMax2 != priceMax)) {
                        setPrice = true;
                        priceMin = priceMin2;
                        priceMax = priceMax2;

                        //if (finish_load == true) {
                            button_call.setText("Поиск квартир...");
                            button_call.setBackgroundResource(R.drawable.corner5);
                            requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0=" + String.valueOf(button_studia_Set) + "&room1=" + String.valueOf(button_1_Set) + "&room2=" + String.valueOf(button_2_Set) + "&room3=" + String.valueOf(button_3_Set) + "&room4=" + String.valueOf(button_4_Set) + "&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id=" + Section_ID.get(SectionCurrentPos) + "&square_min=" + String.valueOf(squareMin) + "&square_max=" + String.valueOf(squareMax) + "&floor_min=" + String.valueOf(floorMin) + "&floor_max=" + String.valueOf(floorMax) + "&price_min=" + String.valueOf(priceMin) + "&price_max=" + String.valueOf(priceMax) + "&sales=" + sales + "&finish=" + finish + "&only_count=1&app=1");
                        //}
                    }

                } catch (NumberFormatException nfe) {

                }

            }
        });

        GK_editText_price_max.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    GK_editText_price_max.setCursorVisible(true);
                }
            }
        });

        requestFilterTown("https://pro100.media/api/kortros/get-city/?token="+token+"&app=1");
        return view;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void requestFilterTown(String url) {

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

                        Town_title.clear();
                        Town_ID.clear();

                        int i = 0;
                        while (i < jsonArray_City.length()) {

                            JSONObject jsonObject_City = jsonArray_City.getJSONObject(i);

                            Town_title.add(jsonObject_City.getString("title").toString().trim());
                            Town_ID.add(jsonObject_City.getString("id").toString().trim());
                            i++;

                        }

                        adapterSpinerTown.notifyDataSetChanged();
                        spinner_Town.setSelection(0,false);

                        requestFilterComplex("https://pro100.media/api/kortros/get-complex/?token="+ token +"&city_id="+ Town_ID.get(0)+"&app=1");

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

    private void requestFilterComplex(String url) {

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

                        GK_complex.clear();
                        GK_complex_ID.clear();

                        for (int i = 0; i < jsonArray_Complex.length(); i++) {

                            JSONObject jsonObject_Complex = jsonArray_Complex.getJSONObject(i);

                            GK_complex.add(jsonObject_Complex.getString("title").toString().trim());
                            GK_complex_ID.add(jsonObject_Complex.getString("id").toString().trim());
                        }

                        adapterSpinerGK.notifyDataSetChanged();

                        boolean find_ID = false;
                        for (int i=0; i < GK_complex_ID.size(); i++) {
                            if (Filter_GK_complex_ID.equalsIgnoreCase(GK_complex_ID.get(i))) {
                                spinner_GK.setSelection(i,false);
                                GKCurrentPos = i;
                                find_ID = true;
                            }
                        }
                        if (!find_ID) {
                            spinner_GK.setSelection(0,false);
                            GKCurrentPos = 0;
                        }

                        requestFilterBilding("https://pro100.media/api/kortros/get-buildings/?token="+ token +"&complex_id="+ GK_complex_ID.get(GKCurrentPos) +"&app=1");

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

    private void requestFilterBilding(String url) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get(url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Response", "Messsage: " + response.toString());

                try {

                    JSONObject jsonObjects = null;
                    JSONObject jsonObjectsData = null;
                    JSONArray jsonArray_Bilding = null;


                    try {

                        jsonObjects = new JSONObject(response.toString());
                        jsonObjectsData = jsonObjects.getJSONObject("data");
                        jsonArray_Bilding = jsonObjectsData.getJSONArray("building");

                        Building_Number.clear();
                        Building_ID.clear();

                        for (int i = 0; i < jsonArray_Bilding.length(); i++) {

                            JSONObject jsonObject_Complex = jsonArray_Bilding.getJSONObject(i);

                            Building_Number.add(jsonObject_Complex.getString("title").toString().trim());
                            Building_ID.add(jsonObject_Complex.getString("id").toString().trim());

                        }

                        adapterSpinerBuilding.notifyDataSetChanged();

                        boolean find_ID = false;
                        for (int i=0; i < Building_ID.size(); i++) {
                            if (Filter_GK_building_ID.equalsIgnoreCase(Building_ID.get(i))) {
                                spinner_Building.setSelection(i,false);
                                BuildingCurrentPos = i;
                                find_ID = true;
                            }
                        }
                        if (!find_ID) {
                            spinner_Building.setSelection(0,false);
                            BuildingCurrentPos = 0;
                        }

                        requestFilterSection("https://pro100.media/api/kortros/get-sections/?token="+ token +"&building_id="+ Building_ID.get(BuildingCurrentPos) +"&app=1");

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

    private void requestFilterSection(String url) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get(url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Response", "Messsage: " + response.toString());

                try {

                    JSONObject jsonObjects = null;
                    JSONObject jsonObjectsData = null;
                    JSONArray jsonArray_Sections = null;

                    try {

                        jsonObjects = new JSONObject(response.toString());
                        jsonObjectsData = jsonObjects.getJSONObject("data");

                        try {

                            jsonArray_Sections = jsonObjectsData.getJSONArray("section");

                        } catch (JSONException e) {
                            Section_Number.add("Все");
                            Section_ID.add("0");

                        }

                        if (jsonArray_Sections != null) {

                            Section_Number.clear();
                            Section_ID.clear();

                            int i = 0;
                            while (i < jsonArray_Sections.length()) {

                                JSONObject jsonObject_Sections = jsonArray_Sections.getJSONObject(i);

                                Section_Number.add(jsonObject_Sections.getString("title").toString().trim());
                                Section_ID.add(jsonObject_Sections.getString("id").toString().trim());

                                i++;
                            }
                        }

                        adapterSpinerSection.notifyDataSetChanged();

                        boolean find_ID = false;
                        for (int i=0; i < Section_ID.size(); i++) {
                            if (Filter_GK_section_ID.equalsIgnoreCase(Section_ID.get(i))) {
                                spinner_Section.setSelection(i,false);
                                SectionCurrentPos = i;
                                find_ID = true;
                            }
                        }
                        if (!find_ID) {
                            spinner_Section.setSelection(0,false);
                            SectionCurrentPos = 0;
                        }

                        requestFilterGetFlats("https://pro100.media/api/kortros/get-flats-filter/?token=" + token + "&complex_id=" + GK_complex_ID.get(GKCurrentPos) + "&limit=100500&offset=0&room0=0&room1=0&room2=0&room3=0&room4=0&building_id=" + Building_ID.get(BuildingCurrentPos) + "&section_id="+Building_ID.get(SectionCurrentPos)+"&square_min=29&square_max=118&floor_min=1&floor_max=49&price_min="+String.valueOf(priceMin)+"&price_max="+String.valueOf(priceMax)+"&sales=0&finish=0&only_count=1&app=1");

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

    private void requestFilterGetFlats(String url) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get(url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Response", "Messsage: " + response.toString());

                try {

                    JSONObject jsonObjects = null;
                    JSONObject jsonObjectsData = null;
                    JSONObject jsonFilterData = null;
                    JSONArray jsonRoomData = null;

                    try {
                        jsonObjects = new JSONObject(response.toString());
                        jsonObjectsData = jsonObjects.getJSONObject("data");

/*
                        "data" : {
                                  "ending" : "",
                                  "floor_max" : "49",
                                  "floor_min" : "1",
                                  "filter_data" : {
                                                   "price_max" : 38165850,
                                                   "floor_min" : 1,
                                                   "room_info" : [
                                                                  "0",
                                                                  "139",
                                                                  "183",
                                                                  "159",
                                                                  "5"
                                                                 ],
                                                   "floor_max" : 49,
                                                   "price_min" : 6448815,
                                                   "square_max" : 118,
                                                   "square_min" : 29
                                                  },
                                  "token" : "ust-358645-c72f3f44ef332beb809d04176aefc1f3",
                                  "price_min" : "1",
                                  "count" : 486,
                                  "complex_title" : "ЖК Headliner",
                                  "price_max" : "100000000",
                                  "number" : "74951043431",
                                  "square_max" : "118",
                                  "square_min" : "29"
                                 },
*/

                        jsonFilterData = jsonObjectsData.getJSONObject("filter_data");
                        jsonRoomData = jsonFilterData.getJSONArray("room_info");

                        if (jsonRoomData.get(0).equals("0")) {
                            button_studia.setBackgroundResource(R.drawable.corner7);
                            button_studia.setEnabled(false);
                        } else {
                            if (button_studia_Set == 0) {
                                button_studia.setBackgroundResource(R.drawable.corner6);
                            } else {
                                button_studia.setBackgroundResource(R.drawable.corner8);
                            }
                            button_studia.setEnabled(true);
                        }
                        if (jsonRoomData.get(1).equals("0")) {
                            button_1.setBackgroundResource(R.drawable.corner7);
                            button_1.setEnabled(false);
                        } else {
                            if (button_1_Set == 0) {
                                button_1.setBackgroundResource(R.drawable.corner6);
                            } else {
                                button_1.setBackgroundResource(R.drawable.corner8);
                            }
                            button_1.setEnabled(true);
                        }
                        if (jsonRoomData.get(2).equals("0")) {
                            button_2.setBackgroundResource(R.drawable.corner7);
                            button_2.setEnabled(false);
                        } else {
                            if (button_2_Set == 0) {
                                button_2.setBackgroundResource(R.drawable.corner6);
                            } else {
                                button_2.setBackgroundResource(R.drawable.corner8);
                            }
                            button_2.setEnabled(true);
                        }
                        if (jsonRoomData.get(3).equals("0")) {
                            button_3.setBackgroundResource(R.drawable.corner7);
                            button_3.setEnabled(false);
                        } else {
                            if (button_3_Set == 0) {
                                button_3.setBackgroundResource(R.drawable.corner6);
                            } else {
                                button_3.setBackgroundResource(R.drawable.corner8);
                            }
                            button_3.setEnabled(true);
                        }
                        if (jsonRoomData.get(4).equals("0")) {
                            button_4.setBackgroundResource(R.drawable.corner7);
                            button_4.setEnabled(false);
                        } else {
                            if (button_4_Set == 0) {
                                button_4.setBackgroundResource(R.drawable.corner6);
                            } else {
                                button_4.setBackgroundResource(R.drawable.corner8);
                            }
                            button_4.setEnabled(true);
                        }


                        if (finish_load == false) {

                            Square.clear();
                            squareMin = 0;
                            try {
                                squareMin = Integer.parseInt(jsonObjectsData.getString("square_min").toString());
                            } catch (NumberFormatException nfe) {

                            }

                            squareMax = 0;
                            try {
                                squareMax = Integer.parseInt(jsonObjectsData.getString("square_max").toString());
                            } catch (NumberFormatException nfe) {

                            }

                            for (int i = squareMin; i <= squareMax; i++) {

                                Square.add(String.valueOf(i));

                            }

                            adapterSpinerSquareMin.notifyDataSetChanged();
                            spinner_SquareMin.setSelection(0);

                            adapterSpinerSquareMax.notifyDataSetChanged();
                            spinner_SquareMax.setSelection(Square.size());

                            Floor.clear();
                            floorMin = 1;
                            floorMax = 1;

                            try {

                                floorMax = Integer.parseInt(jsonFilterData.getString("floor_max").toString());

                            } catch (NumberFormatException nfe) {

                            }

                            for (int i = floorMin; i <= floorMax; i++) {

                                Floor.add(String.valueOf(i));

                            }

                            adapterSpinerFloorMin.notifyDataSetChanged();
                            spinner_FloorMin.setSelection(0);

                            adapterSpinerFloorMax.notifyDataSetChanged();
                            spinner_FloorMax.setSelection(Floor.size());

                            priceMin = 1;

                            try {

                                priceMin = Integer.parseInt(jsonObjectsData.getString("price_min").toString());
                                GK_editText_price_min.setText(String.valueOf(priceMin));

                            } catch (NumberFormatException nfe) {

                            }

                            priceMax = 10000000;

                            try {

                                priceMax = Integer.parseInt(jsonObjectsData.getString("price_max").toString());
                                GK_editText_price_max.setText(String.valueOf(priceMax));

                            } catch (NumberFormatException nfe) {

                            }

                            finish_load = true;

                        } else {

                            button_call.setBackgroundResource(R.drawable.corner4);
                            button_call.setText("Показать " + String.valueOf(jsonObjectsData.getInt("count")) + " квартир");
                            count_flats = jsonObjectsData.getInt("count");

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
    public static void delay_sec(int secs){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish_load = true;
            }
        }, secs * 1000); // afterDelay will be executed after (secs*1000) milliseconds.
    }
  */
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
        public void onTownClickEvent();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
       // void openHome(View view);
    }

}
