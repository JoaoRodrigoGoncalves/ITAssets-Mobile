package pt.itassets.lite.models;

public class PedidoReparacao
{
    private Integer id, requerente_id, responsavel_id, status;
    private String descricaoProblema, respostaObs, dataPedido, dataInicio, dataFim;

    public PedidoReparacao(
            Integer id, Integer requerente_id, Integer responsavel_id, Integer status,
            String descricaoProblema, String respostaObs, String dataPedido, String dataInicio,
            String dataFim)
    {
        this.id = id;
        this.requerente_id = requerente_id;
        this.responsavel_id = responsavel_id;
        this.status = status;
        this.descricaoProblema = descricaoProblema;
        this.respostaObs = respostaObs;
        this.dataPedido = dataPedido;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRequerente_id() {
        return requerente_id;
    }

    public void setRequerente_id(Integer requerente_id) {
        this.requerente_id = requerente_id;
    }

    public Integer getResponsavel_id() {
        return responsavel_id;
    }

    public void setResponsavel_id(Integer responsavel_id) {
        this.responsavel_id = responsavel_id;
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
