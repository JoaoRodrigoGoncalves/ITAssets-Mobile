package pt.itassets.android.vistas;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.itassets.android.Helper;
import pt.itassets.android.R;
import pt.itassets.android.adptadores.ListaItensAdaptador;
import pt.itassets.android.modelos.Item;
import pt.itassets.android.modelos.Singleton;

public class ListagemItensFragment extends Fragment {
    private ListView lvItens;
    private ArrayList<Item> itens;
    private FloatingActionButton fabLista;

    public ListagemItensFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listagem_itens, container, false);

        lvItens = view.findViewById(R.id.lvItens);
        fabLista=view.findViewById(R.id.fabLista);

        itens = Singleton.getInstance(getContext()).getItens();
        lvItens.setAdapter(new ListaItensAdaptador(getContext(), itens) {});

        lvItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positions, long id) {

            }
        });

        return view;
    }

    private void itens(){
        itens = new ArrayList<Item>();

        SharedPreferences user_preferences = getContext().getSharedPreferences(Helper.USER_STORAGE, MODE_PRIVATE);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.start();
        Gson gson = new Gson();

        Singleton singleton = Singleton.getInstance(getContext());

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Helper.APP_SYSTEM_DOMAIN_URL + "item", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + user_preferences.getString(Helper.USER_TOKEN, null));
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}