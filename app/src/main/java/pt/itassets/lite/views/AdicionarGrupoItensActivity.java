package pt.itassets.lite.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import pt.itassets.lite.R;
import pt.itassets.lite.adapters.MyAdapter;
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
        setContentView(R.layout.activity_adicionar_grupo_itens);

        tiNome = findViewById(R.id.tiNome);
        tiNota = findViewById(R.id.tiNota);
        fabAdicionarGrupoItens = findViewById(R.id.fabAdicionarGrupoItens);
        recyclerView = findViewById(R.id.rv);
        Singleton.getInstance(getApplicationContext()).setOperacoesGruposListener(this);

        List<Item> items=Singleton.getInstance(this).getItensBD();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(this, items);
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
                                tiNota.getEditText().getText().toString().trim()
                        );
                        Singleton.getInstance(getBaseContext()).AdicionarGrupoItensAPI(grupoItensAux, getBaseContext());
                    }
                }
            }
        });
    }

    private boolean isGrupoItensValido(){
        String Nome = tiNome.getEditText().getText().toString().trim();

        if(Nome.length() < 1) {
            tiNome.setError("Erro: Insira o Nome do Item!");
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