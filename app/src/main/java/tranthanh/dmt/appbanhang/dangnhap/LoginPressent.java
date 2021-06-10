package tranthanh.dmt.appbanhang.dangnhap;

import android.content.Context;
import android.text.TextUtils;

public class LoginPressent {
    IfLogin ifLogin;
    Context context;

    public LoginPressent(IfLogin ifLogin, Context context) {
        this.ifLogin = ifLogin;
        this.context = context;
    }
    public boolean CheckUser(String chuoi){
        if(TextUtils.isEmpty(chuoi)){
            ifLogin.Messhollow("User chua co thong tin");
         return false;
        }
        return true;
    }
    public boolean CheckPW(String chuoi){
        if(TextUtils.isEmpty(chuoi)){
            ifLogin.Messhollow("Password chua co thong tin");
            return false;
        }
            return true;

    }

}
