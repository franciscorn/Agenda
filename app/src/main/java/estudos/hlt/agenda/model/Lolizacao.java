package estudos.hlt.agenda.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class Lolizacao implements GoogleApiClient.ConnectionCallbacks {

    public Lolizacao(Context context) {
        GoogleApiClient client = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this).build();

        client.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        @SuppressLint("RestrictedApi") LocationRequest request = new LocationRequest();
        request.setSmallestDisplacement(20);//metros
        request.setInterval(1000);//milisegundos
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
