package pt.itassets.lite.views;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import pt.itassets.lite.R;
import pt.itassets.lite.models.Alocacao;
import pt.itassets.lite.models.Singleton;

public class DetalhesPedidoAlocacaoActivity extends AppCompatActivity {

    private TextView TV_id_pedido, TV_estado_pedido, TV_requerente, TV_data_pedido, TV_objeto,
                     TV_observacoes, TV_aprovador, TV_data_inicio, TV_data_fim,
                     TV_observacoes_resposta;

    private LinearLayout LL_dados_resposta;
    private Alocacao pedidoAlocacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pedido_alocacao);

        TV_id_pedido = findViewById(R.id.TV_id_pedido);
        TV_estado_pedido = findViewById(R.id.TV_estado_pedido);
        TV_requerente = findViewById(R.id.TV_requerente);
        TV_data_pedido = findViewById(R.id.TV_data_pedido);
        TV_objeto = findViewById(R.id.TV_objeto);
        TV_observacoes = findViewById(R.id.TV_observacoes);
        TV_aprovador = findViewById(R.id.TV_aprovador);
        TV_data_inicio = findViewById(R.id.TV_data_inicio);
        TV_data_fim = findViewById(R.id.TV_data_fim);
        TV_observacoes_resposta = findViewById(R.id.TV_observacoes_resposta);
        LL_dados_resposta = findViewById(R.id.LL_dados_resposta);

        Integer id_pedido = getIntent().getIntExtra("ID_PEDIDO", -1);

        if(id_pedido != -1)
        {
            pedidoAlocacao = Singleton.getInstance(this).getAlocacao(id_pedido);

            TV_id_pedido.setText(String.valueOf(pedidoAlocacao.getId()));
            TV_estado_pedido.setText(pedidoAlocacao.humanReadableStatus(this));
            TV_requerente.setText(String.valueOf(pedidoAlocacao.getNome_requerente()));
            TV_data_pedido.setText(String.valueOf(pedidoAlocacao.getDataPedido()));

            // Objeto

            TV_observacoes.setText(String.valueOf(pedidoAlocacao.getObs()));

            if(pedidoAlocacao.getStatus() != 10)
            {
                LL_dados_resposta.setVisibility(View.VISIBLE);

                // region Campo Aprovador
                if(pedidoAlocacao.getNome_aprovador() != null)
                {
                    TV_aprovador.setText(String.valueOf(pedidoAlocacao.getNome_aprovador()));
                }
                else
                {
                    TV_aprovador.setTypeface(TV_aprovador.getTypeface(), Typeface.ITALIC);
                    TV_aprovador.setText(R.string.txt_nao_aplicavel);
                }
                // endregion

                TV_data_inicio.setText(String.valueOf(pedidoAlocacao.getDataInicio()));

                // region Campo Data Fim
                if(pedidoAlocacao.getDataFim() != null)
                {
                    TV_data_fim.setText(String.valueOf(pedidoAlocacao.getDataFim()));
                }
                else
                {
                    TV_data_fim.setTypeface(TV_data_fim.getTypeface(), Typeface.ITALIC);
                    TV_data_fim.setText(R.string.txt_nao_aplicavel);
                }
                //endregion

                // region Campo Observações Resposta
                if(pedidoAlocacao.getObsResposta() != null)
                {
                    TV_observacoes_resposta.setText(String.valueOf(pedidoAlocacao.getObsResposta()));
                }
                else
                {
                    TV_observacoes_resposta.setTypeface(TV_observacoes_resposta.getTypeface(), Typeface.ITALIC);
                    TV_observacoes_resposta.setText(R.string.txt_nao_aplicavel);
                }
                //endregion

            }
        }
        else
        {
            Toast.makeText(this, "Pedido não encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}