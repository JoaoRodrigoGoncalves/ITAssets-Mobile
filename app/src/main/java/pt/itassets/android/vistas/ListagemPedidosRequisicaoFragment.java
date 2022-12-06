package pt.itassets.android.vistas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import pt.itassets.android.R;

public class ListagemPedidosRequisicaoFragment extends Fragment {
    private ListView lvPedidosRequisicao;

    public ListagemPedidosRequisicaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listagem_pedidos_requisicao, container, false);
        lvPedidosRequisicao = view.findViewById(R.id.lvPedidosRequisicao);
        return view;
    }
}