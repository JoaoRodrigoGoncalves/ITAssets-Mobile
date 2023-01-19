package pt.itassets.lite.adapters;


import pt.itassets.lite.R;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class recyclerAdapter extends RecyclerView.ViewHolder{

    CheckBox checkBoxitem;
    TextView nomeView;

    public recyclerAdapter(@NonNull View itemView) {
        super(itemView);
        checkBoxitem=itemView.findViewById(R.id.checkbox_item);
        nomeView=itemView.findViewById(R.id.textView);

    }
}
