package estudos.hlt.agenda.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import estudos.hlt.agenda.DAO.AlunoDAO;
import estudos.hlt.agenda.model.Aluno;

public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    private Bitmap bitmap;
    private BitmapDescriptor bitmapDescriptor;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng posicaoInicial = geoLocalizacao("Rua José Guimarães, 49, Mogi das Cruzes, Sao Paulo, Brasil");
        MarkerOptions markerOptions = new MarkerOptions();

        if (posicaoInicial != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicaoInicial, 17);
            googleMap.moveCamera(update);
            markerOptions.position(posicaoInicial);
            googleMap.addMarker(markerOptions);
        }

        AlunoDAO alunoDAO = new AlunoDAO(getContext());
        for (Aluno aluno : alunoDAO.buscaAlunos()) {
            LatLng location = geoLocalizacao(aluno.getEndereco());
            if (location != null) {
                String caminhoFoto = aluno.getCaminhofoto();


                if (caminhoFoto != null) {
                    bitmap = BitmapFactory.decodeFile(caminhoFoto);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                    bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                }

                markerOptions.position(location);
                markerOptions.title(aluno.getNome());
                markerOptions.icon(bitmapDescriptor);
                markerOptions.snippet(String.valueOf(aluno.getNota()));
                googleMap.addMarker(markerOptions);

            }

            alunoDAO.close();

        }

    }

    public LatLng geoLocalizacao(String endereco) {
        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> locationName = geocoder.getFromLocationName(endereco, 1);

            if (!locationName.isEmpty()) {
                LatLng location = new LatLng(locationName.get(0).getLatitude(), locationName.get(0).getLongitude());
                return location;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
