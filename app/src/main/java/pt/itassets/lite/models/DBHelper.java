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
    private static final String TABLE_ITENS_GRUPO ="grupoitensitem";//nome da tabela
    private static final String TABLE_PEDIDOS_REQUISICAO = "pedidosrequisicao"; // Nome da tabela
    private static final String TABLE_PEDIDO_REPARACAO = "pedido_reparcao"; // Nome da tabela
    private static final String TABLE_LINHA_PEDIDO_REPARACAO = "linha_pedido_reparacao"; // Nome da tabelax

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
            COORDENADAS = "coordenadas",
            DATAPEDIDO = "dataPedido",
            DATAINICIO = "dataInicio",
            DATAFIM = "dataFim",
            DESCRICAO_PROBLEMA = "descricaoProblema",
            NOME_RESPONSAVEL = "nome_responsavel",
            RESPOSTA_OBS = "respostaObs",
            OBS = "obs",
            OBSRESPOSTA = "obsResposta",
            NOME_REQUERENTE = "nome_requerente",
            NOME_APROVADOR = "nome_aprovador",
            NOME_ITEM = "nome_item",
            NOME_GRUPO = "nome_grupoItem",
            GRUPOITENSID ="grupoitensid",
            ITEMID="itemid",
            USER_ID="userid",
            PEDIDO_ALOCACAO_ID = "pedido_alocacao_id",
            PEDIDO_REPARACAO_ID = "pedido_reparacao_id";

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
                        SITE_ID + " INTEGER," +
                        PEDIDO_ALOCACAO_ID + " INTEGER," +
                        PEDIDO_REPARACAO_ID + " INTEGER" +")";

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
                        STATUS + " INTEGER NOT NULL," +
                        PEDIDO_ALOCACAO_ID + " INTEGER," +
                        PEDIDO_REPARACAO_ID + " INTEGER" +")";

        sqlLiteDatabase.execSQL(sqlCreateTableGrupoItens);

        String  sqlCreateTableGrupoItensItem=
                "CREATE TABLE " + TABLE_ITENS_GRUPO + "(" +
                        ID + " INTEGER PRIMARY KEY," +
                        GRUPOITENSID + " INTEGER NOT NULL," +
                        ITEMID + " INTEGER NOT NULL" +")";


        sqlLiteDatabase.execSQL(sqlCreateTableGrupoItensItem);

        String sqlCreateTablePedidosAlocacao =
                "CREATE TABLE " + TABLE_PEDIDOS_REQUISICAO + "(" +
                        ID + " INTEGER PRIMARY KEY," +
                        OBS + " TEXT," +
                        OBSRESPOSTA + " TEXT," +
                        DATAPEDIDO + " TEXT NOT NULL," +
                        DATAINICIO + " TEXT," +
                        DATAFIM + " TEXT," +
                        NOME_REQUERENTE + " TEXT NOT NULL," +
                        NOME_APROVADOR + " TEXT," +
                        NOME_ITEM + " TEXT," +
                        ITEMID + " INTEGER," +
                        NOME_GRUPO + " TEXT," +
                        GRUPOITENSID + " INTEGER, " +
                        USER_ID + " INTEGER NOT NULL," +
                        STATUS + " INTEGER NOT NULL" + ")";

        sqlLiteDatabase.execSQL(sqlCreateTablePedidosAlocacao);

        String sqlCreateTablePedidoReparacao =
                "CREATE TABLE " + TABLE_PEDIDO_REPARACAO + "(" +
                        ID + " INTEGER PRIMARY KEY," +
                        DATAPEDIDO + " TEXT NOT NULL," +
                        DATAINICIO + " TEXT," +
                        DATAFIM + " TEXT," +
                        DESCRICAO_PROBLEMA + " TEXT," +
                        NOME_REQUERENTE + " TEXT NOT NULL," +
                        NOME_RESPONSAVEL + " TEXT," +
                        RESPOSTA_OBS + " TEXT," +
                        STATUS + " INTEGER NOT NULL" + ")";
        sqlLiteDatabase.execSQL(sqlCreateTablePedidoReparacao);

        String sqlCreateTableLinhaPedidoReparacao =
                "CREATE TABLE " + TABLE_LINHA_PEDIDO_REPARACAO + "(" +
                        ID + " INTEGER PRIMARY KEY," +
                        PEDIDO_REPARACAO_ID + " INTEGER NOT NULL," +
                        ITEMID + " INTEGER," +
                        GRUPOITENSID + " INTEGER)";

        sqlLiteDatabase.execSQL(sqlCreateTableLinhaPedidoReparacao);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlLiteDatabase, int i, int i1) {
        String sqlDropTableItem = "DROP TABLE IF EXISTS " + TABLE_ITENS;
        sqlLiteDatabase.execSQL(sqlDropTableItem);

        String sqlDropTableSite = "DROP TABLE IF EXISTS " + TABLE_SITE;
        sqlLiteDatabase.execSQL(sqlDropTableSite);

        String sqlDropTableGrupoItens = "DROP TABLE IF EXISTS " + TABLE_GRUPO_ITENS;
        sqlLiteDatabase.execSQL(sqlDropTableGrupoItens);

        String sqlDropTablePedidoReparacao = "DROP TABLE IF EXISTS " + TABLE_PEDIDO_REPARACAO;
        sqlLiteDatabase.execSQL(sqlDropTablePedidoReparacao);

        String sqlDropTablePedidosAlocacao = "DROP TABLE IF EXISTS " + TABLE_PEDIDOS_REQUISICAO;
        sqlLiteDatabase.execSQL(sqlDropTablePedidosAlocacao);

        String sqlDropTableGrupoItensItem = "DROP TABLE IF EXISTS " + TABLE_ITENS_GRUPO;
        sqlLiteDatabase.execSQL(sqlDropTableGrupoItensItem);

        String sqlDropTableLinhaPedidoReparacao = "DROP TABLE IF EXISTS " + TABLE_LINHA_PEDIDO_REPARACAO;
        sqlLiteDatabase.execSQL(sqlDropTableLinhaPedidoReparacao);

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
        values.put(PEDIDO_ALOCACAO_ID, item.getPedido_alocacao_id());
        values.put(PEDIDO_REPARACAO_ID, item.getPedido_reparacao_id());

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
        values.put(PEDIDO_ALOCACAO_ID, item.getPedido_alocacao_id());
        values.put(PEDIDO_REPARACAO_ID, item.getPedido_reparacao_id());

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

        Cursor cursor = db.query(TABLE_ITENS, new String[]{ID, NOME, SERIALNUMBER, NOTAS, STATUS, NOME_CATEGORIA, SITE_ID, PEDIDO_ALOCACAO_ID,PEDIDO_REPARACAO_ID},
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
                        (cursor.isNull(6) ? null : cursor.getInt(6)), // site
                        (cursor.isNull(7) ? null : cursor.getInt(7)), //pedido alocação id
                        (cursor.isNull(8) ? null : cursor.getInt(8)) //pedido alocação id
                );
                itens.add(itemAux);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return itens;
    }

    public Item FindItemDB(Integer id_Item)
    {
        Cursor cursor = db.query(TABLE_ITENS, new String[]{ID, NOME, SERIALNUMBER, NOTAS, STATUS, NOME_CATEGORIA, SITE_ID, PEDIDO_ALOCACAO_ID,PEDIDO_REPARACAO_ID},
                ID + "=" + id_Item, null, null, null, null);

        Item itemAux = null;
        if (cursor.moveToFirst()) {

            itemAux = new Item(
                    cursor.getInt(0), //ID
                    cursor.getString(1), //Nome
                    (cursor.isNull(2) ? null : cursor.getString(2)), //Serial
                    (cursor.isNull(3) ? null : cursor.getString(3)), // Notas
                    cursor.getInt(4), // status
                    (cursor.isNull(5) ? null : cursor.getString(5)), // categoria
                    (cursor.isNull(6) ? null : cursor.getInt(6)), // site
                    (cursor.isNull(7) ? null : cursor.getInt(7)), // pedido alocacao id
                    (cursor.isNull(8) ? null : cursor.getInt(8)) // pedido alocacao id
            );

            cursor.close();
        }
        return itemAux;
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
        values.put(PEDIDO_ALOCACAO_ID, grupoItens.getPedido_alocacao_id());
        values.put(PEDIDO_REPARACAO_ID, grupoItens.getPedido_reparacao_id());

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
        values.put(PEDIDO_ALOCACAO_ID, grupoItens.getPedido_alocacao_id());

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

        Cursor cursor = db.query(TABLE_GRUPO_ITENS, new String[]{ID, NOME, NOTAS, STATUS, PEDIDO_ALOCACAO_ID,PEDIDO_REPARACAO_ID},
                null, null, null, null, null);

        if(cursor.moveToFirst())
        {
            do {
                GrupoItens grupoItensAux = new GrupoItens(
                        cursor.getInt(0), //ID
                        cursor.getInt(3), // status
                        cursor.getString(1), //Nome
                        (cursor.isNull(2) ? null : cursor.getString(2)), // Notas
                        (cursor.isNull(4) ? null : cursor.getInt(4)), // pedido alocacao id
                        (cursor.isNull(5) ? null : cursor.getInt(5)) // pedido Reparacao id
                );
                grupoItens.add(grupoItensAux);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return grupoItens;
    }

    //endregion

    //Funcoes Tabela Linha de Reparacao

    public LinhaPedidoReparacao adicionarLinhaPedidoReparacaoDB(LinhaPedidoReparacao linhaPedidoReparacao)
    {
        ContentValues values = new ContentValues();

        values.put(PEDIDO_REPARACAO_ID, linhaPedidoReparacao.getId_reparacao());
        values.put(ITEMID, linhaPedidoReparacao.getItem_id());
        values.put(GRUPOITENSID, linhaPedidoReparacao.getGrupo_id());


        // devolve -1 em caso de erro, ou o id do novo objeto (long)
        int id = (int) db.insert(TABLE_LINHA_PEDIDO_REPARACAO, null, values);
        if(id != -1)
        {
            linhaPedidoReparacao.setId(id);
            return linhaPedidoReparacao;
        }
        else
        {
            return null;
        }
    }
    //endregion

    //region Funções Tabela Reparacoes

    public PedidoReparacao adicionarPedidoReparacaoDB(PedidoReparacao pedido)
    {
        ContentValues values = new ContentValues();

        values.put(ID, pedido.getId());
        values.put(NOME_REQUERENTE, pedido.getNome_requerente());
        values.put(STATUS, pedido.getStatus());
        values.put(DESCRICAO_PROBLEMA, pedido.getDescricaoProblema());
        values.put(DATAPEDIDO, pedido.getDataPedido());


        // devolve -1 em caso de erro, ou o id do novo objeto (long)
        int id = (int) db.insert(TABLE_PEDIDO_REPARACAO, null, values);
        if(id != -1)
        {
            pedido.setId(id);
            return pedido;
        }
        else
        {
            return null;
        }
    }


    
    public boolean editarPedidoReparacaoDB(PedidoReparacao pedido)
    {
        ContentValues values = new ContentValues();

        values.put(STATUS, pedido.getStatus());
        values.put(RESPOSTA_OBS, pedido.getRespostaObs());
        values.put(DATAINICIO, pedido.getDataInicio());
        values.put(DATAFIM, pedido.getDataFim());

        // devolve o número de linhas atualizadas
        return db.update(TABLE_PEDIDO_REPARACAO, values, ID+"=?", new String[]{String.valueOf(pedido.getId())})==1;
    }

    public boolean removerPedidoReparacaoDB(PedidoReparacao pedido)
    {
        // db.delete devolve o número de linhas eliminadas
        return db.delete(TABLE_PEDIDO_REPARACAO, ID+"=?", new String[]{String.valueOf(pedido.getId())})==1;
    }

    public void removerAllPedidoReparacaoDB()
    {
        db.delete(TABLE_PEDIDO_REPARACAO, null, null);
    }

    public void removerAllLinhaPedidoReparacaoDB()
    {
        db.delete(TABLE_LINHA_PEDIDO_REPARACAO, null, null);
    }

    public ArrayList<PedidoReparacao> getAllPedidosReparacaoDB()
    {
        ArrayList<PedidoReparacao> pedidos = new ArrayList<>();

        Cursor cursor = db.query(TABLE_PEDIDO_REPARACAO, new String[]{ID, NOME_REQUERENTE, NOME_RESPONSAVEL, STATUS, DESCRICAO_PROBLEMA, RESPOSTA_OBS, DATAPEDIDO, DATAINICIO, DATAFIM},
                null, null, null, null, null);

        if(cursor.moveToFirst())
        {
            do {
                PedidoReparacao pedidoAux = new PedidoReparacao(
                                cursor.getInt(0), //ID
                                cursor.getString(1), //Requerente
                                (cursor.isNull(2) ? null : cursor.getString(8)), // Aprovador
                                cursor.getInt(3), // status
                                (cursor.isNull(4) ? null : cursor.getString(5)), //descriçãoProblema
                                (cursor.isNull(5) ? null : cursor.getString(6)), //respostaObs
                                cursor.getString(6), //DataPedido
                                cursor.getString(7), //DataInicio
                                cursor.getString(8) //DataFim
                        );
                        pedidos.add(pedidoAux);
            }while(cursor.moveToNext());
                cursor.close();
        }
        return pedidos;
    }

    //endregion

    //region Funções Tabela Grupo de Itens Item

    public GrupoItensItem adicionarGrupoItensItemDB(GrupoItensItem grupoItensItem)
    {
        ContentValues values = new ContentValues();

        values.put(GRUPOITENSID, grupoItensItem.getGrupoitem_id());
        values.put(ITEMID, grupoItensItem.getItem_id());

        // devolve -1 em caso de erro, ou o id do novo grupo de itens (long)
        int id = (int) db.insert(TABLE_ITENS_GRUPO, null, values);
        if(id != -1)
        {
            grupoItensItem.setItem_id(id);
            return grupoItensItem;
        }
        else
        {
            return null;
        }
    }

    public void removerAllGrupoItensItemDB()
    {
        db.delete(TABLE_ITENS_GRUPO, null, null);
    }

    public ArrayList<GrupoItensItem> findGrupoItensItem(Integer idGrupoItem)
    {
        ArrayList<GrupoItensItem> grupoItensItems = new ArrayList<>();

        Cursor cursor = db.query(TABLE_ITENS_GRUPO, new String[]{ID,GRUPOITENSID, ITEMID},
                GRUPOITENSID + "=" + idGrupoItem, null, null, null, null);

        if(cursor.moveToFirst())
        {
            do {
                GrupoItensItem auxgrupoItensItem = new GrupoItensItem(
                        cursor.getInt(0), //ID
                        cursor.getInt(1), //Grupo Item ID
                        cursor.getInt(2) //Item ID
                );
                grupoItensItems.add(auxgrupoItensItem);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return grupoItensItems;
    }

    public ArrayList<Item> checkItemGrupo(ArrayList<Item> items) {
        ArrayList<Item> itensSemGrupo = new ArrayList<>();
        //Percorrer todos os items na base de dados
        for (int i = 0; i < getAllItensDB().size(); i++) {
            //Procura se o id associado ao item exite
            Cursor cursorGR = db.query(TABLE_ITENS_GRUPO, new String[]{ID, GRUPOITENSID, ITEMID},
                    ITEMID + "=" + items.get(i).getId(), null, null, null, null);

            if (!cursorGR.moveToFirst())
            {
                //caso o item nao esteja associado a nenhum grupo de items vai buscar os dados do item
                Cursor cursorItem = db.query(TABLE_ITENS, new String[]{ID, NOME, SERIALNUMBER, NOTAS, STATUS, NOME_CATEGORIA, SITE_ID, PEDIDO_ALOCACAO_ID,PEDIDO_REPARACAO_ID},
                        ID + "=" + items.get(i).getId(), null, null, null, null);

                Item itemAux = null;
                if (cursorItem.moveToFirst())
                {
                    // Se não tiver um pedido de alocação associado
                    if(cursorItem.isNull(7))
                    {
                        itemAux = new Item(
                                cursorItem.getInt(0), //ID
                                cursorItem.getString(1), //Nome
                                (cursorItem.isNull(2) ? null : cursorItem.getString(2)), //Serial
                                (cursorItem.isNull(3) ? null : cursorItem.getString(3)), // Notas
                                cursorItem.getInt(4), // status
                                (cursorItem.isNull(5) ? null : cursorItem.getString(5)), // categoria
                                (cursorItem.isNull(6) ? null : cursorItem.getInt(6)), // site
                                (cursorItem.isNull(7) ? null : cursorItem.getInt(7)), // pedido alocacao id
                                (cursorItem.isNull(8) ? null : cursorItem.getInt(8)) //pedido reparacao id
                        );
                        itensSemGrupo.add(itemAux);
                    }
                }
            }
        }
        return itensSemGrupo;
    }

    public GrupoItens getActiveGrupoForItem(Integer itemID)
    {
        Cursor cursor = db.query(
                TABLE_ITENS_GRUPO,
                new String[]{ID, GRUPOITENSID, ITEMID},
                ITEMID + "=" + itemID,
                null,
                null,
                null,
                null
        );

        if(cursor.moveToFirst())
        {
            do
            {
                Cursor tabela_grupo = db.query(
                        TABLE_GRUPO_ITENS,
                        new String[]{ID, NOME, NOTAS, STATUS, PEDIDO_ALOCACAO_ID, PEDIDO_REPARACAO_ID},
                        ID + "=" + cursor.getInt(1),
                        null,
                        null,
                        null,
                        null
                );

                if(tabela_grupo.moveToFirst())
                {
                    if(tabela_grupo.getInt(3) == 10)
                    {
                        return new GrupoItens(
                                tabela_grupo.getInt(0), //ID
                                tabela_grupo.getInt(3), // status
                                tabela_grupo.getString(1), //Nome
                                (tabela_grupo.isNull(2) ? null : tabela_grupo.getString(2)), // Notas
                                (tabela_grupo.isNull(4) ? null : tabela_grupo.getInt(4)), // pedido alocacao id
                                (tabela_grupo.isNull(5) ? null : tabela_grupo.getInt(5)) // pedido reparacao id
                        );
                    }
                }
            }
            while (cursor.moveToNext());
        }
        return null;
    }

    //endregion

    //region Funções Tabela Pedidos de Alocação

    public PedidoAlocacao adicionarAlocacaoDB(PedidoAlocacao pedidoAlocacao)
    {
        ContentValues values = new ContentValues();

        values.put(ID, pedidoAlocacao.getId());
        values.put(OBS, pedidoAlocacao.getObs());
        values.put(DATAPEDIDO, String.valueOf(pedidoAlocacao.getDataPedido()));
        values.put(STATUS, pedidoAlocacao.getStatus());
        values.put(NOME_REQUERENTE, pedidoAlocacao.getNome_requerente());
        values.put(NOME_ITEM, pedidoAlocacao.getNome_item());
        values.put(ITEMID, pedidoAlocacao.getItemid());
        values.put(NOME_GRUPO, pedidoAlocacao.getNome_grupoItem());
        values.put(GRUPOITENSID, pedidoAlocacao.getGrupoid());
        values.put(USER_ID, pedidoAlocacao.getUserid());

        // devolve -1 em caso de erro, ou o id do novo pedido de alocação (long)
        int id = (int) db.insert(TABLE_PEDIDOS_REQUISICAO, null, values);
        if(id != -1)
        {
            pedidoAlocacao.setId(id);
            return pedidoAlocacao;
        }
        else
        {
            return null;
        }
    }

    public boolean editarAlocacaoDB(PedidoAlocacao pedidoAlocacao)
    {
        ContentValues values = new ContentValues();
        ;
        values.put(DATAINICIO, String.valueOf(pedidoAlocacao.getDataInicio()));
        values.put(DATAFIM, String.valueOf(pedidoAlocacao.getDataFim()));
        values.put(STATUS, pedidoAlocacao.getStatus());

        // devolve o número de linhas atualizadas
        return db.update(TABLE_PEDIDOS_REQUISICAO, values, ID+"=?", new String[]{String.valueOf(pedidoAlocacao.getId())})==1;
    }

    public boolean removerAlocacaoDB(PedidoAlocacao pedidoAlocacao)
    {
        // db.delete devolve o número de linhas eliminadas
        return db.delete(TABLE_PEDIDOS_REQUISICAO, ID+"=?", new String[]{String.valueOf(pedidoAlocacao.getId())})==1;
    }

    public void removerAllAlocacaoDB()
    {
        db.delete(TABLE_PEDIDOS_REQUISICAO, null, null);
    }

    public ArrayList<PedidoAlocacao> getAllAlocacoesDB()
    {
        ArrayList<PedidoAlocacao> alocacoes = new ArrayList<>();

        Cursor cursor = db.query(TABLE_PEDIDOS_REQUISICAO, new String[]{
                    ID, OBS, OBSRESPOSTA, DATAPEDIDO, DATAINICIO, DATAFIM, STATUS, NOME_ITEM,
                    ITEMID, NOME_GRUPO, GRUPOITENSID, NOME_REQUERENTE, NOME_APROVADOR, USER_ID
                },
                null, null, null, null, null);

        if(cursor.moveToFirst())
        {
            do {
                PedidoAlocacao pedidoAlocacaoAux = new PedidoAlocacao(
                        cursor.getInt(0), //ID
                        cursor.getInt(6), //Status
                        cursor.getString(3), //DataPedido
                        cursor.getString(4), //DataInicio
                        cursor.getString(5), //DataFim
                        (cursor.isNull(1)? null : cursor.getString(1)), //Obs
                        (cursor.isNull(2) ? null : cursor.getString(2)), // ObsResposta
                        cursor.getString(11), // Requerente
                        (cursor.isNull(12) ? null : cursor.getString(12)), // Aprovador
                        (cursor.isNull(7) ? null : cursor.getString(7)), // Item
                        (cursor.isNull(9) ? null : cursor.getString(9)), // Grupo
                        cursor.getInt(13), // UserID (id requerente?)
                        (cursor.isNull(8) ? null : cursor.getInt(8)), // ID item
                        (cursor.isNull(10) ? null : cursor.getInt(10)) //ID GRUPO
                );
                alocacoes.add(pedidoAlocacaoAux);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return alocacoes;
    }

    public ArrayList<PedidoAlocacao> getAllAlocacoesByRequerente(Integer id_user)
    {
        ArrayList<PedidoAlocacao> alocacoes = new ArrayList<>();

        Cursor cursor = db.query(TABLE_PEDIDOS_REQUISICAO, new String[]{
                        ID, OBS, OBSRESPOSTA, DATAPEDIDO, DATAINICIO, DATAFIM, STATUS, NOME_ITEM,
                        ITEMID, NOME_GRUPO, GRUPOITENSID, NOME_REQUERENTE, NOME_APROVADOR, USER_ID
                },
                USER_ID+"="+id_user, null, null, null, null);

        if(cursor.moveToFirst())
        {
            do {
                PedidoAlocacao pedidoAlocacaoAux = new PedidoAlocacao(
                        cursor.getInt(0), //ID
                        cursor.getInt(6), //Status
                        cursor.getString(3), //DataPedido
                        cursor.getString(4), //DataInicio
                        cursor.getString(5), //DataFim
                        (cursor.isNull(1)? null : cursor.getString(1)), //Obs
                        (cursor.isNull(2) ? null : cursor.getString(2)), // ObsResposta
                        cursor.getString(11), // Requerente
                        (cursor.isNull(12) ? null : cursor.getString(12)), // Aprovador
                        (cursor.isNull(7) ? null : cursor.getString(7)), // Item
                        (cursor.isNull(9) ? null : cursor.getString(9)), // Grupo
                        cursor.getInt(13), // UserID (id requerente?)
                        (cursor.isNull(8) ? null : cursor.getInt(8)), // ID item
                        (cursor.isNull(10) ? null : cursor.getInt(10)) //ID GRUPO
                );
                alocacoes.add(pedidoAlocacaoAux);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return alocacoes;
    }
    //endregion

    //region Pedido Reparacao

    public ArrayList<Item> findItemPedidoReparacao(Integer idPedidoReparacao)
    {
        ArrayList<Item> items = new ArrayList<>();

        Cursor cursor = db.query(TABLE_LINHA_PEDIDO_REPARACAO, new String[]{ID, PEDIDO_REPARACAO_ID, ITEMID, GRUPOITENSID},
                PEDIDO_REPARACAO_ID+"="+idPedidoReparacao, null, null, null, null);

        if(cursor.moveToFirst())
        {
            do {
                //verifica se o grupo e diferente de null
                if (!cursor.isNull(2))
                {
                    //vai buscar o grupo de itens
                    Cursor item = db.query(TABLE_ITENS, new String[]{ID, NOME, SERIALNUMBER, NOTAS, STATUS, NOME_CATEGORIA, SITE_ID, PEDIDO_ALOCACAO_ID,PEDIDO_REPARACAO_ID},
                            ID+"="+cursor.getInt(2), null, null, null, null);

                    if(item.moveToFirst())
                    {
                        Item iten = new Item(
                                item.getInt(0), //ID
                                item.getString(1), //Nome
                                (item.isNull(2) ? null : item.getString(2)), //Serial
                                (item.isNull(3) ? null : item.getString(3)), // Notas
                                item.getInt(4), // status
                                (item.isNull(5) ? null : item.getString(5)), // categoria
                                (item.isNull(6) ? null : item.getInt(6)), // site
                                (item.isNull(7) ? null : item.getInt(7)),//pedido alocação id
                                (item.isNull(8) ? null : item.getInt(8)) //pedido alocação id

                        );
                        items.add(iten);
                    }
                }
            }while(cursor.moveToNext());
            cursor.close();
        }

        return items;
    }

    public ArrayList<GrupoItens> findGrupoItensPedidoReparacao(Integer idPedidoReparacao)
    {

        ArrayList<GrupoItens> grupoItensAux= new ArrayList<>();
        Cursor cursor = db.query(TABLE_LINHA_PEDIDO_REPARACAO, new String[]{ID, PEDIDO_REPARACAO_ID, ITEMID, GRUPOITENSID},
                PEDIDO_REPARACAO_ID+"="+idPedidoReparacao, null, null, null, null);

        if(cursor.moveToFirst())
        {
            do {
                //verifica se o grupo e diferente de null
                if (!cursor.isNull(3))
                {
                    //vai buscar o grupo de itens
                    Cursor grupo = db.query(TABLE_GRUPO_ITENS, new String[]{ID, NOME, NOTAS, STATUS, PEDIDO_ALOCACAO_ID,PEDIDO_REPARACAO_ID},
                            ID+"="+cursor.getInt(3), null, null, null, null);

                    if (grupo.moveToFirst())
                    {
                        GrupoItens grupoItens = new GrupoItens(
                                grupo.getInt(0), //ID
                                grupo.getInt(3), // status
                                grupo.getString(1), //Nome
                                (grupo.isNull(2) ? null : grupo.getString(2)), // Notas
                                (grupo.isNull(4) ? null : grupo.getInt(4)), // pedido alocacao id
                                (grupo.isNull(5) ? null : grupo.getInt(5)) // pedido Reparacao id

                        );
                        grupoItensAux.add(grupoItens);
                    }
                }
            }while(cursor.moveToNext());
            cursor.close();
        }

        return grupoItensAux;
    }
    //endregion

    //region alocacao

    public ArrayList<Item> findItensalocados(Integer idPedidoReparacao)
    {

        ArrayList<Item> items = new ArrayList<>();

        Cursor cursor = db.query(TABLE_ITENS, new String[]{ID, NOME, SERIALNUMBER, NOTAS, STATUS, NOME_CATEGORIA, SITE_ID, PEDIDO_ALOCACAO_ID,PEDIDO_REPARACAO_ID},
                PEDIDO_REPARACAO_ID+"="+idPedidoReparacao, null, null, null, null);

        if(cursor.moveToFirst())
        {
            do {
                Item iten = new Item(
                        cursor.getInt(0), //ID
                        cursor.getString(1), //Nome
                        (cursor.isNull(2) ? null : cursor.getString(2)), //Serial
                        (cursor.isNull(3) ? null : cursor.getString(3)), // Notas
                        cursor.getInt(4), // status
                        (cursor.isNull(5) ? null : cursor.getString(5)), // categoria
                        (cursor.isNull(6) ? null : cursor.getInt(6)), // site
                        (cursor.isNull(7) ? null : cursor.getInt(7)),//pedido alocação id
                        (cursor.isNull(8) ? null : cursor.getInt(8)) //pedido alocação id
                );
                items.add(iten);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return items;
    }
    //endregion
}