package ru.mysmartflat.kortros;

import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Required;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Realm_City extends RealmObject {

    @Required
    private String title;
    private String id;
    private Integer zoom;
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

    public Integer getZoom() {
        return zoom;
    }
    public void setZoom(final Integer zoom) {
        this.zoom = zoom;
    }

    public String getCenter_x() {
        return center_x;
    }
    public void setCenter_x(final String center_x) {
        this.center_x = center_x;
    }

    public String getCenter_y() {
        return center_y;
    }
    public void setCenter_y(final String center_y) {
        this.center_y = center_y;
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