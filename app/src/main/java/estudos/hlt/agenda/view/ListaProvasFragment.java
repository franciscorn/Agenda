package estudos.hlt.agenda.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import estudos.hlt.agenda.R;
import estudos.hlt.agenda.model.Prova;

public class ListaProvasFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_provas, container, false);

        List<String> topicosPortugues = Arrays.asList("Sujeito", "Objeto Indireto", "Objeto Direto");
        Prova provaPortugues = new Prova("Portugues", "20/02/2019", topicosPortugues);

        List<String> topicosMatematica = Arrays.asList("Equações de Segundo grau", "Trigonometria", "Geometria");
        Prova provaMatematica = new Prova("Matematica", "10/02/2019", topicosMatematica);


        List<Prova> provasList = Arrays.asList(provaMatematica, provaPortugues);
        ArrayAdapter<Prova> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, provasList);

        ListView lista = view.findViewById(R.id.provas_list);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Prova prova = (Prova) parent.getItemAtPosition(position);

                ListaProvasActivity listaProvasActivity = (ListaProvasActivity) getActivity();
                listaProvasActivity.getProva(prova);

            }
        });

        return view;
    }
}
