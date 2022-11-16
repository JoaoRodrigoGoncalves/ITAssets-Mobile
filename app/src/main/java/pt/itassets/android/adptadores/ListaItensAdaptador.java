package pt.itassets.android.adptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import pt.itassets.android.R;
import pt.itassets.android.modelos.Itens;

public class ListaItensAdaptador extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Itens> itens;

    public ListaItensAdaptador(Context context, ArrayList<Itens> itens) {
        this.context = context;
        this.itens = itens;
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Object getItem(int i) {
        return itens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return itens.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view == null){
            view = inflater.inflate(R.layout.item_lista_itens, null);
        }

        //Otimização
        ViewHolderLista viewHolder = (ViewHolderLista) view.getTag();
        if(viewHolder == null){
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(itens.get(i));

        return view;
    }

    private class ViewHolderLista{
        private ImageView imgCapa;
        private TextView tvNome, tvNumSerie, tvCategoria;

        public ViewHolderLista(View view){
            tvNome=view.findViewById(R.id.tvNome);
            tvNumSerie=view.findViewById(R.id.tvNumSerie);
            tvCategoria=view.findViewById(R.id.tvCategoria);
            imgCapa=view.findViewById(R.id.imgCapa);
        }

        public void update(Itens itens){
            tvNome.setText(itens.getNome());
            tvNumSerie.setText(itens.getNumserie()+"");
            tvCategoria.setText(itens.getCategoria());
            imgCapa.setImageResource(itens.getCapa());
        }
    }
}
