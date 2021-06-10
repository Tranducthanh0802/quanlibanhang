package tranthanh.dmt.appbanhang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Patterns;

import tranthanh.dmt.appbanhang.dangnhap.LoginFragment;
import tranthanh.dmt.appbanhang.storemanage.StoreManage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, LoginFragment.newInstance()).commit();

    }
}