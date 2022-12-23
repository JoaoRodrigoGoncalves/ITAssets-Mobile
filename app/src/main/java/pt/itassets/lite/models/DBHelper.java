package pt.itassets.lite.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DB_ITASSETS";
    private static final int DB_VERSION = 1;

    private final SQLiteDatabase db;

    private static final String TABLE_ITENS = "itens"; // Nome da tabela
    private static final String TABLE_CATEGORIA = "categorias";
    private static final String TABLE_GRUPO_ITENS = "grupoitens"; // Nome da tabela
    private static final String TABLE_PEDIDOS_REQUISICAO = "pedidosrequisicao"; // Nome da tabela
    private static final String TABLE_PEDIDOS_REPARACAO = "pedidosreparcao"; // Nome da tabela

    // Nome dos campos
    private static final String
            ID = "id",
            NOME = "nome",
            SERIALNUMBER = "serialNumber",
            NOTAS = "notas",
            STATUS = "status",
            CATEGORIA_ID = "categoria_id",
            SITE_ID = "site_id";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqlLiteDatabase) {
        //Criar estrutura da base de dados e tabelas
        String sqlCreateTableItem =
                "CREATE TABLE " + TABLE_ITENS + "(" +
                        ID + " INTEGER PRIMARY KEY," +
                        NOME + " TEXT NOT NULL," +
                        SERIALNUMBER + " TEXT," +
                        NOTAS + " TEXT," +
                        STATUS + " INTEGER NOT NULL," +
                        CATEGORIA_ID + " INTEGER," +
                        SITE_ID + " INTEGER" + ")";

        sqlLiteDatabase.execSQL(sqlCreateTableItem);

        String sqlCreateTableCategoria =
                "CREATE TABLE " + TABLE_CATEGORIA + "(" +
                    ID + " INTEGER PRIMARY KEY," +
                    NOME + " TEXT NOT NULL," +
                    STATUS + " INTEGER NOT NULL" + ")";

        sqlLiteDatabase.execSQL(sqlCreateTableCategoria);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlLiteDatabase, int i, int i1) {
        String sqlDropTableItem = "DROP TABLE IF EXISTS " + TABLE_ITENS;
        sqlLiteDatabase.execSQL(sqlDropTableItem);

        String sqlDropTableCategoria = "DROP TABLE IF EXISTS " + TABLE_CATEGORIA;
        sqlLiteDatabase.execSQL(sqlDropTableCategoria);

        onCreate(sqlLiteDatabase);
    }

    //region Funções Tabela Itens

    public Item adicionarItemDB(Item item)
    {
        ContentValues values = new ContentValues();

        values.put(ID, item.getId());
        values.put(NOME, item.getNome());
        values.put(SERIALNUMBER, item.getSerialNumber());
        values.put(CATEGORIA_ID, item.getCategoria_id());
        values.put(NOTAS, item.getNotas());
        values.put(STATUS, item.getStatus());

        // devolve -1 em caso de erro, ou o id do novo livro (long)
        int id = (int) db.insert(TABLE_ITENS, null, values);
        if(id != -1)
        {
            item.setId(id);
            return item;
        }
        else
        {
            return null;
        }
    }

    public boolean editarItemDB(Item item)
    {
        ContentValues values = new ContentValues();

        values.put(NOME, item.getNome());
        values.put(SERIALNUMBER, item.getSerialNumber());
        values.put(CATEGORIA_ID, item.getCategoria_id());
        values.put(NOTAS, item.getNotas());
        values.put(STATUS, item.getStatus());

        // devolve o número de linhas atualizadas
        return db.update(TABLE_ITENS, values, ID+"=?", new String[]{String.valueOf(item.getId())})==1;
    }

    public boolean removerItemDB(Item item)
    {
        // db.delete devolve o número de linhas eliminadas
        return db.delete(TABLE_ITENS, ID+"=?", new String[]{String.valueOf(item.getId())})==1;
    }

    public void removerAllItemDB()
    {
        db.delete(TABLE_ITENS, null, null);
    }

    public ArrayList<Item> getAllItensDB()
    {
        ArrayList<Item> itens = new ArrayList<>();

        Cursor cursor = db.query(TABLE_ITENS, new String[]{ID, NOME, SERIALNUMBER, NOTAS, STATUS, CATEGORIA_ID, SITE_ID},
                null, null, null, null, null);

        if(cursor.moveToFirst())
        {
            do {
                Item itemAux = new Item(
                        cursor.getInt(0), //ID
                        cursor.getString(1), //Nome
                        (cursor.isNull(2) ? null : cursor.getString(2)), //Serial
                        (cursor.isNull(3) ? null : cursor.getString(3)), // Notas
                        cursor.getInt(4), // status
                        (cursor.isNull(5) ? null : cursor.getInt(5)), // categoria
                        (cursor.isNull(6) ? null : cursor.getInt(6)) // site
                );
                itens.add(itemAux);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return itens;
    }

    //endregion

    //region Funções Tabela Categoria

    public Categoria adicionarCategoriaDB(Categoria categoria)
    {
        ContentValues values = new ContentValues();

        values.put(ID, categoria.getId());
        values.put(NOME, categoria.getNome());
        values.put(STATUS, categoria.getStatus());

        // devolve -1 em caso de erro, ou o id da nova categoria (long)
        int id = (int) db.insert(TABLE_CATEGORIA, null, values);
        if(id != -1)
        {
            categoria.setId(id);
            return categoria;
        }
        else
        {
            return null;
        }
    }

    public boolean editarCategoriaDB(Categoria categoria)
    {
        ContentValues values = new ContentValues();

        values.put(NOME, categoria.getNome());
        values.put(STATUS, categoria.getStatus());

        // devolve o número de linhas atualizadas
        return db.update(TABLE_CATEGORIA, values, ID+"=?", new String[]{String.valueOf(categoria.getId())})==1;
    }

    public Categoria getCategoriaDB(Integer id)
    {
        Categoria cat = null;

        Cursor cursor = db.query(TABLE_CATEGORIA, new String[]{ID, NOME, STATUS},
                ID + " = " + id, null,
                null, null, null);

        if(cursor.moveToFirst())
        {
            cat = new Categoria(
                    cursor.getInt(0), //ID
                    cursor.getString(1), //Nome
                    cursor.getInt(2) // status
            );
            cursor.close();
        }
        return cat;
    }

    //endregion
}
