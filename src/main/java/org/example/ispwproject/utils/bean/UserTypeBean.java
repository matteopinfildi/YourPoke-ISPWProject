package org.example.ispwproject.utils.bean;

import org.example.ispwproject.utils.enumeration.UserType;

public class UserTypeBean {

    private String Uid;
    private UserType uType;

    public UserTypeBean(String Uid, UserType uType){
        this.Uid = Uid;
        this.uType = uType;
    }

    public String getUid() {return Uid;}
    public UserType getuType() {return uType;}
}
