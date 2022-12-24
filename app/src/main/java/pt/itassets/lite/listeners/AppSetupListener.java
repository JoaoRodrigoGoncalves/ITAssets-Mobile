package pt.itassets.lite.listeners;

import java.util.Map;

public interface AppSetupListener {
    void onAppSetupSuccess(Map<String, String> responseMap);
    void onAppSetupFail(Map<String, Object> errorMap);
}
