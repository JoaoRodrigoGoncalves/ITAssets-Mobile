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
    private static final String TABLE_SITE = "site";
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
            NOME_CATEGORIA = "nome_categoria",
            SITE_ID = "site_id",
            MORADA = "morada",
            COORDENADAS = "coordenadas";


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
                        NOME_CATEGORIA + " TEXT," +
                        SITE_ID + " INTEGER" + ")";

        sqlLiteDatabase.execSQL(sqlCreateTableItem);

        String sqlCreateTableSite =
                "CREATE TABLE " + TABLE_SITE + "(" +
                        ID + " INTEGER PRIMARY KEY," +
                        NOME + " TEXT NOT NULL," +
                        MORADA + " TEXT," +
                        COORDENADAS + " TEXT" + ")";

        sqlLiteDatabase.execSQL(sqlCreateTableSite);

        String sqlCreateTableGrupoItens =
                "CREATE TABLE " + TABLE_GRUPO_ITENS + "(" +
                        ID + " INTEGER PRIMARY KEY," +
                        NOME + " TEXT NOT NULL," +
                        NOTAS + " TEXT," +
                        STATUS + " INTEGER NOT NULL" + ")";


        sqlLiteDatabase.execSQL(sqlCreateTableGrupoItens);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlLiteDatabase, int i, int i1) {
        String sqlDropTableItem = "DROP TABLE IF EXISTS " + TABLE_ITENS;
        sqlLiteDatabase.execSQL(sqlDropTableItem);

        String sqlDropTableSite = "DROP TABLE IF EXISTS " + TABLE_SITE;
        sqlLiteDatabase.execSQL(sqlDropTableSite);

        String sqlDropTableGrupoItens = "DROP TABLE IF EXISTS " + TABLE_GRUPO_ITENS;
        sqlLiteDatabase.execSQL(sqlDropTableGrupoItens);

        onCreate(sqlLiteDatabase);
    }

    //region Funções Tabela Itens

    public Item adicionarItemDB(Item item)
    {
        ContentValues values = new ContentValues();

        values.put(ID, item.getId());
        values.put(NOME, item.getNome());
        values.put(SERIALNUMBER, item.getSerialNumber());
        values.put(NOME_CATEGORIA, item.getNome_Categoria());
        values.put(NOTAS, item.getNotas());
        values.put(STATUS, item.getStatus());
        values.put(SITE_ID, item.getSite_id());

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
        values.put(NOME_CATEGORIA, item.getNome_Categoria());
        values.put(NOTAS, item.getNotas());
        values.put(STATUS, item.getStatus());
        values.put(SITE_ID, item.getSite_id());

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

        Cursor cursor = db.query(TABLE_ITENS, new String[]{ID, NOME, SERIALNUMBER, NOTAS, STATUS, NOME_CATEGORIA, SITE_ID},
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
                        (cursor.isNull(5) ? null : cursor.getString(5)), // categoria
                        (cursor.isNull(6) ? null : cursor.getInt(6)) // site
                );
                itens.add(itemAux);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return itens;
    }

    //endregion

    //region Funções Tabela Sites

    public Site getSiteDB(Integer id)
    {
       Site site = null;

        Cursor cursor = db.query(TABLE_SITE, new String[]{ID, NOME, MORADA, COORDENADAS},
                ID + "=" + id, null, null, null, null);

        if(cursor.moveToFirst())
        {
                site = new Site(
                        cursor.getInt(0), //ID
                        cursor.getString(1), //Nome
                        (cursor.isNull(2) ? null : cursor.getString(2)), //Morada
                        (cursor.isNull(3) ? null : cursor.getString(3)) // Coordenadas
                );
            cursor.close();
        }
        return site;
    }

    public Site adicionarSiteDB(Site site)
    {
        ContentValues values = new ContentValues();

        values.put(ID, site.getId());
        values.put(NOME, site.getNome());
        values.put(MORADA, site.getMorada());
        values.put(COORDENADAS, site.getCoordenadas());

        // devolve -1 em caso de erro, ou o id do novo site (long)
        int id = (int) db.insert(TABLE_SITE, null, values);
        if(id != -1)
        {
            site.setId(id);
            return site;
        }
        else
        {
            return null;
        }
    }

    public boolean editarSiteDB(Site site)
    {
        ContentValues values = new ContentValues();

        values.put(ID, site.getId());
        values.put(NOME, site.getNome());
        values.put(MORADA, site.getMorada());
        values.put(COORDENADAS, site.getCoordenadas());

        // devolve o número de linhas atualizadas
        return db.update(TABLE_SITE, values, ID+"=?", new String[]{String.valueOf(site.getId())})==1;
    }

    //endregion

    //region Funções Tabela Grupo de Itens

    public GrupoItens adicionarGrupoItensDB(GrupoItens grupoItens)
    {
        ContentValues values = new ContentValues();

        values.put(ID, grupoItens.getId());
        values.put(NOME, grupoItens.getNome());
        values.put(NOTAS, grupoItens.getNotas());
        values.put(STATUS, grupoItens.getStatus());

        // devolve -1 em caso de erro, ou o id do novo grupo de itens (long)
        int id = (int) db.insert(TABLE_GRUPO_ITENS, null, values);
        if(id != -1)
        {
            grupoItens.setId(id);
            return grupoItens;
        }
        else
        {
            return null;
        }
    }

    public boolean editarGrupoItensDB(GrupoItens grupoItens)
    {
        ContentValues values = new ContentValues();

        values.put(NOME, grupoItens.getNome());
        values.put(NOTAS, grupoItens.getNotas());
        values.put(STATUS, grupoItens.getStatus());

        // devolve o número de linhas atualizadas
        return db.update(TABLE_GRUPO_ITENS, values, ID+"=?", new String[]{String.valueOf(grupoItens.getId())})==1;
    }

    public boolean removerGrupoItensDB(GrupoItens grupoItens)
    {
        // db.delete devolve o número de linhas eliminadas
        return db.delete(TABLE_GRUPO_ITENS, ID+"=?", new String[]{String.valueOf(grupoItens.getId())})==1;
    }

    public void removerAllGrupoItensDB()
    {
        db.delete(TABLE_GRUPO_ITENS, null, null);
    }

    public ArrayList<GrupoItens> getAllGruposItensDB()
    {
        ArrayList<GrupoItens> grupoItens = new ArrayList<>();

        Cursor cursor = db.query(TABLE_GRUPO_ITENS, new String[]{ID, NOME, NOTAS, STATUS},
                null, null, null, null, null);

        if(cursor.moveToFirst())
        {
            do {
                GrupoItens grupoItensAux = new GrupoItens(
                        cursor.getInt(0), //ID
                        cursor.getInt(3), // status
                        cursor.getString(1), //Nome
                        (cursor.isNull(2) ? null : cursor.getString(2))// Notas
                );
                grupoItens.add(grupoItensAux);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return grupoItens;
    }

    //endregion
}
