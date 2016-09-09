package com.github.anurag145.getlastaddress;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {
     private TextView textView;
     private ProgressBar progressBar;
    AddressResultReceiver mResultReceiver;
     private  NetworkInfo activeNetwork;
    GoogleApiClient mGoogleApiClient;
    private int PERMISSION_CODE_1 = 23;
    public static final String PACKAGE_NAME =
            "com.github.anurag145.getlastaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

    public static final String LOCATION_LATITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LATITUDE_DATA_EXTRA";
    public static final String LOCATION_LONGITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LONGITUDE_DATA_EXTRA";

    public static final int SUCCESS_RESULT = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23) {

            if ( ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                }

                requestpermisions();


            }
        }
            progressBar= (ProgressBar)findViewById(R.id.progressBar);
        mResultReceiver = new AddressResultReceiver(null);
        setupGoogleApiClient();

        textView=(TextView)findViewById(R.id.textView);




    }

    public void onClick(View view)
    {
        if(networkstate(getApplicationContext()))
        {
              textView.setVisibility(View.INVISIBLE);
              progressBar.setVisibility(View.VISIBLE);

                           getLocation(view);

        }
    }
    GoogleApiClient.ConnectionCallbacks mConnectionCallbacks =  new GoogleApiClient.ConnectionCallbacks()
    {    @Override    public void onConnected(Bundle bundle)
    {
        Toast.makeText(getApplicationContext(),"ApiClient connected",Toast.LENGTH_LONG).show();
    }
        @Override
        public void onConnectionSuspended(int i) {} };

    GoogleApiClient.OnConnectionFailedListener  mOnConnectionFailedListener = new     GoogleApiClient.OnConnectionFailedListener() {
        @Override    public void onConnectionFailed(ConnectionResult connectionResult)
        { Toast.makeText(MainActivity.this, connectionResult.toString(), Toast.LENGTH_LONG).show();


        }
    };
    protected synchronized void setupGoogleApiClient()
    {    mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(mConnectionCallbacks)
            .addOnConnectionFailedListener( mOnConnectionFailedListener)
            .addApi(LocationServices.API)
            .build();
        mGoogleApiClient.connect();
    }
    public void getLocation(View view) {

        try { Location lastLocation = LocationServices.FusedLocationApi.
                getLastLocation( mGoogleApiClient);
            if (lastLocation != null)
            {
                Intent intent = new Intent(this, GeocodeAddressIntentService.class);
                intent.putExtra(RECEIVER, mResultReceiver);

                intent.putExtra(LOCATION_LATITUDE_DATA_EXTRA, lastLocation.getLatitude());
                intent.putExtra(LOCATION_LONGITUDE_DATA_EXTRA,lastLocation.getLongitude());

                startService(intent);

            } else {
                /*
                Handle Location services if turned off here

                */
            }
        }    catch (SecurityException e)
        {e.printStackTrace();
        }
    }

    public void requestpermisions() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE_1);
    }

    boolean networkstate(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork!=null&&activeNetwork.isConnectedOrConnecting();

    }

    public class AddressResultReceiver  extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }


        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode ==SUCCESS_RESULT) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {



                        textView.setText(resultData.getString(RESULT_DATA_KEY));
                        progressBar.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.VISIBLE);

                    }
                });
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),resultData.getString(RESULT_DATA_KEY),Toast.LENGTH_LONG).show();

                    }
                });
            }
        }
    }



}
