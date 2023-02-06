package pt.itassets.lite.views.alocacao;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import pt.itassets.lite.R;
import pt.itassets.lite.listeners.OperacoesPedidoAlocacaoListener;
import pt.itassets.lite.models.PedidoAlocacao;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;
import pt.itassets.lite.views.MenuActivity;

public class DetalhesPedidoAlocacaoActivity extends AppCompatActivity implements OperacoesPedidoAlocacaoListener {

    private TextView TV_id_pedido, TV_estado_pedido, TV_requerente, TV_data_pedido, TV_objeto,
                     TV_observacoes, TV_aprovador, TV_data_inicio, TV_data_fim,
                     TV_observacoes_resposta;
    private Button btn_Devolver, btn_Cancelar;
    private LinearLayout LL_dados_resposta;
    private PedidoAlocacao pedidoAlocacao;
    private SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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
        btn_Devolver = findViewById(R.id.btnDevolver);
        btn_Cancelar = findViewById(R.id.btnCancelar);


        Integer id_pedido = getIntent().getIntExtra("ID_PEDIDO", -1);

        if(id_pedido != -1)
        {
            setTitle("Pedido Alocação Nº" + id_pedido);
            pedidoAlocacao = Singleton.getInstance(this).getAlocacao(id_pedido);

            TV_id_pedido.setText(String.valueOf(pedidoAlocacao.getId()));
            TV_estado_pedido.setText(pedidoAlocacao.humanReadableStatus(this));
            TV_requerente.setText(String.valueOf(pedidoAlocacao.getNome_requerente()));
            TV_data_pedido.setText(String.valueOf(pedidoAlocacao.getDataPedido()));

            // Objeto

            if(pedidoAlocacao.getNome_item() != null)
            {
                TV_objeto.setText(String.valueOf(pedidoAlocacao.getNome_item()));
            }
            else
            {
                TV_objeto.setText(String.valueOf(pedidoAlocacao.getNome_grupoItem()));
            }

            //OBS
            if(pedidoAlocacao.getObs() != null)
            {
                TV_observacoes.setText(String.valueOf(pedidoAlocacao.getObs()));
            }
            else
            {
                TV_observacoes.setTypeface(TV_observacoes.getTypeface(), Typeface.ITALIC);
                TV_observacoes.setText(R.string.txt_nao_aplicavel);
            }

            //Botão
            btn_Cancelar.setVisibility(View.VISIBLE);

            if(pedidoAlocacao.getStatus() != 10)
            {
                LL_dados_resposta.setVisibility(View.VISIBLE);
                btn_Cancelar.setVisibility(View.INVISIBLE);

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

                SharedPreferences preferences = getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);
                if(pedidoAlocacao.getStatus() == PedidoAlocacao.STATUS_APROVADO && !Objects.equals(preferences.getString(Helpers.USER_ROLE, null), "funcionario"))
                {
                    // Se o pedido estiver aprovado e o role não for funcionário
                    btn_Devolver.setVisibility(View.VISIBLE);
                }
            }
        }
        else
        {
            Toast.makeText(this, R.string.txt_erro_pedido_alocacao_nao_encontrado, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onClick_btn_devolver(View view) {
        Date data = new Date();
        String dataFormatada = formatoData.format(data);

        Singleton.getInstance(getApplicationContext()).setOperacoesPedidoAlocacaoListener(this);

        if(pedidoAlocacao.getId() == -1)
        {
            Toast.makeText(this, R.string.txt_erro_pedido_alocacao_nao_encontrado, Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            pedidoAlocacao = Singleton.getInstance(getBaseContext()).getAlocacao(pedidoAlocacao.getId());

            if(pedidoAlocacao != null){
                if (isPedidoAlocacaoDevolverValido()) {
                    if (pedidoAlocacao != null) {
                        pedidoAlocacao.setDataFim(dataFormatada);
                        pedidoAlocacao.setStatus(7);

                        Singleton.getInstance(getApplicationContext()).EditarAlocacaoAPI(pedidoAlocacao, getApplicationContext());

//                        Intent intent = new Intent(getBaseContext(), MenuActivity.class);
//                        startActivityForResult(intent, Helpers.OPERACAO_EDIT); //Método Deprecated
                    }
                }
            }
        }
    }

    public void onClick_btn_cancelar(View view) {
        if (isPedidoAlocacaoCancelarValido()) {
            dialogRemover();
        }
    }

    //Dialog para perguntar se o user pretende mesmo cancelar o Pedido de Alocação
    private void dialogRemover(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.txt_remover_alocacao))
                .setMessage(R.string.txt_confirm_remover_alocacao)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Singleton.getInstance(getApplicationContext()).RemoverAlocacaoAPI(pedidoAlocacao, getApplicationContext());
                        Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                        startActivityForResult(intent, Helpers.OPERACAO_DETALHES); //Método Deprecated
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Não necessita de se inserir nada
                    }
                })
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }

    private boolean isPedidoAlocacaoDevolverValido(){
        Integer estado = pedidoAlocacao.getStatus();

        if(estado != PedidoAlocacao.STATUS_APROVADO) {
            Toast.makeText(getApplicationContext(), getString(R.string.txt_erro_pedido_alocacao_devolver), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isPedidoAlocacaoCancelarValido(){
        Integer estado = pedidoAlocacao.getStatus();

        if(estado != PedidoAlocacao.STATUS_ABERTO) {
            Toast.makeText(getApplicationContext(), getString(R.string.txt_erro_pedido_alocacao_cancelar), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onAlocacaoOperacaoRefresh(int operacao) {
        Intent intent = new Intent();
        intent.putExtra(Helpers.OPERACAO, operacao);
        setResult(RESULT_OK, intent);
        finish();
    }
}