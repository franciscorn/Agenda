package estudos.hlt.agenda.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import estudos.hlt.agenda.model.Aluno;

public class AlunoDAO extends SQLiteOpenHelper {

    public AlunoDAO(Context context) {

        super(context, "Agenda", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE alunos (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, endereco TEXT, telefone TEXT, site TEXT, nota REAL, caminhofoto TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "";

        switch (oldVersion) {
            case 1:
                sql = "ALTER TABLE alunos ADD COLUMN caminhofoto TEXT";
                db.execSQL(sql);
            default:
                break;
        }

    }

    public void inserirAluno(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = getContentValuesAluno(aluno);

        db.insert("alunos", null, dados);

    }

    @NonNull
    private ContentValues getContentValuesAluno(Aluno aluno) {
        ContentValues dados = new ContentValues();
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota", aluno.getNota());
        dados.put("caminhofoto", aluno.getCaminhofoto());
        return dados;
    }

    public List<Aluno> buscaAlunos() {

        String sql = "SELECT * FROM alunos ORDER BY nome;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);


        List<Aluno> alunosList = new ArrayList<Aluno>();

        while (cursor.moveToNext()) {
            Aluno aluno = new Aluno();

            aluno.setId(cursor.getLong(cursor.getColumnIndex("id")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            aluno.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            aluno.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            aluno.setSite(cursor.getString(cursor.getColumnIndex("site")));
            aluno.setNota(cursor.getDouble(cursor.getColumnIndex("nota")));
            aluno.setCaminhofoto(cursor.getString(cursor.getColumnIndex("caminhofoto")));

            alunosList.add(aluno);
        }
        cursor.close();

        return alunosList;
    }

    public void deletaAluno(Aluno aluno) {

        SQLiteDatabase db = getWritableDatabase();

        String[] params = {aluno.getId().toString()};
        db.delete("alunos", "id = ?", params);

    }

    public void alterarAluno(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = getContentValuesAluno(aluno);

        String[] params = {aluno.getId().toString()};
        db.update("alunos", dados, "id = ?", params);

    }

    public boolean alunoBoolean(String telefone) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM alunos WHERE telefone = ?", new String[]{telefone});
        int result = cursor.getCount();
        cursor.close();

        return result > 0;

    }

    public String alunoByPhone(String telefone) {


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nome FROM alunos WHERE telefone = ?", new String[]{telefone});
        String alunoNome = "Vazio";

        while (cursor.moveToNext()) {
            Aluno aluno = new Aluno();
            alunoNome = aluno.getNomePhone(cursor.getString(cursor.getColumnIndex("nome")));
        }

        cursor.close();

        return alunoNome;

    }
}
