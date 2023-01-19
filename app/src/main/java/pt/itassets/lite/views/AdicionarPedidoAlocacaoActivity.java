package pt.itassets.lite.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import pt.itassets.lite.R;

public class AdicionarPedidoAlocacaoActivity extends AppCompatActivity {
    private RadioGroup radioBTNGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_pedido_alocacao);

        radioBTNGrupo = findViewById(R.id.radiogroup);

        addRadioButtons(20);
    }

    public void addRadioButtons(int number) {
        radioBTNGrupo.setOrientation(LinearLayout.VERTICAL);
        //
        for (int i = 1; i <= number; i++) {
            RadioButton rdbtn = new RadioButton(this);
            rdbtn.setId(View.generateViewId());
            rdbtn.setText("Radio " + rdbtn.getId());
//            rdbtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(AdicionarPedidoAlocacaoActivity.this, v., Toast.LENGTH_SHORT).show();
//                }
//            });
            radioBTNGrupo.addView(rdbtn);
        }

        radioBTNGrupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Toast.makeText(AdicionarPedidoAlocacaoActivity.this, "Selecionado: " + String.valueOf(checkedId), Toast.LENGTH_SHORT).show();
            }
        });
    }

}