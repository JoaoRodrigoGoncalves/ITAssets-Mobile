package pt.itassets.lite.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import pt.itassets.lite.R;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;
import pt.itassets.lite.utils.JSONParsers;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private LinearProgressIndicator progressbar_login;
    private String SYSTEM_DOMAIN = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        progressbar_login = findViewById(R.id.progressbar_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        if (preferences.getString(Helpers.DOMAIN, null) == null) {
            FragmentManager fm = getSupportFragmentManager();
            if(fm.findFragmentByTag("config") == null)
            {
                ConfigurarServerFragment csf = new ConfigurarServerFragment();
                csf.show(fm, "config");
            }
            preferences.edit().putString(Helpers.DOMAIN, "https://api.staging.itassets.pt/").commit(); //commit it is
        }
        else
        {
            if(preferences.getString(Helpers.USER_TOKEN, null) != null)
            {
                //TODO: Send heartbeat

                /*
                * Ver se o token é válido e tal
                * mostrar uma progress circle enquanto processa
                */

                // De momento a assumir sessão válida
                Intent abrirAppLoginAnteriorValido = new Intent(getBaseContext(), MenuActivity.class);
                startActivity(abrirAppLoginAnteriorValido);
            }
        }
    }

    public void onClick_btn_login(View view)
    {
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();

        // Remover erros que possam ter sido anteriormente aplicados às editTexts
        etEmail.setError(null);
        etPassword.setError(null);

        if(!Helpers.isEmailValido(email)) {
            etEmail.setError(getString(R.string.email_invalido));
            return;
        }

        if(pass.length() < 1)
        {
            etPassword.setError(getString(R.string.txt_indique_password));
            return;
        }

        SharedPreferences preferences = getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);
        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);

        //Fazer pedido a api com os dados
        if(SYSTEM_DOMAIN != null)
        {
            progressbar_login.setVisibility(View.VISIBLE);
            if(Helpers.isInternetConnectionAvailable(this))
            {
                try
                {
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, SYSTEM_DOMAIN + "login",null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    progressbar_login.setVisibility(View.INVISIBLE);

                                    String response_string = response.toString();
                                    if(Helpers.isValidJSON(response_string))
                                    {
                                        if(JSONParsers.parserJsonLogin(response_string, getBaseContext()))
                                        {
                                            Intent mainMenu = new Intent(getBaseContext(), MenuActivity.class);
                                            startActivity(mainMenu);
                                            finish();
                                        }
                                        else
                                        {
                                            Snackbar.make(getBaseContext(), view, getString(R.string.txt_credenciais_erradas), Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                    else
                                    {
                                        // Algum erro ocurreu, mostrar erro
                                        Snackbar.make(getBaseContext(), view, getString(R.string.txt_generic_error), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressbar_login.setVisibility(View.INVISIBLE);

                                    if(error != null){
                                        if(error.networkResponse != null)
                                        {
                                            String error_string = new String(error.networkResponse.data, StandardCharsets.UTF_8);

                                            if(Helpers.isValidJSON(error_string))
                                            {
                                                Map<String, Object> errorMap = JSONParsers.parseError(error_string);

                                                if((int) errorMap.get("statusCode") == 401)
                                                {
                                                    etPassword.setError((String) errorMap.get("message"));
                                                }
                                                else
                                                {
                                                    Snackbar.make(getBaseContext(), view, (String) errorMap.get("message"), Snackbar.LENGTH_LONG).show();
                                                }
                                            }
                                            else
                                            {
                                                //Outro erro qualquer, mostrar erro genérico
                                                Snackbar.make(getBaseContext(), view, getString(R.string.txt_generic_error), Snackbar.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                }
                            })  {
                        // https://stackoverflow.com/a/44049327
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            String authString = email + ":" + pass;

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json; charset=UTF-8");
                            params.put("Authorization", "Basic " + Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8)));
                            return params;
                        }
                    };
                    // Enviar o application context para não dar problemas

                    Singleton.getInstance(getApplicationContext()).addRequestToQueue(jsonRequest);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Snackbar.make(this, view, getString(R.string.txt_generic_error), Snackbar.LENGTH_LONG).show();
                }
            }
            else
            {
                Snackbar.make(this, view, getString(R.string.txt_sem_internet), Snackbar.LENGTH_LONG).show();
            }
        }
    }
}