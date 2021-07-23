package tranthanh.dmt.appbanhang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash_activity extends AppCompatActivity {
    Animation left;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);
        left= AnimationUtils.loadAnimation(this,R.anim.left_aniation);
        img=findViewById(R.id.imgsplash);
        img.setAnimation(left);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(Splash_activity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        },5000);
    }
}