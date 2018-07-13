package ru.mysmartflat.kortros;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


public class AdapterChats extends BaseAdapter {

    private ArrayList<ModelChats> mListItems;
    private LayoutInflater mLayoutInflater;

    public AdapterChats(Context context, ArrayList<ModelChats> arrayList) {
        mListItems = arrayList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        view = mLayoutInflater.inflate(R.layout.adapter_chats, null);

        TextView textView_TitleChats = (TextView) view.findViewById(R.id.textView_TitleChats_ID);
        TextView textView_LastMessage_Chats = (TextView) view.findViewById(R.id.textView_LastMessage_Chats_ID);
        TextView textView_chatFirstLetter = (TextView) view.findViewById(R.id.textView_chatFirstLetter_ID);
        TextView textView_chatBadge = (TextView) view.findViewById(R.id.textView_chatBadge_ID);

        ModelChats objChat = (ModelChats) mListItems.get(position);
        textView_TitleChats.setText(objChat.getStrTitle());
        textView_chatFirstLetter.setText(objChat.getStrTitle().substring(0, 1));

        if (objChat.getStrBadge().equals("0")) {
            textView_chatBadge.setVisibility(View.INVISIBLE);
            textView_TitleChats.setTextColor(Color.BLACK);
            textView_LastMessage_Chats.setTextColor(Color.GRAY);
            textView_TitleChats.setTextColor(Color.BLACK);
        } else {
            textView_chatBadge.setText(objChat.getStrBadge());
            textView_chatBadge.setVisibility(View.VISIBLE);
            textView_TitleChats.setTextColor(Color.RED);
            textView_LastMessage_Chats.setTextColor(Color.RED);
            textView_TitleChats.setTextColor(Color.RED);
        }

        if ((objChat.getLastDTMessage().length() > 0) && (objChat.getLastDTMessage().charAt(0) != '0') && (objChat.getStrMessage().trim() != "")) {

            textView_LastMessage_Chats.setText(objChat.getStrMessage() + "\n" + getDate(Long.parseLong(objChat.getLastDTMessage()), "dd:MM:yyyy hh:mm"));

        } else {

            textView_LastMessage_Chats.setText(objChat.getStrMessage());

        }
        return view;
    }
    public static String getDate(long milliSeconds, String dateFormat) {

        Calendar calendar = Calendar.getInstance();

        milliSeconds = calendar.getTimeInMillis() - milliSeconds*1000;

        if (milliSeconds < 60000) {
            return "Сейчас";
        }

        long SecondsInMilli = 1000;
        long minutesInMilli = SecondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = milliSeconds / daysInMilli;
        milliSeconds = milliSeconds % daysInMilli;

        long elapsedHours = milliSeconds / hoursInMilli;
        milliSeconds = milliSeconds % hoursInMilli;

        long elapsedMinutes = milliSeconds / minutesInMilli;
        milliSeconds = milliSeconds % minutesInMilli;

        long elapsedSeconds = milliSeconds / SecondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, milliSeconds);

        String ElapsedDateTime = "";


        long year = elapsedDays / 365;
        if (year > 0) {
            if (year == 1) {
                return "Год назад";
            }
            return String.valueOf(year)+ " года назад";
        }

        if (elapsedDays > 0) {
            long mounth = elapsedDays / 30;
            if (mounth > 0) {
                return String.valueOf(mounth)+" мес.назад";
            }
            return String.valueOf(elapsedDays)+" д.назад";
        }

        if (elapsedHours > 0) {
            return String.valueOf(elapsedHours)+" ч.назад";
        }

        if (elapsedMinutes > 0) {
            return String.valueOf(elapsedMinutes)+" мин.назад";
        }

        return ElapsedDateTime;

    }

}