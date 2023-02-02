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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import pt.itassets.lite.R;
import pt.itassets.lite.listeners.MQTTMessageListener;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;
import pt.itassets.lite.views.alocacao.ListaPedidosAlocacaoFragment;
import pt.itassets.lite.views.grupos.DetalhesGrupoActivity;
import pt.itassets.lite.views.grupos.ListaGrupoItensFragment;
import pt.itassets.lite.views.itens.DetalhesItemActivity;
import pt.itassets.lite.views.itens.ListaItensFragment;
import pt.itassets.lite.views.reparacao.ListaReparacoesFragment;

public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MQTTMessageListener {
    BottomNavigationView bottomNav;
    private static final int ACT_QRCODE_READER = 50;
    private boolean isFuncionario = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        isFuncionario = Objects.equals(preferences.getString(Helpers.USER_ROLE, null), "funcionario");

        if(isFuncionario)
        {
            setContentView(R.layout.activity_menu_funcionario);

            bottomNav = findViewById(R.id.bottom_navigation_funcionario);
            bottomNav.setOnNavigationItemSelectedListener(this);
            bottomNav.setSelectedItemId(R.id.item_alocar);
        }
        else
        {
            setContentView(R.layout.activity_menu);

            //Código caso seja outra coisa (operador)
            bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setOnNavigationItemSelectedListener(this);
            bottomNav.setSelectedItemId(R.id.item_lista_itens);

            // Pre-carregar dados para as arrays. Isto é necessário para a pesquisa por QR code, visto
            // visto que é feita uma verificação se o item existe antes de tentar abrir a atividade de
            // detalhes.

            Singleton.getInstance(this).getAllGrupoItensAPI(this); // Pre-carregar grupos de itens
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //MQTT
        Singleton.getInstance(this).setMqttMessageListener(this);
        Singleton.getInstance(this).startMQTT(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // https://www.geeksforgeeks.org/bottom-navigation-bar-in-android/
        switch (item.getItemId()) {
            case R.id.item_lista_itens:
                Fragment listaItensFragment = new ListaItensFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, listaItensFragment).commit();
                setTitle(R.string.txt_itens);
                return true;

            case R.id.item_lista_grupos:
                Fragment ListaGruposfrag = new ListaGrupoItensFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ListaGruposfrag).commit();
                setTitle(R.string.txt_grupo_itens);
                return true;

            case R.id.item_alocar:
                Fragment ListaAlocacoes = new ListaPedidosAlocacaoFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ListaAlocacoes).commit();
                setTitle(R.string.txt_alocar);
                return true;

            case R.id.item_reparar:
                Fragment ListaReparacoes = new ListaReparacoesFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ListaReparacoes).commit();
                setTitle(R.string.txt_reparar);
                return true;

            case R.id.item_user:
                Fragment meusDetalhes = new MeusDetalhesFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, meusDetalhes).commit();
                setTitle(getString(R.string.meus_dados));
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!isFuncionario)
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_top_menu, menu);
        }
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
                Toast.makeText(this, getString(R.string.txt_Sem_permissao_camara), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void abrirQRCodeReader()
    {
        Intent qrcode = new Intent(this, QRCodeReaderActivity.class);
        startActivityForResult(qrcode, ACT_QRCODE_READER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent)
    {
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
                                Toast.makeText(this, getString(R.string.txt_erro_id_item_invalido), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(this, getString(R.string.txt_erro_id_grupoItem_invalido), Toast.LENGTH_SHORT).show();
                            }
                            break;

                        default:
                            Toast.makeText(this, getString(R.string.txt_qrcode_invalido), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception exception)
                {
                    Toast.makeText(this, getString(R.string.txt_qrcode_invalido), Toast.LENGTH_SHORT).show();
                    System.out.println("ERRO: " + exception.getMessage());
                    exception.printStackTrace();
                }
            }
        }
    }

    public void BTN_Logout(View view)
    {
        Singleton.getInstance(this).pararMQTT();
        SharedPreferences preferences = getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);
        preferences.edit().putString(Helpers.USER_TOKEN, null).commit();
        Intent logout = new Intent(this, LoginActivity.class);
        startActivity(logout);
        finish();
    }

    @Override
    public void onMQTTMessageRecieved(String message)
    {
        try
        {
            JSONObject object = new JSONObject(message);
            if(!object.isNull("message"))
            {
                Toast.makeText(this, String.valueOf(object.getString("message")), Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "MQTT Inválido", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException exception)
        {
            exception.printStackTrace();
            Toast.makeText(this, "ERRO Mosquitto: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}