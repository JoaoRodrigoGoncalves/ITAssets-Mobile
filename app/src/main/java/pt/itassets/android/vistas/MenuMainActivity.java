package pt.itassets.android.vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import pt.itassets.android.Helper;
import pt.itassets.android.R;

public class MenuMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private String email;
    public static final String SHAREDUSER="DADOS_USER";
    public static final String EMAIL="EMAIL";
    public static final String USERNAME="USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.txt_abrir, R.string.txt_fechar);

        toggle.syncState();
        drawer.addDrawerListener(toggle);

        carregarCabecalho();
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        carregarFragmentoInicial();

    }

    private void carregarCabecalho(){
        email = getIntent().getStringExtra(EMAIL);

        SharedPreferences infoUser=getSharedPreferences(SHAREDUSER, Context.MODE_PRIVATE);
        if(email!=null){
            SharedPreferences.Editor editor = infoUser.edit();
            editor.putString(EMAIL, email);
            editor.apply();
        }
        else{
            email=infoUser.getString(EMAIL, getString(R.string.default_email));
        }

        View headerView = navigationView.getHeaderView(0);
        TextView tvEmail = headerView.findViewById(R.id.tvMainEmail);
        tvEmail.setText(email);
    }

    private boolean carregarFragmentoInicial() {
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.getItem(0);
        item.setChecked(true);
        return onNavigationItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment =  null;
        switch (item .getItemId()) {
            case R.id.listagemItens:
                fragment = new ListagemItensFragment();
                setTitle(item.getTitle());
                break;

            case R.id.itemLogout:
                SharedPreferences sharedPrefs = getSharedPreferences(Helper.PREF_STORAGE, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.remove(Helper.PREF_USER_TOKEN);
                editor.apply();

                Intent voltar = new Intent(this, LoginActivity.class);
                startActivity(voltar);
                finish();
                break;
        }
        if (fragment != null){
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}