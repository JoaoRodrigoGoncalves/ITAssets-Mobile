package pt.itassets.lite.models;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.itassets.lite.listeners.ItensListener;
import pt.itassets.lite.utils.Helpers;
import pt.itassets.lite.utils.JSONParsers;

public class Singleton {
    private ArrayList<Item> itens;
    private ArrayList<Categoria> categorias;
    private static Singleton instance=null;
    private static RequestQueue volleyQueue=null;
    private DBHelper database = null;
    private ItensListener itensListener;
//    private DetalhesItensListener detalhesItensListener;
    private String SYSTEM_DOMAIN = null;

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
        database = new DBHelper(context);
        volleyQueue = Volley.newRequestQueue(context);
    }

    public void addRequestToQueue(Request request) {
        volleyQueue.add(request);
    }

    //region Listeners

    public void setItensListener(ItensListener itensListener){
        this.itensListener = itensListener;
    }

    //endregion

    //region Funções Interação com tabela local de Itens

    public ArrayList<Item> getItensBD() {
        itens = database.getAllItensDB();
        return new ArrayList(itens);
    }

    public Item getItem(Integer id){
        for(Item i:itens){
            if(i.getId()==id){
                return i;
            }
        }
        return null;
    }

    public void adicionarItensBD(ArrayList<Item>itens){
        database.removerAllItemDB();
        for(Item i : itens){
            adicionarItemBD(i);
        }
    }

    public void adicionarItemBD(Item i){
        database.adicionarItemDB(i);
    }

    public void editarItemBD(Item i){
        Item auxItem = getItem(i.getId());
        if(auxItem!=null){
            database.editarItemDB(i);
        }
    }

    public void removerItemBD(int id){
        Item auxItem = getItem(id);
        if(auxItem != null){
            database.removerItemDB(auxItem);
        }
    }

    //endregion

    //region Funções interação com tabela local de Categorias

    public Categoria getCategoria(Integer id){
        Categoria aux = database.getCategoriaDB(id);
        return aux;
    }

    public void adicionarCategoriaBD(Categoria i){
        database.adicionarCategoriaDB(i);
    }

    public void editarCategoriaBD(Categoria i){
        Categoria auxItem = getCategoria(i.getId());
        if(auxItem!=null){
            database.editarCategoriaDB(i);
        }
    }


    /**
     * Esta função Adiciona ou edita uma categoria na base de dados
     * interna. Faz as verificações necessárias e decide qual operação a
     * executar.
     */
    public void atualizarCategoriaDB(Categoria categoria)
    {
        if(getCategoria(categoria.getId()) == null)
        {
            //Nova categoria
            adicionarCategoriaBD(categoria);
        }
        else
        {
            //Editar
            editarCategoriaBD(categoria);
        }
    }


    //endregion

    public void getAllItensAPI(final Context context){
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, "Sem ligação à internet!", Toast.LENGTH_LONG).show();

                if (itensListener != null) {
                    itensListener.onRefreshListaItens(database.getAllItensDB());
                }
            } else {
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, SYSTEM_DOMAIN + "item", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        itens = JSONParsers.parserJsonItens(response, context);
                        adicionarItensBD(itens);
                        if (itensListener != null) {
                            itensListener.onRefreshListaItens(itens);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json; charset=UTF-8");
                        params.put("Authorization", "Bearer " + preferences.getString(Helpers.USER_TOKEN, null));
                        return params;
                    }
                };
                volleyQueue.add(req);
            }
        }
    }
}
