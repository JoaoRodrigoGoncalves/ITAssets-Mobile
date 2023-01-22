package pt.itassets.lite.views.reparacao;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.itassets.lite.R;
import pt.itassets.lite.adapters.ListaPedidosReparacaoAdaptador;
import pt.itassets.lite.listeners.PedidosReparacaoListener;
import pt.itassets.lite.models.PedidoReparacao;
import pt.itassets.lite.models.Singleton;

public class ListaReparacoesFragment extends Fragment implements PedidosReparacaoListener {

    private ListView lvReparacoes;
    private FloatingActionButton fabListaPedidosReparacao;
    public static final int ACTION_DETALHES = 1, ACTION_ADICIONAR = 1; //Ações

    public ListaReparacoesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_reparacoes, container, false);
        setHasOptionsMenu(true);

        lvReparacoes = view.findViewById(R.id.lvReparacoes);
        fabListaPedidosReparacao = view.findViewById(R.id.fabListaPedidosReparacao);

        lvReparacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        Singleton.getInstance(getContext()).setPedidosReparacaoListener(this);
        Singleton.getInstance(getContext()).getUserReparacoesAPI(getContext());

        fabListaPedidosReparacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    @Override
    public void onRefreshListaReparacoes(ArrayList<PedidoReparacao> listaReparacoes) {
        if(listaReparacoes != null){
            lvReparacoes.setAdapter(new ListaPedidosReparacaoAdaptador(getContext(), listaReparacoes));
        }
    }
}