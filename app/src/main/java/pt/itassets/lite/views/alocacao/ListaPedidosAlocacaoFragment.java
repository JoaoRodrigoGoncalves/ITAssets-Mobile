package pt.itassets.lite.views.alocacao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import pt.itassets.lite.R;
import pt.itassets.lite.adapters.ListaPedidosAlocacaoAdaptador;
import pt.itassets.lite.listeners.PedidosAlocacaoListener;
import pt.itassets.lite.models.PedidoAlocacao;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ListaPedidosAlocacaoFragment extends Fragment implements PedidosAlocacaoListener {

    private ListView lvAlocacoes;
    private FloatingActionButton fabListaPedidosAlocacao;
    private ChipGroup CG_filtro_alocacao;
    private TextView TV_sem_dados;
    private ArrayList<Integer> status_filter;

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
        TV_sem_dados = view.findViewById(R.id.TV_sem_dados);
        fabListaPedidosAlocacao = view.findViewById(R.id.fabListaPedidosAlocacao);
        CG_filtro_alocacao = view.findViewById(R.id.CG_filtro_alocacao);

        status_filter = new ArrayList<>();

        status_filter.add(PedidoAlocacao.STATUS_ABERTO);
        status_filter.add(PedidoAlocacao.STATUS_APROVADO);
        status_filter.add(PedidoAlocacao.STATUS_NEGADO);
        status_filter.add(PedidoAlocacao.STATUS_DEVOLVIDO);
        status_filter.add(PedidoAlocacao.STATUS_CANCELADO);

        Singleton.getInstance(getContext()).setPedidosAlocacaoListener(this);

        CG_filtro_alocacao.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                status_filter.clear();

                for (int i : checkedIds) {
                    Chip local = view.findViewById(i);
                    status_filter.add(Integer.parseInt((String) local.getTag()));
                }
                Singleton.getInstance(getContext()).getUserAlocacoesAPI(getContext());
            }
        });

        lvAlocacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positions, long id) {
                Intent detalhes = new Intent(getContext(), DetalhesPedidoAlocacaoActivity.class);
                detalhes.putExtra("ID_PEDIDO", (int) id);
                startActivityForResult(detalhes, Helpers.OPERACAO_DETALHES);
            }
        });

        Singleton.getInstance(getContext()).getUserAlocacoesAPI(getContext());

        fabListaPedidosAlocacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adicionar = new Intent(getContext(), AdicionarPedidoAlocacaoActivity.class);
                startActivityForResult(adicionar, Helpers.OPERACAO_ADD);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if(resultCode == Activity.RESULT_OK && (requestCode == Helpers.OPERACAO_DETALHES ||
                requestCode == Helpers.OPERACAO_ADD))
        {
            Singleton.getInstance(getContext()).getUserAlocacoesAPI(getContext());

            if(intent != null)
            {
                switch(intent.getIntExtra(Helpers.OPERACAO, -99))
                {
                    case Helpers.OPERACAO_ADD:
                        Toast.makeText(getContext(), R.string.txt_alocacao_submetido, Toast.LENGTH_SHORT).show();
                        break;

                    case Helpers.OPERACAO_EDIT:
                        Toast.makeText(getContext(), R.string.txt_alocacao_atualizada, Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Toast.makeText(getContext(), getString(R.string.txt_operacao_bem_sucedida), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            else
            {
                Toast.makeText(getContext(), getString(R.string.txt_operacao_bem_sucedida), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRefreshListaAlocacoes(ArrayList<PedidoAlocacao> listaAlocacoes) {
        if(listaAlocacoes != null)
        {
            if(listaAlocacoes.size() > 0)
            {
                // Filtrar pedidos
                listaAlocacoes.removeIf(pedido -> !status_filter.contains(pedido.getStatus()));

                if(listaAlocacoes.size() > 0)
                {
                    TV_sem_dados.setVisibility(View.INVISIBLE);
                    lvAlocacoes.setVisibility(View.VISIBLE);
                    lvAlocacoes.setAdapter(new ListaPedidosAlocacaoAdaptador(getContext(), listaAlocacoes));
                    return;
                }
            }
        }
        TV_sem_dados.setVisibility(View.VISIBLE);
        lvAlocacoes.setVisibility(View.INVISIBLE);
    }
}