package Classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.principalflee.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Float.parseFloat;

public class FireBaseRecupera {
    static String marker = "Marker";
    private DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference referenceBase= firebaseReference.child(marker);
    GoogleMap googleMap;

    public FireBaseRecupera(GoogleMap googleMap){

        this.googleMap=googleMap;

    }


    public void recebeFireBaseCidadeNome(){


        referenceBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                    Log.i("FireBase", String.valueOf(zoneSnapshot.child("cidade").getValue(String.class)));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void recebeCidadeLocalizacao(final String cidade){

        Query cidadeReferencia = referenceBase.orderByChild("cidade").equalTo(cidade);
        cidadeReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                    Log.i("FireBase", String.valueOf(zoneSnapshot.child("cidade").getValue(String.class)));
                    String longitude =String.valueOf(zoneSnapshot.child("lgt").getValue());
                    String latitude =String.valueOf(zoneSnapshot.child("ltd").getValue());

                    Log.i("FireBase", latitude);
                    Log.i("FireBase", longitude);
                    addMarker(Double.parseDouble(latitude), Double.parseDouble(longitude));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
    public void addMarker(double lati, double longi){
        LatLng latLng= new LatLng( lati , longi);
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.dog_face_medium)).position(latLng).title("Você está aqui 1!");
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
        googleMap.addMarker(markerOptions);
    }
}

