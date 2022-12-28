package pt.itassets.lite.views;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import pt.itassets.lite.R;
import pt.itassets.lite.models.GrupoItens;
import pt.itassets.lite.models.Singleton;

public class AdicionarGrupoItensActivity extends AppCompatActivity {

    private GrupoItens grupoItens;
    private TextInputLayout tiNome, tiNota;
    private FloatingActionButton fabAdicionarGrupoItens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Para adicionar o botão back, na actionBar
        setContentView(R.layout.activity_adicionar_grupo_itens);

        tiNome = findViewById(R.id.tiNome);
        tiNota = findViewById(R.id.tiNota);
        fabAdicionarGrupoItens = findViewById(R.id.fabAdicionarGrupoItens);

        fabAdicionarGrupoItens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isItemValido()){
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

    private boolean isItemValido(){
        String Nome = tiNome.getEditText().getText().toString().trim();

        if(Nome.length() < 1) {
            tiNome.setError("Erro: Insira o Nome do Item!");
            return false;
        }
        return true;
    }
}