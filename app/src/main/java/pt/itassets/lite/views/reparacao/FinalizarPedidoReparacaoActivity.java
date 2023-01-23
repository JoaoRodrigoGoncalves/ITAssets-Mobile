package pt.itassets.lite.views.reparacao;

import static pt.itassets.lite.views.ListaItensFragment.ACTION_DETALHES;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

import pt.itassets.lite.R;
import pt.itassets.lite.listeners.OperacoesPedidoReparacaoListener;
import pt.itassets.lite.models.PedidoReparacao;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;
import pt.itassets.lite.views.MenuActivity;

public class FinalizarPedidoReparacaoActivity extends AppCompatActivity implements OperacoesPedidoReparacaoListener {

    TextInputLayout tf_respostaObs;
    TextView tv_Id;
    private SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private PedidoReparacao pedidoReparacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_pedido_reparacao);

        Singleton.getInstance(getApplicationContext()).setOperacoesPedidoReparacaoListener(this);

        tf_respostaObs = findViewById(R.id.tf_respostaObs);
        tv_Id = findViewById(R.id.tvId);

        Integer id_reparacao=getIntent().getIntExtra("ID_REPARACAO",-1);

        if (id_reparacao!=-1)
        {
            setTitle(getString(R.string.Titulo_reparacao)+id_reparacao);
            pedidoReparacao= Singleton.getInstance(this).getReparacao(id_reparacao);

            tv_Id.setText(String.valueOf(pedidoReparacao.getId()));
        }
        else
        {
            Toast.makeText(this, R.string.txt_erro_pedido_reparacao_nao_encontrado, Toast.LENGTH_SHORT).show();
            finish();
        }
        tv_Id.setText(String.valueOf(pedidoReparacao.getId()));
    }

    public void onClick_btn_confirmar_finalizar(View view) {
        Date data = new Date();
        String dataFormatada = formatoData.format(data);

        Singleton.getInstance(getApplicationContext()).setOperacoesPedidoReparacaoListener(this);

        pedidoReparacao.setRespostaObs(tf_respostaObs.getEditText().getText().toString().trim());
        pedidoReparacao.setDataFim(dataFormatada);
        pedidoReparacao.setStatus(4);

        Singleton.getInstance(getApplicationContext()).EditarReparacaoAPI(pedidoReparacao, getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivityForResult(intent, ACTION_DETALHES); //Método Deprecated
    }

    @Override
    public void onReparacaoOperacaoRefresh(int operacao) {
        Intent intent = new Intent();
        intent.putExtra(Helpers.OPERACAO, operacao);
        setResult(RESULT_OK, intent);
        finish();
    }
}