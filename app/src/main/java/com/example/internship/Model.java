package com.example.internship;

import android.location.Location;
import com.google.firestore.v1beta1.DocumentTransform;

import static java.sql.Types.TIMESTAMP;

public class Model {
    String id, title,desc,address;
    Object timeStamp;
    String location;

    public Model() {
    }

    public Model(String id, String title, String desc,String address,String location) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.address=address;
        this.timeStamp = TIMESTAMP;
        this.location=location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
