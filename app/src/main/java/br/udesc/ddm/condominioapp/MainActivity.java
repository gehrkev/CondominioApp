package br.udesc.ddm.condominioapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
}