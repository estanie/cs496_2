package com.example.q.cs496_2.views;

public class Listviewitem {
    private String name;
    private String address;
    private String contacts_check = "a";

    public Listviewitem(String name, String address){
        this.address = address;
        this.name = name;
    }

    public Listviewitem(String name, String address, String contactsId){
        this.name = name;
        this.address = address;
        this.contacts_check =contactsId;
    }

    public Listviewitem(){

    }

    public String getName(){
        return name;
    }

    public String getaddress(){
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContacts_check() {
        return contacts_check;
    }

    public void setContacts_check(String contactsId) {
        this.contacts_check = contactsId;
    }
}
