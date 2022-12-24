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
import pt.itassets.lite.listeners.LoginListener;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;
import pt.itassets.lite.utils.JSONParsers;

public class LoginActivity extends AppCompatActivity implements LoginListener {
    private EditText etEmail;
    private EditText etPassword;
    private LinearProgressIndicator progressbar_login;
    private String SYSTEM_DOMAIN = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Singleton.getInstance(this).setLoginListener(this);

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
            Singleton.getInstance(this).loginAPI(email, pass, this);
        }
    }

    @Override
    public void onLoginSuccess() {
        progressbar_login.setVisibility(View.INVISIBLE);
        Intent mainMenu = new Intent(getBaseContext(), MenuActivity.class);
        startActivity(mainMenu);
        finish();
    }

    @Override
    public void onLoginFail(Map<String, Object> errorMap) {
        progressbar_login.setVisibility(View.INVISIBLE);
        etPassword.setError((String) errorMap.get("message"));
    }

    @Override
    public void OnHeartbeatSuccess() {

    }

    @Override
    public void OnHeartbeatFail() {

    }
}