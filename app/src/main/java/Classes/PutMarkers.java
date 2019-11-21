package Classes;

import com.example.principalflee.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.view.View.VISIBLE;

public class PutMarkers {
    GoogleMap map;
    LatLng latLng;


    public PutMarkers(GoogleMap map,LatLng latLng){
        this.map=map;
        this.latLng=latLng;

    }
    public void adicionaMarkerAtual(){
        map.clear();
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.pin)).position(latLng).title("Você está aqui!");
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
        map.addMarker(markerOptions);

    }
    public void addMarkerDog(double lati, double longi){
        LatLng latLng= new LatLng( lati , longi);
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.dog_face_medium)).position(latLng).title("Você está aqui 3!");
        map.addMarker(markerOptions);
    }
}
