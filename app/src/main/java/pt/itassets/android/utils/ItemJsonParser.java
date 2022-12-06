package pt.itassets.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.itassets.android.modelos.Item;

public class ItemJsonParser {
    public static ArrayList<Item> parserJsonItens(JSONArray response){
        ArrayList<Item> itens = new ArrayList<>();
        try{
            for(int i=0; i<response.length(); i++){
                JSONObject item = (JSONObject) response.get(i);
                int id = item.getInt("id");
                int status = item.getInt("status");
                String serialNumber = item.getString("serialNumber");
                int categoria_id = item.getInt("categoria_id");
                String notas = item.getString("notas");
                String nome = item.getString("nome");

                Item auxItem = new Item(id, status, serialNumber, categoria_id, notas, nome);
                itens.add(auxItem);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return itens;
    }

    //Método parserJsonLivro(), que devolve apenas um livro;
    public static Item parserJsonItem(String response){
        Item auxItem = null;
        try{
            JSONObject itens = new JSONObject(response);
            int id = itens.getInt("id");
            int status = itens.getInt("status");
            String serialNumber = itens.getString("serialNumber");
            int categoria_id = itens.getInt("categoria_id");
            String notas = itens.getString("notas");
            String nome = itens.getString("nome");

            auxItem = new Item(id, status, serialNumber, categoria_id, notas, nome);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return auxItem;
    }

    //Método parserJsonLogin(), que efetuará o login na API;
    public static String parserJsonLogin(String response){
        String token = null;
        try{
            JSONObject login = new JSONObject(response);
            if(login.getBoolean("success")){
                token = login.getString("token");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return token;
    }

    public boolean isConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni!=null && ni.isConnected();
    }
}
