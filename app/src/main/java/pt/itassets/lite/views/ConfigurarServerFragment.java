package pt.itassets.lite.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import pt.itassets.lite.listeners.AppSetupListener;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;
import pt.itassets.lite.utils.JSONParsers;

public class ConfigurarServerFragment extends DialogFragment implements View.OnClickListener, AppSetupListener {

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

        Singleton.getInstance(getContext()).setAppSetupListener(this);

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
            Toast.makeText(getContext(), getString(R.string.txt_sem_internet), Toast.LENGTH_SHORT).show();
            return;
        }

        progressbar_configServer.setVisibility(View.VISIBLE);

        CompletableFuture.supplyAsync(() -> {
            return Helpers.isURLValid(et_endereco.getText().toString());
        }).thenAccept(result -> {
            if(!result)
            {
                progressbar_configServer.setVisibility(View.INVISIBLE);
                Snackbar.make(getContext(),view, getString(R.string.txt_url_invalido), Snackbar.LENGTH_LONG).show();
            }
            else
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

                Singleton.getInstance(getContext()).setupApp(url, getContext());
            }
        }).exceptionally(e -> null);
    }

    @Override
    public void onAppSetupSuccess(Map<String, String> responseMap) {
        progressbar_configServer.setVisibility(View.INVISIBLE);
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
            Toast.makeText(getContext(), getString(R.string.txt_generic_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAppSetupFail(Map<String, Object> errorMap) {
        progressbar_configServer.setVisibility(View.INVISIBLE);
        if(errorMap != null)
        {
            Toast.makeText(getContext(), (String) errorMap.get("message"), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getContext(), getString(R.string.txt_generic_error), Toast.LENGTH_SHORT).show();
        }
    }
}