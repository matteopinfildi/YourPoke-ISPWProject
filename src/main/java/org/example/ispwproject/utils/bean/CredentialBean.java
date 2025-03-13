package org.example.ispwproject.utils.bean;

public class CredentialBean {
    private String username;
    private String password;

    public CredentialBean(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {return username;}
    public String getPassword() {return password;}
}
