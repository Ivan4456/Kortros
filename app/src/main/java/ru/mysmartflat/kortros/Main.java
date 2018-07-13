package ru.mysmartflat.kortros;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.lang.reflect.Field;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yandex.mapkit.MapKitFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.internal.framed.Header;

import static com.yandex.runtime.Runtime.getApplicationContext;

public class Main extends Activity implements FlatFilterFragment.Listener, FlatsFilterFragment.Listener, SearchFragment.Listener, SearchTownFragment.Listener, NewsFragment.Listener, FlatsFragment.Listener, FlatFragment.Listener, LoveFragment.Listener, ChatFragment.Listener  {

    BottomNavigationView navigation;

    // private SearchFragment.OnFragmentInteractionListener mListener;
    // private SearchFilterFragment.OnFragmentInteractionListener mListener;
    // private SearchGkFragment.OnFragmentInteractionListener mListener;
    // private SearchTownFragment.OnFragmentInteractionListener mListener;

    public String current_fragment = "SearchFragment";

    private boolean exit = false;

    public static Realm mRealm;

    @Override
    public void onGkClickEvent(Integer Pos, String complex_id, String name) {
        exit = false;
        replaceFragment(SearchGkFragment.newInstance(Pos, complex_id, name));
    }

    @Override
    public void onFilterClickEvent() {
        exit = false;
        replaceFragment(SearchFilterFragment.newInstance());
    }

    @Override
    public void onTownClickEvent(Integer Pos) {
        exit = false;
        replaceFragment(SearchTownFragment.newInstance(Pos));
    }

    @Override
    public void onNewsClickEvent(String NewID) {
        exit = false;
        replaceFragment(NewFragment.newInstance(NewID));
    }

    @Override
    public void onFavoriteClickEvent(String FlatUID) {
        exit = false;
        replaceFragment(LoveFlatFragment.newInstance(FlatUID));
    }

    @Override
    public void onFlatsClickEvent(String Complex_ID, Integer typeApartment) {
        exit = false;
        replaceFragment(FlatsFragment.newInstance(Complex_ID, typeApartment));
    }

    @Override
    public void onFlatFilterClickEvent(String Select_UID, String Town_ID, String GK_complex_ID, Integer button_studia, Integer button_1, Integer button_2, Integer button_3, Integer button_4, String Bilding_ID, String Section_ID, Integer squareMin, Integer squareMax, Integer floorMin, Integer floorMax, Long priceMin, Long priceMax, String sales, String finish) {
        exit = false;
        replaceFragment(FlatFilterFragment.newInstance(Select_UID, Town_ID, GK_complex_ID, button_studia, button_1, button_2, button_3, button_4, Bilding_ID, Section_ID, squareMin, squareMax, floorMin, floorMax, priceMin, priceMax, sales, finish));
    }

    @Override
    public void onFlatsFilterClickEvent(String Town_ID, String GK_complex_ID, Integer button_studia, Integer button_1, Integer button_2, Integer button_3, Integer button_4, String Bilding_ID, String Section_ID, Integer squareMin, Integer squareMax, Integer floorMin, Integer floorMax, Long priceMin, Long priceMax, String sales, String finish) {
        exit = false;
        replaceFragment(FlatsFilterFragment.newInstance(Town_ID, GK_complex_ID, button_studia, button_1, button_2, button_3, button_4, Bilding_ID, Section_ID, squareMin, squareMax, floorMin, floorMax, priceMin, priceMax, sales, finish));
    }

    @Override
    public void onFlatClickEvent(String Complex_ID, Integer typeApartment, String Select_UID) {
        exit = false;
        replaceFragment(FlatFragment.newInstance(Complex_ID, typeApartment, Select_UID));
    }

    @Override
    public void onChatClick(String Select_Chat_Name, String Select_Chat_ID, Integer Select_Chat_ReadOnly) {
        exit = false;
        replaceFragment(ChatFragment.newInstance(Select_Chat_Name, Select_Chat_ID, Select_Chat_ReadOnly));
    }

    @Override
    public void onBackPressed() {

 //       Fragment currentFragment = getFragmentManager().findFragmentById(R.id.Search_Gk_Fragment_ID); //);Search_Gk_Fragment_ID);

//        if (currentFragment.getTag().equals("fragment_tag_gk")) {

        if (!exit) {
            exit = true;
            replaceFragment(SearchFragment.newInstance());
        } else {
            finish();
        }

/*
        } else {
            super.onBackPressed();
        }
*/
        //super.onBackPressed();
    }

    static public class BottomNavigationViewHelper {
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);

                    item.setShiftingMode(false);

                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fg;
            switch (item.getItemId()) {

                case R.id.navigation_search_id:
                    fg = SearchFragment.newInstance();
                    replaceFragment(fg);
                    //mTextMessage.setText(R.string.title_search);
                    break;

                case R.id.navigation_love_id:
                    fg = LoveFragment.newInstance();
                    replaceFragment(fg);
                    //mTextMessage.setText(R.string.title_love);
                    break;

                case R.id.navigation_developer_id:
                    fg = BilderFragment.newInstance();
                    replaceFragment(fg);
                    //mTextMessage.setText(R.string.title_developer);
                    break;

                case R.id.navigation_news_id:
                    fg = NewsFragment.newInstance();
                    replaceFragment(fg);
                    //mTextMessage.setText(R.string.title_news);
                    break;

                case R.id.navigation_chats_id:
                    fg = ChatsFragment.newInstance();
                    replaceFragment(fg);
                    //mTextMessage.setText(R.string.title_help);
                    break;
            }

            // FragmentManager fragmentManager = getFragmentManager()

            return true;
        }
    };


    private void replaceFragment(Fragment newFragment) {

        FragmentTransaction trasaction = getFragmentManager().beginTransaction();

        if(!newFragment.isAdded()){
            try{

                getFragmentManager().beginTransaction();
                trasaction.replace(R.id.main_container, newFragment);
                trasaction.addToBackStack(null);
                trasaction.commit();

            }catch (Exception e) {
                // TODO: handle exception
                // AppConstants.printLog(e.getMessage());
            }
        } else {

            trasaction.show(newFragment);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MapKitFactory.setApiKey("6748b84e-0e7c-49eb-afb3-6e2ee2bf2a52");
        MapKitFactory.initialize(this);

        Realm.init(getApplicationContext());
        RealmConfiguration encryptedConfig = new RealmConfiguration.Builder()
                .name("name")
                .schemaVersion(6)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.removeDefaultConfiguration();
        Realm.setDefaultConfiguration(encryptedConfig);

        mRealm = Realm.getDefaultInstance();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /*
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        */

        replaceFragment(SearchFragment.newInstance());

        //replaceFragment(SearchFilterFragment.newInstance());
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

}
