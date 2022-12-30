package pt.itassets.lite.views;

import static pt.itassets.lite.views.ListaItensFragment.ACTION_DETALHES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pt.itassets.lite.R;
import pt.itassets.lite.adapters.ListaItensAdaptador;
import pt.itassets.lite.models.GrupoItens;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.Singleton;

public class DetalhesGrupoActivity extends AppCompatActivity {

    private TextView tv_nome_grupo, tv_notas;
    private GrupoItens grupoItens;
    private ListView lvItens;
    private ArrayList<Item> item_Grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Para adicionar o botão back, na actionBar
        setContentView(R.layout.activity_detalhes_grupo);

        tv_nome_grupo = findViewById(R.id.tv_nome_Grupo);
        tv_notas=findViewById(R.id.tv_notas);
        lvItens=findViewById(R.id.lvGruposItens);


        Integer grupoId = getIntent().getIntExtra("ID_GRUPO", -1);

        if(grupoId == -1)
        {
            Toast.makeText(this, "Item não encontrado!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            grupoItens = Singleton.getInstance(getBaseContext()).getGrupoItens(grupoId);

            tv_nome_grupo.setText(String.valueOf(grupoItens.getNome()));
            setTitle(String.valueOf(grupoItens.getNome()));

            if(grupoItens.getNotas() == "null") //Sim, texto
            {
                tv_notas.setText("Não Aplicável");
                tv_notas.setTypeface(tv_notas.getTypeface(), Typeface.ITALIC);
            }
            else
            {
                tv_notas.setText(String.valueOf(grupoItens.getNotas()));
            }
        }

        //TODO: Fazer os itens aparecer na view
        /*ArrayList<Item> item = Singleton.getInstance(this).getItensBD();

        for (Item i:item) {
            if(grupoItens.getNome().equals(i.getNome_Categoria()))
            {
                item_Grupo.addAll(item);
            }
        }

        lvItens.setAdapter(new ListaItensAdaptador(this, item_Categoria));

        System.out.println(grupoItens);*/



        //vItens.setAdapter();
    }

    //Icons no menu superior (edit e remove)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(grupoItens!=null){
            getMenuInflater().inflate(R.menu.main_top_edit,menu);
            getMenuInflater().inflate(R.menu.main_top_remove,menu);
            return super.onCreateOptionsMenu(menu);
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem i) {
        switch (i.getItemId()){
            case R.id.edit:
                Intent intent = new Intent(getBaseContext(), EditarItemActivity.class);
                intent.putExtra("ID_GRUPO", grupoItens.getId());
                startActivityForResult(intent, ACTION_DETALHES); //Método Deprecated
                return true;

            case R.id.remove:
                dialogRemover();
                return true;
        }
        return super.onOptionsItemSelected(i);
    }

    //Dialog para perguntar se o user pretende mesmo eliminar/ desativar o item
    private void dialogRemover(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remover Item")
                .setMessage("Tem a certeza que deseja remover o item?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Singleton.getInstance(getApplicationContext()).RemoverGrupoItemAPI(grupoItens, getApplicationContext());
                        Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                        startActivityForResult(intent, ACTION_DETALHES); //Método Deprecated
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Não necessita de se inserir nada
                    }
                })
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }
}