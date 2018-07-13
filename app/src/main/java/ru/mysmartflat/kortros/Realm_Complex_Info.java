package ru.mysmartflat.kortros;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;
import lombok.Getter;
import lombok.Setter;

public class Realm_Complex_Info extends RealmObject {

    @Required
    private String title;

    private RealmList<ModelPrice> price = new RealmList<ModelPrice>();
    private RealmList<ModelIpoteka> ipoteka = new RealmList<ModelIpoteka>();

    private Double build_progress;

    private String action_type;
    private String action_title;
    private String action_number;


    public String getTitle() {
        return title;
    }
    public void setTitle(final String title) {
        this.title = title;
    }


    public RealmList<ModelPrice> getPrice() {
        return price;
    }
    public void setPrice(RealmList<ModelPrice> price) {
        this.price = price;
    }


    public RealmList<ModelIpoteka> getIpoteka() {
        return ipoteka;
    }
    public void setIpoteka(final RealmList<ModelIpoteka> ipoteka) {
        this.ipoteka = ipoteka;
    }


    public Double getBuildProgress() {
        return build_progress;
    }
    public void setBuildProgress(final Double build_progress) {
        this.build_progress = build_progress;
    }


    public String getActionType() {
        return action_type;
    }
    public void setActionType(final String action_type) {
        this.action_type = action_type;
    }


    public String getActionTitle() {
        return action_title;
    }
    public void setActionTitle(final String action_title) {
        this.action_title = action_title;
    }


    public String getActionNumber() {
        return action_number;
    }
    public void setActionNumber(final String action_number) {
        this.action_number = action_number;
    }

}

/*
{
    "command":"kortros->get-complex-info",
    "error":0,
    "message":"",
    "data":{
            "token":"ust-377134-4b15e56857aa9e3dc43f7c3bfb3fe133",
            "info":{
                    "img":[
                           "https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_1.jpg",
                           "https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_2.jpg",
                           "https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_3.jpg",
                           "https://pro100.media/api/ihome/images/kortros/img/small-slider/lub_4.jpg"],

                    "title":"\u0416\u041a \u041b\u044e\u0431\u043b\u0438\u043d\u0441\u043a\u0438\u0439",
                    "date_end":"\u0421\u0440\u043e\u043a \u0441\u0434\u0430\u0447\u0438: 4 \u043a\u0432\u0430\u0440\u0442\u0430\u043b 2018",
                    "price":[
                             {
                              "title":"1-\u043a\u043e\u043c\u043d\u0430\u0442\u043d\u0430\u044f",
                              "count_text":"69 \u043a\u0432\u0430\u0440\u0442\u0438\u0440",
                              "text":"\u041e\u0442 4.52 \u0434\u043e 7.62 \u043c\u043b\u043d \u0440\u0443\u0431",
                              "type_apartment":1
                             },{
                              "title":"2-\u043a\u043e\u043c\u043d\u0430\u0442\u043d\u0430\u044f",
                              "count_text":"46 \u043a\u0432\u0430\u0440\u0442\u0438\u0440","text":
                              "\u041e\u0442 8.25 \u0434\u043e 11.26 \u043c\u043b\u043d \u0440\u0443\u0431",
                              "type_apartment":2
                             },{
                              "title":"3-\u043a\u043e\u043c\u043d\u0430\u0442\u043d\u0430\u044f",
                              "count_text":"5 \u043a\u0432\u0430\u0440\u0442\u0438\u0440",
                              "text":"\u041e\u0442 11.62 \u0434\u043e 15.02 \u043c\u043b\u043d \u0440\u0443\u0431",
                              "type_apartment":3
                             }
                            ],
                    "description":null,
                    "description_more":"",
                    "build_progress":0.75,
                    "ipoteka":[
                               {
                                "logo":"https://test.mysmartflat.ru/img/v1/sber.png",
                                "title":"\u0421\u0431\u0435\u0440\u0431\u0430\u043d\u043a \u0420\u043e\u0441\u0441\u0438\u0438",
                                "text":["\u0421\u0440\u043e\u043a \u043a\u0440\u0435\u0434\u0438\u0442\u0430 \u043e\u0442 1 \u0433\u043e\u0434\u0430 \u0434\u043e 30 \u043b\u0435\u0442",
                                        "\u041f\u0435\u0440\u0432\u043e\u043d\u0430\u0447\u0430\u043b\u044c\u043d\u044b\u0439 \u0432\u0437\u043d\u043e\u0441: \u043e\u0442 15%",
                                        "\u0421\u0440\u043e\u043a \u043a\u0440\u0435\u0434\u0438\u0442\u0430 \u043e\u0442 1 \u0433\u043e\u0434\u0430 \u0434\u043e 30 \u043b\u0435\u0442"
                                       ]
                               },{
                                "logo":"https://test.mysmartflat.ru/img/v1/bopened.png",
                                "title":"\u0411\u0430\u043d\u043a \u041e\u0442\u043a\u0440\u044b\u0442\u0438\u0435",
                                "text":["\u0421\u0440\u043e\u043a \u043a\u0440\u0435\u0434\u0438\u0442\u0430 \u043e\u0442 1 \u0433\u043e\u0434\u0430 \u0434\u043e 30 \u043b\u0435\u0442",
                                        "\u041f\u0435\u0440\u0432\u043e\u043d\u0430\u0447\u0430\u043b\u044c\u043d\u044b\u0439 \u0432\u0437\u043d\u043e\u0441: \u043e\u0442 15%",
                                        "\u0421\u0440\u043e\u043a \u043a\u0440\u0435\u0434\u0438\u0442\u0430 \u043e\u0442 1 \u0433\u043e\u0434\u0430 \u0434\u043e 30 \u043b\u0435\u0442"
                                       ]
                               }
                              ],
                     "action":{
                               "type":"call",
                               "title":"\u041f\u043e\u0437\u0432\u043e\u043d\u0438\u0442\u044c",
                               "number":"74951043431"
                              }
                   }
                   },
             "token":"ust-377134-4b15e56857aa9e3dc43f7c3bfb3fe133","fromdomain":""

}
*/