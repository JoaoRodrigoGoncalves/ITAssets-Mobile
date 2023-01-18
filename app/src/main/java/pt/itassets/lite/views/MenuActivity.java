package pt.itassets.lite.views;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pt.itassets.lite.R;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;

public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNav;
    private static final int ACT_QRCODE_READER = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);
        bottomNav.setSelectedItemId(R.id.item_lista_itens);

        // Pre-carregar dados para as arrays. Isto é necessário para a pesquisa por QR code, visto
        // visto que é feita uma verificação se o item existe antes de tentar abrir a atividade de
        // detalhes.

        Singleton.getInstance(this).getAllGrupoItensAPI(this); // Pre-carregar grupos de itens
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
        switch (item.getItemId()) {
            case R.id.item_lista_itens:
                Fragment listaItensFragment = new ListaItensFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, listaItensFragment).commit();
                setTitle("Itens");
                return true;

            case R.id.item_lista_grupos:
                Fragment ListaGruposfrag = new ListaGrupoItensFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ListaGruposfrag).commit();
                setTitle("Grupo Itens");
                return true;

            case R.id.item_alocar:
                Fragment ListaAlocacoes = new ListaPedidosAlocacaoFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ListaAlocacoes).commit();
                setTitle("Pedidos de Requisição");
                return true;

            case R.id.item_reparar:
                Fragment frag_b = new ListaGrupoItensFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, frag_b).commit();
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
        if (item.getItemId() == R.id.lerQrCode) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Sem permissão
                String[] perm = {Manifest.permission.CAMERA};
                requestPermissions(perm, 1);
            }
            else
            {
                abrirQRCodeReader();
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirQRCodeReader();
            } else {
                Toast.makeText(this, "Sem permissão para camara", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void abrirQRCodeReader()
    {
        Intent qrcode = new Intent(this, QRCodeReaderActivity.class);
        startActivityForResult(qrcode, ACT_QRCODE_READER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == ACT_QRCODE_READER) {
            if(intent != null)
            {
                String qrResult = intent.getStringExtra("resultado");

                List<String> parts = new ArrayList<>(Arrays.asList(qrResult.split("_")));

                try
                {
                    Integer objeto_id = Integer.parseInt(parts.get(1));

                    switch(parts.get(0))
                    {
                        case "ITEM":
                            if(Singleton.getInstance(this).getItem(objeto_id) != null)
                            {
                                Intent item_intent = new Intent(this, DetalhesItemActivity.class);
                                item_intent.putExtra("ID_ITEM", (int) objeto_id);
                                startActivity(item_intent);
                            }
                            else
                            {
                                Toast.makeText(this, "ID do Item inválido", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case "GRUPO":
                            if(Singleton.getInstance(this).getGrupoItens(objeto_id) != null)
                            {
                                Intent grupo_intent = new Intent(this, DetalhesGrupoActivity.class);
                                grupo_intent.putExtra("ID_GRUPO", (int) objeto_id);
                                startActivity(grupo_intent);
                            }
                            else
                            {
                                Toast.makeText(this, "ID do Grupo inválido", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        default:
                            Toast.makeText(this, "QRCode inválido", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception exception)
                {
                    Toast.makeText(this, "QRCode inválido", Toast.LENGTH_SHORT).show();
                    System.out.println("ERRO: " + exception.getMessage());
                    exception.printStackTrace();
                }
            }
        }
    }

    public void BTN_Logout(View view) {
        SharedPreferences preferences = getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);
        preferences.edit().putString(Helpers.USER_TOKEN, null).commit();
        Intent logout = new Intent(this, LoginActivity.class);
        startActivity(logout);
        finish();
    }
}