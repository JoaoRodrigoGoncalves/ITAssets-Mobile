package pt.itassets.lite.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pt.itassets.lite.R;
import pt.itassets.lite.models.DBHelper;
import pt.itassets.lite.models.Item;

public class ListaItensAdaptador extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Item> itens;

    public ListaItensAdaptador(Context context, ArrayList<Item> itens) {
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
        private TextView tvNome, tvNumSerie, tvCategoria, tvNotas, tvStatus;

        public ViewHolderLista(View view){
            tvNome=view.findViewById(R.id.tvNome);
            tvNumSerie=view.findViewById(R.id.tvNumSerie);
            tvCategoria=view.findViewById(R.id.tvCategoria);
        }

        public void update(Item item){
            tvNome.setText(item.getNome());
            tvNumSerie.setText(item.getSerialNumber());

            if(item.getNome_Categoria() == null)
            {
                tvCategoria.setText("Não Aplicável");
                tvCategoria.setTypeface(tvCategoria.getTypeface(), Typeface.ITALIC);
            }
            else
            {
                tvCategoria.setText(String.valueOf(item.getNome_Categoria()));
            }
        }
    }
}
