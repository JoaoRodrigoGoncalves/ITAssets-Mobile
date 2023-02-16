package pt.itassets.lite.listeners;

import java.util.ArrayList;

import pt.itassets.lite.models.Item;

public interface ItensListener {
    void onRefreshListaItens(ArrayList<Item> listaItens);
}
