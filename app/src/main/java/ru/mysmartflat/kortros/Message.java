package ru.mysmartflat.kortros;

/**
 * Created by Prosen on 4/6/2016.
 */
public class Message {
    private String mtext ;
    private Integer mSender;
    private String mDate;

    public String getmDate(){

        return mDate;

    }

    public void setmDate(String mDate){

        this.mDate = mDate;

    }

    public Integer getmSender(){

        return mSender;

    }

    public void setmSender(Integer mSender){

        this.mSender = mSender;

    }

    public String getmText(){

        return mtext;

    }

    public void setmText(String mtext){

        this.mtext = mtext;

    }
}
