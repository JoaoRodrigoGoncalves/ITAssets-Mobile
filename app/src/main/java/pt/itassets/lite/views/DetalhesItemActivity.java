package pt.itassets.lite.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pt.itassets.lite.R;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.models.Site;

public class DetalhesItemActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView tv_nome, tv_serialNumber, tv_categoria, tv_notas;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_item);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tv_nome = findViewById(R.id.tv_item_nome);
        tv_serialNumber = findViewById(R.id.tv_item_serialNumber);
        tv_categoria = findViewById(R.id.tv_item_categoria);
        tv_notas = findViewById(R.id.tv_item_notas);

        Integer itemId = getIntent().getIntExtra("ID_ITEM", -1);

        if(itemId == -1)
        {
            Toast.makeText(this, "Item não encontrado!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            item = Singleton.getInstance(getBaseContext()).getItem(itemId);

            tv_nome.setText(String.valueOf(item.getNome()));
            tv_serialNumber.setText(String.valueOf(item.getSerialNumber()));

            if(item.getNome_Categoria() == null)
            {
                tv_categoria.setText("Não Aplicável");
                tv_categoria.setTypeface(tv_categoria.getTypeface(), Typeface.ITALIC);
            }
            else
            {
                tv_categoria.setText(String.valueOf(item.getNome_Categoria()));
            }

            if(item.getNotas() == "null") //Sim, texto
            {
                tv_notas.setText("Não Aplicável");
                tv_notas.setTypeface(tv_notas.getTypeface(), Typeface.ITALIC);
            }
            else
            {
                tv_notas.setText(String.valueOf(item.getNotas()));
            }
        }
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
        //TODO: Não tem local associado. Fazer outra coisa

    }
}