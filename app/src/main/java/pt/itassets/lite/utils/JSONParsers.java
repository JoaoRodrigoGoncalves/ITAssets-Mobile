package pt.itassets.lite.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.itassets.lite.models.GrupoItens;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.models.Site;

public class JSONParsers {

    /**
     * Faz o parse do login com a API
     * @param response String JSON
     * @return String|null token
     */
    public static boolean parserJsonLogin(String response, Context context){
        /*
          Exemplo de resposta:

          {
              "status": 200,
              "data": {
                  "token": "Fr3zCduYo5yAcaJ8Q_Nq-i__NdIj6YSl",
                  "username": "nome",
                  "email": "email@itassets.pt",
                  "level": "administrator"
              }
          }
         */

        try{
            JSONObject login = new JSONObject(response);
            SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

            if(login.getInt("status") == 200){
                JSONObject data = login.getJSONObject("data");
                SharedPreferences.Editor editor =  preferences.edit();

                editor.putString(Helpers.USER_TOKEN, data.getString("token"));
                editor.putString(Helpers.USER_NAME, data.getString("username"));
                editor.putString(Helpers.USER_EMAIL, data.getString("email"));
                editor.putString(Helpers.USER_ROLE, data.getString("level"));
                editor.commit();
                return true;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Faz o parsing e devolve um hasmap com o erro, o http status, e o nome do erro
     */
    public static Map<String, Object> parseError(String response)
    {
        Map<String, Object> error = null;

        try
        {
            JSONObject errorObject = new JSONObject(response);

            error = new HashMap<>();
            error.put("statusCode", errorObject.getInt("status"));
            error.put("errorName", errorObject.getString("name"));
            error.put("message", errorObject.getString("message"));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return error;
    }

    public static ArrayList<Item> parserJsonItens(JSONObject response, Context context) {
        ArrayList<Item> itens = null;
        try{
            for(int i=0; i<response.length(); i++){

                if(response.getInt("status") == 200)
                {
                    JSONArray dados = response.getJSONArray("data");
                    itens = new ArrayList<>();

                    for (int j = 0; j < dados.length(); j++) {
                        JSONObject thisObject = dados.getJSONObject(j);

                        String nome_categoria = null;

                        if(!thisObject.isNull("categoria"))
                        {
                            JSONObject thisObjectCategoria = thisObject.getJSONObject("categoria");
                            nome_categoria = thisObjectCategoria.getString("nome");
                        }

                        Site thisSite = null;

                        if(!thisObject.isNull("site"))
                        {
                            JSONObject thisSiteObject = thisObject.getJSONObject("site");

                            String morada = null;

                            if(!thisSiteObject.isNull("rua"))
                            {
                                morada = thisSiteObject.getString("rua") + ", " +
                                        thisSiteObject.getString("codPostal") + " " +
                                        thisSiteObject.getString("localidade");
                            }

                            thisSite = new Site(
                                    thisSiteObject.getInt("id"),
                                    thisSiteObject.getString("nome"),
                                    morada,
                                    thisSiteObject.isNull("coordenadas") ? null : thisSiteObject.getString("coordenadas")
                            );

                            Singleton.getInstance(context).atualizarSiteDB(thisSite);
                        }

                        Item auxItem = new Item(
                                thisObject.getInt("id"),
                                thisObject.getString("nome"),
                                thisObject.getString("serialNumber"),
                                thisObject.getString("notas"),
                                (thisObject.isNull("status") ? 10 : thisObject.getInt("status")),
                                nome_categoria,
                                (thisSite == null ? null : thisSite.getId())
                            );
                        itens.add(auxItem);
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return itens;
    }

    public static Item parserJsonItem(String response) {
        Item auxItem = null;
        try{
            JSONObject item = new JSONObject(response);
            if(item.getInt("status") == 200 || item.getInt("status") == 201)
            {
                int id = item.getInt("id");
                String nome = item.getString("nome");
                String serialnumber = item.getString("serialnumber");
                String nota = item.getString("notas");
                auxItem = new Item(id, nome, serialnumber, nota, auxItem.getStatus(), auxItem.getNome_Categoria(), auxItem.getSite_id());
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return auxItem;
    }

    public static Map<String, String> parseJsonLigcaoEmpresa(String response)
    {
        Map<String, String> data = null;

        try
        {
            JSONObject errorObject = new JSONObject(response);

            if(errorObject.getInt("status") == 200)
            {
                data = new HashMap<>();
                JSONObject Jsondata = errorObject.getJSONObject("data");

                data.put("companyName", Jsondata.getString("companyName"));
                data.put("companyNIF", Jsondata.getString("companyNIF"));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return data;
    }

    public static ArrayList<GrupoItens> parserJsonGruposItens(JSONObject response, Context context) {
        ArrayList<GrupoItens> grupoItens = null;
        try{
            for(int i=0; i<response.length(); i++){

                if(response.getInt("status") == 200)
                {
                    JSONArray dados = response.getJSONArray("data");
                    grupoItens = new ArrayList<>();

                    for (int j = 0; j < dados.length(); j++) {
                        JSONObject thisObject = dados.getJSONObject(j);

                        GrupoItens auxGrupoItens = new GrupoItens(
                                thisObject.getInt("id"),
                                (thisObject.isNull("status") ? 10 : thisObject.getInt("status")),
                                thisObject.getString("nome"),
                                thisObject.getString("notas")
                        );
                        grupoItens.add(auxGrupoItens);
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return grupoItens;
    }

    public static GrupoItens parserJsonGrupoItens(String response) {
        GrupoItens auxGrupoItens = null;
        try{
            JSONObject grupoItens = new JSONObject(response);
            if(grupoItens.getInt("status") == 200 || grupoItens.getInt("status") == 201)
            {
                int id = grupoItens.getInt("id");
                String nome = grupoItens.getString("nome");
                String nota = grupoItens.getString("notas");
                auxGrupoItens = new GrupoItens(id, auxGrupoItens.getStatus(), nome, nota);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return auxGrupoItens;
    }
}
