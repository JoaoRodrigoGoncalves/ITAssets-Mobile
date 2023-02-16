package pt.itassets.lite.listeners;

import java.util.ArrayList;

import pt.itassets.lite.models.GrupoItens;

public interface GrupoItensListener {
    void onRefreshListaGrupoItens(ArrayList<GrupoItens> listaGrupoItens);
}
