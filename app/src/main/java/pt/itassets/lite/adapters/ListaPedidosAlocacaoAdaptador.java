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
import pt.itassets.lite.models.Alocacao;

public class ListaPedidosAlocacaoAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Alocacao> alocacoes;

    public ListaPedidosAlocacaoAdaptador(Context context, ArrayList<Alocacao> alocacoes) {
        this.context = context;
        this.alocacoes = alocacoes;
    }

    @Override
    public int getCount() {
        return alocacoes.size();
    }

    @Override
    public Object getItem(int i) {
        return alocacoes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return alocacoes.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view == null){
            view = inflater.inflate(R.layout.item_lista_alocacoes, null);
        }

        //Otimização
        ViewHolderLista viewHolder = (ViewHolderLista) view.getTag();
        if(viewHolder == null){
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(alocacoes.get(i));

        return view;
    }

    private class ViewHolderLista{
        private TextView tvId, tvData, tvItem, tvStatus;

        public ViewHolderLista(View view){
            tvId=view.findViewById(R.id.tvId);
            tvData=view.findViewById(R.id.tvDataPedido);
            tvItem=view.findViewById(R.id.tvItemNome);
            tvStatus=view.findViewById(R.id.tvStatus);
        }

        public void update(Alocacao alocacao){
            tvId.setText(String.valueOf(alocacao.getId()));
            tvData.setText(String.valueOf(alocacao.getDataPedido()));
            //tvItem.setText(alocacao.getNome_item());
            if(alocacao.getNome_item() == null)
            {
                tvItem.setText("Não Aplicável");
                tvItem.setTypeface(tvItem.getTypeface(), Typeface.ITALIC);
            }
            else
            {
                tvItem.setText(String.valueOf(alocacao.getNome_item()));
            }
            tvStatus.setText(String.valueOf(alocacao.getStatus()));
        }
    }
}
