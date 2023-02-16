package pt.itassets.lite.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pt.itassets.lite.R;
import pt.itassets.lite.models.PedidoAlocacao;

public class ListaPedidosAlocacaoAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<PedidoAlocacao> alocacoes;

    public ListaPedidosAlocacaoAdaptador(Context context, ArrayList<PedidoAlocacao> alocacoes) {
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
        private TextView tvId, tvData, tvItem, tvStatus, tvObjeto;

        public ViewHolderLista(View view){
            tvId=view.findViewById(R.id.tvId);
            tvData=view.findViewById(R.id.tvDataPedido);
            tvItem=view.findViewById(R.id.tvItemNome);
            tvStatus=view.findViewById(R.id.tvStatus);
            tvObjeto = view.findViewById(R.id.TV_tipo_objeto);
        }

        public void update(PedidoAlocacao pedidoAlocacao){
            tvId.setText(String.valueOf(pedidoAlocacao.getId()));
            tvData.setText(String.valueOf(pedidoAlocacao.getDataPedido()));

            if(pedidoAlocacao.getNome_item() != null)
            {
                tvObjeto.setText(context.getText(R.string.txt_item));
                tvItem.setText(String.valueOf(pedidoAlocacao.getNome_item()));
            }
            else
            {
                tvObjeto.setText(context.getString(R.string.txt_grupo));
                tvItem.setText(String.valueOf(pedidoAlocacao.getNome_grupoItem()));
            }
            tvStatus.setText(pedidoAlocacao.humanReadableStatus(context));
        }
    }
}
