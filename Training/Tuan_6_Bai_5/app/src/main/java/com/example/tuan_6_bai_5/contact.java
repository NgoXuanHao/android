package com.example.tuan_6_bai_5;

import java.io.Serializable;
import java.security.PublicKey;

import androidx.versionedparcelable.ParcelUtils;

public class contact implements Serializable {
    public String nameContact;
    public String numberContact;
    public String contact;

    public contact(String nameContact, String numberContact) {
        this.nameContact = nameContact;
        this.numberContact = numberContact;
    }

    public String getNameContact() {
        return nameContact;
    }

    public void setNameContact(String nameContact) {
        this.nameContact = nameContact;
    }

    public String getNumberContact() {
        return numberContact;
    }

    public void setNumberContact(String numberContact) {
        this.numberContact = numberContact;
    }
    @Override
    public String toString() {
        return this.contact = this.nameContact + " [" + this.numberContact + "]";
    }

}
