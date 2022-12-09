package pt.itassets.android.vistas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import pt.itassets.android.modelos.Singleton;
import pt.itassets.android.utils.ApiJsonParser;
import pt.itassets.android.utils.Helper;
import pt.itassets.android.R;

public class ConfigurarServerFragment extends DialogFragment implements View.OnClickListener {

    private EditText et_endereco;
    private LinearProgressIndicator progressbar_configServer;
    private String url;

    public ConfigurarServerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configurar_server, null);
        Button btnOK = view.findViewById(R.id.btn_ok);
        et_endereco = view.findViewById(R.id.et_endereco_servidor);
        progressbar_configServer = view.findViewById(R.id.progressbar_configServer);

        btnOK.setOnClickListener(this);
        setCancelable(false);

        return view;
    }

    @Override
    public void onClick(View view) {

        if(et_endereco.getText().toString().length() < 1)
        {
            return;
        }

        if(!Helper.isInternetConnectionAvailable(getContext()))
        {
            Snackbar.make(getContext(), view, getString(R.string.txt_no_connection), Snackbar.LENGTH_LONG).show();
            return;
        }

        progressbar_configServer.setVisibility(View.VISIBLE);

        CompletableFuture.supplyAsync(() -> {
            return Helper.isURLValid_v2(et_endereco.getText().toString());
        }).thenAccept(result -> {
            if(result)
            {
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
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + "sysinfo", null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                if(Helper.isValidJSON(response.toString()))
                                {

                                    Map<String, String> responseMap = ApiJsonParser.parseJsonLigcaoEmpresa(response.toString());

                                    if(responseMap != null)
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                                        builder.setTitle(R.string.txt_confirmar);
                                        builder.setMessage(getString(R.string.txt_confirmar_empresa_login) + "\n" +
                                                getString(R.string.txt_empresa) + ": " + responseMap.get("companyName") + "\n" +
                                                getString(R.string.txt_nif) + ": " + responseMap.get("companyNIF"));

                                        builder.setCancelable(false);

                                        // Add the buttons
                                        builder.setPositiveButton(R.string.txt_sim, (dialog, which)->
                                        {
                                            //Aceder à sharedPreference e definir o modo de acesso
                                            SharedPreferences infoUrl = getContext().getSharedPreferences(Helper.APP_STORAGE, Context.MODE_PRIVATE);

                                            //Definir o Editor para permitir guardar/ alterar o valor
                                            SharedPreferences.Editor editor = infoUrl.edit();
                                            editor.putString(Helper.APP_SYSTEM_DOMAIN_URL, url);
                                            editor.apply();

                                            dismiss();
                                        });
                                        builder.setNegativeButton(R.string.txt_cancelar, (dialog, which)->
                                        {
                                            dialog.cancel();
                                        });

                                        // Create the AlertDialog
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }
                                Snackbar.make(getContext(), view, getString(R.string.txt_generic_error), Snackbar.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorString = new String(error.networkResponse.data, StandardCharsets.UTF_8);

                        if(Helper.isValidJSON(errorString))
                        {
                            Map<String, Object> errorMap = ApiJsonParser.parseError(errorString);
                            if(errorMap == null)
                            {
                                Snackbar.make(getContext(), view, getString(R.string.txt_url_nao_existe_ou_offline), Snackbar.LENGTH_LONG).show();
                            }
                            else
                            {
                                Snackbar.make(getContext(), view, (String) errorMap.get("message"), Snackbar.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Snackbar.make(getContext(), view, getString(R.string.txt_domain_no_api), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
                Singleton.getInstance(getActivity().getApplicationContext()).addRequestToQueue(jsonObjectRequest);
            }
            else
            {
                Snackbar.make(getContext(),view, getString(R.string.txt_url_invalido), Snackbar.LENGTH_LONG).show();
            }
            progressbar_configServer.setVisibility(View.INVISIBLE);
        }).exceptionally(e -> null);
    }
}