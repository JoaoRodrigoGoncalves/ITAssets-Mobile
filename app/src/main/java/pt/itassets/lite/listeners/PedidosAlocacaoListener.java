package pt.itassets.lite.listeners;

import java.util.ArrayList;

import pt.itassets.lite.models.Alocacao;

public interface PedidosAlocacaoListener {
    void onRefreshListaAlocacoes(ArrayList<Alocacao> listaAlocacoes);
}
