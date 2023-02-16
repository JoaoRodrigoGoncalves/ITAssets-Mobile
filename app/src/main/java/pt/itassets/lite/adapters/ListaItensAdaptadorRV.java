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
import pt.itassets.lite.models.GrupoItens;
import pt.itassets.lite.models.Item;

public class ListaItensAdaptadorRV extends RecyclerView.Adapter<ViewHolderItens> {
    
    Context context;
    ArrayList<Item> item;
    ArrayList<Integer> itens;



    public ListaItensAdaptadorRV(Context context, ArrayList<Item> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public ViewHolderItens onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.grupo_lista_item, viewGroup, false);
        itens=new ArrayList<>();

        return new ViewHolderItens(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderItens holder, int i) {
        if (item.size()!=0)
        {
            holder.checkbox.setText(item.get(i).getNome());
            holder.checkbox.setId(item.get(i).getId());
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(holder.checkbox.isChecked())
                    {
                        itens.add(holder.checkbox.getId());
                    }
                    else
                    {
                        Integer itemId=holder.checkbox.getId();
                        itens.remove(itemId);
                        //itens.size();
                    }
                }
            });
        }

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
        //verifica se tem algum item
        if (item!=null)
        {
            return item.size();
        }
        return 0;
    }




}
