package org.example.ispwproject.utils.bean;

import org.example.ispwproject.utils.enumeration.UserType;

public class UserTypeBean {

    private String uid;
    private UserType uType;

    public UserTypeBean(String uid, UserType uType){
        this.uid = uid;
        this.uType = uType;
    }

    public String getUid() {return uid;}
    public UserType getuType() {return uType;}
}
