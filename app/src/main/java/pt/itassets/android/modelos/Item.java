package pt.itassets.android.modelos;

public class Item {

    private int id, status, categoria_id;
    private String serialNumber, notas, nome;

    public Item(int id, int status, String serialNumber, int categoria_id, String notas, String nome) {
        this.id = id;
        this.status = status;
        this.serialNumber = serialNumber;
        this.categoria_id = categoria_id;
        this.notas = notas;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(int categoria_id) {
        this.categoria_id = categoria_id;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
