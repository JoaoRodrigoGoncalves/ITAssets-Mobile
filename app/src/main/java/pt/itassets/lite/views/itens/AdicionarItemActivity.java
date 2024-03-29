package pt.itassets.lite.views.itens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import pt.itassets.lite.R;
import pt.itassets.lite.listeners.OperacoesItensListener;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;

public class AdicionarItemActivity extends AppCompatActivity implements OperacoesItensListener {
    private Item item;
    private TextInputLayout tiNome, tiNumSerie, tiNota;
    private FloatingActionButton fabAdicionarItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Para adicionar o botão back, na actionBar
        setTitle(R.string.txt_adicionar_item);
        setContentView(R.layout.activity_adicionar_item);

        if(!Helpers.isInternetConnectionAvailable(this))
        {
            Toast.makeText(this, R.string.txt_sem_internet, Toast.LENGTH_SHORT).show();
            finish();
        }

        tiNome = findViewById(R.id.tiNome);
        tiNumSerie = findViewById(R.id.tiNSerie);
        tiNota = findViewById(R.id.tiNota);
        fabAdicionarItem = findViewById(R.id.fabGuardarAlteracoesItem);
        Singleton.getInstance(getApplicationContext()).setOperacoesItensListener(this);

        fabAdicionarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isItemValido()){
                    if(item == null){
                        Item itemAux = new Item(
                                0,
                                tiNome.getEditText().getText().toString().trim(),
                                tiNumSerie.getEditText().getText().toString().trim(),
                                tiNota.getEditText().getText().toString().trim(),
                                null,
                                null,
                                null,
                                null,
                                null
                        );
                        Singleton.getInstance(getBaseContext()).AdicionarItemAPI(itemAux, getBaseContext());
                    }
                }
            }
        });
    }

    private boolean isItemValido(){
        String Nome = tiNome.getEditText().getText().toString().trim();

        if(Nome.length() < 1) {
            tiNome.setError(getString(R.string.txt_insira_nome_item));
            return false;
        }
        return true;
    }

    @Override
    public void onItemOperacaoRefresh(int operacao) {
        Intent intent = new Intent();
        intent.putExtra(Helpers.OPERACAO, operacao);
        setResult(RESULT_OK, intent);
        finish();
    }
}