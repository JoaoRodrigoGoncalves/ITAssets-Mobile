package pt.itassets.lite.listeners;

import java.util.ArrayList;

import pt.itassets.lite.models.PedidoAlocacao;

public interface PedidosAlocacaoListener {
    void onRefreshListaAlocacoes(ArrayList<PedidoAlocacao> listaAlocacoes);
}
