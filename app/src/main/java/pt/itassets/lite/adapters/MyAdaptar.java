package pt.itassets.lite.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.itassets.lite.R;
import pt.itassets.lite.models.Item;

public class MyAdaptar extends RecyclerView.Adapter<recyclerAdapter> {

    Context context;
    List<Item>items;

    public MyAdaptar(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public recyclerAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new recyclerAdapter(LayoutInflater.from(context).inflate(R.layout.activity_adicionar_grupo_itens,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter holder, int position) {
        holder.nomeView.setText(items.get(position).getNome());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
