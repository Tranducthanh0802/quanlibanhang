package tranthanh.dmt.appbanhang.storemanage;

import java.util.List;

import tranthanh.dmt.appbanhang.dangnhap.Drinks;

public class hoadon {
    String date;
    List<Drinks> list_coc;
    String total;
    String time;
    String iddiscount;
    String idMembership;
    String tk;

    public hoadon(String date, List<Drinks> list_coc, String total, String time, String iddiscount, String idMembership, String tk) {
        this.date = date;
        this.list_coc = list_coc;
        this.total = total;
        this.time = time;
        this.iddiscount = iddiscount;
        this.idMembership = idMembership;
        this.tk = tk;
    }

    public String getTk() {
        return tk;
    }

    public void setTk(String tk) {
        this.tk = tk;
    }

    public String getIddiscount() {
        return iddiscount;
    }

    public void setIddiscount(String iddiscount) {
        this.iddiscount = iddiscount;
    }

    public String getIdMembership() {
        return idMembership;
    }

    public void setIdMembership(String idMembership) {
        this.idMembership = idMembership;
    }

    public hoadon(String date, List<Drinks> list_coc, String total, String time, String iddiscount, String idMembership) {
        this.date = date;
        this.list_coc = list_coc;
        this.total = total;
        this.time = time;
        this.iddiscount = iddiscount;
        this.idMembership = idMembership;
    }

    public hoadon(String date, List<Drinks> list_coc, String total, String time) {
        this.date = date;
        this.list_coc = list_coc;
        this.total = total;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Drinks> getList_coc() {
        return list_coc;
    }

    public void setList_coc(List<Drinks> list_coc) {
        this.list_coc = list_coc;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
