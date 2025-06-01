package br.udesc.ddm.condominioapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.*;
import android.animation.*;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity{
    private static final long SPLASH_DELAY = 2500; // Duracao de 3 segundos na tela de splash

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        animateWaves();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();                      //
        }, SPLASH_DELAY);
    }

    private void animateWaves() {
        ImageView waveBottom = findViewById(R.id.waveBackground);
        ImageView waveTop    = findViewById(R.id.waveTop);

        ObjectAnimator bottom = ObjectAnimator.ofFloat(waveBottom,"translationX",-150f,150f);
        bottom.setDuration(4000);
        bottom.setRepeatMode(ValueAnimator.REVERSE);
        bottom.setRepeatCount(ValueAnimator.INFINITE);
        bottom.setInterpolator(new AccelerateDecelerateInterpolator());
        bottom.start();

        ObjectAnimator top = ObjectAnimator.ofFloat(waveTop,"translationX",150f,-150f);
        top.setDuration(4000);
        top.setRepeatMode(ValueAnimator.REVERSE);
        top.setRepeatCount(ValueAnimator.INFINITE);
        top.setInterpolator(new AccelerateDecelerateInterpolator());
        top.start();
    }
}
