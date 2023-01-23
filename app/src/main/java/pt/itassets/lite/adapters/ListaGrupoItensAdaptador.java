package pt.itassets.lite.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.itassets.lite.R;
import pt.itassets.lite.models.Item;

public class ListaGrupoItensAdaptador extends RecyclerView.Adapter<MyViewHolder> {
    
    Context context;
    ArrayList<Item> item;
    ArrayList<Integer> itens;

    public ListaGrupoItensAdaptador(Context context, ArrayList<Item> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.grupo_lista_grupoitensitem, viewGroup, false);
        itens=new ArrayList<>();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
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

    public List getListItems() {
        return item;
    }

    public ArrayList getArrayitems()
    {
        return itens;
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

}
