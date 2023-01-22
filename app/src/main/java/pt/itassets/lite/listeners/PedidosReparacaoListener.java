package pt.itassets.lite.listeners;

import java.util.ArrayList;

import pt.itassets.lite.models.PedidoReparacao;

public interface PedidosReparacaoListener {
    void onRefreshListaReparacoes(ArrayList<PedidoReparacao> listaReparacoes);
}
