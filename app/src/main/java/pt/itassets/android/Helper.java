package pt.itassets.android;

import android.util.Patterns;

import org.json.JSONException;
import org.json.JSONObject;

public class Helper {

    public static final String PREF_STORAGE = "PREF_STORAGE";
    public static final String PREF_SYSTEM_DOMAIN_URL = "SYSTEM_DOMAIN_URL";
    public static final String PREF_USER_TOKEN = null;

    /**
     * Verifica se uma URL é válida
     * @param url Endereço web a validar
     * @return boolean
     */
    public static boolean isURLValid(String url){
        if(url == null)
            return false;

        return Patterns.WEB_URL.matcher(url).matches();
    }

    //Método isEmailValido() para validar se foi inserido um email
    public static boolean isEmailValido(String email){
        if(email == null){
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // https://www.baeldung.com/java-validate-json-string
    public static boolean isValidJSON(String string)
    {
        try {
            new JSONObject(string);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

}
