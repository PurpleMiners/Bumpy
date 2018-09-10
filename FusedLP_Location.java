package clover.hamar_bumpy;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

public class FusedLP_Location implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private FusedLocationProviderClient flpClient;
    private Criteria criteria;
    private Context activityContext;
    private GoogleApiClient myClient;
    public Location currLocation;
    private LocationRequest myRequest;
    private Task<Location> locationTask;
    private LocationCallback locationCallback;

    //Constructor
    @SuppressLint("RestrictedApi")
    FusedLP_Location(Context context) {
        activityContext = context;
        myClient = new GoogleApiClient.Builder(activityContext)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        flpClient = LocationServices.getFusedLocationProviderClient(activityContext);
        myRequest = new LocationRequest();
        setLocationParameter();
        if (ActivityCompat.checkSelfPermission(activityContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activityContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        do{
            locationTask = flpClient.getLastLocation();
        }while(locationTask.getResult()!=null);
        currLocation=locationTask.getResult();
        locationCallback=new LocationCallback();
        flpClient.requestLocationUpdates(myRequest, locationCallback,Looper.myLooper());
    }


    private void setLocationParameter() {
        // Set the update interval
        myRequest.setInterval(100);
        // Use high accuracy
        myRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the interval ceiling to one minute
        myRequest.setFastestInterval(100);
        // Set the distance to 10 meters.
        myRequest.setSmallestDisplacement(1);
    }
    @Override
    public void onLocationChanged(Location location) {
        currLocation=location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

