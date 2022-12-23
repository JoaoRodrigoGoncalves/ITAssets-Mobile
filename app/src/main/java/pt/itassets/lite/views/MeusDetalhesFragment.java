package pt.itassets.lite.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.itassets.lite.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MeusDetalhesFragment extends Fragment {

    public MeusDetalhesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meus_detalhes, container, false);
    }

    public void BTN_Logout(View view) {

    }
}