package pt.itassets.lite.views.grupos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import pt.itassets.lite.R;
import pt.itassets.lite.adapters.ListaItensAdaptadorRV;
import pt.itassets.lite.listeners.OperacoesGruposListener;
import pt.itassets.lite.models.GrupoItens;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;

public class AdicionarGrupoItensActivity extends AppCompatActivity implements OperacoesGruposListener {

    private GrupoItens grupoItens;
    private TextInputLayout tiNome, tiNota;
    private FloatingActionButton fabAdicionarGrupoItens;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Para adicionar o botão back, na actionBar
        setTitle(R.string.adicionar_grupo_de_itens);
        setContentView(R.layout.activity_adicionar_grupo_itens);

        tiNome = findViewById(R.id.tiNome);
        tiNota = findViewById(R.id.tiNota);
        fabAdicionarGrupoItens = findViewById(R.id.fabAdicionarGrupoItens);
        recyclerView = findViewById(R.id.rv);
        Singleton.getInstance(getApplicationContext()).setOperacoesGruposListener(this);

        ArrayList<Item> items = Singleton.getInstance(this).getItensSemGrupoItem();


        if(Helpers.isInternetConnectionAvailable(this))
        {
            if(items.size() < 1)
            {
                Toast.makeText(this, R.string.sem_itens_para_grupo, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else
        {
            Toast.makeText(this, R.string.txt_sem_internet, Toast.LENGTH_SHORT).show();
            finish();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListaItensAdaptadorRV adapter = new ListaItensAdaptadorRV(this, items);
        recyclerView.setAdapter(adapter);

        fabAdicionarGrupoItens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isGrupoItensValido()){
                    if(grupoItens == null){
                        GrupoItens grupoItensAux = new GrupoItens(
                                0,
                                null,
                                tiNome.getEditText().getText().toString().trim(),
                                tiNota.getEditText().getText().toString().trim(),
                                null,
                                null
                        );

                        ArrayList<Integer> itemSelected = adapter.getArrayitems();
                        if(itemSelected != null)
                        {
                            if(itemSelected.size() > 0)
                            {
                                Singleton.getInstance(getBaseContext()).AdicionarGrupoItensAPI(grupoItensAux,itemSelected, getBaseContext());
                                return;
                            }
                        }
                        Toast.makeText(AdicionarGrupoItensActivity.this, R.string.txt_select_item_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean isGrupoItensValido(){
        String Nome = tiNome.getEditText().getText().toString().trim();

        if(Nome.length() < 1) {
            tiNome.setError(getString(R.string.txt_insira_nome_item));
            return false;
        }
        return true;
    }

    @Override
    public void onGrupoOperacoesRefresh(int operacao)
    {
        Intent intent = new Intent();
        intent.putExtra(Helpers.OPERACAO, operacao);
        setResult(RESULT_OK, intent);
        finish();
    }
}