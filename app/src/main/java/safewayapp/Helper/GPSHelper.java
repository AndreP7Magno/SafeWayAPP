package safewayapp.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;


import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class GPSHelper {
    private static final GPSHelper ourInstance = new GPSHelper();
    private static Context mContext;
    private static LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 0;  /* 10 secs */
    private long FASTEST_INTERVAL = 0; /* 2 sec */
    Location location = null;

    public static GPSHelper getInstance() {

        return ourInstance;
    }

    private GPSHelper() {

    }

    @SuppressLint({"MissingPermission", "RestrictedApi"})
    public void Inicializa(Context context)
    {
        try {
            mContext = context;
            mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

            // Create LocationSettingsRequest object using location request
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(mLocationRequest);
            LocationSettingsRequest locationSettingsRequest = builder.build();

            // Check whether location settings are satisfied
            // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
            SettingsClient settingsClient = LocationServices.getSettingsClient(mContext);
            settingsClient.checkLocationSettings(locationSettingsRequest);

            // new Google API SDK v11 uses getFusedLocationProviderClient(this)
            getFusedLocationProviderClient(mContext).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            // do work here
                            onLocationChanged(locationResult.getLastLocation());
                        }
                    },
                    Looper.myLooper());
        }catch (Exception ex) {
            String msg = ex.getMessage();
        }
    }

    public void onLocationChanged(Location location) {
        this.location = location;
    }

    public Location getLocation()
    {
        return this.location;
    }

}
