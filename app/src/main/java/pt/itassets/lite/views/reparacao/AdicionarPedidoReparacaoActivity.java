package pt.itassets.lite.views.reparacao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import pt.itassets.lite.R;
import pt.itassets.lite.adapters.ListaGrupoItensAdaptador;
import pt.itassets.lite.listeners.OperacoesGruposListener;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;

public class AdicionarPedidoReparacaoActivity extends AppCompatActivity implements OperacoesGruposListener {

    private Integer id_user;
    private TextInputLayout tiProblema;
    private Button btn_guadar;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_pedido_reparacao);
        SharedPreferences preferences = this.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);
        tiProblema = findViewById(R.id.tiProblema);
        btn_guadar=findViewById(R.id.guardar);

        recyclerView = findViewById(R.id.rv);
        Singleton.getInstance(getApplicationContext()).setOperacoesGruposListener(this);

        ArrayList<Item> items = Singleton.getInstance(this).getItensAlocados(preferences.getInt(Helpers.USER_ID, -1));



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListaGrupoItensAdaptador adapter = new ListaGrupoItensAdaptador(this, items);
        recyclerView.setAdapter(adapter);

        
        ArrayList<Item> itemSelected = adapter.getArrayitems();

    }

    @Override
    public void onGrupoOperacoesRefresh(int operacao) {

    }

    /*public void BTN_Guardar_Reparacao()
    {
        if(selectedObject != null)
        {
            String obs = tiProblema.getEditText().getText().toString().trim();

            SharedPreferences preferences = getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);
            ArrayList<String> part = new ArrayList<>(Arrays.asList(selectedObject.split("_")));
            Map<String, String> jsonBody = new HashMap<>();

            jsonBody.put("requerente_id", String.valueOf(preferences.getInt(Helpers.USER_ID, -1)));

            if(obs.length() > 1)
            {
                jsonBody.put("obs", obs);
            }

            switch (part.get(0))
            {
                case "ITEM":
                    jsonBody.put("item_id", String.valueOf(part.get(1)));
                    break;

                case "GRUPO":
                    jsonBody.put("grupoItem_id", String.valueOf(part.get(1)));
                    break;

                default:
                    Toast.makeText(this, getString(R.string.txt_objeto_invalido), Toast.LENGTH_SHORT).show();
                    System.out.println("[ERRO] Objeto inválido: " + Arrays.toString(part.toArray()));
                    break;
            }

            Singleton.getInstance(this).createPedidoAlocacao(this, new JSONObject(jsonBody));
        }
        else
        {
            // Erro selecionar objeto
            Toast.makeText(this, getString(R.string.txt_selecionar_item_grupo), Toast.LENGTH_SHORT).show();
        }
    }*/
}