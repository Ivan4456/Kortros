package ru.mysmartflat.kortros;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Collections;

import cz.msebera.android.httpclient.Header;


public class ChatFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ListView mList;
    private ArrayList<Message> arrayChat = new ArrayList<>();
    private AdapterChat mAdapter;
    LinearLayout layout_chat_title;
    LinearLayout layout_sendMessage_chat;
    LinearLayout linerLayout_messages;
    RelativeLayout layout_chat_Close;
    EditText EditText_Send;
    ImageView imageView_sendMessage_chat;
    ProgressBar progressBar_chat;

    private SharedPreferences sPref;

    private String token;

    private String Select_Chat_Name;

    private String Select_Chat_ID;

    private Integer Select_Chat_ReadOnly;

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

    interface Listener {
        public void onChatClick(String Select_Chat_Name, String Select_Chat_ID, Integer Select_Chat_ReadOnly);
    }

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String Select_Chat_Name, String Select_Chat_ID, Integer Select_Chat_ReadOnly) {
        ChatFragment fragment = new ChatFragment();

        Bundle bundle = new Bundle();
        bundle.putString("Select_Chat_Name", Select_Chat_Name);
        bundle.putString("Select_Chat_ID", Select_Chat_ID);
        bundle.putInt("Select_Chat_ReadOnly", Select_Chat_ReadOnly);

        fragment.setArguments(bundle);
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        // void openHome(View view);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chat, container, false);

        //Context ctx = this.getApplicationContext();

        sPref = getActivity().getSharedPreferences("City_Settings", Context.MODE_PRIVATE);
        token = sPref.getString("ghost_token", "");

        Select_Chat_Name = getArguments().getString("Select_Chat_Name");

        Select_Chat_ID = getArguments().getString("Select_Chat_ID");

        Select_Chat_ReadOnly = getArguments().getInt("Select_Chat_ReadOnly",0);

        progressBar_chat = (ProgressBar) view.findViewById(R.id.progressBar_chat_ID);
        linerLayout_messages = (LinearLayout) view.findViewById(R.id.linerLayout_messages_ID);
        layout_sendMessage_chat = (LinearLayout) view.findViewById(R.id.layout_sendMessage_chat_ID);

        mList = (ListView) view.findViewById(R.id.list_chat_ID);
        mAdapter = new AdapterChat(getActivity(), arrayChat);
        mList.setAdapter(mAdapter);

        TextView text_chat_title = (TextView) view.findViewById(R.id.text_chat_title_ID);
        text_chat_title.setText(Select_Chat_Name);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        requestChatList("https://pro100.media/api/message/get-chat-messages/?token="+token+"&id="+Select_Chat_ID);

        layout_chat_title = (LinearLayout) view.findViewById(R.id.layout_chat_title_id);
        layout_chat_Close = (RelativeLayout) view.findViewById(R.id.layout_chat_Close_ID);
        layout_chat_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(ChatsFragment.newInstance());

            }
        });

        EditText_Send = (EditText) view.findViewById(R.id.editText_send_ID);
        EditText_Send.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyboardShown(EditText_Send.getRootView())) {
                    Log.d("keyboard", "keyboard UP");
                    mList.setSelection(mAdapter.getCount()-1);
                } else {
                    Log.d("keyboard", "keyboard Down");
                }
            }
        });

        if (Select_Chat_ReadOnly == 1) {
            layout_sendMessage_chat.setVisibility(View.GONE);
        }

        imageView_sendMessage_chat = (ImageView) view.findViewById(R.id.imageView_sendMessage_chat_ID);
        imageView_sendMessage_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EditText_Send.getText().length() > 0) {
                    sendMessage("https://pro100.media/api/message/send-message/?token="+token+"&id="+Select_Chat_ID+"&text="+EditText_Send.getText());
                    //progressBar_chat.setVisibility(View.VISIBLE);
                    EditText_Send.setText("");
                }
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                hideSoftKeyboard(getActivity());
            }
        });

        //progressBar_chat.setVisibility(View.VISIBLE);
        progressBar_chat.setVisibility(View.INVISIBLE);

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

    private void requestChatList(String url) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Response", "Messsage: " + response.toString());
                /*
                struct CommandGetChat: Codable {
                    struct Messages: Codable {
                        let id: String
                        let autor: String
                        let my: Int
                        let img:String
                        let text: String
                        let dt: String
                        let read: Int
                        let link: String
                    }
                    struct Data: Codable {
                        let messages: [Messages]
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
                    JSONArray jsonArray_Messages = null;

                    try {

                        jsonObjects = new JSONObject(response.toString());
                        jsonObjectsData = jsonObjects.getJSONObject("data");
                        jsonArray_Messages = jsonObjectsData.getJSONArray("messages");

                        arrayChat.clear();

                        for (int i = 0; i < jsonArray_Messages.length(); i++) {

                            JSONObject jsonObject = jsonArray_Messages.getJSONObject(i);

                            Message objMessage = new Message();
                            objMessage.setmText(jsonObject.getString("text"));
                            objMessage.setmDate(jsonObject.getString("dt"));
                            objMessage.setmSender(jsonObject.getInt("my"));

                            arrayChat.add(objMessage);
                        }

                        Collections.reverse(arrayChat);

                        mAdapter.notifyDataSetChanged();
                        mList.setSelection(mAdapter.getCount()-1);

                        progressBar_chat.setVisibility(View.INVISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressBar_chat.setVisibility(View.INVISIBLE);
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

    private void sendMessage(String url) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Response", "Messsage: " + response.toString());
                /*
                struct CommandGetChat: Codable {
                    struct Messages: Codable {
                        let id: String
                        let autor: String
                        let my: Int
                        let img:String
                        let text: String
                        let dt: String
                        let read: Int
                        let link: String
                    }
                    struct Data: Codable {
                        let messages: [Messages]
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
                    JSONObject jsonObjectsResponse = null;
                    JSONArray jsonArray_Messages = null;

                    try {

                        jsonObjects = new JSONObject(response.toString());
                        String s = jsonObjects.getString("error");

                        if (s.charAt(0) == '0') {

                           requestChatList("https://pro100.media/api/message/get-chat-messages/?token="+token+"&id=" + Select_Chat_ID);
                        } else {
                            progressBar_chat.setVisibility(View.INVISIBLE);
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

}