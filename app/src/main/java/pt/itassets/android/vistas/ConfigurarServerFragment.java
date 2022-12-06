package pt.itassets.android.vistas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import pt.itassets.android.Helper;
import pt.itassets.android.R;
import pt.itassets.android.modelos.Empresa;

public class ConfigurarServerFragment extends DialogFragment implements View.OnClickListener {

    private EditText et_endereco;
    private String url;

    public ConfigurarServerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configurar_server, null);
        Button btnOK = view.findViewById(R.id.btn_ok);
        et_endereco = view.findViewById(R.id.et_endereco_servidor);

        btnOK.setOnClickListener(this);
        setCancelable(false);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(Helper.isURLValid(et_endereco.getText().toString()))
        {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.start();

            if(!et_endereco.getText().toString().contains("http://"))
            {
                //Caso não contenha já o protocolo explicitamente, adicionar https por padrão
                url="https://"+ et_endereco.getText().toString();
            }

            if(!url.endsWith("/"))
            {
                url += "/";
            }

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "sysinfo",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            Empresa objEmpresa = gson.fromJson(response, Empresa.class);

                            // Verificar código de estado de resposta
                            if(objEmpresa.getStatus() == 200){

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                builder.setTitle(R.string.txt_confirmar);
                                builder.setMessage(getString(R.string.txt_confirmar_empresa_login) + "\n" +
                                        getString(R.string.txt_empresa) + ": " + objEmpresa.getData().getCompanyNome() + "\n" +
                                        getString(R.string.txt_nif) + ": " + objEmpresa.getData().getCompanyNIF());

                                builder.setCancelable(false);

                                // Add the buttons
                                builder.setPositiveButton(R.string.txt_sim, (DialogInterface.OnClickListener)(dialog, which)->
                                {
                                    //Aceder à sharedPreference e definir o modo de acesso
                                    SharedPreferences infoUrl = getContext().getSharedPreferences(Helper.APP_STORAGE, Context.MODE_PRIVATE);

                                    //Definir o Editor para permitir guardar/ alterar o valor
                                    SharedPreferences.Editor editor = infoUrl.edit();
                                    editor.putString(Helper.APP_SYSTEM_DOMAIN_URL, url);
                                    editor.apply();

                                    dismiss();
                                });
                                builder.setNegativeButton(R.string.txt_cancelar, (DialogInterface.OnClickListener)(dialog, which)->
                                {
                                    dialog.cancel();
                                });

                                // Create the AlertDialog
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }else{
                                Snackbar.make(getContext(), view, objEmpresa.getStatus() + ": " + objEmpresa.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Snackbar.make(getContext(), view, getString(R.string.txt_url_nao_existe_ou_offline), Snackbar.LENGTH_LONG).show();
                        }
                    });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
        else
        {
            // Notificar que URL não é válido
            Snackbar.make(getContext(),view, getString(R.string.txt_url_invalido), Snackbar.LENGTH_LONG).show();
        }
    }
}