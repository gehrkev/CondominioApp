package br.udesc.ddm.condominioapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.udesc.ddm.condominioapp.ui.condominio.CondominioFragment;
import br.udesc.ddm.condominioapp.ui.locatario.LocatarioFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new CondominioFragment())
                    .commit();
            bottomNav.setSelectedItemId(R.id.nav_condominios);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment frag;
            if (item.getItemId() == R.id.nav_condominios) {
                frag = new CondominioFragment();
            } else if (item.getItemId() == R.id.nav_locatarios) {
                frag = new LocatarioFragment();
            } else {
                return false;
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, frag)
                    .commit();
            return true;
        });
    }
}
