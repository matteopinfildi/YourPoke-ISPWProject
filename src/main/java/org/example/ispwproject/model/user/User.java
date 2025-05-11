package org.example.ispwproject.model.user;

import org.example.ispwproject.model.pokelab.PokeLab;
import org.example.ispwproject.utils.bean.UserBean;
import org.example.ispwproject.utils.enumeration.UserType;

public class User {

    private String username;
    private PokeLab pokeLab;
    private String password;
    private String email;
    private UserType uType;
    private String address;

    public User(UserBean userBean) {
        this.username = userBean.getUid();
        this.password = userBean.getPassword();
        this.email = userBean.getEmail();
        this.uType = userBean.getuType();
        this.address = userBean.getAddress();
    }

    public User(String username, String password, String email, UserType uType, String address){
        this.username = username;
        this.password = password;
        this.email = email;
        this.uType = uType;
        this.address = address;
    }

    public PokeLab getPokeLab() {return pokeLab;}
    public void setPokeLab(PokeLab pokeLab) {this.pokeLab = pokeLab;}

    public UserType getuType() {return uType;}
    public void setuType(UserType uType) {this.uType = uType;}

    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public String getEmail() {return email;}
    public String getAddress() {return address;}
}
