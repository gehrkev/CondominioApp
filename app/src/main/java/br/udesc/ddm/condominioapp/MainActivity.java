package br.udesc.ddm.condominioapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import br.udesc.ddm.condominioapp.ui.condominio.CondominioFragment;
import br.udesc.ddm.condominioapp.ui.locatario.LocatarioFragment;
import br.udesc.ddm.condominioapp.ui.about.AboutFragment; // Importe o novo AboutFragment

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
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_condominios) {
                selectedFragment = new CondominioFragment();
            } else if (itemId == R.id.nav_locatarios) {
                selectedFragment = new LocatarioFragment();
            } else if (itemId == R.id.nav_about) {
                selectedFragment = new AboutFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });
    }
}