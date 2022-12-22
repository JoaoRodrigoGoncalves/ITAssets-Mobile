package pt.itassets.android.vistas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.itassets.android.R;
import pt.itassets.android.modelos.GrupoItem;

public class ListagemGruposItensFragment extends Fragment {
    private ListView lvGruposItens;
    private ArrayList<GrupoItem> grupoItens;
    private FloatingActionButton fabLista;
    private SearchView searchView;
    public static final int ACT_DETALHES=1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listagem_grupos_itens, container, false);




        return view;
    }
}