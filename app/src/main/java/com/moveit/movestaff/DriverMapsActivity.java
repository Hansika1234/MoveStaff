package com.moveit.movestaff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import  com.google.android.gms.location.LocationListener;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.common.api.GoogleApiClient.*;

public class DriverMapsActivity extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient nGoogleApiClient;
    Location nLastLocation;
    LocationRequest nLocationRequest;
    //   private Object android;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        builGoogleApiClient();
        mMap.setMyLocationEnabled(true);

        //LatLng sydney = new LatLng(-34, 151);//  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    protected  synchronized void builGoogleApiClient(){
       // nGoogleApiClient =new  GoogleApiClient();
        nGoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        nGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        nLastLocation= location;
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));


        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("driverAvailable");

        GeoFire geoFire=new GeoFire(ref);
        geoFire.setLocation(userId,new GeoLocation(location.getLatitude(),location.getLongitude()));
    /* {
            @Override
            public boolean hasConnectedApi(@NonNull Api<?> api) {
                return false;
            }

            @NonNull
            @Override
            public ConnectionResult getConnectionResult(@NonNull Api<?> api) {
                return null;
            }

            @Override
            public void connect() {

            }

            @Override
            public ConnectionResult blockingConnect() {
                return null;
            }

            @Override
            public ConnectionResult blockingConnect(long l, @NonNull TimeUnit timeUnit) {
                return null;
            }

            @Override
            public void disconnect() {

            }

            @Override
            public void reconnect() {

            }

            @Override
            public PendingResult<Status> clearDefaultAccountAndReconnect() {
                return null;
            }

            @Override
            public void stopAutoManage(@NonNull FragmentActivity fragmentActivity) {

            }

            @Override
            public boolean isConnected() {
                return false;
            }

            @Override
            public boolean isConnecting() {
                return false;
            }

            @Override
            public void registerConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {

            }

            @Override
            public boolean isConnectionCallbacksRegistered(@NonNull ConnectionCallbacks connectionCallbacks) {
                return false;
            }

            @Override
            public void unregisterConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {

            }

            @Override
            public void registerConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {

            }

            @Override
            public boolean isConnectionFailedListenerRegistered(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
                return false;
            }

            @Override
            public void unregisterConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {

            }

            @Override
            public void dump(String s, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strings) {

            }
        };*/
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        nLocationRequest = new LocationRequest();
        nLocationRequest.setInterval(1000);
        nLocationRequest.setFastestInterval(1000);
        nLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
         {
             return;
         }

        LocationServices.FusedLocationApi.requestLocationUpdates(nGoogleApiClient,nLocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected  void onStart() {
        super.onStart();

        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("driverAvailable");

        GeoFire geoFire=new GeoFire(ref);
        geoFire.removeLocation(userId);

    }
    @Override
    protected  void onStop() {
        super.onStop();

    }
}