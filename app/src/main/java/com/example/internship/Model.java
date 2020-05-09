package com.example.internship;

import com.google.firestore.v1beta1.DocumentTransform;

public class Model {
    String id, title,desc,address;
    Object timeStamp;
    public Model() {
    }

    public Model(String id, String title, String desc,String address) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.address=address;
//        this.timeStamp = DocumentTransform.FieldTransform.ServerValue.TIMESTAMP;
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
}
