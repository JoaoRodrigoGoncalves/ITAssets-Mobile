package pt.itassets.android;

import android.util.Patterns;

public class Helper {

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

}
