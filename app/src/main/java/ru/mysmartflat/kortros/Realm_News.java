package ru.mysmartflat.kortros;

import io.realm.RealmObject;
import io.realm.annotations.Required;
import lombok.Getter;
import lombok.Setter;

/*
   "id":"1",
   "title":"\u0412 \u043f\u0435\u0440\u0432\u043e\u043c \u043a\u043e\u043c\u043f\u043b\u0435\u043a\u0441\u0435 \u0436\u0438\u043b\u044b\u0445 \u043d\u0435\u0431\u043e\u0441\u043a\u0440\u0435\u0431\u043e\u0432 Headliner \u043e\u0442\u043a\u0440\u044b\u043b\u0441\u044f \u043e\u0444\u0438\u0441 \u043f\u0440\u043e\u0434\u0430\u0436",
   "text":"\u0412 \u043f\u0435\u0440\u0432\u043e\u043c \u043a\u043e\u043c\u043f\u043b\u0435\u043a\u0441\u0435 \u0436\u0438\u043b\u044b\u0445 \u043d\u0435\u0431\u043e\u0441\u043a\u0440\u0435\u0431\u043e\u0432 Headliner \u043e\u0442\u043a\u0440\u044b\u043b\u0441\u044f \u043e\u0444\u0438\u0441 \u043f\u0440\u043e\u0434\u0430\u0436, \u043a\u043e\u0442\u043e\u0440\u044b\u0439 \u0440\u0430\u0431\u043e\u0442\u0430\u0435\u0442 \u0432 \u0435\u0436\u0435\u0434\u043d\u0435\u0432\u043d\u043e\u043c \u0440\u0435\u0436\u0438\u043c\u0435 \u0441 9-00 \u0434\u043e 21-00. \u041e\u0431\u0440\u0430\u0442\u0438\u0442\u044c\u0441\u044f \u0437\u0430 \u043a\u043e\u043d\u0441\u0443\u043b\u044c\u0442\u0430\u0446\u0438\u0435\u0439 \u043a \u0441\u043f\u0435\u0446\u0438\u0430\u043b\u0438\u0441\u0442\u0430\u043c \u043f\u043e \u043f\u043e\u0434\u0431\u043e\u0440\u0443 \u043d\u0435\u0434\u0432\u0438\u0436\u0438\u043c\u043e\u0441\u0442\u0438 \u043c\u043e\u0436\u043d\u043e \u043f\u043e \u0430\u0434\u0440\u0435\u0441\u0443: \u041c\u043e\u0441\u043a\u0432\u0430, \u0428\u043c\u0438\u0442\u043e\u0432\u0441\u043a\u0438\u0439 \u043f\u0440\u043e\u0435\u0437\u0434 39.",
   "dt":"31.01.2018",
   "img":"https://pro100.media/api/ihome/images/kortros/img/dce8916aa0affd08653e6c39dc8b30d6_375.jpg"
 */


@Getter
@Setter
public class Realm_News extends RealmObject {

    @Required
    String news_ID;
    String str_title;
    String str_text;
    String str_message_dt;
    String str_img;
    byte [] img_bitmap;


    public String getNewsID() {
        return news_ID;
    }
    public void setNewsID(String news_ID) {
        this.news_ID = news_ID;
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

/*
https://pro100.media/api/kortros/get-city/?token=&app=1
{
 "command":"kortros->get-city",
 "error":0,
 "message":"",
 "data":{
         "ghost_token":"ust-377150-c8ee62f534c75cb49196800cd3f21eb9",
         "token":"ust-377150-c8ee62f534c75cb49196800cd3f21eb9",
         "city":[{
                  "center":{
                            "x":55.713095595192,
                            "y":37.56365262247
                           },
                  "zoom":9,
                  "id":"50",
                  "title":"\u041c\u043e\u0441\u043a\u0432\u0430"}
                ]},
         "token":"",
         "ghost_token":"ust-377150-c8ee62f534c75cb49196800cd3f21eb9",
         "fromdomain":""
}
*/