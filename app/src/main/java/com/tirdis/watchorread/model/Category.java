package com.tirdis.watchorread.model;

/**
 * Created by admin on 5/22/2015.
 */
public class Category {
    public Category(String id, String cdscp, String cimage) {
        this.id = id;
        this.cdscp = cdscp;
        this.cimage = cimage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCdscp() {
        return cdscp;
    }

    public void setCdscp(String cdscp) {
        this.cdscp = cdscp;
    }

    public String getCimage() {
        return cimage;
    }

    public void setCimage(String cimage) {
        this.cimage = cimage;
    }

    String id;
    String cdscp;
    String cimage;
}
