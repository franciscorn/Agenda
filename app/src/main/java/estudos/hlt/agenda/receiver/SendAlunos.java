package estudos.hlt.agenda.receiver;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.util.List;
import estudos.hlt.agenda.DAO.AlunoDAO;
import estudos.hlt.agenda.converter.AlunoConverter;
import estudos.hlt.agenda.model.Aluno;

public class SendAlunos extends AsyncTask<Object, Object, String> {
    private Context context;
    private ProgressDialog dialog;

    public SendAlunos(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Object[] objects) {

        AlunoDAO alunoDAO = new AlunoDAO(context);
        List<Aluno> alunos = alunoDAO.buscaAlunos();
        alunoDAO.close();

        AlunoConverter conversor = new AlunoConverter();
        String json = conversor.convertToJSON(alunos);

        WebClient client = new WebClient();
        String resposta = client.post(json);

        return resposta;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Aguarde", "Enviando alunos...", true, false);

    }

    @Override
    protected void onPostExecute(String resposta) {
        Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();
        dialog.dismiss();
        super.onPostExecute(resposta);
    }
}
