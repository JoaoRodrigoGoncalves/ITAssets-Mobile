package pt.itassets.lite.views;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import pt.itassets.lite.R;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.Singleton;

public class AdicionarItemActivity extends AppCompatActivity {
    private Item item;
    private TextInputLayout tiNome, tiNumSerie, tiNota;
    private FloatingActionButton fabAdicionarItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Para adicionar o botão back, na actionBar
        setContentView(R.layout.activity_adicionar_item);

        tiNome = findViewById(R.id.tiNome);
        tiNumSerie = findViewById(R.id.tiNSerie);
        tiNota = findViewById(R.id.tiNota);
        fabAdicionarItem = findViewById(R.id.fabAdicionarItem);

        fabAdicionarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isItemValido()){
                    if(item == null){
                        Item itemAux = new Item(0, tiNome.getEditText().getText().toString().trim(), tiNumSerie.getEditText().getText().toString().trim(), tiNota.getEditText().getText().toString().trim(), null, null, null);
                        Singleton.getInstance(getApplicationContext()).AdicionarItemAPI(itemAux, getApplicationContext());

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