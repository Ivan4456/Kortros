package ru.mysmartflat.kortros;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


public class AdapterChat extends BaseAdapter {

    private ArrayList<Message> mListItems;
    private LayoutInflater mLayoutInflater;

    TextView textView_Message_Chat;
    TextView textView_DataTime_Chat;

    public AdapterChat(Context context, ArrayList<Message> arrayList) {
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

        Message objChat = mListItems.get(position);

        if (objChat.getmSender() != 0) {
            view = mLayoutInflater.inflate(R.layout.adapter_chat_in, null);
            textView_Message_Chat = (TextView) view.findViewById(R.id.textView_Message_Chat_in_ID);
            textView_DataTime_Chat = (TextView) view.findViewById(R.id.textView_DataTime_Chat_in_ID);
        } else {
            view = mLayoutInflater.inflate(R.layout.adapter_chat_out, null);
            textView_Message_Chat = (TextView) view.findViewById(R.id.textView_Message_Chat_out_ID);
            textView_DataTime_Chat = (TextView) view.findViewById(R.id.textView_DataTime_Chat_out_ID);
        }

        textView_Message_Chat.setText(objChat.getmText());

        String dt = objChat.getmDate();
        String dataTime = getDate(Long.parseLong(dt), "dd:MM:yyyy hh:mm");

        textView_DataTime_Chat.setText(dataTime);

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
