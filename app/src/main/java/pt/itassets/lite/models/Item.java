package pt.itassets.lite.models;

import android.content.Context;

public class Item {

    private Integer id;
    private Integer status;
    private Integer site_id;
    private Integer pedido_alocacao_id;

    public Integer getPedido_reparacao_id() {
        return pedido_reparacao_id;
    }

    public void setPedido_reparacao_id(Integer pedido_reparacao_id) {
        this.pedido_reparacao_id = pedido_reparacao_id;
    }

    private Integer pedido_reparacao_id;
    private String nome, serialNumber, notas, nome_categoria;

    public Item(Integer id, String nome, String serialNumber, String notas, Integer status, String nome_categoria, Integer site_id, Integer pedido_alocacao_id,Integer pedido_reparacao_id)
    {
        this.id = id;
        this.status = status;
        this.nome_categoria = nome_categoria;
        this.site_id = site_id;
        this.nome = nome;
        this.serialNumber = serialNumber;
        this.notas = notas;
        this.pedido_alocacao_id = pedido_alocacao_id;
        this.pedido_reparacao_id = pedido_reparacao_id;
    }

    // region Getters e Setters

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

    public String getNome_Categoria() {
        return nome_categoria;
    }

    public void setNome_categoria(String nome_categoria) {
        this.nome_categoria = nome_categoria;
    }

    public Integer getSite_id() {
        return site_id;
    }

    public void setSite_id(Integer site_id) {
        this.site_id = site_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

    //endregion

    public boolean isInGrupo(Context context)
    {
        return Singleton.getInstance(context).getActiveGrupoForItem(this.id) != null;
    }

}
