package pt.itassets.lite.adapters;

import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pt.itassets.lite.R;

public class ViewHolderItens extends RecyclerView.ViewHolder {
    public CheckBox checkbox;

    public ViewHolderItens(@NonNull View itemView){
        super(itemView);

        checkbox = itemView.findViewById(R.id.checkbox_Item);
    }

}
