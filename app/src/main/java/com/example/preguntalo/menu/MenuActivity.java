package com.example.preguntalo.menu;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.preguntalo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.preguntalo.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

    private MenuActivityViewModel menuActivityViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuActivityViewModel = new MenuActivityViewModel(this.getApplication());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //prueba de appbar
        setSupportActionBar(binding.myToolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_vista_principal, R.id.navigation_nueva_consulta, R.id.navigation_estadisticas, R.id.navigation_perfil)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);;

        // Listener para re seleccionar el mismo valor
        navView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                // navegar nuevamente al mismo fragment
                navController.popBackStack(item.getItemId(), true);
                navController.navigate(item.getItemId());
            }
        });

        // Listener para seleccionar diferentes items
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navController.navigate(item.getItemId());
                return true;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

}