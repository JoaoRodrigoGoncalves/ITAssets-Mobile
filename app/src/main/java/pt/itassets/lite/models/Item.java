package pt.itassets.lite.models;

public class Item {

    private Integer id, status, site_id;
    private String nome, serialNumber, notas, nome_categoria;

    public Item(Integer id, String nome, String serialNumber, String notas, Integer status, String nome_categoria, Integer site_id)
    {
        this.id = id;
        this.status = status;
        this.nome_categoria = nome_categoria;
        this.site_id = site_id;
        this.nome = nome;
        this.serialNumber = serialNumber;
        this.notas = notas;
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
}
