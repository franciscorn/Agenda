package estudos.hlt.agenda.controler;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import estudos.hlt.agenda.R;
import estudos.hlt.agenda.model.Aluno;
import estudos.hlt.agenda.view.ListaAlunosActivity;

public class AlunosAdapter extends BaseAdapter {

    private final List<Aluno> alunos;
    private final Context context;

    public AlunosAdapter(Context context, List<Aluno> alunos) {
        this.alunos = alunos;
        this.context = context;
    }

    @Override
    public int getCount() {

        return alunos.size();
    }

    @Override
    public Object getItem(int position) {
        return alunos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return alunos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Aluno aluno = alunos.get(position);
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_lista_alunos, parent, false);
        }


        TextView campoNome = view.findViewById(R.id.layout_nome);
        TextView campoTelefone = view.findViewById(R.id.layout_telefone);
        TextView campoEndereco = view.findViewById(R.id.layout_endereco);
        TextView campoSite = view.findViewById(R.id.layout_site);

        campoNome.setText(aluno.getNome());
        campoTelefone.setText(aluno.getTelefone());

        if (campoEndereco != null) {
            campoEndereco.setText(aluno.getEndereco());
        }
        if (campoSite != null) {
            campoSite.setText(aluno.getSite());
        }


        CircleImageView campoFoto = view.findViewById(R.id.layout_foto);
        String caminhoFoto = aluno.getCaminhofoto();


        if (caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            campoFoto.setImageBitmap(bitmap);
            campoFoto.setScaleType(CircleImageView.ScaleType.CENTER_CROP);
        } else {
            Resources res = view.getContext().getResources();
            int id = R.drawable.person;
            Bitmap bitmap = BitmapFactory.decodeResource(res, id);
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            campoFoto.setImageBitmap(bitmap);
            campoFoto.setScaleType(CircleImageView.ScaleType.CENTER_CROP);
        }

        return view;
    }
}
