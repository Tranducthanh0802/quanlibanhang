package tranthanh.dmt.appbanhang.storemanage;

public class list_cup {
    String namecup;
    int sl;
    int gia;

    public list_cup(String namecup, int sl, int gia) {
        this.namecup = namecup;
        this.sl = sl;
        this.gia = gia;
    }

    public String getNamecup() {
        return namecup;
    }

    public void setNamecup(String namecup) {
        this.namecup = namecup;
    }

    public int getSl() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }
}
