package pt.itassets.lite.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Patterns;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import pt.itassets.lite.R;

public class Helpers {
    public static final String SHAREDPREFERENCES = "IT_ASSETS_SHAREDPREFERENCES";
    public static final String DOMAIN = "IT_ASSETS_DOMAIN";
    public static final String USER_TOKEN = "IT_ASSETS_API_TOKEN";
    public static final String USER_ROLE = "IT_ASSETS_USER_ROLE";
    public static final String USER_NAME = "IT_ASSETS_USER_NOME";
    public static final String USER_EMAIL = "IT_ASSETS_USER_EMAIL";
    public static final String USER_ID = "IT_ASSETS_USER_ID";

    public static final int OPERACAO_ADD = 10;
    public static final int OPERACAO_EDIT = 20;
    public static final int OPERACAO_DELETE = 30;
    public static final String OPERACAO = "OPERACAO";

    /**
     * Valida se a String indicada é um email válido
     * @param email String com email a validar
     * @return boolean
     */
    public static boolean isEmailValido(String email){
        if(email == null){
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Valida se a String indicada é JSON válido
     * @see <a href="https://www.baeldung.com/java-validate-json-string">Fonte</a>
     * @param string String a validar
     * @return boolean
     */
    public static boolean isValidJSON(String string)
    {
        try {
            new JSONObject(string);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    /**
     * Verifica se existe ligação à internet
     * @param context
     * @return bool
     */
    public static boolean isInternetConnectionAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni!=null && ni.isConnected();
    }

    /**
     * Verifica se uma URL é válida
     * @param url Endereço web a validar
     * @return boolean
     */
    public static boolean isURLValid(String url)
    {
        try {
            InetAddress.getByName(url);
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    public static void parseVolleyErrors(Context context, VolleyError error)
    {
        if(error != null)
        {
            if(error.networkResponse != null)
            {
                if(error.networkResponse.data != null)
                {
                    try
                    {
                        JSONObject erro = new JSONObject(new String(error.networkResponse.data, StandardCharsets.UTF_8));
                        Toast.makeText(context, String.valueOf(erro.getString("message")), Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Toast.makeText(context, "Erro interno: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }
        }
        Toast.makeText(context, context.getString(R.string.txt_generic_error), Toast.LENGTH_SHORT).show();
    }

}
