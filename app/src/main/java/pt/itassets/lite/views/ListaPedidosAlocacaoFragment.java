package pt.itassets.lite.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.itassets.lite.R;
import pt.itassets.lite.adapters.ListaPedidosAlocacaoAdaptador;
import pt.itassets.lite.listeners.PedidosAlcoacaoListener;
import pt.itassets.lite.models.Alocacao;
import pt.itassets.lite.models.Singleton;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ListaPedidosAlocacaoFragment extends Fragment implements PedidosAlcoacaoListener{

    private ListView lvAlocacoes;
    private FloatingActionButton fabListaPedidosAlocacao;
    private SearchView searchView;
    public static final int ACTION_DETALHES = 1, ACTION_ADICIONAR = 1; //Ações

    public ListaPedidosAlocacaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_pedidos_alocacao, container, false);
        setHasOptionsMenu(true);

        lvAlocacoes = view.findViewById(R.id.lvAlocacoes);
        fabListaPedidosAlocacao = view.findViewById(R.id.fabListaPedidosAlocacao);

        lvAlocacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positions, long id) {
                Intent detalhes = new Intent(getContext(), DetalhesPedidoAlocacaoActivity.class);
                detalhes.putExtra("ID_PEDIDO", (int) id);
                startActivity(detalhes);
            }
        });

        Singleton.getInstance(getContext()).setPedidosAlcoacaoListener(this);
        Singleton.getInstance(container.getContext()).getUserAlocacoesAPI(getContext());

        fabListaPedidosAlocacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Implementar criar pedido
            }
        });

        return view;
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
    public void onRefreshListaAlocacoes(ArrayList<Alocacao> listaAlocacoes) {
        if(listaAlocacoes != null){
            lvAlocacoes.setAdapter(new ListaPedidosAlocacaoAdaptador(getContext(), listaAlocacoes));
        }
    }
}