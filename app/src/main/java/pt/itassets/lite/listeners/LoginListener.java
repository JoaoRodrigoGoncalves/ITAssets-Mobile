package pt.itassets.lite.listeners;

import java.util.Map;

public interface LoginListener {
    /**
     * Função para quando o login é feito com sucesso
     */
    void onLoginSuccess();

    /**
     * Função para quando ocorre um erro ao fazer o login.
     * O errormap tem informações sobre o erro.
     */
    void onLoginFail(Map<String, Object> errorMap);

    /**
     * Função chamada quando a verificação da sessão é feita
     * com sucesso. Deve ser usada para permitir que o utilizador continue.
     * ATENÇÃO: Deve ser chamada mesmo quando não há ligação à internet e existe
     * um token registado para que o utilizador tenha acesso às funcionalidades
     * offline
     */
    void OnHeartbeatSuccess();

    /**
     * Função chamada quando a verificação da sessão é feita e
     * falha. Não deve ser chamado quando existe um token guardado mas
     * não existe ligação à internet
     */
    void OnHeartbeatFail();
}
