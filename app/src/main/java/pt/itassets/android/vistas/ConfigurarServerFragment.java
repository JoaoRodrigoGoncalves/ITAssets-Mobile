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

import com.google.android.material.snackbar.Snackbar;

import pt.itassets.android.Helper;
import pt.itassets.android.R;

public class ConfigurarServerFragment extends DialogFragment implements View.OnClickListener {

    private EditText et_endereco;

    public ConfigurarServerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configurar_server, null);
        Button btnOK = view.findViewById(R.id.btn_ok);
        et_endereco = view.findViewById(R.id.et_endereco_servidor);

        btnOK.setOnClickListener(this);
        setCancelable(false);

        return view;
    }

    @Override
    public void onClick(View view) {
        // TODO: Terminar configuração da aplicação (seleção de servidor)
        if(Helper.isURLValid(et_endereco.getText().toString()))
        {
            // Obter informações sobre a instancia no servidor (Nome da empresa... idk)
            // Questionar o utilizador para garantir que é esse o servidor
            dismiss();
        }
        else
        {
            // Notificar que URL não é válido
            Snackbar.make(getContext(),view, "URL inválido", Snackbar.LENGTH_LONG).show();
        }
    }
}