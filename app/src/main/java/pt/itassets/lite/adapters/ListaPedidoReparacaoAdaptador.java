package pt.itassets.lite.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pt.itassets.lite.R;
import pt.itassets.lite.models.Item;

public class ListaPedidoReparacaoAdaptador extends RecyclerView.Adapter<ViewHolderPedidoReparacao>{
    Context context;
    ArrayList<Item> item;
    ArrayList<Integer> itens;

    public ListaPedidoReparacaoAdaptador(Context context, ArrayList<Item> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public ViewHolderPedidoReparacao onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_adicionar_pedido_reparacao, parent, false);
        itens=new ArrayList<>();
        return new ViewHolderPedidoReparacao(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPedidoReparacao holder, int i) {
        holder.checkbox_Item.setText(item.get(i).getNome());
        holder.checkbox_Item.setId(item.get(i).getId());

        holder.checkbox_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.checkbox_Item.isChecked())
                {
                    itens.add(holder.checkbox_Item.getId());
                }
                else
                {
                    Integer itemId=holder.checkbox_Item.getId();
                    itens.remove(itemId);
                    itens.size();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public ArrayList getArrayitems()
    {
        return itens;
    }
}
