package pt.itassets.lite.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Map;
import java.util.concurrent.Executor;

import pt.itassets.lite.R;
import pt.itassets.lite.listeners.LoginListener;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;

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
            if(preferences.getString(Helpers.USER_TOKEN, null) != null && Helpers.IsBiometricAvailable(this))
            {
                if(Helpers.isInternetConnectionAvailable(this))
                {
                    //Enviar um pedido para validar que o token ainda é válido
                    progressbar_login.setVisibility(View.VISIBLE);
                    Singleton.getInstance(this).sendHeartbeat(this);
                }
                else
                {
                    OnHeartbeatSuccess(); // Não há internet. Entrar com biometria.
                }
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
            etEmail.setError(getString(R.string.txt_email_invalido));
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
        progressbar_login.setVisibility(View.INVISIBLE);

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt bioPrompt = new androidx.biometric.BiometricPrompt(this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent abrirAppLoginAnteriorValido = new Intent(getBaseContext(), MenuActivity.class);
                startActivity(abrirAppLoginAnteriorValido);
                finish();
            }
        });

        BiometricPrompt.PromptInfo.Builder prompt = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.txt_coloque_dedo_sensor_title))
                .setDescription(getString(R.string.txt_coloque_dedo_sensor_description))
                .setNegativeButtonText(getString(R.string.txt_cancelar));

        bioPrompt.authenticate(prompt.build());
    }

    @Override
    public void OnHeartbeatFail() {
        progressbar_login.setVisibility(View.INVISIBLE);
        Toast.makeText(this, getString(R.string.txt_token_invalido), Toast.LENGTH_SHORT).show();
    }
}