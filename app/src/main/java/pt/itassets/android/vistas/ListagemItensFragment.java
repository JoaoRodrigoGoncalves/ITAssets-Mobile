package pt.itassets.android.vistas;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.itassets.android.R;
import pt.itassets.android.adptadores.ListaItensAdaptador;
import pt.itassets.android.listeners.ItensListener;
import pt.itassets.android.modelos.Item;
import pt.itassets.android.modelos.Singleton;

public class ListagemItensFragment extends Fragment implements ItensListener {
    private ListView lvItens;
    private FloatingActionButton fabLista;
    private SearchView searchView;
    public static final int ACT_DETALHES=1;

    public ListagemItensFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listagem_itens, container, false);

        setHasOptionsMenu(true);

        lvItens = view.findViewById(R.id.lvItens);
        fabLista=view.findViewById(R.id.fabLista);

        lvItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positions, long id) {
                Intent intent = new Intent(getContext(), DetalhesItemActivity.class);
                intent.putExtra("ID_ITEM", (int) id);
                startActivityForResult(intent, ACT_DETALHES);//Método Deprecated
            }
        });

        Singleton.getInstance(getContext()).setItensListener((ItensListener) this);
        Singleton.getInstance(container.getContext()).getAllItensAPI(getContext());

        fabLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetalhesItemActivity.class);
                startActivityForResult(intent, ACT_DETALHES);//Método Deprecated
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if(resultCode == Activity.RESULT_OK && requestCode == ACT_DETALHES){
            Singleton.getInstance(getContext()).getAllItensAPI(getContext());
            switch (intent.getIntExtra(MenuMainActivity.OPERACAO, 0)){
                case MenuMainActivity.ADD:
                    //Toast.makeText(getContext(), R.string.txt_item_adicionado_sucesso, Toast.LENGTH_SHORT).show();
                    break;
                case MenuMainActivity.EDIT:
                    //Toast.makeText(getContext(), R.string.txt_item_modificado_sucesso, Toast.LENGTH_SHORT).show();
                    break;
                case MenuMainActivity.DELETE:
                    //Toast.makeText(getContext(), R.string.txt_item_removido_sucesso, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pesquisa, menu);
        MenuItem itemPesquisa = menu.findItem(R.id.itemPesquisa);
        searchView=(SearchView) itemPesquisa.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Item> temLista = new ArrayList<>();
                for(Item i : Singleton.getInstance(getContext()).getItensBD()) {
                    if (i.getNome().toLowerCase().contains(s.toLowerCase())){
                        temLista.add(i);
                    }
                }
                lvItens.setAdapter(new ListaItensAdaptador(getContext(),temLista));
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
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
    public void onRefreachListaItens(ArrayList<Item> listaItens) {
        if(listaItens != null){
            lvItens.setAdapter(new ListaItensAdaptador(getContext(), listaItens));
        }
    }
}