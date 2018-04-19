package com.fidenz.dev.crmdemo.Models;

/**
 * Created by fidenz on 4/12/18.
 */

public class Contact {


    String contactId;
    String Userid;
    String name;
    String mobileNO;
    String telephoneno;
    String email;
    String description;
    String address;

    public Contact() {
    }

    public Contact(String contactId, String userid, String name, String mobileNO, String telephoneno, String email, String description, String address) {
        this.contactId = contactId;
        this.Userid = userid;
        this.name = name;
        this.mobileNO = mobileNO;
        this.telephoneno = telephoneno;
        this.email = email;
        this.description = description;
        this.address = address;
    }

    public String getContactId() {
        return contactId;
    }

    public String getUserid() {
        return Userid;
    }

    public String getName() {
        return name;
    }

    public String getMobileNO() {
        return mobileNO;
    }

    public String getTelephoneno() {
        return telephoneno;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }
}
