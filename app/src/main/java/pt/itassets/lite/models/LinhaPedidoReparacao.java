package pt.itassets.lite.models;

public class LinhaPedidoReparacao {

    private Integer id;
    private Integer id_reparacao;
    private Integer item_id;
    private Integer grupo_id;

    public LinhaPedidoReparacao(Integer id,Integer id_reparacao, Integer item_id, Integer grupo_id) {
        this.id=id;
        this.id_reparacao = id_reparacao;
        this.item_id = item_id;
        this.grupo_id = grupo_id;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getId_reparacao() {
        return id_reparacao;
    }

    public void setId_reparacao(Integer id_reparacao) {
        this.id_reparacao = id_reparacao;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public Integer getGrupo_id() {
        return grupo_id;
    }

    public void setGrupo_id(Integer grupo_id) {
        this.grupo_id = grupo_id;
    }
}
