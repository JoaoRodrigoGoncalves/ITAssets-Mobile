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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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

        SharedPreferences prefs = getSharedPreferences(Helper.PREF_STORAGE, MODE_PRIVATE);

        if(prefs.getString(Helper.PREF_SYSTEM_DOMAIN_URL, null) == null){
            FragmentManager fm = getSupportFragmentManager();
            ConfigurarServerFragment csf = new ConfigurarServerFragment();
            csf.show(fm, null); //Tag?
        }
        else
        {
            if(prefs.getString(Helper.PREF_USER_TOKEN, null) != null)
            {
                // TODO: Validar que o token ainda é válido, testar biometria e enviar email para a atividade
                Intent autologinIntent = new Intent(this, MenuMainActivity.class);
                startActivity(autologinIntent);
                finish();
            }
            SYSTEM_DOMAIN = prefs.getString(Helper.PREF_SYSTEM_DOMAIN_URL, null);
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
                                System.out.println(response);

                                if(Helper.isValidJSON(response))
                                {
                                    // TODO: uniformizar classes json usadas
                                    Gson loginJson = new Gson();
                                    Login loginObj = loginJson.fromJson(response, Login.class);


                                    switch(loginObj.status)
                                    {
                                        case 200:
                                            SharedPreferences prefs = thisContext.getSharedPreferences(Helper.PREF_STORAGE, MODE_PRIVATE);
                                            SharedPreferences.Editor prefEditor = prefs.edit();
                                            prefEditor.putString(Helper.PREF_USER_TOKEN, loginObj.token);
                                            prefEditor.apply();

                                            Intent mainMenu = new Intent(thisContext, MenuMainActivity.class);
                                            mainMenu.putExtra(MenuMainActivity.EMAIL, email);
                                            startActivity(mainMenu);
                                            finish();
                                            break;

                                        case 403:
                                            etPassword.setError(loginObj.getMessage());
                                            break;

                                        default:
                                            //TODO: Dialog com message do erro
                                    }
                                }
                                else
                                {
                                    // TODO: Handle erro que não vem em JSON
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO: Handle erros relcaionados com a execução do pedido
                        System.out.println(error.networkResponse.statusCode);
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

                System.out.println("EXCEPTION: " + e.getMessage());
            }
        }
    }
}