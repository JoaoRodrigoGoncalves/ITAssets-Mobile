package pt.itassets.lite.models;

public class Site {

    private Integer id;
    private String nome, morada, coordenadas;

    public Site(Integer id, String nome, String morada, String coordenadas) {
        this.id = id;
        this.nome = nome;
        this.morada = morada;
        this.coordenadas = coordenadas;
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

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }
}
