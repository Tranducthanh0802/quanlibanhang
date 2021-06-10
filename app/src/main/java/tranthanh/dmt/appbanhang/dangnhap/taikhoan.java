package tranthanh.dmt.appbanhang.dangnhap;

public class taikhoan {
    String tk;
    String pw;
    String name;
    String chucvu;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChucvu() {
        return chucvu;
    }

    public void setChucvu(String chucvu) {
        this.chucvu = chucvu;
    }

    public taikhoan(String tk, String name, String chucvu) {
        this.tk = tk;
        this.name = name;
        this.chucvu = chucvu;
    }

    public taikhoan(String tk, String pw) {
        this.tk = tk;
        this.pw = pw;
    }

    public String getTk() {
        return tk;
    }

    public void setTk(String tk) {
        this.tk = tk;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}
