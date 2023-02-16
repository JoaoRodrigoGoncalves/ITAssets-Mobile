package pt.itassets.lite.adapters;

import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pt.itassets.lite.R;

public class ViewHolderGrupoItens extends RecyclerView.ViewHolder {
    public CheckBox checkbox;

    public ViewHolderGrupoItens(@NonNull View itemView){
        super(itemView);

        checkbox = itemView.findViewById(R.id.checkbox_Item);
    }

}
