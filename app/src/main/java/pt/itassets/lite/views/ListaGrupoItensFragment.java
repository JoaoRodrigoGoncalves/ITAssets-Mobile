package pt.itassets.lite.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.itassets.lite.R;
import pt.itassets.lite.adapters.ListaGruposItensAdaptador;
import pt.itassets.lite.listeners.GrupoItensListener;
import pt.itassets.lite.models.GrupoItens;
import pt.itassets.lite.models.Singleton;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ListaGrupoItensFragment extends Fragment implements GrupoItensListener {

    private ListView lvGruposItens;
    private FloatingActionButton fabListaGruposItens;
    private SearchView searchView;
    public static final int ACTION_DETALHES = 1, ACTION_ADICIONAR = 1; //Ações

    public ListaGrupoItensFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment9
        View view = inflater.inflate(R.layout.fragment_lista_grupos_itens, container, false);

        setHasOptionsMenu(true);

        lvGruposItens = view.findViewById(R.id.lvGruposItens);
        fabListaGruposItens = view.findViewById(R.id.fabListaGruposItens);

        lvGruposItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positions, long id) {
                //Intent intent = new Intent(getContext(), DetalhesItemActivity.class);
                //intent.putExtra("ID_ITEM", (int) id);
                //startActivityForResult(intent, ACTION_DETALHES); //Método Deprecated
            }
        });

        Singleton.getInstance(getContext()).setGrupoItensListener((GrupoItensListener) this);
        Singleton.getInstance(container.getContext()).getAllGrupoItensAPI(getContext());

        fabListaGruposItens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AdicionarGrupoItensActivity.class);
                startActivityForResult(intent, ACTION_DETALHES); //Método Deprecated
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if(resultCode == Activity.RESULT_OK && requestCode == ACTION_DETALHES){
            Singleton.getInstance(getContext()).getAllItensAPI(getContext());
            Toast.makeText(getContext(), "Operação bem sucedida!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onResume() {
        // Quando se faz Go Back a searchView fecha-se
        if(searchView!=null){
            searchView.onActionViewCollapsed();
        }
        super.onResume();
    }

    @Override
    public void onRefreshListaGrupoItens(ArrayList<GrupoItens> listaGrupoItens) {
        if(listaGrupoItens != null){
            lvGruposItens.setAdapter(new ListaGruposItensAdaptador(getContext(), listaGrupoItens));
        }
    }
}