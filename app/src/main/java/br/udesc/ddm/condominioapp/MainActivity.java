package br.udesc.ddm.condominioapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import br.udesc.ddm.condominioapp.ui.condominio.CondominioFormActivity;
import br.udesc.ddm.condominioapp.ui.condominio.CondominioListActivity;
import br.udesc.ddm.condominioapp.ui.locatario.LocatarioListActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnCadastrarCondominio;
    private Button btnListarCondominios;
    private Button btnGerenciarLocatarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        setupListeners();

        animateWaves();
    }

    private void initComponents() {
        btnCadastrarCondominio = findViewById(R.id.btnCadastrarCondominio);
        btnListarCondominios = findViewById(R.id.btnListarCondominios);
        btnGerenciarLocatarios = findViewById(R.id.btnGerenciarLocatarios);
    }

    private void setupListeners() {
        btnCadastrarCondominio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CondominioFormActivity.class);
                startActivity(intent);
            }
        });

        btnListarCondominios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CondominioListActivity.class);
                startActivity(intent);
            }
        });

        btnGerenciarLocatarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocatarioListActivity.class);
                startActivity(intent);
            }
        });
    }
    private void animateWaves() {
        ImageView waveBottom = findViewById(R.id.waveBackground);
        ImageView waveTop = findViewById(R.id.waveTop);

        ObjectAnimator bottomWaveAnim = ObjectAnimator.ofFloat(waveBottom, "translationX", -150f, 150f);
        bottomWaveAnim.setDuration(4000);
        bottomWaveAnim.setRepeatMode(ValueAnimator.REVERSE);
        bottomWaveAnim.setRepeatCount(ValueAnimator.INFINITE);
        bottomWaveAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        bottomWaveAnim.start();

        ObjectAnimator topWaveAnim = ObjectAnimator.ofFloat(waveTop, "translationX", 150f, -150f);
        topWaveAnim.setDuration(4000);
        topWaveAnim.setRepeatMode(ValueAnimator.REVERSE);
        topWaveAnim.setRepeatCount(ValueAnimator.INFINITE);
        topWaveAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        topWaveAnim.start();
    }
}