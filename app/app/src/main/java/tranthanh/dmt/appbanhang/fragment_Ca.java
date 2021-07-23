package tranthanh.dmt.appbanhang;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tranthanh.dmt.appbanhang.dangnhap.Drinks;
import tranthanh.dmt.appbanhang.dangnhap.taikhoan;
import tranthanh.dmt.appbanhang.storemanage.StoreManage;
import tranthanh.dmt.appbanhang.storemanage.ionGetName;

public class fragment_Ca extends Fragment {
    List<String> ca;
    TextView txt_hoten, txt_date, txt_chucvu;
    Button btn_ok;
    Spinner spinner;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int j;

    public static fragment_Ca newInstance() {

        Bundle args = new Bundle();

        fragment_Ca fragment = new fragment_Ca();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nhanvien, container, false);
        anhxa(view);
        volley(view);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = prefs.edit();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
        String date = dateformat.format(c.getTime());
        txt_date.setText(" " + date);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("calam").child(date+"");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                j = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    databaseReference.child(txt_hoten.getText() + spinner.getItemAtPosition(j).toString()).setValue(date);
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("quanli").replace(R.id.frame_main, StoreManage.newInstance()).commit();
                }catch(Exception e) {
                    Toast.makeText(getActivity(), "Loi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void volley(View view) {
        String url = "http://192.168.1.58:8080/TraSua/trasua.php";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<String> list_cas = new ArrayList<>();
                List<taikhoan> list_tk = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray ca = jsonObject.getJSONArray("Ca");
                    JSONArray nhanvien = jsonObject.getJSONArray("NhanVien");
                    for (int i = 0; i < ca.length() / 2; i++) {
                        // JSONObject object1 = ca.getJSONObject(i);
                        String name = ca.getString(i);
                        list_cas.add(name);

                    }
                    for (int i = 0; i < nhanvien.length(); i++) {
                        JSONObject object1 = nhanvien.getJSONObject(i);
                        String name = object1.getString("Name");
                        String tk = object1.getString("username");
                        String chucvu = object1.getString("position");
                        list_tk.add(new taikhoan(tk, name, chucvu));
                        if (prefs.getString("tk", "").equals(tk)) {
                            txt_hoten.setText(name);
                            txt_chucvu.setText(chucvu);
                        }
                    }
                    ArrayAdapter adapterSpinner = new ArrayAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, list_cas);
                    spinner.setAdapter(adapterSpinner);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("12345", "onResponse: " + e.toString());
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), "loi!" + error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("abc", "onErrorResponse: " + error.toString());
            }
        });
        requestQueue.add(request);
    }


    private void anhxa(View view) {
        txt_hoten = view.findViewById(R.id.txt_hvt);
        txt_chucvu = view.findViewById(R.id.txt_cv);
        txt_date = view.findViewById(R.id.txt_time);
        spinner = view.findViewById(R.id.spinner_ca);
        btn_ok = view.findViewById(R.id.btn_ok);
    }
}
