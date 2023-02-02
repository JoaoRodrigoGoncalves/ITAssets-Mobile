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

public class ListaReparacoesFragment extends Fragment implements PedidosReparacaoListener {

    private ListView lvReparacoes;
    private FloatingActionButton fabListaPedidosReparacao;
    private TextView TV_sem_dados;
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
        TV_sem_dados = view.findViewById(R.id.TV_sem_dados);
        fabListaPedidosReparacao = view.findViewById(R.id.fabListaPedidosReparacao);

        lvReparacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positions, long id) {
                Intent intent = new Intent(getContext(), DetalhesReparacaoActivity.class);
                intent.putExtra("ID_REPARACAO", (int) id);
                startActivityForResult(intent, ACTION_DETALHES); //Método Deprecated
            }
        });

        Singleton.getInstance(getContext()).setPedidosReparacaoListener(this);
        Singleton.getInstance(getContext()).getUserReparacoesAPI(getContext());

        fabListaPedidosReparacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AdicionarPedidoReparacaoActivity.class);
                startActivityForResult(intent, ACTION_ADICIONAR); //Método Deprecated
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if(resultCode == Activity.RESULT_OK && requestCode == ACTION_ADICIONAR){
            Singleton.getInstance(getContext()).getUserReparacoesAPI(getContext());
            Toast.makeText(getContext(), getString(R.string.txt_operacao_bem_sucedida), Toast.LENGTH_SHORT).show();
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