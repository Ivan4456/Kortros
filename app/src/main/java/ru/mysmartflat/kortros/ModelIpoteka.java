package ru.mysmartflat.kortros;

/*
{
        "logo": "https://test.mysmartflat.ru/img/v1/sber.png",
        "title":"\u0421\u0431\u0435\u0440\u0431\u0430\u043d\u043a \u0420\u043e\u0441\u0441\u0438\u0438",
        "text":[
                "\u0421\u0440\u043e\u043a \u043a\u0440\u0435\u0434\u0438\u0442\u0430 \u043e\u0442 1 \u0433\u043e\u0434\u0430 \u0434\u043e 30 \u043b\u0435\u0442",
                "\u041f\u0435\u0440\u0432\u043e\u043d\u0430\u0447\u0430\u043b\u044c\u043d\u044b\u0439 \u0432\u0437\u043d\u043e\u0441: \u043e\u0442 15%",
                "\u0421\u0440\u043e\u043a \u043a\u0440\u0435\u0434\u0438\u0442\u0430 \u043e\u0442 1 \u0433\u043e\u0434\u0430 \u0434\u043e 30 \u043b\u0435\u0442"
               ]
}
*/

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ModelIpoteka extends RealmObject {

    String title;
    String logo;
    byte [] img_logo;
    RealmList<String> text = new RealmList<String>();

    public ModelIpoteka() {
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }

    public byte [] getImgLogo() {
        return img_logo;
    }
    public void setImgLogo(byte [] img_logo) {
        this.img_logo = img_logo;
    }

    public void setText(RealmList<String> text) {
        this.text = text;
    }
    public RealmList<String> getText() {
        return text;
    }

}

