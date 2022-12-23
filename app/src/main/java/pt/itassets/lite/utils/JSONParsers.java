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

import pt.itassets.lite.models.Categoria;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.Singleton;

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
                  "level": "administrator"
              }
          }
         */

        try{
            JSONObject login = new JSONObject(response);
            SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

            //TODO: Confirmar que isto precisa de estar aqui, visto que
            // esta função só deverá ser chamada quando receber um HTTP status positivo
            if(login.getInt("status") == 200){
                JSONObject data = login.getJSONObject("data");
                SharedPreferences.Editor editor =  preferences.edit();

                editor.putString(Helpers.USER_TOKEN, data.getString("token"));
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

                        Integer categoria_id = null;

                        if(!thisObject.isNull("categoria"))
                        {
                            JSONObject thisObjectCategoria = thisObject.getJSONObject("categoria");
                            categoria_id = thisObjectCategoria.getInt("id");

                            Categoria tempCat = new Categoria(
                                    thisObjectCategoria.getInt("id"),
                                    thisObjectCategoria.getString("nome"),
                                    thisObjectCategoria.getInt("status")
                            );

                            Singleton.getInstance(context).atualizarCategoriaDB(tempCat);
                        }

                        Item auxItem = new Item(
                                thisObject.getInt("id"),
                                thisObject.getString("nome"),
                                thisObject.getString("serialNumber"),
                                thisObject.getString("notas"),
                                (thisObject.isNull("status") ? 10 : thisObject.getInt("status")),
                                categoria_id,
                                (thisObject.isNull("site_id") ? null : thisObject.getInt("site_id"))
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
}