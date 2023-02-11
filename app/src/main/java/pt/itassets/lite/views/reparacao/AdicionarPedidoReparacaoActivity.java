package pt.itassets.lite.views.reparacao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.itassets.lite.R;
import pt.itassets.lite.adapters.ListaGrupoItensAdaptorRV;
import pt.itassets.lite.adapters.ListaItensAdaptadorRV;
import pt.itassets.lite.listeners.OperacoesGruposListener;
import pt.itassets.lite.models.GrupoItens;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;

public class AdicionarPedidoReparacaoActivity extends AppCompatActivity implements OperacoesGruposListener {

    private Integer id_user;
    private TextInputLayout tiProblema;
    private FloatingActionButton fab_PedidoReparacao;
    RecyclerView recyclerView;
    ArrayList<Item> itemSelected;
    ArrayList<GrupoItens> grupoItensSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Para adicionar o botão back, na actionBar
        setTitle(R.string.adicionar_pedido_reparacao);
        setContentView(R.layout.activity_adicionar_pedido_reparacao);
        SharedPreferences preferences = this.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);
        tiProblema = findViewById(R.id.tiProblema);
        fab_PedidoReparacao =findViewById(R.id.fabAdicionarPedidoReparacao);

        if(!Helpers.isInternetConnectionAvailable(this))
        {
            Toast.makeText(this, R.string.txt_sem_internet, Toast.LENGTH_SHORT).show();
            finish();
        }

        Singleton.getInstance(getApplicationContext()).setOperacoesGruposListener(this);

        // Atualizar todos os dados
        Singleton.getInstance(this).getAllItensAPI(this);
        Singleton.getInstance(this).getAllGrupoItensAPI(this);
        Singleton.getInstance(this).getUserAlocacoesAPI(this);
        Singleton.getInstance(this).getUserReparacoesAPI(this);

        //region Lista do Item
        recyclerView = findViewById(R.id.rv_Itens);
        ArrayList<Item> items = Singleton.getInstance(this).getItensAlocados(preferences.getInt(Helpers.USER_ID, -1));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListaItensAdaptadorRV adapter_item = new ListaItensAdaptadorRV(this, items);
        recyclerView.setAdapter(adapter_item);
        //endregion

        //region Lista do Grupo de Itens
        recyclerView = findViewById(R.id.rv_Grupo);
        ArrayList<GrupoItens> grupoItens = Singleton.getInstance(this).getGrupoItensAlocados(preferences.getInt(Helpers.USER_ID, -1));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListaGrupoItensAdaptorRV adaptador_grupoitens = new ListaGrupoItensAdaptorRV(this, grupoItens);
        recyclerView.setAdapter(adaptador_grupoitens);
        //endregion

        fab_PedidoReparacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSelected = adapter_item.getArrayitems();
                grupoItensSelected=adaptador_grupoitens.getArrayGrupo();

                if(itemSelected != null || grupoItensSelected != null)
                {
                    String obs = tiProblema.getEditText().getText().toString().trim();

                    SharedPreferences preferences = getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

                    Map<String, Object> jsonBody = new HashMap<>();

                    jsonBody.put("requerente_id", String.valueOf(preferences.getInt(Helpers.USER_ID, -1)));

                    if(obs.length() > 1)
                    {
                        jsonBody.put("descricaoProblema", obs);
                        if (itemSelected!=null)
                        {
                            jsonBody.put("items", itemSelected);
                        }

                        if (grupoItensSelected!=null)
                        {
                            jsonBody.put("grupoItens",grupoItensSelected);
                        }
                        Singleton.getInstance(getApplicationContext()).createPedidoReparacao(getApplicationContext(), new JSONObject(jsonBody));
                    }
                    else
                    {
                    // Erro selecionar objeto
                    Toast.makeText(getApplicationContext(), getString(R.string.error_problema), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    // Erro selecionar objeto
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_selecionar_item_grupo), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onGrupoOperacoesRefresh(int operacao) {
        Intent intent = new Intent();
        intent.putExtra(Helpers.OPERACAO, operacao);
        setResult(RESULT_OK, intent);
        finish();
    }
}