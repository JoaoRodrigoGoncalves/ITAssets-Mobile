package pt.itassets.android.modelos;

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
        private static final String TABLE_GRUPO_ITENS = "grupoitens"; // Nome da tabela
        private static final String TABLE_PEDIDOS_REQUISICAO = "pedidosrequisicao"; // Nome da tabela
        private static final String TABLE_PEDIDOS_REPARACAO = "pedidosreparcao"; // Nome da tabela

        // Nome dos campos
        private static final String NOME = "nome", SERIALNUMBER = "serialNumber", CATEGORIA_ID = "categoria_id",
                NOTAS = "notas", STATUS = "status", ID = "id";


        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            db = getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            //Criar estrutura da base de dados e tabelas
            String sqlCreateTableItem =
                    "CREATE TABLE " + TABLE_ITENS + "(" +
                            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                            NOME + " TEXT NOT NULL," +
                            SERIALNUMBER + " STRING NOT NULL," +
                            CATEGORIA_ID + " INTEGER," +
                            NOTAS + " TEXT," +
                            STATUS + " INTEGER NOT NULL" + ")";

            sqLiteDatabase.execSQL(sqlCreateTableItem);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            String sqlDropTableItem = "DROP TABLE IF EXISTS " + TABLE_ITENS;
            sqLiteDatabase.execSQL(sqlDropTableItem);

            onCreate(sqLiteDatabase);
        }

        public Item adicionarItemDB(Item item)
        {
            ContentValues values = new ContentValues();

            values.put(NOME, item.nome);
            values.put(SERIALNUMBER, item.serialNumber);
            values.put(CATEGORIA_ID, item.categoria_id);
            values.put(NOTAS, item.notas);
            values.put(STATUS, item.status);

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

            values.put(NOME, item.nome);
            values.put(SERIALNUMBER, item.serialNumber);
            values.put(CATEGORIA_ID, item.categoria_id);
            values.put(NOTAS, item.notas);
            values.put(STATUS, item.status);

            // devolve o número de linhas atualizadas
            return db.update(TABLE_ITENS, values, ID+"=?", new String[]{String.valueOf(item.getId())})==1;
        }

        public boolean removerItemDB(Item item)
        {
            // db.delete devolve o número de linhas eliminadas
            return db.delete(TABLE_ITENS, ID+"=?", new String[]{String.valueOf(item.getId())})==1;
        }

        public ArrayList<Item> getAllLivrosDB()
        {
            ArrayList<Item> itens = new ArrayList<>();

            Cursor cursor = db.query(TABLE_ITENS, new String[]{NOTAS, STATUS, NOME, SERIALNUMBER, CATEGORIA_ID, ID},
                    null, null, null, null, null);

            if(cursor.moveToFirst())
            {
                do {
                    Item itemAux = new Item(cursor.getInt(5), cursor.getInt(1), cursor.getString(3),
                            cursor.getInt(4), cursor.getString(0), cursor.getString(2));
                    itens.add(itemAux);
                }while(cursor.moveToNext());
                cursor.close();
            }
            return itens;
        }
}
