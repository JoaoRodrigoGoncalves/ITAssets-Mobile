package pt.itassets.android.vistas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import pt.itassets.android.Helper;
import pt.itassets.android.R;
import pt.itassets.android.modelos.Login;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private String SYSTEM_DOMAIN = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences app_preferences = getSharedPreferences(Helper.APP_STORAGE, MODE_PRIVATE);

        if(app_preferences.getString(Helper.APP_SYSTEM_DOMAIN_URL, null) == null){
            FragmentManager fm = getSupportFragmentManager();
            ConfigurarServerFragment csf = new ConfigurarServerFragment();
            csf.show(fm, null); //Tag?
        }
        else
        {
            SYSTEM_DOMAIN = app_preferences.getString(Helper.APP_SYSTEM_DOMAIN_URL, null);

            SharedPreferences user_preferences = getSharedPreferences(Helper.USER_STORAGE, MODE_PRIVATE);

            if(user_preferences.getString(Helper.USER_TOKEN, null) != null && Helper.IsBiometricAvailable(this))
            {
                Executor executor = ContextCompat.getMainExecutor(this);
                BiometricPrompt bioPrompt = new androidx.biometric.BiometricPrompt(this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
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
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.start();
            Gson loginJson = new Gson();

            try
            {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, SYSTEM_DOMAIN + "login",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(Helper.isValidJSON(response))
                                {
                                    // TODO: uniformizar classes json usadas
                                    Login loginObj = loginJson.fromJson(response, Login.class);

                                    if(loginObj.status == 200)
                                    {
                                        SharedPreferences prefs = getBaseContext().getSharedPreferences(Helper.USER_STORAGE, MODE_PRIVATE);
                                        SharedPreferences.Editor prefEditor = prefs.edit();
                                        prefEditor.putString(Helper.USER_TOKEN, loginObj.token);
                                        prefEditor.apply();

                                        Intent mainMenu = new Intent(getBaseContext(), MenuMainActivity.class);
                                        mainMenu.putExtra(Helper.USER_EMAIL, email);
                                        startActivity(mainMenu);
                                        finish();
                                    }
                                    else
                                    {
                                        Snackbar.make(getBaseContext(), view, getString(R.string.txt_erro) + ": " + loginObj.getMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                                else
                                {
                                    Snackbar.make(getBaseContext(), view, getString(R.string.txt_erro_login_no_json), Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse.statusCode == 403) {
                            etPassword.setError(getString(R.string.txt_credenciais_incorretas));
                        }
                        else
                        {
                            Snackbar.make(getBaseContext(), view, error.toString(), Snackbar.LENGTH_LONG).show();
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
                queue.add(stringRequest);
            }
            catch (Exception e)
            {
                String erro = e.getMessage() == null ? getString(R.string.txt_erro) : e.getMessage();
                Snackbar.make(this, view, erro, Snackbar.LENGTH_LONG).show();
                System.out.println(getString(R.string.txt_erro) + ": " + e.getMessage());
            }
        }
    }
}