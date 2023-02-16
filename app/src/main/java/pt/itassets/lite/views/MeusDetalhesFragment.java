package pt.itassets.lite.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pt.itassets.lite.R;
import pt.itassets.lite.models.DBHelper;
import pt.itassets.lite.utils.Helpers;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MeusDetalhesFragment extends Fragment {

    SharedPreferences preferences;

    public MeusDetalhesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meus_detalhes, container, false);
        preferences = getContext().getSharedPreferences(Helpers.SHAREDPREFERENCES, Context.MODE_PRIVATE);

        TextView username = view.findViewById(R.id.tv_username),
                email = view.findViewById(R.id.tv_email),
                role = view.findViewById(R.id.tv_role);

        username.setText(String.valueOf(preferences.getString(Helpers.USER_NAME, null)));
        email.setText(String.valueOf(preferences.getString(Helpers.USER_EMAIL, null)));
        role.setText(String.valueOf(preferences.getString(Helpers.USER_ROLE, null)));

        return view;
    }
}