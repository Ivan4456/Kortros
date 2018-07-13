package ru.mysmartflat.kortros;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

/*
{"command":"kortros->get-complex",
 "error":0,
 "message":"",
 "data":{
         "token":"ust-377134-4b15e56857aa9e3dc43f7c3bfb3fe133",
         "complex":[{
                     "id":"12",
                     "city_id":"50",
                     "title":"\u0416\u041a Headliner",
                     "description":"\u0416\u041a Headliner",
                     "img":[
                            "https://pro100.media/api/ihome/images/kortros/img/small-slider/slider01_375.jpg",
                            "https://pro100.media/api/ihome/images/kortros/img/small-slider/slider02_375.jpg",
                            "https://pro100.media/api/ihome/images/kortros/img/small-slider/slider03_375.jpg",
                            "https://pro100.media/api/ihome/images/kortros/img/small-slider/slider04_375.jpg",
                            "https://pro100.media/api/ihome/images/kortros/img/small-slider/slider05_375.jpg",
                            "https://pro100.media/api/ihome/images/kortros/img/small-slider/slider06_375.jpg"
                           ],
                     "address":"\u041c\u043e\u0441\u043a\u0432\u0430,\u0428\u043c\u0438\u0442\u043e\u0432\u0441\u043a\u0438\u0439 \u043f\u0440\u043e\u0435\u0437\u0434, \u0434. 39",
                     "center":{
                               "x":"55.7551133807672",
                               "y":"37.545453489882"
                              },
                     "date_end":"\u0421\u0440\u043e\u043a \u0441\u0434\u0430\u0447\u0438: 4 \u043a\u0432\u0430\u0440\u0442\u0430\u043b 2019"
                    },{
                     "id":"18","city_id":"50","title":"\u0410\u043f\u0430\u0440\u0442\u0430\u043c\u0435\u043d\u0442\u044b \u0414\u043e\u043c 128","description":"\u0410\u043f\u0430\u0440\u0442\u0430\u043c\u0435\u043d\u0442\u044b \u0414\u043e\u043c 128","img":["https://pro100.media/api/ihome/images/kortros/img/small-slider/128_1.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/128_2.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/128_3.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/128_4.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/128_5.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/128_6.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/128_7.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/128_8.jpg"],"address":"\u0433. \u041c\u043e\u0441\u043a\u0432\u0430, \u0443\u043b. \u041f\u0440\u043e\u0444\u0441\u043e\u044e\u0437\u043d\u0430\u044f, \u0432\u043b. 128","center":{"x":"55.630401","y":"37.515102"},"date_end":"\u0421\u0440\u043e\u043a \u0441\u0434\u0430\u0447\u0438: 4 \u043a\u0432\u0430\u0440\u0442\u0430\u043b 2018"},{"id":"11","city_id":"50","title":"\u0416\u041a \u0414\u043e\u043c \u0441\u0435\u0440\u0435\u0431\u0440\u044f\u043d\u044b\u0439 \u0431\u043e\u0440","description":"\u0416\u041a \u0414\u043e\u043c \u0441\u0435\u0440\u0435\u0431\u0440\u044f\u043d\u044b\u0439 \u0431\u043e\u0440","img":["https://pro100.media/api/ihome/images/kortros/img/small-slider/silver_1.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/silver_2.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/silver_3.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/silver_4.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/silver_5.jpg"],"address":"\u0433. \u041c\u043e\u0441\u043a\u0432\u0430, \u0443\u043b. \u0416\u0438\u0432\u043e\u043f\u0438\u0441\u043d\u0430\u044f, \u0432\u043b. 21","center":{"x":"55.793065","y":"37.448509"},"date_end":"\u0421\u0440\u043e\u043a \u0441\u0434\u0430\u0447\u0438: 1 \u043a\u0432\u0430\u0440\u0442\u0430\u043b 2019"},{"id":"13","city_id":"50","title":"\u0416\u041a \u041b\u044e\u0431\u043b\u0438\u043d\u0441\u043a\u0438\u0439","description":"\u0416\u041a \u041b\u044e\u0431\u043b\u0438\u043d\u0441\u043a\u0438\u0439","img":["https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_1.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_2.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_3.jpg","https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_4.jpg"],"address":"\u041c\u043e\u0441\u043a\u0432\u0430, \u043f\u0440. 40 \u043b\u0435\u0442 \u041e\u043a\u0442\u044f\u0431\u0440\u044f, \u0432\u043b. 36","center":{"x":"55.673803","y":"37.745546"},"date_end":"\u0421\u0440\u043e\u043a \u0441\u0434\u0430\u0447\u0438: 4 \u043a\u0432\u0430\u0440\u0442\u0430\u043b 2018"}
                   ]},
         "token":"ust-377134-4b15e56857aa9e3dc43f7c3bfb3fe133",
         "fromdomain":""
}       }
*/

public class Realm_Complex extends RealmObject {

    @Required
    private String title;
    private String id;
    private String city_id;
    private String description;
    private String address;
    private String date_end;
    private RealmList<String> img = new RealmList<String>();
    private RealmList<byte []> imges_bitmap = new RealmList<byte []>();
    private String center_x;
    private String center_y;

    public String getTitle() {
        return title;
    }
    public void setTitle(final String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }
    public void setId(final String ID) {
        this.id = ID;
    }

    public String getCityId() {
        return city_id;
    }
    public void setCityId(final String city_id) {
        this.city_id = city_id;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription( String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress( String address) {
        this.address = address;
    }

    public String getDate_end() {
        return date_end;
    }
    public void setDate_end(final String date_end) {
        this.date_end = date_end;
    }

    public RealmList<String> getImgSliders() {
        return img;
    }
    public void setImgSliders( RealmList<String> img) {
        this.img = img;
    }

    public RealmList<byte []> getImgesBitmap() {
        return imges_bitmap;
    }
    public void setImgesBitmap( RealmList<byte []> imges_bitmap) {
        this.imges_bitmap = imges_bitmap;
    }

    public String getCenter_x() {
        return center_x;
    }
    public void setCenter_x( String center_x) {
        this.center_x = center_x;
    }

    public String getCenter_y() {
        return center_y;
    }
    public void setCenter_y( String center_y) {
        this.center_y = center_y;
    }
}