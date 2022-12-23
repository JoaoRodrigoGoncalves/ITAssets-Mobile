package pt.itassets.lite.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

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
    public static final int ACTION_DETALHES = 1; //Ações

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
                //TODO: IMPLMENTAr
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
                //TODO IMPLEMENTAR
                Intent intent = new Intent(getContext(), DetalhesItemActivity.class);
                startActivityForResult(intent, ACTION_DETALHES);//Método Deprecated
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if(resultCode == Activity.RESULT_OK && requestCode == ACTION_DETALHES){
            Singleton.getInstance(getContext()).getAllItensAPI(getContext());
            Toast.makeText(getContext(), "Operação bem sucedida!", Toast.LENGTH_SHORT).show();
            //TODO: Talvez apagar
//            switch (intent.getIntExtra(MenuMainActivity.OPERACAO, 0)){
//                case MenuMainActivity.ADD:
//                    //Toast.makeText(getContext(), R.string.txt_item_adicionado_sucesso, Toast.LENGTH_SHORT).show();
//                    break;
//                case MenuMainActivity.EDIT:
//                    //Toast.makeText(getContext(), R.string.txt_item_modificado_sucesso, Toast.LENGTH_SHORT).show();
//                    break;
//                case MenuMainActivity.DELETE:
//                    //Toast.makeText(getContext(), R.string.txt_item_removido_sucesso, Toast.LENGTH_SHORT).show();
//                    break;
//            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //TODO: Possivelmente não vou implementar
//        inflater.inflate(R.menu.menu_pesquisa, menu);
//        MenuItem itemPesquisa = menu.findItem(R.id.itemPesquisa);
//        searchView=(SearchView) itemPesquisa.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                ArrayList<Item> temLista = new ArrayList<>();
//                for(Item i : Singleton.getInstance(getContext()).getItensBD()) {
//                    if (i.getNome().toLowerCase().contains(s.toLowerCase())){
//                        temLista.add(i);
//                    }
//                }
//                lvItens.setAdapter(new ListaItensAdaptador(getContext(),temLista));
//                return true;
//            }
//        });
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
    public void onRefreshListaItens(ArrayList<Item> listaItens) {
        if(listaItens != null){
            lvItens.setAdapter(new ListaItensAdaptador(getContext(), listaItens));
        }
    }
}