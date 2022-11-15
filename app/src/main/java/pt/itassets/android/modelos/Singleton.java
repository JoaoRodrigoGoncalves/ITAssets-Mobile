package pt.itassets.android.modelos;

import java.util.ArrayList;

import pt.itassets.android.R;

public class Singleton {

    private static Singleton instance=null;
    private ArrayList<Itens> itens;

    public static synchronized Singleton getInstance(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    private Singleton(){
        itens = new ArrayList<>();
        itens.add(new Itens(1,351515313, R.drawable.pc,"Computador - 1","Equipamento"));
        itens.add(new Itens(2,789456123, R.drawable.pc,"Computador - 2","Equipamento"));
        itens.add(new Itens(3,741852963, R.drawable.pc, "Computador - 3","Equipamento"));
        itens.add(new Itens(4,987456321, R.drawable.pc,"Computador - 4","Equipamento"));
        itens.add(new Itens(5,951753852, R.drawable.pc,"Computador - 5","Equipamento"));
        itens.add(new Itens(6,357485962, R.drawable.pc, "Computador - 6","Equipamento"));
        itens.add(new Itens(7,247513896, R.drawable.pc,"Computador - 7","Equipamento"));
        itens.add(new Itens(8,745745475, R.drawable.pc,"Computador - 8","Equipamento"));
        itens.add(new Itens(9,123546888, R.drawable.pc, "Computador - 9","Equipamento"));
        itens.add(new Itens(10,915456865, R.drawable.pc,"Computador - 10","Equipamento"));
    }

    public ArrayList<Itens> getItens() {
        return new ArrayList<>(itens);
    }
}
