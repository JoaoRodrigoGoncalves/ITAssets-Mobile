package pt.itassets.android.modelos;

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

import pt.itassets.android.listeners.DetalhesItensListener;
import pt.itassets.android.listeners.ItensListener;
import pt.itassets.android.utils.ApiJsonParser;
import pt.itassets.android.utils.Helper;
import pt.itassets.android.vistas.MenuMainActivity;

public class Singleton {
    private ArrayList<Item> itens;
    private static Singleton instance=null;
    private static RequestQueue volleyQueue=null;
    private DBHelper itensDB = null;
    private ItensListener itensListener;
    private DetalhesItensListener detalhesItensListener;
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
        itensDB = new DBHelper(context);
        volleyQueue = Volley.newRequestQueue(context);
    }

    //region Itens

    public void setItensListener(ItensListener itensListener){
        this.itensListener = itensListener;
    }

    public void setDetalhesItensListener(DetalhesItensListener detalhesItensListener) {
        this.detalhesItensListener = detalhesItensListener;
    }

    public ArrayList<Item> getItensBD() {
       itens = itensDB.getAllItensDB();
       return new ArrayList(itens);
   }

    public Item getItem(int id){
        for(Item i:itens){
            if(i.getId()==id){
                return i;
            }
        }
        return null;
    }

    public void adicionarItensBD(ArrayList<Item>itens){
        itensDB.removerAllItemDB();
        for(Item i : itens){
            adicionarItemBD(i);
        }
    }

    public void adicionarItemBD(Item i){
        itensDB.adicionarItemDB(i);
    }

    public void editarItemBD(Item i){
        Item auxItem = getItem(i.getId());
        if(auxItem!=null){
            itensDB.editarItemDB(i);
        }
    }

    public void removerItemBD(int id){
        Item auxItem = getItem(id);
        if(auxItem != null){
            itensDB.removerItemDB(auxItem);
        }
    }

    //endregion



    //region Itens_API

    public void adicionarItemAPI(final Item item, final Context context){
        SharedPreferences app_preferences = context.getSharedPreferences(Helper.APP_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences user_preferences = context.getSharedPreferences(Helper.USER_STORAGE, context.MODE_PRIVATE);

        SYSTEM_DOMAIN = app_preferences.getString(Helper.APP_SYSTEM_DOMAIN_URL, null);

        if(SYSTEM_DOMAIN != null)
        {
            if(!Helper.isInternetConnectionAvailable(context)){
                Toast.makeText(context, "Erro: Sem ligação à internet!", Toast.LENGTH_LONG).show();
            }else {
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, SYSTEM_DOMAIN + "item", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        adicionarItemBD(ApiJsonParser.parserJsonItem(response.toString()));

                        if(detalhesItensListener!=null){
                            detalhesItensListener.onRefreshDetalhesItens(MenuMainActivity.ADD);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                        //Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json; charset=UTF-8");
                        params.put("Authorization", "Bearer " + user_preferences.getString(Helper.USER_TOKEN, null));
                        return params;
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("nome", item.getNome());
                        params.put("serialNumber", item.getSerialNumber());
                        params.put("categoria_id", item.getCategoria_id());
                        params.put("notas", item.getNotas());
                        params.put("status", String.valueOf(item.getStatus()));
                        return params;
                    }
                };
                volleyQueue.add(req);
            }
        }
    }

    public void getAllItensAPI(final Context context){
        SharedPreferences app_preferences = context.getSharedPreferences(Helper.APP_STORAGE, context.MODE_PRIVATE);
        SharedPreferences user_preferences = context.getSharedPreferences(Helper.USER_STORAGE, context.MODE_PRIVATE);

        SYSTEM_DOMAIN = app_preferences.getString(Helper.APP_SYSTEM_DOMAIN_URL, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helper.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, "Sem ligação à internet!", Toast.LENGTH_LONG).show();
                if (itensListener != null) {
                    itensListener.onRefreachListaItens(itensDB.getAllItensDB());
                }
            } else {
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, SYSTEM_DOMAIN + "item", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        itens = ApiJsonParser.parserJsonItens(response);
                        adicionarItensBD(itens);
                        if (itensListener != null) {
                            itensListener.onRefreachListaItens(itens);
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
                        params.put("Authorization", "Bearer " + user_preferences.getString(Helper.USER_TOKEN, null));
                        return params;
                    }
                };
                volleyQueue.add(req);
            }
        }
    }

    public void removerItemAPI(final Item item, final Context context){
        SharedPreferences app_preferences = context.getSharedPreferences(Helper.APP_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences user_preferences = context.getSharedPreferences(Helper.USER_STORAGE, context.MODE_PRIVATE);

        SYSTEM_DOMAIN = app_preferences.getString(Helper.APP_SYSTEM_DOMAIN_URL, null);

        if(SYSTEM_DOMAIN != null)
        {
            if(!Helper.isInternetConnectionAvailable(context)){
                Toast.makeText(context, "Erro: Sem ligação à internet!", Toast.LENGTH_LONG).show();
            }else {
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, SYSTEM_DOMAIN + "item" + "/" + item.getId(), null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        removerItemBD(item.getId());

                        if(detalhesItensListener!=null){
                            detalhesItensListener.onRefreshDetalhesItens(MenuMainActivity.DELETE);
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
                        params.put("Authorization", "Bearer " + user_preferences.getString(Helper.USER_TOKEN, null));
                        return params;
                    }
                };
                volleyQueue.add(req);
            }
        }
    }

    public void editarItemAPI(final Item item, final Context context){
        SharedPreferences app_preferences = context.getSharedPreferences(Helper.APP_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences user_preferences = context.getSharedPreferences(Helper.USER_STORAGE, context.MODE_PRIVATE);

        SYSTEM_DOMAIN = app_preferences.getString(Helper.APP_SYSTEM_DOMAIN_URL, null);

        if(SYSTEM_DOMAIN != null)
        {
            if(!Helper.isInternetConnectionAvailable(context)){
                Toast.makeText(context, "Erro: Sem ligação à internet!", Toast.LENGTH_LONG).show();
            }else {
                String url = SYSTEM_DOMAIN + "item" + "/" + item.getId();
                System.out.println(url);
                System.out.println(user_preferences.getString(Helper.USER_TOKEN, null));
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        editarItemBD(item);

                        if(detalhesItensListener!=null){
                            detalhesItensListener.onRefreshDetalhesItens(MenuMainActivity.EDIT);
                        }

                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json; charset=UTF-8");
                        params.put("Authorization", "Bearer " + user_preferences.getString(Helper.USER_TOKEN, null));
                        return params;
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("nome", item.getNome());
                        params.put("serialNumber", item.getSerialNumber());
                        params.put("categoria_id", item.getCategoria_id());
                        params.put("notas", item.getNotas());
                        params.put("status", String.valueOf(item.getStatus()));
                        return params;
                    }
                };
                volleyQueue.add(req);
            }
        }
    }

    public void addRequestToQueue(Request request) {
        volleyQueue.add(request);
    }
    //endregion

}
