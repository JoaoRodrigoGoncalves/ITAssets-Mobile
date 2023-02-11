package pt.itassets.lite.models;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;
import pt.itassets.lite.R;
import pt.itassets.lite.listeners.AppSetupListener;
import pt.itassets.lite.listeners.GrupoItensListener;
import pt.itassets.lite.listeners.ItensListener;
import pt.itassets.lite.listeners.LoginListener;
import pt.itassets.lite.listeners.MQTTMessageListener;
import pt.itassets.lite.listeners.OperacoesGruposListener;
import pt.itassets.lite.listeners.OperacoesItensListener;
import pt.itassets.lite.listeners.OperacoesPedidoAlocacaoListener;
import pt.itassets.lite.listeners.OperacoesPedidoReparacaoListener;
import pt.itassets.lite.listeners.PedidosAlocacaoListener;
import pt.itassets.lite.listeners.PedidosReparacaoListener;
import pt.itassets.lite.utils.Helpers;
import pt.itassets.lite.utils.JSONParsers;

public class Singleton {
    private ArrayList<Item> itens;
    private ArrayList<GrupoItens> grupoItens;
    private ArrayList<PedidoAlocacao> alocacoes;
    private ArrayList<GrupoItensItem> grupoItensItems;
    private ArrayList<PedidoReparacao> reparacoes;
    private static Singleton instance=null;
    private static RequestQueue volleyQueue=null;
    private DBHelper database = null;
    private AppSetupListener appSetupListener;
    private LoginListener loginListener;
    private ItensListener itensListener;
    private OperacoesItensListener operacoesItensListener;
    private GrupoItensListener grupoItensListener;
    private OperacoesGruposListener operacoesGruposListener;
    private PedidosAlocacaoListener pedidosAlocacaoListener;
    private OperacoesPedidoAlocacaoListener operacoesPedidoAlocacaoListener;
    private PedidosReparacaoListener pedidosReparacaoListener;
    private OperacoesPedidoReparacaoListener operacoesPedidoReparacaoListener;
    private MQTTMessageListener mqttMessageListener;
    private MqttAndroidClient mqttClient;

    private String SYSTEM_DOMAIN = null;

    public static synchronized Singleton getInstance(Context context)
    {
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

    public void loginAPI(String email, String password, Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);
        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);

        if(Helpers.isInternetConnectionAvailable(context))
        {
            try
            {
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, SYSTEM_DOMAIN + "login",null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String response_string = response.toString();
                                if(Helpers.isValidJSON(response_string))
                                {
                                    if(JSONParsers.parserJsonLogin(response_string, context))
                                    {
                                        if(loginListener != null)
                                        {
                                            loginListener.onLoginSuccess();
                                        }
                                    }
                                    else
                                    {
                                        if(loginListener != null)
                                        {
                                            // É preciso fazer aqui um hack quanto ao fail do login
                                            // por causa da parte de impedir o admin de iniciar
                                            // sessão.

                                            Map<String, Object> hack = new HashMap<>();
                                            hack.put("message", context.getString(R.string.txt_credenciais_erradas));
                                            loginListener.onLoginFail(hack);
                                        }
                                    }
                                }
                                else
                                {
                                    Toast.makeText(context, context.getString(R.string.txt_generic_error), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                if(error != null){
                                    if(error.networkResponse != null)
                                    {
                                        String error_string = new String(error.networkResponse.data, StandardCharsets.UTF_8);

                                        if(Helpers.isValidJSON(error_string))
                                        {
                                            Map<String, Object> errorMap = JSONParsers.parseError(error_string);

                                            if((int) errorMap.get("statusCode") == 401)
                                            {
                                                if(loginListener != null)
                                                {
                                                    loginListener.onLoginFail(errorMap);
                                                }
                                            }
                                            else
                                            {
                                                Toast.makeText(context, (String) errorMap.get("message"), Toast.LENGTH_SHORT).show();
                                            }
                                            return;
                                        }
                                    }
                                }
                                Toast.makeText(context, context.getString(R.string.txt_generic_error), Toast.LENGTH_SHORT).show();
                            }
                        })  {
                    // https://stackoverflow.com/a/44049327
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        String authString = email + ":" + password;

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json; charset=UTF-8");
                        params.put("Authorization", "Basic " + Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8)));
                        return params;
                    }
                };
                volleyQueue.add(jsonRequest);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(context, context.getString(R.string.txt_generic_error), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(context, context.getString(R.string.txt_sem_internet), Toast.LENGTH_SHORT).show();
        }
    }

    public void sendHeartbeat(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);
        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);

        if(Helpers.isInternetConnectionAvailable(context))
        {
            try
            {
                JsonObjectRequest jsonRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        SYSTEM_DOMAIN + "heartbeat",
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if(loginListener != null)
                                {
                                    loginListener.OnHeartbeatSuccess();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if(loginListener != null)
                                {
                                    loginListener.OnHeartbeatFail();
                                }
                            }
                        }
                )
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json; charset=UTF-8");
                        params.put("Authorization", "Bearer " + preferences.getString(Helpers.USER_TOKEN, null));
                        return params;
                    }
                };
                volleyQueue.add(jsonRequest);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(context, context.getString(R.string.txt_generic_error), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(context, context.getString(R.string.txt_sem_internet), Toast.LENGTH_SHORT).show();
        }
    }

    public void setupApp(String url, Context context)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + "sysinfo", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if(Helpers.isValidJSON(response.toString()))
                        {
                            Map<String, String> responseMap = JSONParsers.parseJsonLigcaoEmpresa(response.toString());
                            if(appSetupListener != null)
                            {
                                appSetupListener.onAppSetupSuccess(responseMap);
                            }
                        }
                        else
                        {
                            Toast.makeText(context, context.getString(R.string.txt_generic_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error != null)
                {
                    if(error.networkResponse != null)
                    {
                        String errorString = new String(error.networkResponse.data, StandardCharsets.UTF_8);

                        if(Helpers.isValidJSON(errorString))
                        {
                            Map<String, Object> errorMap = JSONParsers.parseError(errorString);
                            if(errorMap == null)
                            {
                                Toast.makeText(context, context.getString(R.string.txt_url_invalido), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                appSetupListener.onAppSetupFail(errorMap);
                            }
                        }
                        else
                        {
                            Toast.makeText(context, context.getString(R.string.txt_url_invalido), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                Toast.makeText(context, context.getString(R.string.txt_generic_error), Toast.LENGTH_SHORT).show();
            }
        });
        volleyQueue.add(jsonObjectRequest);
    }

    //region MQTT


    public void startMQTT(Context context)
    {
        if(mqttClient != null)
            return;

        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        //Converter url de dominio guardado para dominio simples
        String domain =
                preferences.getString(Helpers.DOMAIN, null)
                        .replace("https://", "")
                        .replace("/", "");


        mqttClient = new MqttAndroidClient(
                context,
                "tcp://" + domain + ":1883",
                "USER_" + preferences.getInt(Helpers.USER_ID, -1),
                Ack.AUTO_ACK
        );

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setKeepAliveInterval(60*60*24);

        IMqttToken token = mqttClient.connect(options);
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.d("MQTT", "Ligado ao broker");

                mqttClient.subscribe(
                        "USER_" + preferences.getInt(Helpers.USER_ID, -1) + "_TOPIC",
                        0);

                mqttClient.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        Log.d("MQTT", "Ligação ao broker perdida");
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        Log.d("MQTT", "Mensagem recebida");
                        //Enviar mensagem para o listener
                        if(mqttMessageListener != null)
                        {
                            mqttMessageListener.onMQTTMessageRecieved(new String(message.getPayload(), StandardCharsets.UTF_8));
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        Log.d("MQTT", "Entrega concluída");
                    }
                });
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                // Something went wrong e.g. connection timeout or firewall problems
                Log.d("MQTT", "Erro ao ligar ao broker: " + exception.getMessage());
            }
        });
    }

    /**
     * Termina a execução do serviço cliente de MQTT
     */
    public void pararMQTT()
    {
        if(mqttClient != null)
        {
            mqttClient.disconnect();
            mqttClient = null;
        }
    }

    //endregion

    //region Listeners

    public void setMqttMessageListener(MQTTMessageListener mqttMessageListener) {
        this.mqttMessageListener = mqttMessageListener;
    }

    public void setItensListener(ItensListener itensListener)
    {
        this.itensListener = itensListener;
    }

    public void setOperacoesItensListener(OperacoesItensListener operacoesItensListener)
    {
        this.operacoesItensListener = operacoesItensListener;
    }

    public void setGrupoItensListener(GrupoItensListener grupoItensListener)
    {
        this.grupoItensListener = grupoItensListener;
    }

    public void setOperacoesGruposListener(OperacoesGruposListener operacoesGruposListener)
    {
        this.operacoesGruposListener = operacoesGruposListener;
    }

    public void setLoginListener(LoginListener loginListener)
    {
        this.loginListener = loginListener;
    }

    public void setAppSetupListener(AppSetupListener appSetupListener)
    {
        this.appSetupListener = appSetupListener;
    }

    public void setPedidosAlocacaoListener(PedidosAlocacaoListener pedidosAlocacaoListener)
    {
        this.pedidosAlocacaoListener = pedidosAlocacaoListener;
    }

    public void setOperacoesPedidoAlocacaoListener(OperacoesPedidoAlocacaoListener operacoesPedidoAlocacaoListener)
    {
        this.operacoesPedidoAlocacaoListener = operacoesPedidoAlocacaoListener;
    }

    public void setPedidosReparacaoListener(PedidosReparacaoListener pedidosReparacaoListener)
    {
        this.pedidosReparacaoListener = pedidosReparacaoListener;
    }

    public void setOperacoesPedidoReparacaoListener(OperacoesPedidoReparacaoListener operacoesPedidoReparacaoListener)
    {
        this.operacoesPedidoReparacaoListener = operacoesPedidoReparacaoListener;
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

    // region Funções interação com a tabela local de Sites

    public void atualizarSiteDB(Site site)
    {
        if(getSiteDB(site.getId()) == null)
        {
            adicionarSiteDB(site);
        }
        else
        {
            editarSiteDB(site);
        }
    }

    public Site getSiteDB(Integer id)
    {
        Site aux = database.getSiteDB(id);
        return aux;
    }

    //endregion

    // region Funções interação com a tabela local de Grupo Item Itens

    public void adicionarGrupoItensItemBD(ArrayList<GrupoItensItem> grupoItensItems){
        database.removerAllGrupoItensItemDB();
        for(GrupoItensItem i : grupoItensItems){
            database.adicionarGrupoItensItemDB(i);
        }
    }

    public void adicionarSiteDB(Site site)
    {
        database.adicionarSiteDB(site);
    }

    public void editarSiteDB(Site site)
    {
        database.editarSiteDB(site);
    }

    //endregion

    //region Funções Interação com tabela local de Grupos de Itens

    public ArrayList<GrupoItens> getGrupoItensBD() {
        grupoItens = database.getAllGruposItensDB();
        return new ArrayList(grupoItens);
    }

    public GrupoItens getGrupoItens(Integer id){
        for(GrupoItens i:grupoItens){
            if(i.getId()==id){
                return i;
            }
        }
        return null;
    }

    public void adicionarGruposItensBD(ArrayList<GrupoItens>gruposItens){
        database.removerAllGrupoItensDB();
        for(GrupoItens i : gruposItens){
            adicionarGrupoItensBD(i);
        }
    }

    public void adicionarGrupoItensBD(GrupoItens i){
        database.adicionarGrupoItensDB(i);
    }

    public void editarGrupoItensBD(GrupoItens i){
        GrupoItens auxItem = getGrupoItens(i.getId());
        if(auxItem!=null){
            database.editarGrupoItensDB(i);
        }
    }

    public void removerGrupoItensBD(int id){
        GrupoItens auxItem = getGrupoItens(id);
        if(auxItem != null){
            database.removerGrupoItensDB(auxItem);
        }
    }

    //endregion

    //region Funções Interação com tabela local de Itens de um Grupo Itens

    public ArrayList<Item> getItensdoGrupoItem(Integer grupoitem_id)
    {
        ArrayList<Item> local_itens = new ArrayList<>();
        ArrayList<GrupoItensItem> grupoItensItems = database.findGrupoItensItem(grupoitem_id);

        for (GrupoItensItem gii: grupoItensItems)
        {
            local_itens.add(database.FindItemDB(gii.getItem_id()));
        }
        return local_itens;
    }

    public ArrayList<GrupoItens> getGrupodeItemdoPedidoReparacao(Integer pedido_reparacao_id)
    {
        ArrayList<GrupoItens> grupoItens=new ArrayList<>();
        grupoItens= database.findGrupoItensPedidoReparacao(pedido_reparacao_id);
        return grupoItens;
    }

    public ArrayList<Item> getItensdoPedidoReparacao(Integer pedido_reparacao_id)
    {
        ArrayList<Item> item=new ArrayList<>();

        item=database.findItemPedidoReparacao(pedido_reparacao_id);

        return item;
    }

    public ArrayList<Item> getItensSemGrupoItem()
    {
        ArrayList<Item> item=getItensBD();
        ArrayList<Item> itensSemGrupo = database.checkItemGrupo(item);
        return itensSemGrupo;
    }


    public ArrayList<Item> getItensAlocados(Integer user_id)
    {
        ArrayList<PedidoAlocacao> ItensAlocadosUser = database.getAllAlocacoesByRequerente(user_id);
        ArrayList<Item> item=new ArrayList<>();
        ArrayList<Item> itemBD=database.getAllItensDB();

        for (int j=0;j<ItensAlocadosUser.size();j++)
        {
            if (ItensAlocadosUser.get(j).getStatus()==9)
            {
            for (Item i:itemBD) {

                    if (i.getNome().equals(ItensAlocadosUser.get(j).getNome_item()))
                    {
                        if (i.getPedido_reparacao_id()==null)
                        {
                            item.add(i);
                        }
                    }

                }
            }

        }

        return item;

    }

    public ArrayList<GrupoItens> getGrupoItensAlocados(Integer user_id)
    {
        ArrayList<PedidoAlocacao> ItensAlocadosUser = database.getAllAlocacoesByRequerente(user_id);
        ArrayList<GrupoItens> grupoItens=new ArrayList<>();
        ArrayList<GrupoItens> grupoItensBD=database.getAllGruposItensDB();

        for (int j=0;j<ItensAlocadosUser.size();j++)
        {
            if (ItensAlocadosUser.get(j).getStatus()==9)
            {
                for (GrupoItens i:grupoItensBD) {

                    if (i.getNome().equals(ItensAlocadosUser.get(j).getNome_grupoItem()))
                    {
                        if (i.getPedido_reparacao_id()==null)
                        {
                            grupoItens.add(i);
                        }
                    }

                }
            }

        }

        return grupoItens;

    }


    //endregion

    //region Funções Interação com tabela local de Pedidos de Alocação

    public ArrayList<PedidosAlocacaoListener> getAlocacoesBD() {
        alocacoes = database.getAllAlocacoesDB();
        return new ArrayList(alocacoes);
    }

    public PedidoAlocacao getAlocacao(Integer id){
        for(PedidoAlocacao i:alocacoes){
            if(i.getId()==id){
                return i;
            }
        }
        return null;
    }

    public void adicionarAlocacoesBD(ArrayList<PedidoAlocacao> alocacoes){
        database.removerAllAlocacaoDB();
        for(PedidoAlocacao i : alocacoes){
            adicionarAlocacaoBD(i);
        }
    }

    public void adicionarAlocacaoBD(PedidoAlocacao i){
        database.adicionarAlocacaoDB(i);
    }

    public void editarAlocacaoBD(PedidoAlocacao i){
        PedidoAlocacao auxPedidoAlocacao = getAlocacao(i.getId());
        if(auxPedidoAlocacao !=null){
            database.editarAlocacaoDB(i);
        }
    }

    public void removerAlocacaoBD(int id){
        PedidoAlocacao auxPedidoAlocacao = getAlocacao(id);
        if(auxPedidoAlocacao != null){
            database.removerAlocacaoDB(auxPedidoAlocacao);
        }
    }

    //endregion

    //region Funções Interação com tabela local de Pedidos de Reparação

    public ArrayList<PedidosReparacaoListener> getReparacoesBD() {
        reparacoes = database.getAllPedidosReparacaoDB();
        return new ArrayList(reparacoes);
    }

    public PedidoReparacao getReparacao(Integer id){
        for(PedidoReparacao i:reparacoes){
            if(i.getId()==id){
                return i;
            }
        }
        return null;
    }

    public void adicionarReparacoesBD(ArrayList<PedidoReparacao> reparacoes){
        database.removerAllPedidoReparacaoDB();
        for(PedidoReparacao i : reparacoes){
            adicionarReparacaoBD(i);
        }
    }

    public void adicionarReparacaoBD(PedidoReparacao i){
        database.adicionarPedidoReparacaoDB(i);
    }

    public void editarReparacaoBD(PedidoReparacao i){
        PedidoReparacao auxReparacao = getReparacao(i.getId());
        if(auxReparacao!=null){
            database.editarPedidoReparacaoDB(i);
        }
    }

    public void removerReparacaoBD(int id){
        PedidoReparacao auxReparacao = getReparacao(id);
        if(auxReparacao != null){
            database.removerPedidoReparacaoDB(auxReparacao);
        }
    }

    //endregion

    //region Funções Interação com API Itens

    public void getAllItensAPI(final Context context){
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, R.string.txt_sem_internet, Toast.LENGTH_LONG).show();
                itens = database.getAllItensDB();
                if (itensListener != null) {
                    itensListener.onRefreshListaItens(itens);
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

    public void getUserItensAPI(final Context context, Integer userID)
    {
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
            
                Toast.makeText(context, R.string.txt_sem_internet, Toast.LENGTH_LONG).show();
                itens = database.getAllItensDB();

                if (itensListener != null) {
                    itensListener.onRefreshListaItens(itens);
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
                        Helpers.parseVolleyErrors(context, error);
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

    public void AdicionarItemAPI(final Item item, final Context context){
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, R.string.txt_sem_internet, Toast.LENGTH_LONG).show();
            } else {
                try
                {
                    Map<String, String> jsonBody = new HashMap<>();
                    jsonBody.put("nome", item.getNome());
                    jsonBody.put("serialNumber", item.getSerialNumber());
                    jsonBody.put("notas", item.getNotas());

                    JsonObjectRequest req = new JsonObjectRequest(
                            Request.Method.POST,
                            SYSTEM_DOMAIN + "item",
                            new JSONObject(jsonBody),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    adicionarItemBD(JSONParsers.parserJsonItem(response.toString()));
                                    if (operacoesItensListener != null) {
                                        operacoesItensListener.onItemOperacaoRefresh(Helpers.OPERACAO_ADD);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Helpers.parseVolleyErrors(context, error);
                                }
                            }
                    )
                    {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("Content-Type", "application/json; charset=UTF-8");
                            params.put("Authorization", "Bearer " + preferences.getString(Helpers.USER_TOKEN, null));
                            return params;
                        }
                    };
                    volleyQueue.add(req);
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        }
    }

    public void EditarItemAPI(final Item item, final Context context){
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, R.string.txt_sem_internet, Toast.LENGTH_LONG).show();
            } else {
                Map<String, String> jsonBody = new HashMap<>();
                jsonBody.put("nome", item.getNome());
                jsonBody.put("serialNumber", item.getSerialNumber());
                jsonBody.put("notas", item.getNotas());

                JsonObjectRequest req = new JsonObjectRequest(
                        Request.Method.PUT,
                        SYSTEM_DOMAIN + "item/" + item.getId(),
                        new JSONObject(jsonBody),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                editarItemBD(item);

                                if (operacoesItensListener != null) {
                                    operacoesItensListener.onItemOperacaoRefresh(Helpers.OPERACAO_EDIT);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Helpers.parseVolleyErrors(context, error);
                            }
                        }
                )
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
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

    public void RemoverItemAPI(final Item item, final Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, R.string.txt_sem_internet, Toast.LENGTH_LONG).show();
            } else {
                StringRequest req = new StringRequest(
                    Request.Method.DELETE,
                    SYSTEM_DOMAIN + "item/" + item.getId(),
                     new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            removerItemBD(item.getId());

                            if (itensListener != null) {
                                itensListener.onRefreshListaItens(itens);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Helpers.parseVolleyErrors(context, error);
                        }
                    }
                )
                {
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


    //endregion

    //region Funções Interação com API Grupo de Itens

    public void getAllGrupoItensAPI(final Context context){
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, R.string.txt_sem_internet, Toast.LENGTH_LONG).show();
                grupoItens = database.getAllGruposItensDB();//ver se isto estava aqui antes
                if (grupoItensListener != null) {
                    grupoItensListener.onRefreshListaGrupoItens(grupoItens);
                }
            } else {
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,SYSTEM_DOMAIN + "grupoitens",null,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        grupoItens = JSONParsers.parserJsonGruposItens(response);
                        adicionarGruposItensBD(grupoItens);
                        //faz a tanela de relações
                        grupoItensItems = JSONParsers.parserJsonGruposItensItem(response);
                        adicionarGrupoItensItemBD(grupoItensItems);
                        if (grupoItensListener != null) {
                            grupoItensListener.onRefreshListaGrupoItens(grupoItens);
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Helpers.parseVolleyErrors(context, error);
                            }
                        }
                )
                {
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

    public void AdicionarGrupoItensAPI(final GrupoItens gruposItens,final ArrayList<Integer> itemSelected, final Context context){
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, R.string.txt_sem_internet, Toast.LENGTH_LONG).show();
            } else {
                Map<String, Object> jsonBody = new HashMap<>();
                jsonBody.put("nome", gruposItens.getNome());
                if (gruposItens.getNotas() != null)
                {
                    jsonBody.put("notas", gruposItens.getNotas());
                }
                jsonBody.put("itens", itemSelected);

                JsonObjectRequest req = new JsonObjectRequest(
                        Request.Method.POST,
                        SYSTEM_DOMAIN + "grupoitens",
                        new JSONObject(jsonBody),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                adicionarGrupoItensBD(JSONParsers.parserJsonGrupoItens(response.toString()));
                                if (operacoesGruposListener != null) {
                                    operacoesGruposListener.onGrupoOperacoesRefresh(Helpers.OPERACAO_ADD);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Helpers.parseVolleyErrors(context, error);
                            }
                        }
                )
                {
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

    public void EditarGrupoItensAPI(final GrupoItens grupoItens, final Context context){
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, R.string.txt_sem_internet, Toast.LENGTH_LONG).show();
            } else {
                Map<String, String> jsonBody = new HashMap<>();
                jsonBody.put("nome", grupoItens.getNome());
                jsonBody.put("notas", grupoItens.getNotas());

                JsonObjectRequest req = new JsonObjectRequest(
                    Request.Method.PUT,
                    SYSTEM_DOMAIN + "grupoitens/" + grupoItens.getId(),
                    new JSONObject(jsonBody),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            editarGrupoItensBD(grupoItens);

                            if (operacoesGruposListener != null) {
                                operacoesGruposListener.onGrupoOperacoesRefresh(Helpers.OPERACAO_EDIT);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Helpers.parseVolleyErrors(context, error);
                        }
                    }
                )
                {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", "Bearer " + preferences.getString(Helpers.USER_TOKEN, null));
                        return params;
                    }
                };
                volleyQueue.add(req);
            }
        }
    }

    public void RemoverGrupoItemAPI(final GrupoItens grupoItem, final Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, R.string.txt_sem_internet, Toast.LENGTH_LONG).show();
            } else {
                StringRequest req = new StringRequest(
                    Request.Method.DELETE,
                    SYSTEM_DOMAIN + "grupoitens/" + grupoItem.getId(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            removerGrupoItensBD(grupoItem.getId());

                            if (grupoItensListener != null) {
                                grupoItensListener.onRefreshListaGrupoItens(grupoItens);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Helpers.parseVolleyErrors(context, error);
                        }
                    }
                )
                {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", "Bearer " + preferences.getString(Helpers.USER_TOKEN, null));
                        return params;
                    }
                };
                volleyQueue.add(req);
            }
        }
    }

    //endregion

    //region Funções Interação com API Pedidos Alocação/ Requisição

    public void getUserAlocacoesAPI(final Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, context.getString(R.string.txt_sem_internet), Toast.LENGTH_LONG).show();
                alocacoes = database.getAllAlocacoesDB();
                if (pedidosAlocacaoListener != null) {
                    pedidosAlocacaoListener.onRefreshListaAlocacoes(alocacoes);
                }
            } else {
                JsonObjectRequest req = new JsonObjectRequest(
                    Request.Method.GET,
                    SYSTEM_DOMAIN + "pedidoalocacao/user/" + preferences.getInt(Helpers.USER_ID, -1),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            alocacoes = JSONParsers.parserJsonAlocacoes(response, context);
                            adicionarAlocacoesBD(alocacoes);
                            if (pedidosAlocacaoListener != null) {
                                pedidosAlocacaoListener.onRefreshListaAlocacoes(alocacoes);
                            }
                            /*getAllItensAPI(context);
                            getAllGrupoItensAPI(context);*/
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Helpers.parseVolleyErrors(context, error);
                        }
                    }
                )
                {
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

    public void createPedidoAlocacao(final Context context, final JSONObject jsonBody)
    {
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, context.getString(R.string.txt_sem_internet), Toast.LENGTH_LONG).show();
            } else {
                JsonObjectRequest req = new JsonObjectRequest(
                        Request.Method.POST,
                        SYSTEM_DOMAIN + "pedidoalocacao",
                        jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                adicionarAlocacaoBD(JSONParsers.parserJsonAlocacao(response));

                                if (operacoesPedidoAlocacaoListener!= null) {
                                    operacoesPedidoAlocacaoListener.onAlocacaoOperacaoRefresh(Helpers.OPERACAO_ADD);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Helpers.parseVolleyErrors(context, error);
                            }
                        }
                )
                {
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


    public void EditarAlocacaoAPI(final PedidoAlocacao pedidoAlocacao, final Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, context.getString(R.string.txt_sem_internet), Toast.LENGTH_LONG).show();
            } else {
                Map<String, String> jsonBody = new HashMap<>();
                jsonBody.put("dataFim", pedidoAlocacao.getDataFim());
                jsonBody.put("status", String.valueOf(pedidoAlocacao.getStatus()));

                JsonObjectRequest req = new JsonObjectRequest(
                        Request.Method.PUT,
                        SYSTEM_DOMAIN + "pedidoalocacao/" + pedidoAlocacao.getId(),
                        new JSONObject(jsonBody),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                editarAlocacaoBD(pedidoAlocacao);

                                if (operacoesPedidoAlocacaoListener != null) {
                                    operacoesPedidoAlocacaoListener.onAlocacaoOperacaoRefresh(Helpers.OPERACAO_EDIT);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Helpers.parseVolleyErrors(context, error);
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
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

    public void RemoverAlocacaoAPI(final PedidoAlocacao pedidoAlocacao, final Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, context.getString(R.string.txt_sem_internet), Toast.LENGTH_LONG).show();
            } else {
                StringRequest req = new StringRequest(Request.Method.DELETE, SYSTEM_DOMAIN + "pedidoalocacao/" + pedidoAlocacao.getId(), new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        removerAlocacaoBD(pedidoAlocacao.getId());

                        if (pedidosAlocacaoListener != null) {
                            pedidosAlocacaoListener.onRefreshListaAlocacoes(alocacoes);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Helpers.parseVolleyErrors(context, error);
                    }
                })
                {
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

    public GrupoItens getActiveGrupoForItem(Integer item_id) {
        return database.getActiveGrupoForItem(item_id);
    }
    //endregion

    //region Funções Interação com API Pedidos de Reparação

    public void getUserReparacoesAPI(final Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, context.getString(R.string.txt_sem_internet), Toast.LENGTH_LONG).show();
                reparacoes = database.getAllPedidosReparacaoDB();
                if (pedidosReparacaoListener != null) {
                    pedidosReparacaoListener.onRefreshListaReparacoes(reparacoes);
                }
            } else {
                JsonObjectRequest req = new JsonObjectRequest(
                        Request.Method.GET,
                        SYSTEM_DOMAIN + "pedidoreparacao/user/" + preferences.getInt(Helpers.USER_ID, -1),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                reparacoes = JSONParsers.parserJsonReparacoes(response, context);
                                adicionarReparacoesBD(reparacoes);
                                if (pedidosReparacaoListener != null) {
                                    pedidosReparacaoListener.onRefreshListaReparacoes(reparacoes);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Helpers.parseVolleyErrors(context, error);
                            }
                        }
                )
                {
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

    public void createPedidoReparacao(final Context context, final JSONObject jsonBody) {
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, context.getString(R.string.txt_sem_internet), Toast.LENGTH_LONG).show();
            } else {
                JsonObjectRequest req = new JsonObjectRequest(
                        Request.Method.POST,
                        SYSTEM_DOMAIN + "pedidoreparacao",
                        jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                adicionarAlocacaoBD(JSONParsers.parserJsonAlocacao(response));

                                if (operacoesPedidoReparacaoListener!= null) {
                                    operacoesPedidoReparacaoListener.onReparacaoOperacaoRefresh(Helpers.OPERACAO_ADD);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Helpers.parseVolleyErrors(context, error);
                            }
                        }
                )
                {
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
    public void EditarReparacaoAPI(final PedidoReparacao reparacao, final Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, context.getString(R.string.txt_sem_internet), Toast.LENGTH_LONG).show();
            } else {
                Map<String, String> jsonBody = new HashMap<>();
                jsonBody.put("respostaObs", reparacao.getRespostaObs());
                jsonBody.put("dataFim", reparacao.getDataFim());
                jsonBody.put("dataInicio", reparacao.getDataInicio());
                jsonBody.put("status", String.valueOf(reparacao.getStatus()));

                JsonObjectRequest req = new JsonObjectRequest(
                        Request.Method.PUT,
                        SYSTEM_DOMAIN + "pedidoreparacao/" + reparacao.getId(),
                        new JSONObject(jsonBody),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                editarReparacaoBD(reparacao);

                                if (operacoesPedidoReparacaoListener != null) {
                                    operacoesPedidoReparacaoListener.onReparacaoOperacaoRefresh(Helpers.OPERACAO_EDIT);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Helpers.parseVolleyErrors(context, error);
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
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

    public void RemoverReparacaoAPI(final PedidoReparacao reparacao, final Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(Helpers.SHAREDPREFERENCES, MODE_PRIVATE);

        SYSTEM_DOMAIN = preferences.getString(Helpers.DOMAIN, null);
        if(SYSTEM_DOMAIN != null) {
            if (!Helpers.isInternetConnectionAvailable(context)) {
                Toast.makeText(context, context.getString(R.string.txt_sem_internet), Toast.LENGTH_LONG).show();
            } else {
                StringRequest req = new StringRequest(Request.Method.DELETE, SYSTEM_DOMAIN + "pedidoreparacao/" + reparacao.getId(), new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        removerReparacaoBD(reparacao.getId());

                        if (pedidosReparacaoListener != null) {
                            pedidosReparacaoListener.onRefreshListaReparacoes(reparacoes);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Helpers.parseVolleyErrors(context, error);
                    }
                })
                {
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



    //endregion

}
