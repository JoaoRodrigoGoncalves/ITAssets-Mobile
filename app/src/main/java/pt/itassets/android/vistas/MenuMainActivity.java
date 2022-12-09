package pt.itassets.android.vistas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import pt.itassets.android.utils.Helper;
import pt.itassets.android.R;

public class MenuMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private String email;

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
        email = getIntent().getStringExtra(Helper.USER_EMAIL);

        SharedPreferences infoUser=getSharedPreferences(Helper.USER_STORAGE, Context.MODE_PRIVATE);
        if(email!=null){
            SharedPreferences.Editor editor = infoUser.edit();
            editor.putString(Helper.USER_EMAIL, email);
            editor.apply();
        }
        else{
            email=infoUser.getString(Helper.USER_EMAIL, getString(R.string.default_email));
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

            case R.id.registoRequisicaoItem:
                fragment = new ListagemPedidosRequisicaoFragment();
                break;

            case R.id.itemLogout:
                SharedPreferences sharedPrefs = getSharedPreferences(Helper.USER_STORAGE, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.remove(Helper.USER_TOKEN);
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