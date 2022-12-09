package pt.itassets.android.modelos;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class Singleton {
    private final ArrayList<Item> itens;
    private static Singleton instance=null;
    private static RequestQueue volleyQueue=null;
    //private LivroDBHelper livrosDB = null;

    public static synchronized Singleton getInstance(Context context){
        if(instance == null){
            instance = new Singleton(context);
            volleyQueue = Volley.newRequestQueue(context);
            volleyQueue.start();
        }
        return instance;
    }

    private Singleton(Context context){
        itens = new ArrayList<>();
        volleyQueue = Volley.newRequestQueue(context);
        //livrosDB = new LivroDBHelper(context);
    }

    public void addRequestToQueue(Request request)
    {
        volleyQueue.add(request);
    }

    /*
    public ArrayList<Livro> getLivrosBD() {
        livros = livrosDB.getAllLivrosDB();
        return new ArrayList(livros);
    }

    public Livro getLivro(int id){
        for(Livro l:livros){
            if(l.getId()==id){
                return l;
            }
        }
        return null;
    }

    public void adicionarLivroBD(Livro l){
        Livro auxLivro = livrosDB.adicionarLivroDB(l);
        if(auxLivro != null){
            livros.add(auxLivro);
        }
    }

    public void editarLivroBD(Livro l){
        Livro auxLivro = getLivro(l.getId());
        if(auxLivro!=null){
            if(livrosDB.editarLivroDB(l)) {
                auxLivro.setTitulo(l.getTitulo());
                auxLivro.setAno(l.getAno());
                auxLivro.setAutor(l.getAutor());
                auxLivro.setSerie(l.getSerie());
            }
        }
    }

    public void removerLivroBD(int id){
        Livro auxLivro = getLivro(id);
        if(auxLivro != null){
            if(livrosDB.removerLivroDB(getLivro(id))) {
                livros.remove(auxLivro);
            }
        }
    }
    */

    public ArrayList<Item> getItens() {
        return new ArrayList<>(itens);
    }


}
