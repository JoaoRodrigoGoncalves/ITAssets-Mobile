package pt.itassets.lite.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pt.itassets.lite.R;
import pt.itassets.lite.models.PedidoReparacao;

public class ListaPedidosReparacaoAdaptador extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<PedidoReparacao> reparacoes;

    public ListaPedidosReparacaoAdaptador(Context context, ArrayList<PedidoReparacao> reparacoes) {
        this.context = context;
        this.reparacoes = reparacoes;
    }

    @Override
    public int getCount() {
        return reparacoes.size();
    }

    @Override
    public Object getItem(int i) {
        return reparacoes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return reparacoes.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view == null){
            view = inflater.inflate(R.layout.item_lista_reparacoes, null);
        }

        //Otimização
        ViewHolderLista viewHolder = (ViewHolderLista) view.getTag();
        if(viewHolder == null){
            viewHolder = new ViewHolderLista(view);
            view.setTag(viewHolder);
        }

        viewHolder.update(reparacoes.get(i));

        return view;
    }

    private class ViewHolderLista{
        private TextView tvId, tvData, tvStatus;

        public ViewHolderLista(View view){
            tvId=view.findViewById(R.id.tvId);
            tvData=view.findViewById(R.id.tvDataPedido);
            tvStatus=view.findViewById(R.id.tvStatus);
        }

            public void update(PedidoReparacao reparacao){
            tvId.setText(String.valueOf(reparacao.getId()));
            tvData.setText(String.valueOf(reparacao.getDataPedido()));
            tvStatus.setText(reparacao.humanReadableStatus(context));
        }
    }
}
