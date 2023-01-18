package pt.itassets.lite.listeners;

import java.util.ArrayList;

import pt.itassets.lite.models.Alocacao;

public interface PedidosAlcoacaoListener {
    void onRefreshListaAlocacoes(ArrayList<Alocacao> listaAlocacoes);
}
