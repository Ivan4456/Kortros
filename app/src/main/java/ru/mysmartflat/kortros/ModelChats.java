package ru.mysmartflat.kortros;

/**
 * Created by ruslangasimov on 16.03.2018.
 */

public class ModelChats {


    String chat_ID;
    String str_title;
    String str_message;
    String last_message_dt;
    String str_badge;
    Integer int_readOnly;


    public ModelChats(String str_title, String str_message, String last_message_dt) {
        this.str_title = str_title;
        this.str_message = str_message;
        this.last_message_dt = last_message_dt;
    }

    public ModelChats() {

    }

    public String getChatID() {
        return chat_ID;
    }

    public void setChatID(String chat_ID) {
        this.chat_ID = chat_ID;
    }

    public String getStrTitle() {
        return str_title;
    }

    public void setStrTitle(String str_title) {
        this.str_title = str_title;
    }

    public String getStrMessage() {
        return str_message;
    }

    public void setStrBadge(String str_badge) {
        this.str_badge = str_badge;
    }

    public String getStrBadge() {
        return str_badge;
    }

    public void setIntReadOnly(Integer int_readOnly) {
        this.int_readOnly = int_readOnly;
    }

    public Integer getIntReadOnly() {
        return int_readOnly;
    }

    public String getLastDTMessage() {
        return last_message_dt;
    }

    public void setStrMessage(String str_message) {
        this.str_message = str_message;
    }

}
