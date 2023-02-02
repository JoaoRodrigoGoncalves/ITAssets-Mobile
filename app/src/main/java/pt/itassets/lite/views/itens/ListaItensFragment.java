package pt.itassets.lite.views.itens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.itassets.lite.R;
import pt.itassets.lite.adapters.ListaItensAdaptador;
import pt.itassets.lite.listeners.ItensListener;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.Singleton;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ListaItensFragment extends Fragment implements ItensListener{

    private ListView lvItens;
    private FloatingActionButton fabListaItens;
    private SearchView searchView;
    public static final int ACTION_DETALHES = 1, ACTION_ADICIONAR = 1; //Ações

    public ListaItensFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_itens, container, false);

        setHasOptionsMenu(true);

        lvItens = view.findViewById(R.id.lvItens);
        fabListaItens = view.findViewById(R.id.fabListaItens);

        lvItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positions, long id) {
                Intent intent = new Intent(getContext(), DetalhesItemActivity.class);
                intent.putExtra("ID_ITEM", (int) id);
                startActivityForResult(intent, ACTION_DETALHES); //Método Deprecated
            }
        });

        Singleton.getInstance(getContext()).setItensListener((ItensListener) this);
        Singleton.getInstance(container.getContext()).getAllItensAPI(getContext());

        fabListaItens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AdicionarItemActivity.class);
                startActivityForResult(intent, ACTION_ADICIONAR); //Método Deprecated
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if(resultCode == Activity.RESULT_OK && requestCode == ACTION_DETALHES){
            Singleton.getInstance(getContext()).getAllItensAPI(getContext());
            Toast.makeText(getContext(), getString(R.string.txt_operacao_bem_sucedida), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_top_search, menu);
        MenuItem itemPesquisa = menu.findItem(R.id.pesquisarItem);
        searchView=(SearchView) itemPesquisa.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Item> listaItens = new ArrayList<>();
                for(Item i : Singleton.getInstance(getContext()).getItensBD()) {//Mudar
                    if (i.getNome().toLowerCase().contains(s.toLowerCase())){
                        listaItens.add(i);
                    }
                }
                lvItens.setAdapter(new ListaItensAdaptador(getContext(),listaItens));
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume(){
        // Quando faço Go Back fecho a searchView
        if(searchView!=null){
            searchView.onActionViewCollapsed();
        }
        super.onResume();
    }

    @Override
    public void onRefreshListaItens(ArrayList<Item> listaItens) {
        if(listaItens != null){
            lvItens.setAdapter(new ListaItensAdaptador(getContext(), listaItens));
        }
    }
}