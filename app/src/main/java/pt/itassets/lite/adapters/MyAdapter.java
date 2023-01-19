package pt.itassets.lite.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.itassets.lite.R;
import pt.itassets.lite.models.Item;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context c;
    List<Item> mdata;

    public MyAdapter(Context c, List<Item> mdata) {
        this.c = c;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(c).inflate(R.layout.list_view, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.checkbox_Item.setText(mdata.get(i).getNome());
        holder.checkbox_Item.setId(mdata.get(i).getId());
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }
}
