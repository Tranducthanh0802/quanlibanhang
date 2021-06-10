package tranthanh.dmt.appbanhang.dangnhap;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tranthanh.dmt.appbanhang.R;
import tranthanh.dmt.appbanhang.fragment_Ca;
import tranthanh.dmt.appbanhang.storemanage.StoreManage;

public class LoginFragment  extends Fragment implements IfLogin {
    EditText txt_user;
    EditText edt_pw;
    Button btn_signin,btn_connect;
    LoginPressent loginPressent;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login,container,false);
        Anhxa(view);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = prefs.edit();
        loginPressent =new LoginPressent(this,view.getContext());
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginPressent.CheckUser(txt_user.getText().toString().trim()) && loginPressent.CheckPW(edt_pw.getText().toString().trim())){
                    kiemtra(txt_user.getText().toString(),edt_pw.getText().toString());
                }else{

                }

            }
        });

        return view;
    }
    void kiemtra(String tk,String pw){
        String url = "http://192.168.1.5:8080/TraSua/trasua.php";
        List<taikhoan> list_tks=new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Log.d("abc", "onResponse: a");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray Nhanvien = jsonObject.getJSONArray("NhanVien");
                    for (int i = 0; i < Nhanvien.length(); i++) {
                        JSONObject object1 = Nhanvien.getJSONObject(i);
                        String username = object1.getString("username");
                        String password = object1.getString("password");
                        list_tks.add(new taikhoan(username, password));
                    }

                    for (int i = 0; i < list_tks.size(); i++) {
                        if (list_tks.get(i).getTk().toString().equals(tk) && list_tks.get(i).getPw().toString().equals(pw)) {
                            Toast.makeText(getContext(), "Thanh cong", Toast.LENGTH_SHORT).show();
                            editor.putString("tk", tk);
                            editor.commit();
                            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("login").replace(R.id.frame_main, fragment_Ca.newInstance()).commit();
                            Toast.makeText(getContext(), "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                            break;
                        }else {
                            Toast.makeText(getContext(), "Mat khau hoac tai khoan khong dung", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "loi!" + error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("abc", "onErrorResponse: " + error.toString());
            }
        });
        requestQueue.add(request);
    }

    private void Anhxa(View view) {
        edt_pw=view.findViewById(R.id.edt_password);
        txt_user=view.findViewById(R.id.txt_User);
        btn_signin=view.findViewById(R.id.btn_SignIn);

    }

    @Override
    public void Successful() {

        Toast.makeText(getContext(), "Thanh cong", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Messhollow(String chuoi) {
        Patterns.EMAIL_ADDRESS.matcher(chuoi).matches();
        Toast.makeText(getContext(), chuoi, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void Messfalse() {
        Toast.makeText(getContext(), "That bai", Toast.LENGTH_SHORT).show();

    }

}
