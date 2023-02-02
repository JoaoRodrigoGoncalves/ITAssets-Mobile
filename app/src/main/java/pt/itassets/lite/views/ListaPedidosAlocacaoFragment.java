package pt.itassets.lite.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.itassets.lite.R;
import pt.itassets.lite.adapters.ListaPedidosAlocacaoAdaptador;
import pt.itassets.lite.listeners.PedidosAlocacaoListener;
import pt.itassets.lite.models.PedidoAlocacao;
import pt.itassets.lite.models.Singleton;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ListaPedidosAlocacaoFragment extends Fragment implements PedidosAlocacaoListener {

    private ListView lvAlocacoes;
    private FloatingActionButton fabListaPedidosAlocacao;
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
                startActivityForResult(detalhes, ACTION_DETALHES);
            }
        });

        Singleton.getInstance(getContext()).setPedidosAlocacaoListener(this);
        Singleton.getInstance(getContext()).getUserAlocacoesAPI(getContext());

        fabListaPedidosAlocacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adicionar = new Intent(getContext(), AdicionarPedidoAlocacaoActivity.class);
                startActivityForResult(adicionar, ACTION_ADICIONAR);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if(resultCode == Activity.RESULT_OK && requestCode == ACTION_ADICIONAR){
            Singleton.getInstance(getContext()).getUserAlocacoesAPI(getContext());
            Toast.makeText(getContext(), getString(R.string.txt_operacao_bem_sucedida), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefreshListaAlocacoes(ArrayList<PedidoAlocacao> listaAlocacoes) {
        if(listaAlocacoes != null){
            lvAlocacoes.setAdapter(new ListaPedidosAlocacaoAdaptador(getContext(), listaAlocacoes));
        }
    }
}