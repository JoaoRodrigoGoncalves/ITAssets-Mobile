package pt.itassets.lite.models;

public class GrupoItens {

    private Integer id, status;
    private String nome, notas;

    public GrupoItens(Integer id, Integer status, String nome, String notas) {
        this.id = id;
        this.status = status;
        this.nome = nome;
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
}
