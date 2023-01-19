package pt.itassets.lite.models;

public class Alocacao {

    private Integer id, status, nome_requerente, nome_aprovador;
    //private Date dataPedido, dataInicio, dataFim;
    private String obs, obsResposta, nome_item, nome_grupoItem,  dataPedido, dataInicio, dataFim;

    public Alocacao(Integer id, Integer status, String dataPedido, String dataInicio, String dataFim, String obs, String obsResposta, Integer nome_requerente, Integer nome_aprovador, String nome_item, String nome_grupoItem) {
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
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
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

    public Integer getNome_requerente() {
        return nome_requerente;
    }

    public void setNome_requerente(Integer nome_requerente) {this.nome_requerente = nome_requerente;}

    public Integer getNome_aprovador() {
        return nome_aprovador;
    }

    public void setNome_aprovador(Integer nome_aprovador) {
        this.nome_aprovador = nome_aprovador;
    }

    public String getNome_item() {
        return nome_item;
    }

    public void setNome_item(String nome_item) {
        this.nome_item = nome_item;
    }

    public String getNome_grupoItem() {
        return nome_item;
    }

    public void setNome_grupoItem(String nome_grupoItem) {
        this.nome_grupoItem = nome_grupoItem;
    }
}
