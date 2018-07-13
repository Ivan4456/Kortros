package ru.mysmartflat.kortros;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.VideoView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import io.realm.RealmResults;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

public class NewsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ListView mList;
    private ArrayList<Realm_News> arrayNews = new ArrayList<>();
    private AdapterNews mAdapter;

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
        public void onNewsClickEvent(String NewID);
    }

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        mList = (ListView) view.findViewById(R.id.ListView_news_ID);
        mAdapter = new AdapterNews(view.getContext(), arrayNews);
        mList.setAdapter(mAdapter);

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

                Realm_News objNews = (Realm_News) arrayNews.get(position);

                String new_ID = objNews.getNewsID();
                ((NewsFragment.Listener) getActivity()).onNewsClickEvent(new_ID);
            }
        });

        refresh_Data_News();

        requestNewsList("https://pro100.media/api/kortros/get-news");

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void requestNewsList(String url) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get(url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Response", "Messsage: " + response.toString());

                try {

                    JSONObject jsonObjects = null;
                    JSONObject jsonObjectsData = null;
                    JSONArray jsonArray_Chats = null;

                    try {

                        jsonObjects = new JSONObject(response.toString());
                        jsonObjectsData = jsonObjects.getJSONObject("data");
                        jsonArray_Chats = jsonObjectsData.getJSONArray("news");

                        //User_ghost_token = jsonObjectsData.getString("ghost_token");
                        //User_token = jsonObjectsData.getString("token");

                        Main.mRealm.beginTransaction();

                        for (int i = 0; i < jsonArray_Chats.length(); i++) {

                            JSONObject jsonObject = jsonArray_Chats.getJSONObject(i);

                            Realm_News news = Main.mRealm.where(Realm_News.class).equalTo("news_ID", jsonObject.getString("id")).findFirst();

                            if (news == null) {
                                news = Main.mRealm.createObject(Realm_News.class);
                            }

                            news.setNewsID(jsonObject.getString("id"));
                            news.setStrTitle(jsonObject.getString("title"));
                            news.setStrText(jsonObject.getString("text"));
                            news.setDTMessage(jsonObject.getString("dt"));
                            news.setImg(jsonObject.getString("img"));

                        }

                        Main.mRealm.commitTransaction();

                        refresh_Data_News();

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

    private void refresh_Data_News() {

        RealmResults<Realm_News> array_news = Main.mRealm.where(Realm_News.class).findAll();

        if (array_news != null) {

            arrayNews.clear();

            for(int i=0; i < array_news.size(); i++) {

                arrayNews.add(array_news.get(i));

            }

            mAdapter.notifyDataSetChanged();
        }
    }

/*
https://pro100.media/api/kortros/get-news
{
 "command":"kortros->get-news",
 "error":0,
 "message":"",
 "data":{
         "ghost_token":"ust-375353-33ca52050e4765495b9f90a25efb6ab4",
         "token":"ust-375353-33ca52050e4765495b9f90a25efb6ab4",
         "news":[{
                  "id":"2",
                  "title":"\u0420\u044f\u0434\u043e\u043c \u0441 \u0436\u0438\u043b\u044b\u043c \u043a\u0432\u0430\u0440\u0442\u0430\u043b\u043e\u043c HEADLINER \u043e\u0442\u043a\u0440\u044b\u043b\u0430\u0441\u044c \u0441\u0442\u0430\u043d\u0446\u0438\u044f \u043c\u0435\u0442\u0440\u043e",
                  "text":"26 \u0444\u0435\u0432\u0440\u0430\u043b\u044f \u0440\u044f\u0434\u043e\u043c \u0441 \u043a\u0432\u0430\u0440\u0442\u0430\u043b\u043e\u043c \u0436\u0438\u043b\u044b\u0445 \u043d\u0435\u0431\u043e\u0441\u043a\u0440\u0435\u0431\u043e\u0432 Headliner \u043e\u0442\u043a\u0440\u044b\u043b\u0430\u0441\u044c \u043d\u043e\u0432\u0430\u044f \u0441\u0442\u0430\u043d\u0446\u0438\u044f \u043c\u043e\u0441\u043a\u043e\u0432\u0441\u043a\u043e\u0433\u043e \u043c\u0435\u0442\u0440\u043e \u00ab\u0428\u0435\u043b\u0435\u043f\u0438\u0445\u0430\u00bb. \u0422\u0435\u043f\u0435\u0440\u044c \u043f\u043e\u043a\u0443\u043f\u0430\u0442\u0435\u043b\u0438 \u043a\u0432\u0430\u0440\u0442\u0438\u0440 \u043c\u043e\u0433\u0443\u0442 \u043e\u0446\u0435\u043d\u0438\u0442\u044c \u0432\u0441\u0435 \u043f\u0440\u0435\u0438\u043c\u0443\u0449\u0435\u0441\u0442\u0432\u0430 \u0440\u0430\u0441\u043f\u043e\u043b\u043e\u0436\u0435\u043d\u0438\u044f \u043d\u043e\u0432\u043e\u0433\u043e \u043a\u043e\u043c\u043f\u043b\u0435\u043a\u0441\u0430 \u0438 \u0441\u0442\u0430\u0442\u044c \u043e\u0434\u043d\u0438\u043c\u0438 \u0438\u0437 \u043f\u0435\u0440\u0432\u044b\u0445 \u043f\u0430\u0441\u0441\u0430\u0436\u0438\u0440\u043e\u0432 \u0411\u043e\u043b\u044c\u0448\u043e\u0439 \u043a\u043e\u043b\u044c\u0446\u0435\u0432\u043e\u0439 \u043b\u0438\u043d\u0438\u0438 \u0441\u0442\u043e\u043b\u0438\u0447\u043d\u043e\u0433\u043e \u043c\u0435\u0442\u0440\u043e\u043f\u043e\u043b\u0438\u0442\u0435\u043d\u0430.",
                  "dt":"26.07.2018",
                  "img":"https://pro100.media/api/ihome/images/kortros/img/5e1545abac73f272d085735f5fd281dc_375.jpg"
                 },{
                  "id":"1",
                  "title":"\u0412 \u043f\u0435\u0440\u0432\u043e\u043c \u043a\u043e\u043c\u043f\u043b\u0435\u043a\u0441\u0435 \u0436\u0438\u043b\u044b\u0445 \u043d\u0435\u0431\u043e\u0441\u043a\u0440\u0435\u0431\u043e\u0432 Headliner \u043e\u0442\u043a\u0440\u044b\u043b\u0441\u044f \u043e\u0444\u0438\u0441 \u043f\u0440\u043e\u0434\u0430\u0436",
                  "text":"\u0412 \u043f\u0435\u0440\u0432\u043e\u043c \u043a\u043e\u043c\u043f\u043b\u0435\u043a\u0441\u0435 \u0436\u0438\u043b\u044b\u0445 \u043d\u0435\u0431\u043e\u0441\u043a\u0440\u0435\u0431\u043e\u0432 Headliner \u043e\u0442\u043a\u0440\u044b\u043b\u0441\u044f \u043e\u0444\u0438\u0441 \u043f\u0440\u043e\u0434\u0430\u0436, \u043a\u043e\u0442\u043e\u0440\u044b\u0439 \u0440\u0430\u0431\u043e\u0442\u0430\u0435\u0442 \u0432 \u0435\u0436\u0435\u0434\u043d\u0435\u0432\u043d\u043e\u043c \u0440\u0435\u0436\u0438\u043c\u0435 \u0441 9-00 \u0434\u043e 21-00. \u041e\u0431\u0440\u0430\u0442\u0438\u0442\u044c\u0441\u044f \u0437\u0430 \u043a\u043e\u043d\u0441\u0443\u043b\u044c\u0442\u0430\u0446\u0438\u0435\u0439 \u043a \u0441\u043f\u0435\u0446\u0438\u0430\u043b\u0438\u0441\u0442\u0430\u043c \u043f\u043e \u043f\u043e\u0434\u0431\u043e\u0440\u0443 \u043d\u0435\u0434\u0432\u0438\u0436\u0438\u043c\u043e\u0441\u0442\u0438 \u043c\u043e\u0436\u043d\u043e \u043f\u043e \u0430\u0434\u0440\u0435\u0441\u0443: \u041c\u043e\u0441\u043a\u0432\u0430, \u0428\u043c\u0438\u0442\u043e\u0432\u0441\u043a\u0438\u0439 \u043f\u0440\u043e\u0435\u0437\u0434 39.",
                  "dt":"31.01.2018",
                  "img":"https://pro100.media/api/ihome/images/kortros/img/dce8916aa0affd08653e6c39dc8b30d6_375.jpg"
                 }
                ]},
         "token":"",
         "ghost_token":"ust-375353-33ca52050e4765495b9f90a25efb6ab4",
         "fromdomain":""
        }
}
*/
}



