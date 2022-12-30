package pt.itassets.lite.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pt.itassets.lite.R;
import pt.itassets.lite.utils.Helpers;

public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);
        bottomNav.setSelectedItemId(R.id.item_lista_itens);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
        switch (item.getItemId()) {
            case R.id.item_lista_itens:
                Fragment listaItensFragment = new ListaItensFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, listaItensFragment).commit();
                return true;

            case R.id.item_lista_grupos:
                Fragment ListaGruposfrag = new ListaGrupoItensFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ListaGruposfrag).commit();
                return true;

            case R.id.item_reparar:
                Fragment frag_b = new ListaGrupoItensFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, frag_b).commit();
                return true;

            case R.id.item_alocar:
                Fragment frag_a = new ListaGrupoItensFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, frag_a).commit();
                return true;

            case R.id.item_user:
                Fragment meusDetalhes = new MeusDetalhesFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, meusDetalhes).commit();
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.lerQrCode:
                Toast.makeText(this, "QRCode lol", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public void BTN_Logout(View view) {
        SharedPreferences preferences = getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);
        preferences.edit().putString(Helpers.USER_TOKEN, null).commit();
        Intent logout = new Intent(this, LoginActivity.class);
        startActivity(logout);
        finish();
    }
}