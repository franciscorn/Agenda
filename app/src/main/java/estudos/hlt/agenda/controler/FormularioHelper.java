package estudos.hlt.agenda.controler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.RatingBar;

import de.hdodenhof.circleimageview.CircleImageView;
import estudos.hlt.agenda.R;
import estudos.hlt.agenda.model.Aluno;
import estudos.hlt.agenda.view.FormularioActivity;

public class FormularioHelper {


    private final EditText campoNome;
    private final EditText campoEndereco;
    private final EditText campoTelefone;
    private final EditText campoSite;
    private final RatingBar campoNota;
    private final CircleImageView campoFoto;

    private Aluno aluno;

    public FormularioHelper(FormularioActivity formularioActivity) {

        campoNome = formularioActivity.findViewById(R.id.formulario_nome);
        campoEndereco = formularioActivity.findViewById(R.id.formulario_endereco);
        campoTelefone = formularioActivity.findViewById(R.id.formulario_telefone);
        campoSite = formularioActivity.findViewById(R.id.formulario_site);
        campoNota = formularioActivity.findViewById(R.id.formulario_nota);
        campoFoto = formularioActivity.findViewById(R.id.formulario_foto);

        aluno = new Aluno();

    }

    public Aluno getAluno() {

        aluno.setNome(campoNome.getText().toString());
        aluno.setEndereco(campoEndereco.getText().toString());
        aluno.setTelefone(campoTelefone.getText().toString());
        aluno.setSite(campoSite.getText().toString());
        aluno.setNota(Double.valueOf(campoNota.getProgress()));
        aluno.setCaminhofoto((String) campoFoto.getTag());

        return aluno;

    }

    public void preencherFormulario(Aluno aluno) {
        campoNome.setText(aluno.getNome());
        campoEndereco.setText(aluno.getEndereco());
        campoTelefone.setText(aluno.getTelefone());
        campoSite.setText(aluno.getSite());
        campoNota.setProgress((int) aluno.getNota());
        getImageAluno(aluno.getCaminhofoto());

        this.aluno = aluno;

    }

    public void getImageAluno(String caminhoFoto) {
        if (caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            campoFoto.setImageBitmap(bitmap);
            campoFoto.setScaleType(CircleImageView.ScaleType.CENTER_CROP);
            campoFoto.setTag(caminhoFoto);
        }
    }
}
