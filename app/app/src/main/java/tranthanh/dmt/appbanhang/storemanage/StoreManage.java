package tranthanh.dmt.appbanhang.storemanage;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tranthanh.dmt.appbanhang.R;
import tranthanh.dmt.appbanhang.dangnhap.Drinks;
import tranthanh.dmt.appbanhang.dangnhap.LoginFragment;

public class StoreManage extends Fragment implements ionGetName, ionAddMinus, ValueEventListener {
    Button btn_delelte, btn_out, btn_pay, btn_find,btn_discount,btn_membership,btn_registration;
    EditText edt_moneyofcustomer,edt_find;
    TextView txt_time, txt_numbercustomer, txt_numbercup, txt_allmoney, txt_discount, txt_moneyreturn;
    RecyclerView recyclerView_name, recyclerView_category, recyclerView_cup;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    List<Drinks> arrayList;
    int totalMoney = 0;
    List<Drinks> list_cups ;
    List<Drinks> list_names = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String date;
    adapter_listname adapter_listname;
    adapter_category adapter_category;
    adapter_listcup adapter_listcup;
    int sokhach=0,khuyenmai=0,thanhvien=0,coc=0;
    String iddiscount="",idmembership="",tiennhap="";
    public static boolean numbersl=false;
    private static int save = -1;
    public static StoreManage newInstance() {

        Bundle args = new Bundle();

        StoreManage fragment = new StoreManage();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_storemanage, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = prefs.edit();
        list_cups=new ArrayList<>();
        Calendar c = Calendar.getInstance();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        SimpleDateFormat dateformat=new SimpleDateFormat("dd-MMM-yyyy");
        date =dateformat.format(c.getTime());
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("user").child(date+"");
        databaseReference.addValueEventListener(this);
        anhxa(view);
        resest();
        thanhvien=0;
        totalMoney=0;
        volley(view);
        Gson gson = new Gson();
        String json = prefs.getString("json", "");
        Type type = new TypeToken<List<Drinks>>() {
        }.getType();
        arrayList = gson.fromJson(json, type);
        List<Drinks> list_categorys = new ArrayList<>();
        List<Drinks> list_names = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getCategory().toString().equals("Combo yeu thich")) {
                list_names.add(arrayList.get(i));
            }
        }
        adapter_listname = new adapter_listname(list_names, view.getContext(), this,arrayList);
        //RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView_name.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
        recyclerView_name.setAdapter(adapter_listname);
        adapter_listcup = new adapter_listcup(list_cups, getActivity(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_cup.setLayoutManager(layoutManager);
        recyclerView_cup.setAdapter(adapter_listcup);

        for (int i = 0; i < arrayList.size(); i++) {
            list_categorys.add(arrayList.get(i));
            for (int j = i + 1; j < arrayList.size(); j++) {
                if (arrayList.get(i).getCategory().equals(arrayList.get(j).getCategory())) {
                    if(j==arrayList.size()-1){
                        i=arrayList.size();
                    }
                } else {
                    i = j;
                    break;
                }
            }
        }
        adapter_category = new adapter_category(list_categorys, view.getContext(), this);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_category.setLayoutManager(layoutManager1);
        recyclerView_category.setAdapter(adapter_category);

        edt_moneyofcustomer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                end();
                return true;
            }
        });
        btn_delelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resest();
                restore();
                thanhvien=0;
                totalMoney=0;
                list_cups.clear();
                adapter_listcup.notifyDataSetChanged();
                numbersl=true;

            }
        });
        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, LoginFragment.newInstance()).commit();
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (end()) {
                    numbersl=true;
                    String tk=prefs.getString("tk","");
                    hoadon hd = new hoadon(date, list_cups, txt_allmoney.getText().toString(), txt_time.getText().toString(),iddiscount,idmembership,tk);
                    databaseReference.child(txt_time.getText().toString()).setValue(hd);
                    Log.d("12334", "onClick: "+iddiscount+idmembership+tk);
                    Toast.makeText(getActivity(), "Thanh Cong", Toast.LENGTH_SHORT).show();
                    list_cups.clear();
                    recyclerView_cup.removeAllViews();
                    Toast.makeText(getActivity(), "Thanh Cong", Toast.LENGTH_SHORT).show();
                    resest();
                    thanhvien=0;
                    totalMoney=0;
                    restore();
                }else{
                    Toast.makeText(getActivity(), "Thanh toan chua thanh cong", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_find.setVisibility(view.getVisibility());
                edt_find.requestFocus();
                edt_find.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        adapter_listname.getFilter().filter(charSequence);
                        adapter_listname.notifyDataSetChanged();
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });
        btn_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendiscountDialog(Gravity.CENTER); end();
            }

        });
        btn_membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMemberShip(Gravity.CENTER);
                end();
            }
        });
        btn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegistration(Gravity.CENTER);
            }

        });
        return view;
    }

    Boolean end(){
        if(!TextUtils.isEmpty(edt_moneyofcustomer.getText()) && Integer.valueOf(edt_moneyofcustomer.getText().toString())>totalMoney) {
            int conso = Integer.valueOf(edt_moneyofcustomer.getText().toString()) - totalMoney-khuyenmai*totalMoney/100-thanhvien *totalMoney/100;
            if(conso>0) {
                txt_moneyreturn.setText(moneyFormat(conso) + "");
            }else {
                txt_moneyreturn.setText("0");

            }
            return true;
        }else{
            txt_moneyreturn.setText( "0");
            return false;
        }
    }

    private void volley(View view) {
        String url = "http://192.168.1.58:8080/TraSua/trasua.php";
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray ThucDon = jsonObject.getJSONArray("ThucDon");
                    JSONArray khuyenmai = jsonObject.getJSONArray("khuyenmai");
                    JSONArray membership = jsonObject.getJSONArray("Thanhvien");
                    JSONArray Registration = jsonObject.getJSONArray("Registration");

                    List<Drinks> List_Drinks = new ArrayList<>();
                    for (int i = 0; i < ThucDon.length(); i++) {
                        JSONObject object1 = ThucDon.getJSONObject(i);
                        String name = object1.getString("Name");
                        String category = object1.getString("Category");
                        int cost = object1.getInt("Cost");
                        List_Drinks.add(new Drinks(name, category, cost));
                    }
                    ArrayList<Khuyenmai> list_khuyenmai=new ArrayList<>();
                    for (int i = 0; i < khuyenmai.length(); i++) {
                        JSONObject object1 = khuyenmai.getJSONObject(i);
                        int id=object1.getInt("id");
                        String timestart = object1.getString("timestart");
                        String timeend = object1.getString("timeend");
                        int percent = object1.getInt("PercentDiscount");
                        list_khuyenmai.add(new Khuyenmai(id,timestart, timeend, percent));
                    }
                    ArrayList<Integer> list_thanhvien=new ArrayList<Integer>();
                    for (int i = 0; i < membership.length() ; i++) {
                        // JSONObject object1 = ca.getJSONObject(i);
                        int id = membership.getInt(i);
                        list_thanhvien.add(id);
                    }
                    ArrayList<String> list_registration=new ArrayList<String>();
                    for (int i = 0; i < Registration.length() ; i++) {
                        // JSONObject object1 = ca.getJSONObject(i);
                        String res = Registration.getString(i);
                        list_registration.add(res);
                    }
                    editor = prefs.edit();
                    Gson gson = new Gson();

                    String json = gson.toJson(List_Drinks);
                    String jsonDiscount=gson.toJson(list_khuyenmai);
                    String JsonMembership=gson.toJson(list_thanhvien);
                    String jsonRegistration=gson.toJson(list_registration);
                    editor.putString("json", json);
                    editor.putString("jsondiscount",jsonDiscount);
                    editor.putString("jsonmembership",JsonMembership);
                    editor.putString("jsonRegistration",jsonRegistration);

                    editor.commit();
                    Log.d("Ten", "onClick:  cap nhat" +jsonRegistration);
                } catch (JSONException e) {
                    e.printStackTrace();
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
        btn_delelte = view.findViewById(R.id.btn_delete);
        btn_out = view.findViewById(R.id.btn_out);
        btn_pay = view.findViewById(R.id.btn_pay);
        txt_numbercup = view.findViewById(R.id.numbertable);
        txt_time = view.findViewById(R.id.time);
        txt_numbercustomer = view.findViewById(R.id.numbercustomer);
        txt_allmoney = view.findViewById(R.id.numbertotalmoney);
        edt_moneyofcustomer = view.findViewById(R.id.numbermoneyofcustomer);
        txt_discount = view.findViewById(R.id.numberdiscount);
        txt_moneyreturn = view.findViewById(R.id.numberreturnmoney);
        recyclerView_name = view.findViewById(R.id.rec_cupname);
        recyclerView_category = view.findViewById(R.id.rec_category);
        recyclerView_cup = view.findViewById(R.id.rec_listcup);
        btn_find = view.findViewById(R.id.btn_find);
        edt_find=view.findViewById(R.id.edttimkiem);
        btn_discount=view.findViewById(R.id.btn_discount);
        btn_membership=view.findViewById(R.id.btn_membership);
        btn_registration=view.findViewById(R.id.btn_dangkimembership);
    }
    private void resest(){
        txt_allmoney.setText("0");
        txt_discount.setText("0");
        txt_moneyreturn.setText("0");
        edt_moneyofcustomer.setText("");
        txt_numbercup.setText("so coc:");
        txt_time.setText("thoi gian:");
        iddiscount="";
        khuyenmai=0;
        coc=0;
    }

    @Override
    public void getname(String name) {
        coc++;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm");
        String datetime = dateformat.format(c.getTime());
        dateformat=new SimpleDateFormat("dd-MMM-yyyy");
        date =dateformat.format(c.getTime());
        txt_time.setText("thoi gian:"+datetime);
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getName().equals(name)) {
                list_cups.add(arrayList.get(i));
                totalMoney += arrayList.get(i).getCost()*arrayList.get(i).getSl();
                break;
            }
        }
        txt_numbercup.setText("so coc:"+coc);
        adapter_listcup.notifyDataSetChanged();
        txt_allmoney.setText(moneyFormat(totalMoney) + "");
        end();

    }
    void restore(){
        for(int i=0;i<arrayList.size();i++){
            if(arrayList.get(i).getSl()!=1){
                arrayList.get(i).setSl(1);
            }
        }
    }
    int totalCup(List<Drinks> list){
        int tong=0;
        for(int i=0;i<list.size();i++){
            tong+=list.get(i).getSl();
        }
        return tong;
    }

    @Override
    public void getNameCatgory(String name) {
        list_names.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getCategory().toString().equals(name)) {
                list_names.add(arrayList.get(i));
            }
        }
       adapter_listname = new adapter_listname(list_names, getActivity(), this,arrayList);
        recyclerView_name.setAdapter(adapter_listname);
       // end();

    }


    @Override
    public void add(String name,int sl) {
        coc++;
        txt_numbercup.setText("so coc:"+coc);
        for (int i = 0; i < list_cups.size(); i++) {
            if (list_cups.get(i).getName().toString().equals(name)) {
                totalMoney += list_cups.get(i).getCost();
                txt_allmoney.setText( moneyFormat(totalMoney) + "");
                break;
            }
        }
        end();

    }

    @Override
    public void minus(String name,int sl) {
        coc--;
        txt_numbercup.setText("so coc:"+coc);
        for (int i = 0; i < list_cups.size(); i++) {
            if (list_cups.get(i).getName().toString().equals(name ) ) {
                totalMoney -= list_cups.get(i).getCost();
                txt_allmoney.setText(moneyFormat(totalMoney) + "");
                break;
            }
        }
        end();
    }


    @Override
    public void delete(String name ,int sl,int posion) {
        numbersl=false;
        edt_moneyofcustomer.setText("");
        coc-=sl;
        totalMoney-=sl*list_cups.get(posion).getCost();
        list_cups.remove(posion);
        txt_numbercup.setText("so coc:"+coc);
        adapter_listcup.notifyDataSetChanged();
        txt_allmoney.setText(moneyFormat(totalMoney) + "");
         end();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        sokhach=0;
        Iterable<DataSnapshot> nodechid = snapshot.getChildren();
        for(DataSnapshot dataSnapshot1:nodechid){
                sokhach++;

        }
        txt_numbercustomer.setText("so khach:"+sokhach);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
   public static   String moneyFormat(int i){
        NumberFormat formatter = new DecimalFormat("#,###");
        double myNumber = i;
        String formattedNumber = formatter.format(myNumber);
        return  formattedNumber;
    }

    void opendiscountDialog(int gravity){
        Dialog dialog =new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_khuyenmai);
        Window window=dialog.getWindow();
        if(window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT , WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
        WindowManager.LayoutParams windowAttributes=window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);
        if(Gravity.CENTER==gravity){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }
        Button btn=dialog.findViewById(R.id.btn_searchdiscount);
        EditText edt=dialog.findViewById(R.id.edt_filliddiscount);
        TextView txt=dialog.findViewById(R.id.txt_notificationdiscount);
        Gson gson = new Gson();
        String json = prefs.getString("jsondiscount", "");
        Type type = new TypeToken<List<Khuyenmai>>() {
        }.getType();
        List<Khuyenmai> list_discount = gson.fromJson(json, type);

       edt.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                iddiscount=charSequence.toString().trim();
           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });

           btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   try {
                   for (int i = 0; i < list_discount.size(); i++) {
                       if (list_discount.get(i).getId() == Integer.valueOf(iddiscount)) {
                           Toast.makeText(getActivity(), "thanh cong" + list_discount.get(i).getPercent(), Toast.LENGTH_SHORT).show();
                           khuyenmai = list_discount.get(i).getPercent();
                           txt_discount.setText(thanhvien + khuyenmai + "%");
                           txt.setText("ma khuyen mai hop le");
                           edt_moneyofcustomer.setText("");
                           txt_moneyreturn.setText("0");
                           break;
                       } else if (i == list_discount.size() - 1) {
                           Toast.makeText(getActivity(), "ma khuyen mai khong co", Toast.LENGTH_SHORT).show();
                           txt.setText("ma khuyen mai khong hop le");
                           khuyenmai = 0;
                           iddiscount = "";
                       }
                   }
                   }catch (Exception e){
                       Toast.makeText(getActivity(), "Khong hop lệ", Toast.LENGTH_SHORT).show();
                   }
               }
           });

        dialog.show();

    }void openMemberShip(int gravity){
        Dialog dialog =new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thethanhvien);
        Window window=dialog.getWindow();
        if(window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT , WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
        WindowManager.LayoutParams windowAttributes=window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);
        if(Gravity.CENTER==gravity){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }
        Button btn=dialog.findViewById(R.id.btn_searchnumbershipcard);
        EditText edt=dialog.findViewById(R.id.edt_fillidnumbership);
        TextView txt=dialog.findViewById(R.id.txt_notification);
        Gson gson = new Gson();
        String json = prefs.getString("jsonmembership", "");
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> list_membership = gson.fromJson(json, type);

       edt.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                idmembership=charSequence.toString().trim();
           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });

           btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   try {
                   for (int i = 0; i < list_membership.size(); i++) {
                       Log.d("1234", "onClick: " + list_membership.size() + idmembership);
                       if (Integer.valueOf(list_membership.get(i)) == Integer.valueOf(idmembership)) {
                           thanhvien = 5;
                           txt_discount.setText(thanhvien + khuyenmai + "%");
                           txt.setText("the thanh vien hop le");
                           edt_moneyofcustomer.setText("");
                           txt_moneyreturn.setText("0");
                           break;
                       } else if (i == list_membership.size() - 1) {
                           Toast.makeText(getActivity(), "the thanh vien khong hop le", Toast.LENGTH_SHORT).show();
                           txt.setText("the thanh vien khong hop le");
                           thanhvien = 0;
                           idmembership = "";
                       }

                   }
                   }catch (Exception e){
                       Toast.makeText(getActivity(), "Khong hop le", Toast.LENGTH_SHORT).show();
                   }
               }
           });

        dialog.show();

    }
    private void openRegistration(int gravity) {
        Dialog dialog =new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_registration);
        Window window=dialog.getWindow();
        if(window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT , WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
        WindowManager.LayoutParams windowAttributes=window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);
        if(Gravity.CENTER==gravity){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }
        Button btn1=dialog.findViewById(R.id.btn_dangki);
        EditText edt=dialog.findViewById(R.id.edthvt);
        EditText edtdiachi=dialog.findViewById(R.id.edtdiachi);
        EditText edtsdt=dialog.findViewById(R.id.edtsdt);

        TextView txt=dialog.findViewById(R.id.txt_notificationRegistration);
        Gson gson = new Gson();

        String url="http://192.168.1.58:8080/TraSua/insert.php";

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                    String json = prefs.getString("jsonRegistration", "");
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> list_registration = gson.fromJson(json, type);
                    if (!TextUtils.isEmpty(edt.getText().toString().trim())) {
                        for (int i = 0; i < list_registration.size(); i++) {

                            if (list_registration.get(i).toLowerCase().trim().equals(edt.getText().toString().toLowerCase().trim())) {
                                Toast.makeText(getContext(), " da co thanh vien nay", Toast.LENGTH_SHORT).show();
                                txt.setText(" da co thanh vien nay");
                                return;
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "ban chua nhap thong tin ", Toast.LENGTH_SHORT).show();
                        txt.setText("ban chua nhap thong tin ");
                        return;
                    }

                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringReque = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("success")) {
                                Toast.makeText(getContext(), "Them thanh cong", Toast.LENGTH_SHORT).show();
                                txt.setText("Them thanh cong");
                                edt.setText("");
                                edtdiachi.setText("");
                                edtsdt.setText("");

                                volley(view);

                            } else {
                                Toast.makeText(getContext(), "Loi them" + response.trim(), Toast.LENGTH_SHORT).show();
                                txt.setText("Loi them");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "xay ra loi", Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("hoten", edt.getText().toString().trim());
                            params.put("Diachi", edtdiachi.getText().toString().trim());
                            params.put("sdt", edtsdt.getText().toString().trim());

                            return params;
                        }
                    };
                    requestQueue.add(stringReque);
                    }catch (Exception e){
                        Toast.makeText(getActivity(), "da co thanh vien nay", Toast.LENGTH_SHORT).show();
                    }
                }

            });


        dialog.show();

    }
}
