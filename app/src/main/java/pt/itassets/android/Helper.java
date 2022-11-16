package pt.itassets.android;

import android.util.Patterns;

import org.json.JSONException;
import org.json.JSONObject;

public class Helper {

    //---------------------- APP STORAGE ---------------------------
    public static final String APP_STORAGE = "APP_STORAGE";
    public static final String APP_SYSTEM_DOMAIN_URL = "SYSTEM_DOMAIN_URL";

    //--------------------- USER STORAGE ---------------------------
    public static final String USER_STORAGE = "USER_STORAGE";
    public static final String USER_TOKEN = "USER_TOKEN";
    public static final String USER_EMAIL = "USER_EMAIL";

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
