package ru.mysmartflat.kortros;

import io.realm.RealmObject;
import io.realm.annotations.Required;
import lombok.Getter;
import lombok.Setter;

/*
                             "date_end":"4 \u043a\u0432\u0430\u0440\u0442\u0430\u043b 2019 \u0433",
                             "img_big":"https://pro100.media/api/ihome/images/plan/big/37688.png",
                             "img_small":"https://pro100.media/api/ihome/images/plan/small/37688.png",
                             "number":"145",
                             "rooms":"1",
                             "square":"25.96",
                             "price":4524725,
                             "floor":"10",
                             "floor_total":"15",
                             "description_small":"\u0431\u0435\u0437 \u043e\u0442\u0434\u0435\u043b\u043a\u0438",
                             "favorites":0,
                             "uid":"88521761-ca1a-e711-80d1-005056010696"
*/

@Getter
@Setter
public class Realm_Flats extends RealmObject {
    @Required
    String complex_ID;
    Integer type_Apartament;
    String flats_UID;
    String date_end;

    String number;
    String rooms;

    Integer square;
    String price;

    Integer floor;
    String floor_total;

    String description_small;

    Integer favorites;

    String img_big;
    byte [] img_big_bitmap;
    String img_small;
    byte [] img_small_bitmap;


    public String getComplexID() {
        return complex_ID;
    }
    public void setComplexID(String complex_ID) {
        this.complex_ID = complex_ID;
    }


    public Integer getTypeApartment() {
        return type_Apartament;
    }
    public void setTypeApartment(Integer type_Apartament) {
        this.type_Apartament = type_Apartament;
    }


    public String getFlatsUID() {
        return flats_UID;
    }
    public void setFlatsUID(String flats_UID) {
        this.flats_UID = flats_UID;
    }


    public String getDateEnd() {
        return date_end;
    }
    public void setDateEnd(String date_end) {
        this.date_end = date_end;
    }


    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }


    public String getRooms() {
        return rooms;
    }
    public void setRooms(String rooms) {
        this.rooms = rooms;
    }


    public Integer getSquare() {
        return square;
    }
    public void setSquare(Integer square) {
        this.square = square;
    }


    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }


    public Integer getFloor() {
        return floor;
    }
    public void setFloor(Integer floor) {
        this.floor = floor;
    }


    public String getFloorTotal() {
        return floor_total;
    }
    public void setFloorTotal(String floor_total) {
        this.floor_total = floor_total;
    }


    public String getDescriptionSmall() {
        return description_small;
    }
    public void setDescriptionSmall(String description_small) {
        this.description_small = description_small;
    }


    public Integer getFavorites() {
        return favorites;
    }
    public void setFavorites(Integer favorites) {
        this.favorites = favorites;
    }


    public void setImgBig(String img_big) {
        this.img_big = img_big;
    }
    public String getImgBig() {
        return img_big;
    }


    public byte [] getImgBigBitmap() {
        return img_big_bitmap;
    }
    public void setImgBigBitmap(byte [] img_big_bitmap) {
        this.img_big_bitmap = img_big_bitmap;
    }


    public void setImgSmall(String img_small) {
        this.img_small = img_small;
    }
    public String getImgSmall() {
        return img_small;
    }


    public byte [] getImgSmallBitmap() {
        return img_small_bitmap;
    }
    public void setImgSmallBitmap(byte [] img_small_bitmap) {
        this.img_small_bitmap = img_small_bitmap;
    }

}

/*
https://pro100.media/api/kortros/get-flats/?token=ust-358645-c72f3f44ef332beb809d04176aefc1f3&complex_id=13&type_apartment=1&app=1

{
"command":"kortros->get-flats",
"error":0,
"message":"",
"data":{
        "token":"ust-358645-c72f3f44ef332beb809d04176aefc1f3",
        "count":69,
        "flat":[{
                 "date_end":"4 \u043a\u0432\u0430\u0440\u0442\u0430\u043b 2019 \u0433",
                 "img_big":"https://pro100.media/api/ihome/images/plan/big/37688.png",
                 "img_small":"https://pro100.media/api/ihome/images/plan/small/37688.png",
                 "number":"145",
                 "rooms":"1",
                 "square":"25.96",
                 "price":4524725,
                 "floor":"10",
                 "floor_total":"15",
                 "description_small":"\u0431\u0435\u0437 \u043e\u0442\u0434\u0435\u043b\u043a\u0438",
                 "favorites":0,
                 "uid":"88521761-ca1a-e711-80d1-005056010696"
                },{

               }],
        "ending":""
       },
"token":"ust-358645-c72f3f44ef332beb809d04176aefc1f3",
"fromdomain":""
}

*/