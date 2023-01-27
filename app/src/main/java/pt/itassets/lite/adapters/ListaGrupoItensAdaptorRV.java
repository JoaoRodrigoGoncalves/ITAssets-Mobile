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

public class ListaGrupoItensAdaptorRV extends RecyclerView.Adapter<ViewHolderItens>{
    Context context;
    ArrayList<GrupoItens> grupoItensArrayList;
    //era susposto tar aqi grupoItens so mas por alguma razao ele nao quer
    ArrayList<Integer> grupoItens_id;



    public ListaGrupoItensAdaptorRV(Context context, ArrayList<GrupoItens> grupoItens) {
        this.context = context;
        this.grupoItensArrayList = grupoItens;
    }

    @NonNull
    @Override
    public ViewHolderItens onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.grupo_lista_item, viewGroup, false);
        grupoItens_id=new ArrayList<>();

        return new ViewHolderItens(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderItens holder, int i) {
        if (grupoItensArrayList.size()!=0)
        {
            holder.checkbox.setText(grupoItensArrayList.get(i).getNome());
            holder.checkbox.setId(grupoItensArrayList.get(i).getId());
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(holder.checkbox.isChecked())
                    {
                        grupoItens_id.add(holder.checkbox.getId());
                    }
                    else
                    {
                        Integer grupoId=holder.checkbox.getId();
                        grupoItens_id.remove(grupoId);

                    }
                }
            });
        }

    }

    public List getListItems() {

        return grupoItensArrayList;
    }


    public ArrayList getArrayGrupo()
    {
        return grupoItens_id;
    }


    @Override
    public int getItemCount() {
        //verifica se tem algum item
        if (grupoItensArrayList!=null)
        {
            return grupoItensArrayList.size();
        }
        return 0;
    }
}
