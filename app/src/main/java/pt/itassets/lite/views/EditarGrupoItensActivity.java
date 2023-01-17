package pt.itassets.lite.views;


import static pt.itassets.lite.views.ListaItensFragment.ACTION_DETALHES;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import pt.itassets.lite.R;
import pt.itassets.lite.models.GrupoItens;
import pt.itassets.lite.models.Singleton;

public class EditarGrupoItensActivity extends AppCompatActivity {

    private TextInputLayout tiNomeGrupo, tiNotas;
    private GrupoItens grupoItens;
    private FloatingActionButton fabEditarGrupoItens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Para adicionar o botão back, na actionBar
        setContentView(R.layout.activity_editar_grupo_itens);


        tiNomeGrupo = findViewById(R.id.tiGrupo);
        tiNotas=findViewById(R.id.tiNotas);
        fabEditarGrupoItens=findViewById(R.id.fabEditarGrupoItens);

        Integer grupoId = getIntent().getIntExtra("ID_GRUPO", -1);

        if(grupoId == -1)
        {
            Toast.makeText(this, "Item não encontrado!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            grupoItens = Singleton.getInstance(getBaseContext()).getGrupoItens(grupoId);

            if(grupoItens != null){

                tiNomeGrupo.getEditText().setText(grupoItens.getNome());
                if(grupoItens.getNotas()!=null)
                {
                    tiNotas.getEditText().setText(grupoItens.getNotas());
                }
                else
                {
                    tiNotas.getEditText().setText("");
                }
            }

            fabEditarGrupoItens.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((isGrupoItensValido())) {
                        if (grupoItens != null) {

                            grupoItens.setNome(tiNomeGrupo.getEditText().getText().toString().trim());
                            grupoItens.setNotas(tiNotas.getEditText().getText().toString().trim());

                            Singleton.getInstance(getApplicationContext()).EditarGrupoItensAPI(grupoItens, getApplicationContext());
                            Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                            startActivityForResult(intent, ACTION_DETALHES); //Método Deprecated
                        }
                    }
                }
            });
        }
    }

    private boolean isGrupoItensValido(){
        String Nome = tiNomeGrupo.getEditText().getText().toString().trim();

        if(Nome.length() < 1) {
            tiNomeGrupo.setError("Erro: Insira o Nome do Grupo de Itens!");
            return false;
        }
        return true;
    }

}