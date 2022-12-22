package pt.itassets.android.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.itassets.android.modelos.Item;

public class ApiJsonParser {

    /**
     * Devolve uma array de itens
     * @param response JSONArray
     * @return Lista de itens
     */
    public static ArrayList<Item> parserJsonItens(JSONObject response){
        ArrayList<Item> itens = null;
        try{
            for(int i=0; i<response.length(); i++){

                if(response.getInt("status") == 200)
                {
                    JSONArray dados = response.getJSONArray("data");
                    itens = new ArrayList<>();

                    for (int j = 0; j < dados.length(); j++) {
                        JSONObject thisObject = dados.getJSONObject(j);
                        Item auxItem = new Item(
                                thisObject.getInt("id"),
                                thisObject.getInt("status"),
                                thisObject.getString("serialNumber"),
                                thisObject.getString("categoria_id"),
                                thisObject.getString("notas"),
                                thisObject.getString("nome")
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

    /**
     * Dá parse de um único item
     * @param response String JSON
     * @return Item
     */
    public static Item parserJsonItem(String response){
        Item auxItem = null;
        try{
                JSONObject itens = new JSONObject(response);
                int id = itens.getInt("id");
                int status = itens.getInt("status");
                String serialNumber = itens.getString("serialNumber");
                String categoria_id = itens.getString("categoria_id");
                String notas = itens.getString("notas");
                String nome = itens.getString("nome");

                auxItem = new Item(id, status, serialNumber, categoria_id, notas, nome);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return auxItem;
    }

    /**
     * Faz o parse do login com a API
     * @param response String JSON
     * @return String|null token
     */
    public static String parserJsonLogin(String response){
        /*
          Exemplo de resposta:

          {
              "status": 200,
              "data": {
                  "token": "Fr3zCduYo5yAcaJ8Q_Nq-i__NdIj6YSl"
              }
          }
         */

        String token = null;
        try{
            JSONObject login = new JSONObject(response);

            //TODO: Confirmar que isto precisa de estar aqui, visto que
            // esta função só deverá ser chamada quando receber um HTTP status positivo
            if(login.getInt("status") == 200){
                JSONObject data = login.getJSONObject("data");
                token = data.getString("token");
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        return token;
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
}
