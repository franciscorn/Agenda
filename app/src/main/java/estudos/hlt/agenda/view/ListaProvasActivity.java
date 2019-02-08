package estudos.hlt.agenda.view;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import estudos.hlt.agenda.R;
import estudos.hlt.agenda.model.Prova;

public class ListaProvasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provas);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (modoPaisagem()) {
            transaction.replace(R.id.fragment_frame_principal, new ListaProvasFragment());
            transaction.replace(R.id.fragment_frame_secundario, new DetalhesProvaFragment());
        } else {
            transaction.replace(R.id.frame_principal, new ListaProvasFragment());
        }

        transaction.commit();

    }

    private boolean modoPaisagem() {
        return getResources().getBoolean(R.bool.modoPaisagem);
    }

    public void getProva(Prova prova) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (!modoPaisagem()) {
            DetalhesProvaFragment detalhesProvaFragment = new DetalhesProvaFragment();

            Bundle parametros = new Bundle();
            parametros.putSerializable("provaTag", prova);
            detalhesProvaFragment.setArguments(parametros);

            transaction.replace(R.id.frame_principal, detalhesProvaFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        } else {
            DetalhesProvaFragment detalhesProvaFragment = (DetalhesProvaFragment) fragmentManager.findFragmentById(R.id.fragment_frame_secundario);
            detalhesProvaFragment.popularCampos(prova);
        }
    }
}
