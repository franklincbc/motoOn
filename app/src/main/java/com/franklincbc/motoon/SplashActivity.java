package com.franklincbc.motoon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity implements Runnable {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        handler.postDelayed(this, 2000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        run();
    }

    @Override
    public void run() {
        // Faça o carregamento necessário aqui...

        // Depois abre a atividade principal e fecha esta
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);

        finish();

        overridePendingTransition(
                android.R.anim.fade_in, android.R.anim.fade_out);

    }


}
