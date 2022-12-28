package pt.itassets.lite.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pt.itassets.lite.R;
import pt.itassets.lite.models.GrupoItens;

public class ListaGruposItensAdaptador extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<GrupoItens> grupoItens;

    public ListaGruposItensAdaptador(Context context, ArrayList<GrupoItens> grupoItens) {
        this.context = context;
        this.grupoItens = grupoItens;
    }

    @Override
    public int getCount() {
        return grupoItens.size();
    }

    @Override
    public Object getItem(int i) {
        return grupoItens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return grupoItens.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (view == null) {
            view = inflater.inflate(R.layout.item_lista_grupo_itens, null);
        }

        //Otimização
        ViewHolderLista viewHolder = (ViewHolderLista) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(grupoItens.get(i));

        return view;
    }

    private class ViewHolderLista {
        private TextView tvNome, tvNotas;

        public ViewHolderLista(View view) {
            tvNome = view.findViewById(R.id.tvNome);
            tvNotas = view.findViewById(R.id.tvNotas);
        }

        public void update(GrupoItens grupoItens) {
            tvNome.setText(grupoItens.getNome());
            tvNotas.setText(grupoItens.getNotas());
        }
    }
}
