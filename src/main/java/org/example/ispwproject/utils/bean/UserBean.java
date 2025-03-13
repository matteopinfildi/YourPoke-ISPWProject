package org.example.ispwproject.utils.bean;

import org.example.ispwproject.utils.enumeration.UserType;

public class UserBean {
    private String Uid;
    private String plid;
    private String email;
    private String password;
    private UserType uType;
    private String address;

    public UserBean(String Uid, String password, String email, UserType uType, String address) {
        this.Uid = Uid;
        this.password = password;
        this.email = email;
        this.uType = uType;
        this.address = address;
    }

    public String getUid() {return Uid;}
    public void setUid() {this.Uid = Uid;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public UserType getuType() {return uType;}
    public void setuType(UserType uType) {this.uType = uType;}

    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}

}
