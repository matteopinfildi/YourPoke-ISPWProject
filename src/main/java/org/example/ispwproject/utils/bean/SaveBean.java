package org.example.ispwproject.utils.bean;

// è una DTO che serve ad incapsulare l'id dell'utente quando viene passato tra i vari livelli dell'applicazione
public class SaveBean {

    private String uid;

    public SaveBean (String uid){this.uid = uid;}

    public String getUid() {return uid;}
}
