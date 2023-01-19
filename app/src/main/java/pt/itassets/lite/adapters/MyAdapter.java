package pt.itassets.lite.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.itassets.lite.R;
import pt.itassets.lite.models.Item;
import pt.itassets.lite.models.Singleton;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context c;
    ArrayList<Item> mdata;
    ArrayList<Item> itens;

    public MyAdapter(Context c, ArrayList<Item> mdata) {
        this.c = c;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(c).inflate(R.layout.list_view, viewGroup, false);
        itens=new ArrayList<>();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.checkbox_Item.setText(mdata.get(i).getNome());
        holder.checkbox_Item.setId(mdata.get(i).getId());


        holder.checkbox_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.checkbox_Item.isChecked())
                {
                    Item item= Singleton.getInstance(c.getApplicationContext()).getItem(holder.checkbox_Item.getId());
                    itens.add(item);
                    itens.size();
                }
                else
                {
                    Item item= Singleton.getInstance(c.getApplicationContext()).getItem(holder.checkbox_Item.getId());
                    itens.remove(item.getId());
                }
            }
        });
    }

    public List getListItems() {
        return mdata;
    }

    public ArrayList getArrayitems()
    {
        return itens;
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

}
