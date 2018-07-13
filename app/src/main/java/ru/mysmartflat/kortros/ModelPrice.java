package ru.mysmartflat.kortros;


/*
 * Created by ruslangasimov on 16.03.2018.
 */

/*
    "title":"1-\u043a\u043e\u043c\u043d\u0430\u0442\u043d\u0430\u044f",
    "count_text":"69 \u043a\u0432\u0430\u0440\u0442\u0438\u0440",
    "text":"\u041e\u0442 4.52 \u0434\u043e 7.62 \u043c\u043b\u043d \u0440\u0443\u0431",
    "type_apartment":1
 */

import io.realm.RealmObject;

public class ModelPrice extends RealmObject {

    String title;
    String count_text;
    String text;
    Integer type_apartment;


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public String getCountText() {
        return count_text;
    }
    public void setCountText(String count_text) {
        this.count_text = count_text;
    }


    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }


    public void setTypeApartment(Integer type_apartment) {
        this.type_apartment = type_apartment;
    }
    public Integer getTypeApartment() {
        return type_apartment;
    }

}