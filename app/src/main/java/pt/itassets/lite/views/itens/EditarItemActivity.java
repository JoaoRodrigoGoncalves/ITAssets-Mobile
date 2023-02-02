package pt.itassets.lite.views.itens;

import static pt.itassets.lite.views.itens.ListaItensFragment.ACTION_DETALHES;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import pt.itassets.lite.R;
import pt.itassets.lite.listeners.OperacoesItensListener;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;
import pt.itassets.lite.views.MenuActivity;

public class EditarItemActivity extends AppCompatActivity  implements OperacoesItensListener {
    private Item item;
    private TextInputLayout tiNome, tiNumSerie, tiNota;
    private TextView tvTitulo;
    private FloatingActionButton fabGuardarAlteracoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Para adicionar o botão back, na actionBar
        setContentView(R.layout.activity_editar_item);

        tvTitulo = findViewById(R.id.tvTitulo);
        tiNome = findViewById(R.id.tiNome);
        tiNumSerie = findViewById(R.id.tiNSerie);
        tiNota = findViewById(R.id.tiNota);
        fabGuardarAlteracoes = findViewById(R.id.fabGuardarAlteracoesItem);
        Singleton.getInstance(getApplicationContext()).setOperacoesItensListener(this);

        Integer itemId = getIntent().getIntExtra("ID_ITEM", -1);

        if(itemId == -1)
        {
            Toast.makeText(this, getString(R.string.txt_item_nao_encontrado), Toast.LENGTH_SHORT).show();
            finish();
        }
        else {

            if(Helpers.isInternetConnectionAvailable(this))
            {
                item = Singleton.getInstance(getBaseContext()).getItem(itemId);

                if(item != null){
                    String titulo = String.format(getString(R.string.act_Titulo), item.getNome());
                    tvTitulo.setText(titulo);
                    tiNome.getEditText().setText(item.getNome());
                    tiNumSerie.getEditText().setText(item.getSerialNumber());
                    tiNota.getEditText().setText(item.getNotas());
                }

                fabGuardarAlteracoes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isItemValido()) {
                            if (item != null) {
                                setTitle(tiNome.getEditText().getText().toString().trim());
                                item.setNome(tiNome.getEditText().getText().toString().trim());
                                item.setSerialNumber(tiNumSerie.getEditText().getText().toString().trim());
                                item.setNotas(tiNota.getEditText().getText().toString().trim());
                                Singleton.getInstance(getApplicationContext()).EditarItemAPI(item, getApplicationContext());

                                Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                                startActivityForResult(intent, ACTION_DETALHES); //Método Deprecated
                            }
                        }
                    }
                });
            }
            else
            {
                // Sem internet
                Toast.makeText(this, R.string.txt_sem_internet, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
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