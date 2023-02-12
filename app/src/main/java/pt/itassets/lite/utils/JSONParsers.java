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

import pt.itassets.lite.models.LinhaPedidoReparacao;
import pt.itassets.lite.models.PedidoAlocacao;
import pt.itassets.lite.models.GrupoItens;
import pt.itassets.lite.models.GrupoItensItem;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.PedidoReparacao;
import pt.itassets.lite.models.Singleton;
import pt.itassets.lite.models.Site;

public class JSONParsers {

    /**
     * Faz o parse do login com a API
     * @param response String JSON
     * @return String|null token
     */
    public static boolean parserJsonLogin(String response, Context context)
    {
        /*
          Exemplo de resposta:

          {
              "status": 200,
              "data": {
                  "token": "Fr3zCduYo5yAcaJ8Q_Nq-i__NdIj6YSl",
                  "userID": 1,
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

                if(data.getString("level").equals("administrador"))
                {
                    return false; // Administrador não pode iniciar sessão
                }

                SharedPreferences.Editor editor =  preferences.edit();

                editor.putString(Helpers.USER_TOKEN, data.getString("token"));
                editor.putInt(Helpers.USER_ID, data.getInt("userID"));
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

    public static ArrayList<Item> parserJsonItens(JSONObject response, Context context)
    {
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
                                thisObject.isNull("serialNumber") ? null : thisObject.getString("serialNumber"),
                                thisObject.isNull("notas") ? null : thisObject.getString("notas"),
                                (thisObject.isNull("status") ? 10 : thisObject.getInt("status")),
                                nome_categoria,
                                (thisSite == null ? null : thisSite.getId()),
                                (thisObject.isNull("pedido_alocacao") ? null : thisObject.getInt("pedido_alocacao")),
                                (thisObject.isNull("pedido_reparacao") ? null : thisObject.getInt("pedido_reparacao"))
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

    public static Item parserJsonItem(String response)
    {
        Item auxItem = null;
        try{
            JSONObject item = new JSONObject(response);
            if(item.getInt("status") == 200 || item.getInt("status") == 201)
            {
                JSONObject data = item.getJSONObject("data");
                JSONObject data_categoria = null;
                JSONObject data_site = null;

                if(!data.isNull("categoria"))
                {
                    data_categoria = data.getJSONObject("categoria");
                }

                if(!data.isNull("site"))
                {
                    data_site = data.getJSONObject("site");
                }

                auxItem = new Item(
                        data.getInt("id"),
                        data.getString("nome"),
                        data.isNull("serialNumber") ? null : data.getString("serialNumber"),
                        data.isNull("notas") ? null : data.getString("notas"),
                        data.isNull("status") ? 10 : data.getInt("status"),
                        data_categoria == null ? null : data_categoria.getString("nome"),
                        data_site == null ? null : data_site.getInt("id"),
                        data.isNull("pedido_alocacao") ? null : data.getInt("pedido_alocacao"),
                        data.isNull("pedido_reparacao") ? null : data.getInt("pedido_reparacao")

                );
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

    public static ArrayList<GrupoItens> parserJsonGruposItens(JSONObject response)
    {
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
                                thisObject.getString("notas"),
                                (thisObject.isNull("pedido_alocacao") ? null : thisObject.getInt("pedido_alocacao")),
                                (thisObject.isNull("pedido_reparacao") ? null : thisObject.getInt("pedido_reparacao"))
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

    public static GrupoItens parserJsonGrupoItens(String response)
    {
        GrupoItens auxGrupoItens = null;
        try{
            JSONObject grupoItens = new JSONObject(response);
            if(grupoItens.getInt("status") == 200 || grupoItens.getInt("status") == 201)
            {
                JSONObject data = grupoItens.getJSONObject("data");

                auxGrupoItens = new GrupoItens(
                        data.getInt("id"),
                        data.isNull("status") ? 10 : grupoItens.getInt("status"),
                        data.getString("nome"),
                        data.isNull("notas") ? null : grupoItens.getString("notas"),
                        data.isNull("pedido_alocacao") ? null : grupoItens.getInt("pedido_alocacao"),
                        data.isNull("pedido_reparacao") ? null : grupoItens.getInt("pedido_reparacao")
                );
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return auxGrupoItens;
    }

    public static ArrayList<PedidoAlocacao> parserJsonAlocacoes(JSONObject response, Context context)
    {
        ArrayList<PedidoAlocacao> alocacoes = null;
        try{
            for(int i=0; i<response.length(); i++){

                if(response.getInt("status") == 200)
                {
                    JSONArray dados = response.getJSONArray("data");
                    alocacoes = new ArrayList<>();

                    for (int j = 0; j < dados.length(); j++) {
                        JSONObject thisObject = dados.getJSONObject(j);

                        PedidoAlocacao auxAlcoacao = new PedidoAlocacao(
                                thisObject.getInt("id"),
                                thisObject.getInt("status"),
                                thisObject.getString("dataPedido"),
                                thisObject.isNull("dataInicio") ? null : thisObject.getString("dataInicio"),
                                thisObject.isNull("dataFim") ? null : thisObject.getString("dataFim"),
                                thisObject.isNull("obs") ? null : thisObject.getString("obs"),
                                thisObject.isNull("obsResposta") ? null : thisObject.getString("obsResposta"),
                                thisObject.getJSONObject("requerente").getString("username"),
                                thisObject.isNull("aprovador") ? null : thisObject.getJSONObject("aprovador").getString("username"),
                                thisObject.isNull("item") ? null : thisObject.getJSONObject("item").getString("nome"),
                                thisObject.isNull("grupoItens") ? null : thisObject.getJSONObject("grupoItens").getString("nome"),
                                thisObject.getJSONObject("requerente").getInt("id"),
                                thisObject.isNull("item") ? null : thisObject.getJSONObject("item").getInt("id"),
                                thisObject.isNull("grupoItens") ? null : thisObject.getJSONObject("grupoItens").getInt("id")
                        );
                        alocacoes.add(auxAlcoacao);
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return alocacoes;
    }

    public static PedidoAlocacao parserJsonAlocacao(JSONObject response)
    {
        PedidoAlocacao pedidoAlocacao = null;

        try
        {
            if(response.getInt("status") == 200 || response.getInt("status") == 201)
            {
                JSONObject data = response.getJSONObject("data");

                pedidoAlocacao = new PedidoAlocacao(
                        data.getInt("id"),
                        data.getInt("status"),
                        data.getString("dataPedido"),
                        data.isNull("dataInicio") ? null : data.getString("dataInicio"),
                        data.isNull("dataFim") ? null : data.getString("dataFim"),
                        data.isNull("obs") ? null : data.getString("obs"),
                        data.isNull("obsResposta") ? null : data.getString("obsResposta"),
                        data.getJSONObject("requerente").getString("username"),
                        data.isNull("aprovador") ? null : data.getJSONObject("aprovador").getString("username"),
                        data.isNull("item") ? null : data.getJSONObject("item").getString("nome"),
                        data.isNull("grupoItens") ? null : data.getJSONObject("grupoItens").getString("nome"),
                        data.getJSONObject("requerente").getInt("id"),
                        data.isNull("item") ? null : data.getJSONObject("item").getInt("id"),
                        data.isNull("grupoItens") ? null : data.getJSONObject("grupoItens").getInt("id")
                );
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return pedidoAlocacao;
    }

    public static ArrayList<GrupoItensItem> parserJsonGruposItensItem(JSONObject response)
    {
        ArrayList<GrupoItensItem> grupoItensItems = null;
        try{

            for(int i=0; i<response.length(); i++){
                grupoItensItems = new  ArrayList<>();
                if(response.getInt("status") == 200)
                {
                    JSONArray dados = response.getJSONArray("data");


                    for (int j = 0; j < dados.length(); j++) {


                        JSONObject thisObject = dados.getJSONObject(j);
                        JSONArray itens=thisObject.getJSONArray("itens");
                        if (itens.length()!=0)
                        {
                            for (int k=0;k<itens.length();k++)
                            {
                                JSONObject thisitem = itens.getJSONObject(k);

                                GrupoItensItem auxGrupoItensItem=new GrupoItensItem(
                                        k,
                                        thisObject.getInt("id"),
                                        thisitem.getInt("id")
                                );
                                grupoItensItems.add(auxGrupoItensItem);
                            }
                        }

                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return grupoItensItems;
    }

    public static ArrayList<PedidoReparacao> parserJsonReparacoes(JSONObject response, Context context)
    {
        ArrayList<PedidoReparacao> reparacoes = null;
        try{
            for(int i=0; i<response.length(); i++){

                if(response.getInt("status") == 200)
                {
                    JSONArray dados = response.getJSONArray("data");
                    reparacoes = new ArrayList<>();

                    for (int j = 0; j < dados.length(); j++) {
                        JSONObject thisObject = dados.getJSONObject(j);

                        PedidoReparacao auxReparacao = new PedidoReparacao(
                                thisObject.getInt("id"),
                                thisObject.getJSONObject("requerente").getString("username"),
                                thisObject.isNull("responsavel") ? null : thisObject.getJSONObject("responsavel").getString("username"),
                                thisObject.getInt("status"),
                                thisObject.isNull("descricaoProblema") ? null : thisObject.getString("descricaoProblema"),
                                thisObject.isNull("respostaObs") ? null : thisObject.getString("respostaObs"),
                                thisObject.getString("dataPedido"),
                                thisObject.isNull("dataInicio") ? null : thisObject.getString("dataInicio"),
                                thisObject.isNull("dataFim") ? null : thisObject.getString("dataFim")
                        );
                        reparacoes.add(auxReparacao);
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return reparacoes;
    }

    public static ArrayList<LinhaPedidoReparacao> parserJsonLinhaPedidoReparacoes(JSONObject response)
    {
        ArrayList<LinhaPedidoReparacao> linhaPedidoReparacaos = null;
        try{

            for(int i=0; i<response.length(); i++){

                if(response.getInt("status") == 200)
                {
                    JSONArray dados = response.getJSONArray("data");
                    linhaPedidoReparacaos = new  ArrayList<>();

                    for (int j = 0; j < dados.length(); j++) {


                        JSONObject thisObject = dados.getJSONObject(j);

                        //vai buscar os itens que estao associados
                        JSONArray itens=thisObject.getJSONObject("objetos").optJSONArray("itens");
                        //caso existam itens associados
                        if (itens!=null) {
                            for (int k = 0; k < itens.length(); k++) {
                                JSONObject thisitem = itens.getJSONObject(k);

                                LinhaPedidoReparacao auxLinhaReparacao = new LinhaPedidoReparacao(
                                        k,
                                        thisObject.getInt("id"),
                                        thisitem.getInt("id"),
                                        null
                                );
                                linhaPedidoReparacaos.add(auxLinhaReparacao);

                            }
                        }

                        JSONArray grupos=thisObject.getJSONObject("objetos").optJSONArray("grupos");
                        //caso existam itens associados
                        if (grupos!=null) {
                            for (int k = 0; k < grupos.length(); k++) {
                                JSONObject thisgrupo = grupos.getJSONObject(k);

                                LinhaPedidoReparacao auxLinhaReparacao = new LinhaPedidoReparacao(
                                        k,
                                        thisObject.getInt("id"),
                                        null,
                                        thisgrupo.getInt("id")
                                );
                                linhaPedidoReparacaos.add(auxLinhaReparacao);

                            }
                        }


                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return linhaPedidoReparacaos;
    }
}
