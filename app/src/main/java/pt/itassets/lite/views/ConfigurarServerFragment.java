package pt.itassets.lite.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import pt.itassets.lite.R;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;
import pt.itassets.lite.utils.JSONParsers;

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

        if(!Helpers.isInternetConnectionAvailable(getContext()))
        {
            Snackbar.make(getContext(), view, getString(R.string.txt_sem_internet), Snackbar.LENGTH_LONG).show();
            return;
        }

        progressbar_configServer.setVisibility(View.VISIBLE);

        CompletableFuture.supplyAsync(() -> {
            return Helpers.isURLValid(et_endereco.getText().toString());
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

                                if(Helpers.isValidJSON(response.toString()))
                                {
                                    Map<String, String> responseMap = JSONParsers.parseJsonLigcaoEmpresa(response.toString());

                                    if(responseMap != null)
                                    {
                                        new MaterialAlertDialogBuilder(getContext())
                                                .setIcon(R.drawable.ic_action_ligar)
                                                .setTitle(R.string.txt_confirmar_empresa_login)
                                                .setMessage(
                                                        getString(R.string.txt_empresa) + ": " + responseMap.get("companyName") + "\n" +
                                                                getString(R.string.txt_nif) + ": " + responseMap.get("companyNIF")
                                                )
                                                .setCancelable(false)
                                                .setNegativeButton(R.string.txt_cancelar, (dialogInterface, i) -> dialogInterface.cancel())
                                                .setPositiveButton(R.string.txt_sim, (dialogInterface, i) -> {
                                                    //Aceder à sharedPreference e definir o modo de acesso
                                                    SharedPreferences infoUrl = getContext().getSharedPreferences(Helpers.SHAREDPREFERENCES, Context.MODE_PRIVATE);

                                                    //Definir o Editor para permitir guardar/ alterar o valor
                                                    SharedPreferences.Editor editor = infoUrl.edit();
                                                    editor.putString(Helpers.DOMAIN, url);
                                                    editor.apply();

                                                    getDialog().dismiss();

                                                    dismiss();
                                                })
                                                .show();
                                    }
                                    else
                                    {
                                        Snackbar.make(getContext(), view, getString(R.string.txt_generic_error), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                                else
                                {
                                    Snackbar.make(getContext(), view, getString(R.string.txt_generic_error), Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse != null)
                        {
                            String errorString = new String(error.networkResponse.data, StandardCharsets.UTF_8);

                            if(Helpers.isValidJSON(errorString))
                            {
                                Map<String, Object> errorMap = JSONParsers.parseError(errorString);
                                if(errorMap == null)
                                {
                                    Snackbar.make(getContext(), view, getString(R.string.txt_url_invalido), Snackbar.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Snackbar.make(getContext(), view, (String) errorMap.get("message"), Snackbar.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Snackbar.make(getContext(), view, getString(R.string.txt_url_invalido), Snackbar.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Snackbar.make(getContext(), view, getString(R.string.txt_generic_error), Snackbar.LENGTH_LONG).show();
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