package pt.itassets.lite.models;

public class Categoria {

    private Integer id, status;
    private String nome;

    public Categoria(Integer id, String nome, Integer status) {
        this.id = id;
        this.nome = nome;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
