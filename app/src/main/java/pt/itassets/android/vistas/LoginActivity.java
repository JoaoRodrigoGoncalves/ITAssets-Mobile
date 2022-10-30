package pt.itassets.android.vistas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.DialogCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import pt.itassets.android.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // TODO: Apresentar apenas quando a aplicação não estiver configurada
        FragmentManager fm = getSupportFragmentManager();
        ConfigurarServerFragment csf = new ConfigurarServerFragment();
        csf.show(fm, null); // TAG?

    }

    public void onClick_btn_login(View view) {
        Intent mainMenu = new Intent(this, MenuMainActivity.class);
        startActivity(mainMenu);
        finish();
    }
}