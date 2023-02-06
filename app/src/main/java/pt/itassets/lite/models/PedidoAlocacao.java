package pt.itassets.lite.models;

import android.content.Context;

import pt.itassets.lite.R;

public class PedidoAlocacao {

    public static final int STATUS_ABERTO = 10;
    public static final int STATUS_APROVADO = 9;
    public static final int STATUS_NEGADO = 8;
    public static final int STATUS_DEVOLVIDO = 7;
    public static final int STATUS_CANCELADO = 0;

    private Integer id, status,userid;
    private String obs, obsResposta, nome_item, nome_grupoItem,  dataPedido, dataInicio, dataFim, nome_requerente, nome_aprovador;

    public PedidoAlocacao(Integer id, Integer status, String dataPedido, String dataInicio, String dataFim, String obs, String obsResposta, String nome_requerente, String nome_aprovador, String nome_item, String nome_grupoItem,Integer userid) {
        this.id = id;
        this.status = status;
        this.dataPedido = dataPedido;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.obs = obs;
        this.obsResposta = obsResposta;
        this.nome_requerente = nome_requerente;
        this.nome_aprovador = nome_aprovador;
        this.nome_item = nome_item;
        this.nome_grupoItem = nome_grupoItem;
        this.userid=userid;
    }

    public String humanReadableStatus(final Context context)
    {
        switch (this.status)
        {
            case STATUS_ABERTO:
                return context.getString(R.string.txt_aberto);

            case STATUS_APROVADO:
                return context.getString(R.string.txt_aprovado);

            case STATUS_NEGADO:
                return context.getString(R.string.txt_negado);

            case STATUS_DEVOLVIDO:
                return context.getString(R.string.txt_devolvido);

            case STATUS_CANCELADO:
                return context.getString(R.string.txt_cancelado);

            default:
                return String.valueOf(this.status);
        }
    }

    // region Getters e setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getObsResposta() {
        return obsResposta;
    }

    public void setObsResposta(String obsResposta) {
        this.obsResposta = obsResposta;
    }

    public String getNome_requerente() {
        return nome_requerente;
    }

    public void setNome_requerente(String nome_requerente) {this.nome_requerente = nome_requerente;}

    public String getNome_aprovador() {
        return nome_aprovador;
    }

    public void setNome_aprovador(String nome_aprovador) {
        this.nome_aprovador = nome_aprovador;
    }

    public String getNome_item() {
        return nome_item;
    }

    public void setNome_item(String nome_item) {
        this.nome_item = nome_item;
    }

    public String getNome_grupoItem() {
        return nome_grupoItem;
    }

    public void setNome_grupoItem(String nome_grupoItem) {
        this.nome_grupoItem = nome_grupoItem;
    }
    //endregion


}
