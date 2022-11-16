package pt.itassets.android.modelos;

public class Itens {
        private int id, numserie, capa;
        private String nome, categoria;

        public Itens(int id, int numserie, int capa, String nome, String categoria) {
            this.id = id;
            this.numserie = numserie;
            this.capa = capa;
            this.nome = nome;
            this.categoria = categoria;
        }

        public int getId() {
            return id;
        }

    /*public void setId(int id) {
        this.id = id;
    }*/

        public int getNumserie() {
            return numserie;
        }

        public void setNumserie(int numserie) {

            this.numserie = numserie;
        }

        public int getCapa() {
            return capa;
        }

        public void setCapa(int capa) {
            this.capa = capa;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }
    }
