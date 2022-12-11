package pt.itassets.android.vistas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

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

import pt.itassets.android.modelos.Singleton;
import pt.itassets.android.utils.ApiJsonParser;
import pt.itassets.android.utils.Helper;
import pt.itassets.android.R;

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
        SharedPreferences app_preferences = getSharedPreferences(Helper.APP_STORAGE, MODE_PRIVATE);

        if (app_preferences.getString(Helper.APP_SYSTEM_DOMAIN_URL, null) == null) {
            FragmentManager fm = getSupportFragmentManager();
            if(fm.findFragmentByTag("config") == null)
            {
                ConfigurarServerFragment csf = new ConfigurarServerFragment();
                csf.show(fm, "config");
            }
        } else {
            SYSTEM_DOMAIN = app_preferences.getString(Helper.APP_SYSTEM_DOMAIN_URL, null);
            SharedPreferences user_preferences = getSharedPreferences(Helper.USER_STORAGE, MODE_PRIVATE);

            if (user_preferences.getString(Helper.USER_TOKEN, null) != null && Helper.isBiometricAvailable(this)) {
                Executor executor = ContextCompat.getMainExecutor(getBaseContext());
                BiometricPrompt bioPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Intent autologinIntent = new Intent(getBaseContext(), MenuMainActivity.class);
                        startActivity(autologinIntent);
                        finish();
                    }
                });
                BiometricPrompt.PromptInfo.Builder prompt = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle(getString(R.string.txt_coloque_dedo_sensor_title))
                        .setDescription(getString(R.string.txt_coloque_dedo_sensor_description))
                        .setNegativeButtonText(getString(R.string.txt_cancelar));
                bioPrompt.authenticate(prompt.build());
            }
        }
    }

    public void onClick_btn_login(View view) {
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();

        // Remover erros que possam ter sido anteriormente aplicados às editTexts
        etEmail.setError(null);
        etPassword.setError(null);

        if(!Helper.isEmailValido(email)) {
            etEmail.setError(getString(R.string.email_invalido));
            return;
        }

        if(pass.length() < 1)
        {
            etPassword.setError(getString(R.string.txt_indique_password));
            return;
        }
        //Fazer pedido a api com os dados
        if(SYSTEM_DOMAIN != null)
        {
            progressbar_login.setVisibility(View.VISIBLE);
            if(Helper.isInternetConnectionAvailable(this))
            {
                Context thisContext = this;
                try
                {
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, SYSTEM_DOMAIN + "login",null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    progressbar_login.setVisibility(View.INVISIBLE);

                                    String response_string = response.toString();
                                    if(Helper.isValidJSON(response_string))
                                    {
                                        String token = ApiJsonParser.parserJsonLogin(response_string);
                                        if(token != null)
                                        {
                                            SharedPreferences prefs = getBaseContext().getSharedPreferences(Helper.USER_STORAGE, MODE_PRIVATE);
                                            SharedPreferences.Editor prefEditor = prefs.edit();
                                            prefEditor.putString(Helper.USER_TOKEN, token);
                                            prefEditor.apply();

                                            Intent mainMenu = new Intent(getBaseContext(), MenuMainActivity.class);
                                            mainMenu.putExtra(Helper.USER_EMAIL, email);
                                            startActivity(mainMenu);
                                            finish();
                                        }
                                    }
                                    // Algum erro ocurreu, mostrar erro
                                    Snackbar.make(thisContext, view, getString(R.string.txt_erro_login_no_json), Snackbar.LENGTH_LONG).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressbar_login.setVisibility(View.INVISIBLE);

                                    String error_string = new String(error.networkResponse.data, StandardCharsets.UTF_8);

                                    if(Helper.isValidJSON(error_string))
                                    {
                                        Map<String, Object> errorMap = ApiJsonParser.parseError(error_string);

                                        if((int) errorMap.get("statusCode") == 401)
                                        {
                                            etPassword.setError((String) errorMap.get("message"));
                                        }
                                        else
                                        {
                                            Snackbar.make(thisContext, view, (String) errorMap.get("message"), Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                    else
                                    {
                                        //Outro erro qualquer, mostrar erro genérico
                                        Snackbar.make(thisContext, view, getString(R.string.txt_generic_error), Snackbar.LENGTH_LONG).show();
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
                // Sem ligação à internet.
                Snackbar.make(this, view, getString(R.string.txt_no_connection_cant_login), Snackbar.LENGTH_LONG).show();
            }
        }
    }
}