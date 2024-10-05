package com.example.lab4_20206456;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        // Obtener NavController usando el FragmentContainerView con su ID
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // Configurar botones de navegación
        findViewById(R.id.btnLigas).setOnClickListener(v -> navController.navigate(R.id.ligasFragment));
        findViewById(R.id.btnPosiciones).setOnClickListener(v -> navController.navigate(R.id.posicionesFragment));
        findViewById(R.id.btnResultados).setOnClickListener(v -> navController.navigate(R.id.resultadosFragment));
    }

    @Override
    public void onBackPressed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // Si estamos en el primer fragmento (Ligas), salir del AppActivity
        if (navController.getCurrentDestination().getId() == R.id.ligasFragment) {
            finish();  // Regresar al MainActivity
        } else {
            super.onBackPressed();  // De lo contrario, retrocede entre fragmentos
        }
    }
}