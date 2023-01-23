package pt.itassets.lite.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import pt.itassets.lite.R;
import pt.itassets.lite.listeners.OperacoesPedidoAlocacaoListener;
import pt.itassets.lite.models.GrupoItens;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;

public class AdicionarPedidoAlocacaoActivity extends AppCompatActivity implements OperacoesPedidoAlocacaoListener {
    private RadioGroup radioBTNGrupo;
    private TextInputLayout TI_Observacoes;
    private String selectedObject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_pedido_alocacao);
        setTitle(getString(R.string.txt_novo_pedido_alocacao));

        radioBTNGrupo = findViewById(R.id.radiogroup);
        TI_Observacoes = findViewById(R.id.TI_Observacoes);

        Singleton.getInstance(this).setOperacoesPedidoAlocacaoListener(this); //Subscrever o listener
        // Atualizar todos os itens e grupos
        Singleton.getInstance(this).getAllItensAPI(this);
        Singleton.getInstance(this).getAllGrupoItensAPI(this);

        radioBTNGrupo.setOrientation(LinearLayout.VERTICAL);

        ArrayList<Item> itens = Singleton.getInstance(this).getItensBD();
        ArrayList<GrupoItens> grupos = Singleton.getInstance(this).getGrupoItensBD();

        for (Item item:itens)
        {
            if(item.getPedido_alocacao_id() == null)
            {
                if(!item.isInGrupo(this))
                {
                    RadioButton rdbtn = new RadioButton(this);
                    rdbtn.setId(View.generateViewId());
                    rdbtn.setText(String.format(
                            "[Item] %s - %s",
                            String.valueOf(item.getNome()),
                            String.valueOf(item.getSerialNumber())
                    ));
                    rdbtn.setTag("ITEM_" + item.getId());
                    rdbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedObject = v.getTag().toString();
                        }
                    });
                    radioBTNGrupo.addView(rdbtn);
                }
            }
        }

        for (GrupoItens grupoItens:grupos)
        {
            if(grupoItens.getPedido_alocacao_id() == null)
            {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(View.generateViewId());
                rdbtn.setText(String.format(
                        "[Grupo] %s - %s",
                        String.valueOf(grupoItens.getNome()),
                        String.valueOf(grupoItens.getNotas())
                ));
                rdbtn.setTag("GRUPO_" + grupoItens.getId());
                rdbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedObject = v.getTag().toString();
                    }
                });
                radioBTNGrupo.addView(rdbtn);
            }
        }
    }

    public void BTN_Guardar(View view)
    {
        if(selectedObject != null)
        {
            String obs = TI_Observacoes.getEditText().getText().toString().trim();

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
                    Toast.makeText(this, "Objeto inválido", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onAlocacaoOperacaoRefresh(int operacao) {
        Intent intent = new Intent();
        intent.putExtra(Helpers.OPERACAO, operacao);
        setResult(RESULT_OK, intent);
        finish();
    }
}