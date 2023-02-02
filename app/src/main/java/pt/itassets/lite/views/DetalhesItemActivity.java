package pt.itassets.lite.views;

import static pt.itassets.lite.views.ListaItensFragment.ACTION_DETALHES;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pt.itassets.lite.R;
import pt.itassets.lite.listeners.OperacoesItensListener;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.models.Site;
import pt.itassets.lite.utils.Helpers;

public class DetalhesItemActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView tv_nome, tv_serialNumber, tv_categoria, tv_notas;
    private Item item;
    private ImageView img_no_site;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Para adicionar o botão back, na actionBar
        setContentView(R.layout.activity_detalhes_item);


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        tv_nome = findViewById(R.id.tv_item_nome);
        tv_serialNumber = findViewById(R.id.tv_item_serialNumber);
        tv_categoria = findViewById(R.id.tv_item_categoria);
        tv_notas = findViewById(R.id.tv_item_notas);
        img_no_site = findViewById(R.id.img_no_site);

        Integer item_id = getIntent().getIntExtra("ID_ITEM", -1);

        if(item_id == -1)
        {
            Toast.makeText(this, "Item não encontrado!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            item = Singleton.getInstance(getBaseContext()).getItem(item_id);

            tv_nome.setText(String.valueOf(item.getNome()));
            setTitle(String.valueOf(item.getNome()));

            tv_serialNumber.setText(String.valueOf(item.getSerialNumber()));

            if(item.getNome_Categoria() == null)
            {
                tv_categoria.setText(R.string.txt_nao_aplicavel);
                tv_categoria.setTypeface(tv_categoria.getTypeface(), Typeface.ITALIC);
            }
            else
            {
                tv_categoria.setText(String.valueOf(item.getNome_Categoria()));
            }

            if(item.getNotas() == null)
            {
                tv_notas.setText(R.string.txt_nao_aplicavel);
                tv_notas.setTypeface(tv_notas.getTypeface(), Typeface.ITALIC);
            }
            else
            {
                tv_notas.setText(String.valueOf(item.getNotas()));
            }

            if(item.getSite_id() == null)
            {
                mapFragment.getView().setVisibility(View.GONE);
                img_no_site.setVisibility(View.VISIBLE);
            }
        }
    }

    //Icons no menu superior (edit e remove)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(item!=null){
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
                intent.putExtra("ID_ITEM", item.getId());
                startActivityForResult(intent, ACTION_DETALHES); //Método Deprecated
                return true;

            case R.id.remove:
                if(Helpers.isInternetConnectionAvailable(this))
                {
                    dialogRemover();
                }
                else
                {
                    Toast.makeText(this, R.string.txt_sem_internet, Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(i);
    }

    //Dialog para perguntar se o user pretende mesmo eliminar/ desativar o item
    private void dialogRemover(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.txt_remover_item))
                .setMessage(R.string.txt_confirm_remover_item)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Singleton.getInstance(getApplicationContext()).RemoverItemAPI(item, getApplicationContext());
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if(item.getSite_id() != null)
        {
            Site site = Singleton.getInstance(this).getSiteDB(item.getSite_id());

            String[] coords = site.getCoordenadas().split(", "); // Sim, é virgula e espaço

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])))
                    .title(String.valueOf(item.getNome())));

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])), 15));

        }
    }
}