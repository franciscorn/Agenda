package estudos.hlt.agenda.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import estudos.hlt.agenda.DAO.AlunoDAO;
import estudos.hlt.agenda.R;
import estudos.hlt.agenda.receiver.SendAlunos;
import estudos.hlt.agenda.controler.AlunosAdapter;
import estudos.hlt.agenda.model.Aluno;

public class ListaAlunosActivity extends AppCompatActivity {

    public static final int COD_CALL_PHONE = 123;
    private static final int COD_SMS = 789;
    private ListView listaAlunos;
    private int posicaoAluno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        listaAlunos = (ListView) findViewById(R.id.lista_alunos);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            String[] permissoes = {Manifest.permission.RECEIVE_SMS};

            ActivityCompat.requestPermissions(this, permissoes, COD_SMS);

        }

        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(position);

                Intent intentFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intentFormulario.putExtra("aluno", aluno);
                startActivity(intentFormulario);

            }
        });


        Button novoAluno = (Button) findViewById(R.id.novo_aluno);
        novoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentVaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intentVaiProFormulario);
            }
        });

        registerForContextMenu(listaAlunos);


    }

    private void carregaListaAlunos() {
        AlunoDAO alunoDAO = new AlunoDAO(this);
        List<Aluno> alunos = alunoDAO.buscaAlunos();
        alunoDAO.close();


        AlunosAdapter adapter = new AlunosAdapter(this, alunos);
        listaAlunos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaListaAlunos();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista_alunos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_enviar_notas:
                new SendAlunos(this).execute();
                break;
            case R.id.menu_baixar_provas:
                Intent vaiParaProvas = new Intent(this, ListaProvasActivity.class);
                startActivity(vaiParaProvas);
                break;
            case R.id.menu_mapa:
                Intent vaiParaMapa = new Intent(this, MapaActivity.class);
                startActivity(vaiParaMapa);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        posicaoAluno = info.position;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(posicaoAluno);

        MenuItem telefonar = menu.add("Ligar");

        telefonar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    String[] permissoes = {Manifest.permission.CALL_PHONE};

                    ActivityCompat.requestPermissions(ListaAlunosActivity.this, permissoes, COD_CALL_PHONE);

                } else {
                    itemTelefonarAluno();
                }
                return false;
            }
        });


        MenuItem enviarsms = menu.add("Enviar SMS");
        Intent intentSms = new Intent(Intent.ACTION_VIEW);
        intentSms.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentSms.setData(Uri.parse("sms:" + aluno.getTelefone()));
        enviarsms.setIntent(intentSms);


        MenuItem visualizarMapa = menu.add("Visualizar Mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String enderecoAluno = "geo:0,0?q=" + aluno.getEndereco();
        intentMapa.setData(Uri.parse(enderecoAluno));
        visualizarMapa.setIntent(intentMapa);

        MenuItem visitarSite = menu.add("Visitar Site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        intentSite.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String site = aluno.getSite();
        if (!site.startsWith("http")) {
            site = "http://" + site;
        }
        intentSite.setData(Uri.parse(site));
        visitarSite.setIntent(intentSite);


        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlunoDAO alunoDAO = new AlunoDAO(ListaAlunosActivity.this);
                alunoDAO.deletaAluno(aluno);
                alunoDAO.close();

                onResume();

                return false;
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case COD_CALL_PHONE:
                //fazer a ligacao
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    itemTelefonarAluno();
                } else {
                    Toast.makeText(this, "Permissão Ligar Negada", Toast.LENGTH_SHORT).show();
                }
                break;
            case COD_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Você será avisado de SMS", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permissão SMS Negada", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }

    }

    private void itemTelefonarAluno() {
        Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(posicaoAluno);
        Intent intentLigar = new Intent(Intent.ACTION_CALL);
        intentLigar.setData(Uri.parse("tel:" + aluno.getTelefone()));
        startActivity(intentLigar);
    }
}
