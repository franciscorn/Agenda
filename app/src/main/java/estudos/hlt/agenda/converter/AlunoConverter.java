package estudos.hlt.agenda.converter;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

import estudos.hlt.agenda.model.Aluno;

public class AlunoConverter {
    public String convertToJSON(List<Aluno> alunos) {
        JSONStringer JString = new JSONStringer();

        try {
            JString.object().key("list").array().object().key("aluno").array();
            for (Aluno aluno : alunos) {
                JString.object();
                JString.key("id").value(aluno.getId());
                JString.key("nome").value(aluno.getNome());
                JString.key("endereco").value(aluno.getEndereco());
                JString.key("telefone").value(aluno.getTelefone());
                JString.key("site").value(aluno.getSite());
                JString.key("nota").value(aluno.getNota());
                JString.endObject();
            }
            JString.endArray().endObject().endArray().endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return JString.toString();
    }
}
