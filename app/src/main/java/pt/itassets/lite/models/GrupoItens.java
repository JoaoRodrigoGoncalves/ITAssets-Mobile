package pt.itassets.lite.models;

public class GrupoItens {

    private Integer id, status, pedido_alocacao_id,pedido_reparacao_id;
    private String nome, notas;

    public GrupoItens(Integer id, Integer status, String nome, String notas, Integer pedido_alocacao_id,Integer pedido_reparacao_id) {
        this.id = id;
        this.status = status;
        this.nome = nome;
        this.notas = notas;
        this.pedido_alocacao_id = pedido_alocacao_id;
        this.pedido_reparacao_id=pedido_reparacao_id;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Integer getPedido_alocacao_id() {
        return pedido_alocacao_id;
    }

    public void setPedido_alocacao_id(Integer pedido_alocacao_id) {
        this.pedido_alocacao_id = pedido_alocacao_id;
    }

    public Integer getPedido_reparacao_id() {
        return pedido_reparacao_id;
    }

    public void setPedido_reparacao_id(Integer pedido_reparacao_id) {
        this.pedido_reparacao_id = pedido_reparacao_id;
    }
}
