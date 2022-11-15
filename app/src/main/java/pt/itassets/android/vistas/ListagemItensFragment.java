package pt.itassets.android.vistas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import pt.itassets.android.R;
import pt.itassets.android.adptadores.ListaItensAdaptador;
import pt.itassets.android.modelos.Itens;
import pt.itassets.android.modelos.Singleton;

public class ListagemItensFragment extends Fragment {
    private ListView lvItens;
    private ArrayList<Itens> itens;

    public ListagemItensFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listagem_itens, container, false);
        lvItens = view.findViewById(R.id.lvItens);
        itens = Singleton.getInstance().getItens();
        lvItens.setAdapter(new ListaItensAdaptador(getContext(), itens) {
        });
        return view;
    }
}