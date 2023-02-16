package pt.itassets.lite.adapters;

import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pt.itassets.lite.R;

public class ViewHolderPedidoReparacao extends RecyclerView.ViewHolder {
    public CheckBox checkbox_Item;

    public ViewHolderPedidoReparacao(@NonNull View itemView){
        super(itemView);

        checkbox_Item = itemView.findViewById(R.id.checkbox_Item);
    }

}