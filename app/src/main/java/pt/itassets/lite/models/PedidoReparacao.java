package pt.itassets.lite.models;

import android.content.Context;

import pt.itassets.lite.R;

public class PedidoReparacao
{
    private Integer id, status;
    private String descricaoProblema, respostaObs, dataPedido, dataInicio, dataFim, nome_requerente, nome_responsavel;

    public static final int STATUS_ABERTO = 8;
    public static final int STATUS_EM_PREPARACAO = 10;
    public static final int STATUS_CONCLUIDO = 4;
    public static final int STATUS_EM_REVISAO = 6;
    public static final int STATUS_CANCELADO = 0;

    public PedidoReparacao(
            Integer id, String nome_requerente, String nome_responsavel, Integer status,
            String descricaoProblema, String respostaObs, String dataPedido, String dataInicio,
            String dataFim)
    {
        this.id = id;
        this.nome_requerente = nome_requerente;
        this.nome_responsavel = nome_responsavel;
        this.status = status;
        this.descricaoProblema = descricaoProblema;
        this.respostaObs = respostaObs;
        this.dataPedido = dataPedido;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public String humanReadableStatus(final Context context)
    {
        switch (this.status)
        {
            case STATUS_ABERTO:
                return context.getString(R.string.txt_aberto);

            case STATUS_EM_PREPARACAO:
                return context.getString(R.string.txt_emPreparacao);

            case STATUS_CONCLUIDO:
                return context.getString(R.string.txt_concluido);

            case STATUS_EM_REVISAO:
                return context.getString(R.string.txt_emRevisao);

            case STATUS_CANCELADO:
                return context.getString(R.string.txt_cancelado);

            default:
                return String.valueOf(this.status);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome_requerente() {
        return nome_requerente;
    }

    public void setNome_requerente(String nome_requerente) {
        this.nome_requerente = nome_requerente;
    }

    public String getNome_responsavel() {
        return nome_responsavel;
    }

    public void setNome_responsavel(String nome_responsavel) {
        this.nome_responsavel = nome_responsavel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescricaoProblema() {
        return descricaoProblema;
    }

    public void setDescricaoProblema(String descricaoProblema) {
        this.descricaoProblema = descricaoProblema;
    }

    public String getRespostaObs() {
        return respostaObs;
    }

    public void setRespostaObs(String respostaObs) {
        this.respostaObs = respostaObs;
    }

    public String getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(String dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }
}
