package com.pleximus.pet_app.core.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by pleximus on 06/05/17.
 */
@DatabaseTable(tableName = "DBUserContact")
public class DBUserContact implements Serializable{

    @DatabaseField(generatedId = true)
    public String contactId;
    @DatabaseField
    private String contactJID;
    @DatabaseField
    private String contactName;
    @DatabaseField
    private String contactStatus;
    @DatabaseField
    private String conatctPresence;


    public DBUserContact(String contactJID, String contactName, String contactStatus, String conatctPresence) {
        this.contactJID = contactJID;
        this.contactName = contactName;
        this.contactStatus = contactStatus;
        this.conatctPresence = conatctPresence;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactJID() {
        return contactJID;
    }

    public void setContactJID(String contactJID) {
        this.contactJID = contactJID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }

    public String getConatctPresence() {
        return conatctPresence;
    }

    public void setConatctPresence(String conatctPresence) {
        this.conatctPresence = conatctPresence;
    }
}
