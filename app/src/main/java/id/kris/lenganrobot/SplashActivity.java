package id.kris.lenganrobot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splash = new Intent(SplashActivity.this, id.kris.lenganrobot.MainActivity.class);
                startActivity(splash);
                finish();
            }
        },3000);

        Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        ImageView splash1 = findViewById(R.id.splash);
        splash1.startAnimation(fade_in);

        ProgressBar progressBar = findViewById(R.id.loading);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.loading_screen);
        progressBar.startAnimation(animation);

    }
}