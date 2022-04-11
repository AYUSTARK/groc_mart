package com.dsc.grocerymanagement.model;


public class Offermodel {
    String offername;
    String offerimg;
    String offertype;
    String c1, offerdesc, offerimage, c2, c3;

    public Offermodel() {

    }

    public Offermodel(String offername, String offerimg, String offertype) {
        this.offername = offername;
        this.offerimg = offerimg;
        this.offertype = offertype;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public String getC3() {
        return c3;
    }

    public void setC3(String c3) {
        this.c3 = c3;
    }


    public String getOfferdesc() {
        return offerdesc;
    }

    public void setOfferdesc(String offerdesc) {
        this.offerdesc = offerdesc;
    }

    public String getOfferimage() {
        return offerimage;
    }

    public void setOfferimage(String offerimage) {
        this.offerimage = offerimage;
    }

    public String getOffername() {
        return offername;
    }

    public void setOffername(String offername) {
        this.offername = offername;
    }

    public String getOfferimg() {
        return offerimg;
    }

    public void setOfferimg(String offerimg) {
        this.offerimg = offerimg;
    }

    public String getOffertype() {
        return offertype;
    }

    public void setOffertype(String offertype) {
        this.offertype = offertype;
    }
}
