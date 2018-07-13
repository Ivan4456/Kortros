package ru.mysmartflat.kortros;

import io.realm.RealmObject;
import io.realm.annotations.Required;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Realm_New extends RealmObject {

    @Required
    String new_ID;
    String str_title;
    String str_text;
    String str_message_dt;
    String str_img;
    byte [] img_bitmap;


    public String getNewsID() {
        return new_ID;
    }
    public void setNewsID(String news_ID) {
        this.new_ID = news_ID;
    }


    public String getStrTitle() {
        return str_title;
    }
    public void setStrTitle(String str_title) {
        this.str_title = str_title;
    }


    public void setStrText(String str_text) {
        this.str_text = str_text;
    }
    public String getStrText() {
        return str_text;
    }


    public void setDTMessage(String str_message_dt) {
        this.str_message_dt = str_message_dt;
    }
    public String getDTMessage() {
        return str_message_dt;
    }


    public void setImg(String str_img) {
        this.str_img = str_img;
    }
    public String getImg() {
        return str_img;
    }

    public byte [] getImgBitmap() {
        return img_bitmap;
    }
    public void setImgBitmap(byte [] img_bitmap) {
        this.img_bitmap = img_bitmap;
    }
}
