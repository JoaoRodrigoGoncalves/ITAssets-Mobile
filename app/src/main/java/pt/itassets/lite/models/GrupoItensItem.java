package pt.itassets.lite.models;

import java.util.ArrayList;

public class GrupoItensItem {

    private Integer item_id,grupoitem_id,id;

    public GrupoItensItem(Integer id,Integer grupoitem_id,Integer item_id) {

        this.id=id;
        this.grupoitem_id=grupoitem_id;
        this.item_id = item_id;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public Integer getGrupoitem_id() {
        return grupoitem_id;
    }

    public void setGrupoitem_id(Integer grupoitem_id) {
        this.grupoitem_id = grupoitem_id;
    }

}
