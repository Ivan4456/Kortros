package ru.mysmartflat.kortros;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ChatsFragment extends Fragment  {

    private OnFragmentInteractionListener mListener;

    private ListView mList;
    private ArrayList<ModelChats> arrayChats = new ArrayList<>();
    private ArrayList<ModelChats> arrayChats2 = new ArrayList<>();
    private AdapterChats mAdapter;

    private SharedPreferences sPref;
    String token;

  //  public static Chats instance = null;

    public static String Select_Chat_ID = "0";
    public static String Select_Chat_Name = "";
    public static Integer Select_Chat_ReadOnly = 1;

    EditText editText_search_chats;
    String User_ID = "";
    String perv_Search_Contact_Chats = "";
    String select_Delete_Chat_ID = "";
    Integer select_Delete_POS = 0;
    ProgressBar progressBar_chats;

    boolean contact_searching_Flag_ready = true;

    RelativeLayout layout_chats_Close;
    AlertDialog.Builder adb;

    View view;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public ChatsFragment() {
        // Required empty public constructor
    }

    public static ChatsFragment newInstance() {
        ChatsFragment fragment = new ChatsFragment();
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

        view = inflater.inflate(R.layout.fragment_chats, container, false);

        //super.onCreate(saveInstanceState);
        //setContentView(R.layout.activity_chats);
        //instance = this;
        //getSupportActionBar().hide();

        mList = (ListView) view.findViewById(R.id.list_chats_ID);
        mAdapter = new AdapterChats(getActivity(), arrayChats);
        mList.setAdapter(mAdapter);

        EditText editText_search_chats = (EditText) view.findViewById(R.id.editText_search_chats_ID);

        progressBar_chats = (ProgressBar) view.findViewById(R.id.progressBar_chats_ID);

        sPref = getActivity().getSharedPreferences("City_Settings", Context.MODE_PRIVATE);
        token = sPref.getString("ghost_token", "");

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {

                hideSoftKeyboard(getActivity());

                if (arrayChats.get(pos).last_message_dt == "") {

                    Select_Chat_Name = arrayChats.get(pos).getStrTitle();
                    createChat("https://pro100.media/api/message/new-chat/?token="+token+"&title="+arrayChats.get(pos).getStrTitle()+"&users="+User_ID+","+arrayChats.get(pos).getChatID()+"&app=1");

                } else {

                    ((ChatFragment.Listener) getActivity()).onChatClick(arrayChats.get(pos).str_title, arrayChats.get(pos).chat_ID , arrayChats.get(pos).int_readOnly);

                }
            }
        });



        editText_search_chats.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Log.d("onTextChanged", "CharSequence:" + s + ":" + String.valueOf(start) + ":" + String.valueOf(start) + ":" + String.valueOf(count));

                if ((contact_searching_Flag_ready == true)&&(perv_Search_Contact_Chats != s.toString())) {

                    if (count > 3) {
                        contact_searching_Flag_ready = false;
                        perv_Search_Contact_Chats = s.toString();
                        search_contacts("https://pro100.media/api/message/search-contact/?token=" + token + "&search=" + s.toString()+"&app=1");
                    }

                } else {

                    if (count == 0) {
                        contact_searching_Flag_ready = false;
                        perv_Search_Contact_Chats = "";
                        requestChatsList("https://pro100.media/api/message/get-chat-channels/?token=" + token + "&app=1");
                        //progressBar_chats.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.d("beforeTextChanged", "CharSequence:" + s + ":" + String.valueOf(start) + ":" + String.valueOf(start) + ":" + String.valueOf(count));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        requestChatsList("https://pro100.media/api/message/get-chat-channels/?token=" + token+"&app=1");
        progressBar_chats.setVisibility(View.VISIBLE);

        return view;
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                .getWindowToken(), 0);
    }

    private boolean keyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    private void search_contacts(String url) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Response", "Messsage: " + response.toString());

                /*
                struct CommandSearchUser: Codable {
                    struct Contact: Codable {
                        let id: String
                        let name: String
                        let avatar: String
                    }
                    struct Data: Codable {
                        let all_count: String
                        let show_count: Int
                        let contact:[Contact]?
                    }
                    let command: String
                    let error: Int
                    let message: String
                    let data: Data
                    let token: String
                    let fromdomain: String
                }
                */

                try {
                    JSONObject jsonObjects = null;
                    JSONObject jsonObjectsData = null;
                    JSONArray jsonArray_Chats = null;

                    try {

                        jsonObjects = new JSONObject(response.toString());
                        jsonObjectsData = jsonObjects.getJSONObject("data");
                        jsonArray_Chats = jsonObjectsData.getJSONArray("contact");

                        //arrayChats2.clear();

                        arrayChats.clear();

                        for (int i = 0; i < jsonArray_Chats.length(); i++) {

                            JSONObject jsonObject = jsonArray_Chats.getJSONObject(i);

                            String nameChat = jsonObject.getString("name");
                            boolean findChat = false;

                            for (int j = 0; j < arrayChats2.size(); j++) {
                                String s = arrayChats2.get(j).getStrTitle();
                                if (s.equals(nameChat)) {
                                    findChat = true;
                                }
                            }

                            if (findChat == false) {

                                ModelChats objChat = new ModelChats();
                                objChat.setChatID(jsonObject.getString("id"));
                                objChat.setStrTitle(jsonObject.getString("name"));
                                objChat.setStrMessage("");
                                objChat.last_message_dt = "";
                                objChat.setStrBadge("0");

                                arrayChats.add(objChat);
                            }
                        }


                        //arrayChats.clear();
                        //if (arrayChats2.size() > 0) {
                        //    for (int i = 0; i < arrayChats2.size(); i++) {
                        //        arrayChats.add(arrayChats2.get(i));
                        //    }
                        //}


                        mAdapter.notifyDataSetChanged();
                        progressBar_chats.setVisibility(View.INVISIBLE);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }

                contact_searching_Flag_ready = true;
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                Log.e("ERROR", e.toString());
                contact_searching_Flag_ready = true;
            }
        });
    }

    private void requestChatsList(String url) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Response", "Messsage: " + response.toString());

                /*
                https://pro100.media/api/message/get-chat-channels/?token=ust-80-d0436c93724fb19971ce7d335aadf00d

                struct CommandGetChatChannels: Codable {
                    struct Chats: Codable {
                        let id: String
                        let remove_caption: String
                        let title: String
                        let count:String
                        let not_read: String
                        let last_message: String
                        let last_dt: String
                        let last_autor: String
                        let last_autor_img: String
                        let readonly: Int
                    }
                    struct Data: Codable {
                        let user_id: String
                        let chats: [Chats]
                        let not_read: Int
                    }

                    let command: String
                    let error: Int
                    let message: String
                    let data: Data
                    let token: String
                    let fromdomain: String
                }
                */

                try {
                    JSONObject jsonObjects = null;
                    JSONObject jsonObjectsData = null;
                    JSONArray jsonArray_Chats = null;

                    try {

                        jsonObjects = new JSONObject(response.toString());
                        jsonObjectsData = jsonObjects.getJSONObject("data");
                        jsonArray_Chats = jsonObjectsData.getJSONArray("chats");

                        User_ID = jsonObjectsData.getString("user_id");

                        arrayChats.clear();
                        arrayChats2.clear();

                        for (int i = 0; i < jsonArray_Chats.length(); i++) {

                            JSONObject jsonObject = jsonArray_Chats.getJSONObject(i);

                            ModelChats objChat = new ModelChats();
                            objChat.setChatID(jsonObject.getString("id"));
                            objChat.setStrTitle(jsonObject.getString("title"));
                            objChat.setStrBadge(jsonObject.getString("not_read"));
                            objChat.setStrMessage(jsonObject.getString("last_message"));
                            objChat.setIntReadOnly(jsonObject.getInt("readonly"));

                            objChat.last_message_dt = jsonObject.getString("last_dt");

                            arrayChats.add(objChat);
                            arrayChats2.add(objChat);

                        }

                        mAdapter.notifyDataSetChanged();
                        progressBar_chats.setVisibility(View.INVISIBLE);
                        contact_searching_Flag_ready = true;

                        //hideSoftKeyboard(Chats.this);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressBar_chats.setVisibility(View.INVISIBLE);
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

    private void deleteChat(String url) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                progressBar_chats.setVisibility(View.INVISIBLE);

                Log.d("Response", "Messsage: " + response.toString());

                try {

                    JSONObject jsonObjects = null;

                    try {

                        jsonObjects = new JSONObject(response.toString());
                        String error = jsonObjects.getString("error");

                        if(error == "0") {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }

                arrayChats.remove(select_Delete_POS.intValue());
                mAdapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e,
                                  JSONObject response) {
                Log.e("ERROR", e.toString());
            }
        });
    }

    private void createChat(String url) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Response", "Messsage: " + response.toString());

                /*
                https://pro100.media/api/message/get-chat-channels/?token=ust-80-d0436c93724fb19971ce7d335aadf00d

                struct CommandCreateChat: Codable {
                   struct Data: Codable {
                       let id: String
                   }
                   let command: String
                   let error: Int
                   let message: String
                   let data: Data
                   let token: String
                   let fromdomain: String
                }
                */
                try {

                    JSONObject jsonObjects = null;
                    JSONObject jsonObjectsData = null;

                    jsonObjects = new JSONObject(response.toString());
                    String error = jsonObjects.getString("error");
                    jsonObjectsData = jsonObjects.getJSONObject("data");

                    //if (error == "0") {


                    ((ChatFragment.Listener) getActivity()).onChatClick(Select_Chat_Name, jsonObjectsData.getString("id") , 0);

                    //}
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
    @Override
    protected void onResume() {
        super.onResume();

        requestChatsList("https://pro100.media/api/message/get-chat-channels/?token=" + token);

    }


    @Override
    public void finish() {
        super.finish();
        instance = null;
    }
*/
}
