package pt.itassets.android.vistas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
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
            SharedPreferences user_preferences = getSharedPreferences(Helper.USER_STORAGE, MODE_PRIVATE);
            if(user_preferences.getString(Helper.USER_TOKEN, null) != null)
            {
                // TODO: Validar que o token ainda é válido, testar biometria e enviar email para a atividade
                Intent autologinIntent = new Intent(this, MenuMainActivity.class);
                startActivity(autologinIntent);
                finish();
            }
            SYSTEM_DOMAIN = app_preferences.getString(Helper.APP_SYSTEM_DOMAIN_URL, null);
        }
    }

    public void onClick_btn_login(View view) {
        //Validar dados inseridos nas EditText
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();

        if(!Helper.isEmailValido(email)) {
            etEmail.setError(getString(R.string.email_invalido));
            return;
        }

        //Fazer pedido a api com os dados
        if(SYSTEM_DOMAIN != null)
        {
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.start();

            try
            {
                Context thisContext = this;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, SYSTEM_DOMAIN + "login",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(Helper.isValidJSON(response))
                                {
                                    // TODO: uniformizar classes json usadas
                                    Gson loginJson = new Gson();
                                    Login loginObj = loginJson.fromJson(response, Login.class);

                                    if(loginObj.status == 200)
                                    {
                                        SharedPreferences prefs = thisContext.getSharedPreferences(Helper.USER_STORAGE, MODE_PRIVATE);
                                        SharedPreferences.Editor prefEditor = prefs.edit();
                                        prefEditor.putString(Helper.USER_TOKEN, loginObj.token);
                                        prefEditor.apply();

                                        Intent mainMenu = new Intent(thisContext, MenuMainActivity.class);
                                        mainMenu.putExtra(Helper.USER_EMAIL, email);
                                        startActivity(mainMenu);
                                        finish();
                                    }
                                    else
                                    {
                                        Snackbar.make(thisContext, view, getString(R.string.txt_erro) + ": " + loginObj.getMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                                else
                                {
                                    Snackbar.make(thisContext, view, getString(R.string.txt_erro_login_no_json), Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse.statusCode == 403) {
                            etPassword.setError(new String(error.networkResponse.data, StandardCharsets.UTF_8));
                        }
                        else
                        {
                            Snackbar.make(thisContext, view, error.toString(), Snackbar.LENGTH_LONG).show();
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