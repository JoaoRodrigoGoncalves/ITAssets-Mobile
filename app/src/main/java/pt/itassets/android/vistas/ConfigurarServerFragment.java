package pt.itassets.android.vistas;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pt.itassets.android.R;

public class ConfigurarServerFragment extends DialogFragment implements View.OnClickListener {

    public ConfigurarServerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configurar_server, null);
        Button btnOK = view.findViewById(R.id.btn_ok);

        btnOK.setOnClickListener(this);
        setCancelable(false);

        return view;
    }

    @Override
    public void onClick(View view) {
        EditText et_endereco = view.findViewById(R.id.et_endereco);

        // TODO: Terminar configuração da aplicação (seleção de servidor)
        // Validar URL

        // Obter informações sobre a instancia no servidor (Nome da empresa... idk)

        // Questionar o utilizador para garantir que é esse o servidor

        Toast.makeText(getActivity(), "AAAA", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}