package pt.itassets.lite.views.reparacao;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pt.itassets.lite.R;
import pt.itassets.lite.adapters.ListaGruposItensAdaptador;
import pt.itassets.lite.adapters.ListaItensAdaptador;
import pt.itassets.lite.listeners.ItensListener;
import pt.itassets.lite.listeners.OperacoesPedidoReparacaoListener;
import pt.itassets.lite.models.GrupoItens;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.PedidoReparacao;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.utils.Helpers;
import pt.itassets.lite.views.MenuActivity;


public class DetalhesReparacaoActivity extends AppCompatActivity implements OperacoesPedidoReparacaoListener, ItensListener {

    private TextView TV_id_pedido, TV_estado_pedido, TV_requerente, TV_data_pedido,
            TV_Descricao, TV_Responsavel, TV_data_inicio, TV_data_fim,
            TV_observacoes_resposta,TV_Sem_Dados;
    private Button btn_finalizar, btn_Cancelar;
    private ListView LV_Reparacoes,LV_Grupo;
    private LinearLayout LL_dados_resposta;
    private PedidoReparacao pedidoReparacao;
    private SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private ArrayList<GrupoItens> grupoItens = null;
    private ArrayList<Item> item=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_detalhes_reparacao);

        TV_id_pedido = findViewById(R.id.TV_id_pedido_reparacao);
        TV_estado_pedido = findViewById(R.id.TV_estado_pedido);
        TV_requerente = findViewById(R.id.TV_requerente);
        TV_Responsavel = findViewById(R.id.TV_reponsavel);
        TV_data_pedido = findViewById(R.id.TV_data_pedido);
        TV_data_inicio = findViewById(R.id.TV_data_inicio);
        TV_data_fim = findViewById(R.id.TV_data_fim);
        LV_Reparacoes = findViewById(R.id.LV_objeto);
        TV_Descricao = findViewById(R.id.TV_descricao);
        TV_observacoes_resposta = findViewById(R.id.TV_observacoes_resposta);
        btn_Cancelar = findViewById(R.id.btnCancelar);
        btn_finalizar = findViewById(R.id.btnFinalizar);
        TV_Sem_Dados=findViewById(R.id.TV_sem_dados);
        LV_Grupo=findViewById(R.id.LV_Grupo);
        LL_dados_resposta = findViewById(R.id.LL_dados_resposta);

        Integer id_reparacao = getIntent().getIntExtra("ID_REPARACAO", -1);

        if (id_reparacao != -1) {
            setTitle(getString(R.string.Titulo_reparacao) + id_reparacao);
            pedidoReparacao = Singleton.getInstance(this).getReparacao(id_reparacao);

            TV_id_pedido.setText(String.valueOf(pedidoReparacao.getId()));
            TV_estado_pedido.setText(pedidoReparacao.humanReadableStatus(this));
            TV_requerente.setText(String.valueOf(pedidoReparacao.getNome_requerente()));
            TV_data_pedido.setText(String.valueOf(pedidoReparacao.getDataPedido()));
            //objeto

            grupoItens=Singleton.getInstance(this).getGrupodeItemdoPedidoReparacao(pedidoReparacao.getId());
            item=Singleton.getInstance(this).getItensdoPedidoReparacao(pedidoReparacao.getId());


            if (grupoItens.size()==0 && item.size()==0)
            {
                TV_Sem_Dados.setVisibility(View.VISIBLE);
                TV_Sem_Dados.setText(R.string.itens_nao_associados);
            }
            else{

                if (grupoItens.size()!=0)
                {
                    onRefreshListaGrupoItens(grupoItens);
                }

                if (item.size()!=0)
                {
                    onRefreshListaItens(item);
                }
            }

            if (pedidoReparacao.getDataPedido() != null) {
                TV_data_inicio.setText(String.valueOf(pedidoReparacao.getDataInicio()));
            }

            // region Campo Observações Resposta
            if (pedidoReparacao.getDescricaoProblema() != null) {
                TV_Descricao.setText(String.valueOf(pedidoReparacao.getDescricaoProblema()));
            } else {
                TV_Descricao.setTypeface(TV_Descricao.getTypeface(), Typeface.ITALIC);
                TV_Descricao.setText(R.string.txt_nao_aplicavel);
            }

            //Botão
            btn_Cancelar.setVisibility(View.VISIBLE);

            // Aprovador
            if (pedidoReparacao.getStatus() != 10 || pedidoReparacao.getStatus() != 8) {
                LL_dados_resposta.setVisibility(View.VISIBLE);
                //btn_Cancelar.setVisibility(View.INVISIBLE);
                btn_finalizar.setVisibility(View.VISIBLE);

                // region Campo Responsavel
                if (pedidoReparacao.getNome_responsavel() != null) {
                    TV_Responsavel.setText(String.valueOf(pedidoReparacao.getNome_responsavel()));
                } else {
                    TV_Responsavel.setTypeface(TV_Responsavel.getTypeface(), Typeface.ITALIC);
                    TV_Responsavel.setText(R.string.txt_nao_aplicavel);
                }
                // endregion

                // region Campo Data Inicio
                if (pedidoReparacao.getDataInicio() != null) {
                    TV_data_inicio.setText(String.valueOf(pedidoReparacao.getDataFim()));
                } else {
                    TV_data_inicio.setTypeface(TV_data_inicio.getTypeface(), Typeface.ITALIC);
                    TV_data_inicio.setText(R.string.txt_nao_aplicavel);
                }
                //endregion

                // region Campo Data Fim
                if (pedidoReparacao.getDataFim() != null) {
                    TV_data_fim.setText(String.valueOf(pedidoReparacao.getDataFim()));
                } else {
                    TV_data_fim.setTypeface(TV_data_fim.getTypeface(), Typeface.ITALIC);
                    TV_data_fim.setText(R.string.txt_nao_aplicavel);
                }
                //endregion

                // region Campo Observações Resposta
                if (pedidoReparacao.getRespostaObs() != null) {
                    TV_observacoes_resposta.setText(String.valueOf(pedidoReparacao.getRespostaObs()));
                } else {
                    TV_observacoes_resposta.setTypeface(TV_observacoes_resposta.getTypeface(), Typeface.ITALIC);
                    TV_observacoes_resposta.setText(R.string.txt_nao_aplicavel);
                }
                //endregion
            }
        } else {
            Toast.makeText(this, R.string.txt_erro_pedido_reparacao_nao_encontrado, Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    public void onClick_btn_finalizar(View view) {
        Date data = new Date();
        String dataFormatada = formatoData.format(data);

        Singleton.getInstance(getApplicationContext()).setOperacoesPedidoReparacaoListener(this);

        if (pedidoReparacao.getId() == -1) {
            Toast.makeText(getApplicationContext(), getString(R.string.txt_erro_pedido_reparacao_nao_encontrado), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            pedidoReparacao = Singleton.getInstance(getBaseContext()).getReparacao(pedidoReparacao.getId());

            if (pedidoReparacao != null) {
                if (isPedidoReparacaoFinalizarValido()) {
                    pedidoReparacao.setDataFim(dataFormatada);
                    pedidoReparacao.setDataInicio(dataFormatada);
                    pedidoReparacao.setStatus(PedidoReparacao.STATUS_CONCLUIDO);

                    Intent intent = new Intent(getBaseContext(), FinalizarPedidoReparacaoActivity.class);
                    intent.putExtra("ID_REPARACAO", pedidoReparacao.getId());
                    startActivityForResult(intent, Helpers.OPERACAO_DETALHES); //Método Deprecated
                }
            }
        }
    }

    private boolean isPedidoReparacaoFinalizarValido() {
        Integer estado = pedidoReparacao.getStatus();

        if (estado != PedidoReparacao.STATUS_EM_REVISAO) {
            Toast.makeText(getApplicationContext(), R.string.txt_erro_pedido_reparacao_finalizado, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onClick_btn_cancelar(View view) {
        if (isPedidoReparacaoCancelarValido()) {
            dialogRemover();
        }
    }

    //Dialog para perguntar se o user pretende mesmo cancelar o Pedido de Alocação
    private void dialogRemover() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.txt_remover_reparacao))
                .setMessage(R.string.txt_confirmar_remover_pedido_reparacao)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Singleton.getInstance(getApplicationContext()).RemoverReparacaoAPI(pedidoReparacao, getApplicationContext());
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

    private boolean isPedidoReparacaoCancelarValido() {
        Integer estado = pedidoReparacao.getStatus();

        if (estado != PedidoReparacao.STATUS_EM_PREPARACAO && estado != PedidoReparacao.STATUS_ABERTO) {
            Toast.makeText(getApplicationContext(), getString(R.string.txt_erro_pedido_reparacao_cancelado), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onReparacaoOperacaoRefresh(int operacao) {
        Intent intent = new Intent();
        intent.putExtra(Helpers.OPERACAO, operacao);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRefreshListaItens(ArrayList<Item> listaItens) {
        if (listaItens.size()!=0)
        {
            LV_Reparacoes.setAdapter(new ListaItensAdaptador(this, listaItens));
        }
    }


    public void onRefreshListaGrupoItens(ArrayList<GrupoItens> listaGrupoItens) {
        if (listaGrupoItens.size()!=0)
        {
            LV_Grupo.setAdapter(new ListaGruposItensAdaptador(this, listaGrupoItens));
        }
    }
}