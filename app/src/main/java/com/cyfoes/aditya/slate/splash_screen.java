package com.cyfoes.aditya.slate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class splash_screen extends AppCompatActivity{
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_splash_screen);
        ImageView imageView = (ImageView) findViewById(R.id.white);
        final ImageView imageView2 = (ImageView) findViewById(R.id.text);
        imageView2.setVisibility(View.VISIBLE);
        final Animation loadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.name_fadein);
        Animation loadAnimation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_white_zoomin);
        imageView.startAnimation(loadAnimation2);
        loadAnimation2.setAnimationListener(new AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                imageView2.setVisibility(View.INVISIBLE);
                imageView2.startAnimation(loadAnimation);
            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(splash_screen.this, MainActivity.class));
                finish();
            }
        }, 4000);
    }
}
