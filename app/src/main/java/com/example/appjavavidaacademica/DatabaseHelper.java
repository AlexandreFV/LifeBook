package com.example.appjavavidaacademica;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "VidaAcademica";
    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Chamado quando o banco de dados é criado pela primeira vez
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crie as tabelas necessárias
        criarTabelas(db);
    }

    private void criarTabelas(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS agrupamento("
                    + " id_agrupamento INTEGER PRIMARY KEY AUTOINCREMENT "
                    + " , nome_agrupamento VARCHAR "
                    + " , categoria VARCHAR"
                    + " , corFundoHex VARCHAR"
                    + " , corTextoHex VARCHAR"
                    + " , corBotoesHex VARCHAR"
                    + " , iconEscolhido INTEGER"
                    + " , CONSTRAINT pk_agrupamento UNIQUE (id_agrupamento));");

            db.execSQL("CREATE TABLE IF NOT EXISTS materias("
                    + " id_materia INTEGER PRIMARY KEY AUTOINCREMENT"
                    + " , nome_materia VARCHAR "
                    + " , id_agrupamento INTEGER"
                    + " , dia_semana VARCHAR"
                    + " , quantAulas INTEGER"
                    + " , CONSTRAINT pk_materias UNIQUE (id_materia)"
                    + " , CONSTRAINT fk_materia_agrupamento FOREIGN KEY (id_agrupamento) REFERENCES agrupamento (id_agrupamento));");

            db.execSQL("CREATE TABLE IF NOT EXISTS atividades("
                    + " id_atividades INTEGER PRIMARY KEY AUTOINCREMENT"
                    + " , nome_atividade VARCHAR "
                    + " , pontos FLOAT "
                    + " , data_entrega DATE "
                    + " , entregue BOOL "
                    + " , id_materia INTEGER"
                    + " , CONSTRAINT pk_atividades UNIQUE (id_atividades)"
                    + " , CONSTRAINT fk_atividades_materia FOREIGN KEY (id_materia) REFERENCES materias (id_materia));");

            db.execSQL("CREATE TABLE IF NOT EXISTS faltas("
                    + " id_faltas INTEGER PRIMARY KEY AUTOINCREMENT"
                    + " , data DATE "
                    + " , pontos FLOAT "
                    + " , id_materia INTEGER "
                    + " , quantidade INTEGER "
                    + " , CONSTRAINT pk_faltas UNIQUE (id_faltas)"
                    + " , CONSTRAINT fk_faltas_materia FOREIGN KEY (id_materia) REFERENCES materias (id_materia));");


            Log.i("TAG", "Tabelas criadas com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Erro ao criar tabelas: " + e.getMessage());
        }
    }



    // Chamado quando a versão do banco de dados é atualizada
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualizações de esquema aqui, se necessário
    }

    // Método para obter uma instância de SQLiteDatabase para operações no banco de dados
    public SQLiteDatabase getDatabase() {
        return this.getWritableDatabase();
    }
}
