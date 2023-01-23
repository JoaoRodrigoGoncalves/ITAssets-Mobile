package pt.itassets.lite.views.reparacao;

import static pt.itassets.lite.views.ListaItensFragment.ACTION_DETALHES;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import pt.itassets.lite.R;
import pt.itassets.lite.models.Alocacao;
import pt.itassets.lite.models.PedidoReparacao;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;
import pt.itassets.lite.views.MenuActivity;

public class DetalhesReparacaoActivity extends AppCompatActivity {

    private TextView TV_id_pedido, TV_estado_pedido, TV_requerente, TV_data_pedido, TV_objeto,
            TV_Descricao, TV_Responsavel, TV_data_inicio, TV_data_fim,
            TV_observacoes_resposta;
    private Button btn_finalizar, btn_Cancelar;
    private LinearLayout LL_dados_resposta;
    private PedidoReparacao pedidoReparacao;
    private SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_detalhes_reparacao);

        TV_id_pedido=findViewById(R.id.TV_id_pedido_reparacao);
        TV_estado_pedido=findViewById(R.id.TV_estado_pedido);
        TV_requerente=findViewById(R.id.TV_requerente);
        TV_Responsavel=findViewById(R.id.TV_reponsavel);
        TV_data_pedido=findViewById(R.id.TV_data_pedido);
        TV_data_inicio=findViewById(R.id.TV_data_inicio);
        TV_data_fim=findViewById(R.id.TV_data_fim);
        TV_objeto=findViewById(R.id.TV_objeto);
        TV_Descricao=findViewById(R.id.TV_descricao);
        TV_observacoes_resposta=findViewById(R.id.TV_observacoes_resposta);
        btn_Cancelar=findViewById(R.id.btnCancelar);
        btn_finalizar=findViewById(R.id.btnFinalizar);
        LL_dados_resposta=findViewById(R.id.LL_dados_resposta);

        Integer id_reparacao=getIntent().getIntExtra("ID_REPARACAO",-1);

        if (id_reparacao!=-1)
        {
            setTitle(getString(R.string.Titulo_reparacao)+id_reparacao);
            pedidoReparacao= Singleton.getInstance(this).getReparacao(id_reparacao);

            TV_id_pedido.setText(String.valueOf(pedidoReparacao.getId()));
            TV_estado_pedido.setText(pedidoReparacao.humanReadableStatus(this));
            TV_requerente.setText(String.valueOf(pedidoReparacao.getNome_Requerente()));
            TV_Responsavel.setText(String.valueOf(pedidoReparacao.getNome_Responsavel()));
            TV_data_pedido.setText(String.valueOf(pedidoReparacao.getDataPedido()));
            //objeto

            //obter o objeto
            /*if (pedidoReparacao.)*/

            if (pedidoReparacao.getDataPedido()!=null)
            {
                TV_data_inicio.setText(String.valueOf(pedidoReparacao.getDataInicio()));
            }


            // region Campo Observações Resposta
            if(pedidoReparacao.getDescricaoProblema() != null)
            {
                TV_observacoes_resposta.setText(String.valueOf(pedidoReparacao.getDescricaoProblema()));
            }
            else
            {
                TV_observacoes_resposta.setTypeface(TV_observacoes_resposta.getTypeface(), Typeface.ITALIC);
                TV_observacoes_resposta.setText(R.string.txt_nao_aplicavel);
            }

            // Aprovador
           if (pedidoReparacao.getStatus()!=10)
           {
               LL_dados_resposta.setVisibility(View.VISIBLE);
               btn_Cancelar.setVisibility(View.INVISIBLE);
               btn_finalizar.setVisibility(View.VISIBLE);

               //TODO: Mostar o nome ao invés do ID
               if(pedidoReparacao.getNome_Responsavel() != null)
               {
                   TV_Responsavel.setText(String.valueOf(pedidoReparacao.getNome_Responsavel()));
               }
               else
               {
                   TV_Responsavel.setTypeface(TV_Responsavel.getTypeface(), Typeface.ITALIC);
                   TV_Responsavel.setText(R.string.txt_nao_aplicavel);
               }
               // endregion

               TV_data_inicio.setText(String.valueOf(pedidoReparacao.getDataInicio()));

               // region Campo Data Fim
               if(pedidoReparacao.getDataFim() != null)
               {
                   TV_data_fim.setText(String.valueOf(pedidoReparacao.getDataFim()));
               }
               else
               {
                   TV_data_fim.setTypeface(TV_data_fim.getTypeface(), Typeface.ITALIC);
                   TV_data_fim.setText(R.string.txt_nao_aplicavel);
               }
               //endregion

               // region Campo Observações Resposta
               if(pedidoReparacao.getRespostaObs() != null)
               {
                   TV_observacoes_resposta.setText(String.valueOf(pedidoReparacao.getRespostaObs()));
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

    public void onClick_btn_finalizar()
    {

    }

    public void onClick_btn_cancelar(View view)
    {

    }

}