package pt.itassets.android.vistas;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pt.itassets.android.R;
import pt.itassets.android.listeners.DetalhesItensListener;
import pt.itassets.android.modelos.Item;
import pt.itassets.android.modelos.Singleton;

public class DetalhesItemActivity extends AppCompatActivity implements DetalhesItensListener {
    private Item item;
    private EditText etNome, etSerialNumber, etCategoria, etNotas;
    private TextView txtTitulo;
    private FloatingActionButton fabGuardar;
    public static final int MAX_CHAR_NOME = 50, MAX_CHAR_NOTAS = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_item);

        int id = getIntent().getIntExtra("ID_ITEM", 0);
        item = Singleton.getInstance(getApplicationContext()).getItem(id);

        txtTitulo = findViewById(R.id.txtTitulo);
        etNome = findViewById(R.id.etNome);
        etSerialNumber = findViewById(R.id.etSerialNumber);
        etCategoria = findViewById(R.id.etCategoria);
        etNotas = findViewById(R.id.etNotas);
        fabGuardar = findViewById(R.id.floatingActionButton);
        Singleton.getInstance(getApplicationContext()).setDetalhesItensListener(this);

        if(item!=null){
            carregarItem();
            fabGuardar.setImageResource(R.drawable.ic_action_guardar);
        }else{
            setTitle(getString(R.string.adicionar_item_name));
            txtTitulo.setText(R.string.txt_registar);
            fabGuardar.setImageResource(R.drawable.ic_action_adicionar);
        }

        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isItemValido()){
                    if(item != null){
                        item.setNome(etNome.getText().toString());
                        item.setSerialNumber(etSerialNumber.getText().toString());
                        item.setCategoria_id(etCategoria.getText().toString());
                        item.setNotas(etNotas.getText().toString());
                        Singleton.getInstance(getApplicationContext()).editarItemAPI(item, getApplicationContext());
                    }else{
                        Item itemAux = new Item(0, 10, etSerialNumber.getText().toString(), etCategoria.getText().toString(), etNotas.getText().toString(), etNome.getText().toString());
                        Singleton.getInstance(getApplicationContext()).adicionarItemAPI(itemAux, getApplicationContext());
                    }
                }
            }
        });
    }

    private boolean isItemValido(){
        String Nome =  etNome.getText().toString();
        String Notas = etNotas.getText().toString();
        if(Nome.length() >  MAX_CHAR_NOME){
            etNome.setError(getString(R.string.Erro_Caracteres_Max));
            return false;
        }
        if(Notas.length() > MAX_CHAR_NOTAS){
            etNotas.setError(getString(R.string.Erro_Caracteres_Max));
            return false;
        }
        return true;
    }

    private void carregarItem(){
        Resources res = getResources();
        String Nome = String.format(res.getString(R.string.act_Titulo), item.getNome());
        setTitle(Nome);
        etNome.setText(item.getNome());
        etSerialNumber.setText(item.getSerialNumber());
        etCategoria.setText(item.getCategoria_id());
        etNotas.setText(item.getNotas());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(item!=null){
            getMenuInflater().inflate(R.menu.menu_detalhes_item, menu);
            return super.onCreateOptionsMenu(menu);
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemRemover:
                dialogRemover();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogRemover() {
        new MaterialAlertDialogBuilder(getApplicationContext())
                .setIcon(R.drawable.ic_action_remover)
                .setTitle(R.string.txt_TituloDialogRemover)
                .setMessage(R.string.txt_verificaçãoDialogRemover)
                .setNegativeButton(getString(R.string.txt_nao), (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(getString(R.string.txt_sim), ((dialogInterface, i) ->
                        Singleton.getInstance(getApplicationContext()).removerItemAPI(item, getApplicationContext())
                )).show();
    }


    @Override
    public void onRefreshDetalhesItens(int operacao) {
        Intent intent = new Intent();
        intent.putExtra(MenuMainActivity.OPERACAO, operacao);
        setResult(RESULT_OK, intent);
        finish();
    }
}