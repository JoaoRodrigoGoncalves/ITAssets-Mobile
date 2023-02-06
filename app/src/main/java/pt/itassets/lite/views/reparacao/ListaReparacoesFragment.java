package pt.itassets.lite.views.reparacao;

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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.itassets.lite.R;
import pt.itassets.lite.adapters.ListaPedidosReparacaoAdaptador;
import pt.itassets.lite.listeners.PedidosReparacaoListener;
import pt.itassets.lite.models.PedidoReparacao;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;

public class ListaReparacoesFragment extends Fragment implements PedidosReparacaoListener {

    private ListView lvReparacoes;
    private FloatingActionButton fabListaPedidosReparacao;
    private TextView TV_sem_dados;

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
        TV_sem_dados = view.findViewById(R.id.TV_sem_dados);
        fabListaPedidosReparacao = view.findViewById(R.id.fabListaPedidosReparacao);

        lvReparacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positions, long id) {
                Intent intent = new Intent(getContext(), DetalhesReparacaoActivity.class);
                intent.putExtra("ID_REPARACAO", (int) id);
                startActivityForResult(intent, Helpers.OPERACAO_DETALHES); //Método Deprecated
            }
        });

        Singleton.getInstance(getContext()).setPedidosReparacaoListener(this);
        Singleton.getInstance(getContext()).getUserReparacoesAPI(getContext());

        fabListaPedidosReparacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AdicionarPedidoReparacaoActivity.class);
                startActivityForResult(intent, Helpers.OPERACAO_ADD); //Método Deprecated
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if(resultCode == Activity.RESULT_OK && (requestCode == Helpers.OPERACAO_DETALHES ||
                requestCode == Helpers.OPERACAO_ADD))
        {
            Singleton.getInstance(getContext()).getUserReparacoesAPI(getContext());

            if(intent != null)
            {
                switch(intent.getIntExtra(Helpers.OPERACAO, -99))
                {
                    case Helpers.OPERACAO_ADD:
                        Toast.makeText(getContext(), R.string.txt_reparacao_submetido, Toast.LENGTH_SHORT).show();
                        break;

                    case Helpers.OPERACAO_EDIT:
                        Toast.makeText(getContext(), R.string.txt_reparacao_atualizado, Toast.LENGTH_SHORT).show();
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
    public void onRefreshListaReparacoes(ArrayList<PedidoReparacao> listaReparacoes) {
        if(listaReparacoes != null)
        {
            if(listaReparacoes.size() > 0)
            {
                lvReparacoes.setAdapter(new ListaPedidosReparacaoAdaptador(getContext(), listaReparacoes));
            }
            else
            {
                lvReparacoes.setVisibility(View.INVISIBLE);
                TV_sem_dados.setVisibility(View.VISIBLE);
            }
        }
    }
}