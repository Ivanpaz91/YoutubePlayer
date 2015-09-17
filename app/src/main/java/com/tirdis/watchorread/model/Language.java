package com.tirdis.watchorread.model;

/**
 * Created by admin on 5/22/2015.
 */
public class Language {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLdscp() {
        return ldscp;
    }

    public void setLdscp(String ldscp) {
        this.ldscp = ldscp;
    }

    public Language(String id, String ldscp) {

        this.id = id;
        this.ldscp = ldscp;
    }

    String id;
    String ldscp;

}
