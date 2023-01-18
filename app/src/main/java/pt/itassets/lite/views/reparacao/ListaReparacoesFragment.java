package pt.itassets.lite.views.reparacao;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.itassets.lite.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaReparacoesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaReparacoesFragment extends Fragment {

    public ListaReparacoesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_reparacoes, container, false);
    }
}