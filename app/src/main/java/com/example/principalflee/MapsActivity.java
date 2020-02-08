package com.example.principalflee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jaeger.library.StatusBarUtil;

import java.io.IOException;
import java.net.CookieHandler;
import java.util.List;
import java.util.Locale;

import Classes.FireBaseInsert;
import Classes.FireBaseRecupera;
import Classes.MarkerSets;
import Classes.PutMarkers;

import static android.view.View.VISIBLE;
import static com.jaeger.library.StatusBarUtil.setTransparent;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    //GoogleMap.OnCameraIdleListener,
    private Location localizacaoCorrente;
    private FusedLocationProviderClient functionalInterface;
    private SupportMapFragment mapFragment;
    private static final int Request_Code = 101;
    private GoogleMap mMap;
    ImageView markerImage;
    private Marker mMarker;
    private Geocoder geocoder;
    LatLng latLng,latlnIdle;
    private Button btMarcador, btDesmarcador;

    // setting from ui
    UiSettings uiSettings;
    // status bar


    static String marker = "Marker";
    private DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference referenceBase= firebaseReference.child(marker);

    // classes criadas por  mim.
    FireBaseRecupera fireBaseRecupera;
    FireBaseInsert fireBaseInsert= new FireBaseInsert();
    MarkerSets markerSets = new MarkerSets();
    PutMarkers putMarkers;

    private DrawerLayout drawerLayout;
    private AppBarConfiguration mAppBarConfiguration;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        // toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setPadding(0,20,0,0);
        toolbar.setScrollBarSize(12);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        toggle.getDrawerArrowDrawable().setColor(Color.BLACK);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        Log.i("Marker","Create");

        functionalInterface = LocationServices.getFusedLocationProviderClient(this);
        recuperaLocalizacaoAtual();
        markerImage = findViewById(R.id.marcador);
        btMarcador = findViewById(R.id.btMarcar);
        btDesmarcador = findViewById(R.id.btDesmarca);

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        Log.d("aaa","aaa");


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }


    }


    public void recuperaLocalizacaoAtual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Request_Code);
            return;

        }
        Task<Location> resultTask = functionalInterface.getLastLocation();
        resultTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    localizacaoCorrente = location;
                    mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert mapFragment != null;
                    mapFragment.getMapAsync(MapsActivity.this);



                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this,R.raw.json_map);

        try {
            mMap = googleMap;
            mMap.setMapStyle(mapStyleOptions);
            fireBaseRecupera = new FireBaseRecupera(mMap);
            uiSettings = mMap.getUiSettings();
        } catch (Exception e) {
            e.printStackTrace();
        }
        uiSettings.setZoomControlsEnabled(true);
        //uiSettings.setMyLocationButtonEnabled(true);

        LatLng latLng= new LatLng(localizacaoCorrente.getLatitude(), localizacaoCorrente.getLongitude());
        if (latLng != null) {
            Log.i("Marker", "Cachorro");

            putMarkers = new PutMarkers(mMap,latLng);
            putMarkers.adicionaMarkerAtual();
            markerImage.setVisibility(VISIBLE);



            /*
            MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.dog_face_medium)).position(latLng).title("Você está aqui 1!");
            markerOptions.draggable(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));
            mMap.addMarker(markerOptions);

            */


        }

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                Log.i("Marker", "Idle Move Start");
                markerImage.setVisibility(VISIBLE);
            }
        });


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Log.i("Marker", "Idle Listener");

                latlnIdle= mMap.getCameraPosition().target;
                //markerImage.setVisibility(View.INVISIBLE);

               /* if (latLng != null) {

                    MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.dog_face_medium)).position(latLng).title("Você está aqui 1!");
                    mMap.addMarker(markerOptions);
                    markerImage.setVisibility(View.INVISIBLE);

                }
                */
            }
        });

        Query cidadeReferencia = referenceBase.orderByChild("cidade").equalTo(getGeolacal().getCidade());
        cidadeReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {

                    Log.i("FireBase", String.valueOf(zoneSnapshot.child("cidade").getValue(String.class)));
                    String longitude =String.valueOf(zoneSnapshot.child("lgt").getValue());
                    String latitude =String.valueOf(zoneSnapshot.child("ltd").getValue());

                    Log.i("FireBase", latitude);
                    Log.i("FireBase", longitude);
                    putMarkers.addMarkerDog(Double.parseDouble(latitude), Double.parseDouble(longitude));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case Request_Code:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recuperaLocalizacaoAtual();
                }
                break;
        }
    }
    // pega a localização atual a partir do latlng
    // que, com base nessa informa para o geocoder que transforma em um nome
    public MarkerSets getGeolacal(){

        try {
            LatLng latLng= new LatLng(localizacaoCorrente.getLatitude(), localizacaoCorrente.getLongitude());
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> endereco =  geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

            markerSets.setPais(endereco.get(0).getCountryName());
            markerSets.setCidade(endereco.get(0).getSubLocality());
            markerSets.setEstado(endereco.get(0).getAdminArea());
            markerSets.setLgt((float) latLng.longitude);
            markerSets.setLtd((float) latLng.latitude);

            //fireBaseInsert.enviaFireBase(markerSets);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  markerSets;
    }

    public void adicionaMarkerFireBase(){

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> enderocoMarker = geocoder.getFromLocation(latlnIdle.latitude, latlnIdle.longitude, 1);
            markerSets.setPais(enderocoMarker.get(0).getCountryName());
            markerSets.setCidade(enderocoMarker .get(0).getSubLocality());
            markerSets.setEstado(enderocoMarker.get(0).getAdminArea());
            markerSets.setLgt((float) latlnIdle.longitude);
            markerSets.setLtd((float) latlnIdle.latitude);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // adiciona o marcador no mapa
        putMarkers.addMarkerDog(latlnIdle.latitude,latlnIdle.longitude);
        // guarda os sets do relacionados a posiçao do ponteiro no centro do mapa
        fireBaseInsert.enviaFireBase(markerSets);
    }

    private void desmarcadorMarker() {
    }

    public void onClick(View view){
        switch (view.getId()){
            case  R.id.btMarcar :
                adicionaMarkerFireBase();
                break;

            default :
                break;
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();
    }
}
