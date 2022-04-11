package com.dsc.grocerymanagement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsc.grocerymanagement.R;

public class Splashscreen extends AppCompatActivity {

    private static int Splash_screen = 4000;
    //Variables
    Animation leftanim, bottomanim;
    ImageView mainlogo;
    TextView Appname, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //Animation
        leftanim = AnimationUtils.loadAnimation(this, R.anim.left_anim);
        bottomanim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);
        //Hooks
        mainlogo = findViewById(R.id.imageView);
        Appname = findViewById(R.id.textView);
        slogan = findViewById(R.id.textView2);

        mainlogo.setAnimation(leftanim);
        Appname.setAnimation(bottomanim);
        slogan.setAnimation(bottomanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splashscreen.this, PhoneLoginActivity.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(mainlogo, "logo_image");
                pairs[1] = new Pair<View, String>(Appname, "logo_text");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Splashscreen.this, pairs);
                    startActivity(intent, options.toBundle());

                }
            }
        }, Splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5000);
    }
}