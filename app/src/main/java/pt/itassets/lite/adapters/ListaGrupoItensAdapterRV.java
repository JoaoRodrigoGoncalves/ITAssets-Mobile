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

public class ListaGrupoItensAdapterRV extends RecyclerView.Adapter<ViewHolderItens> {


    /*
    * Estamos a usar a mesma base da lista dos itens mas depois separamos cada uma devido as categorias nao serem iguais
    * e devido a eu nao saber mexer com isto*/
    Context context;
    ArrayList<GrupoItens> grupoItens;
    ArrayList<Integer> grupoItens_id;

    public ListaGrupoItensAdapterRV(Context context, ArrayList<GrupoItens> grupoItens) {
        this.context = context;
        this.grupoItens = grupoItens;
    }

    @NonNull


    @Override
    public ViewHolderItens onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.grupo_lista_item, viewGroup, false);
        grupoItens=new ArrayList<>();

        return new ViewHolderItens(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderItens holder, int i) {
        if (grupoItens.size() != 0) {
            holder.checkbox.setText(grupoItens.get(i).getNome());
            holder.checkbox.setId(grupoItens.get(i).getId());
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.checkbox.isChecked()) {
                        grupoItens_id.add(holder.checkbox.getId());
                        grupoItens_id.size();


                    } else {
                        Integer grupoId = holder.checkbox.getId();
                        grupoItens_id.remove(grupoId);
                        grupoItens_id.size();
                    }
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public List getListGrupo() {

        return grupoItens;
    }


    public ArrayList getArrayGrupo()
    {
        return grupoItens_id;
    }

    public int getGrupoCount() {
        //verifica se tem algum item
        if (grupoItens_id!=null)
        {
            return grupoItens_id.size();
        }
        return 0;
    }

}
