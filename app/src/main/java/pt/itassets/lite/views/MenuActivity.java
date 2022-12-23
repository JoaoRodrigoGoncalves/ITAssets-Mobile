package pt.itassets.lite.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pt.itassets.lite.R;

public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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

            case R.id.item_reparar:
                Fragment frag = new Frag_B();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, frag).commit();
                return true;

            case R.id.item_alocar:
                Fragment frag_a = new Frag_B();
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
}