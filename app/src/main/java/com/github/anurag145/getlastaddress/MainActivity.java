package com.github.anurag145.getlastaddress;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
     private TextView textView;
    private Button button;
     private  NetworkInfo activeNetwork;

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

        textView=(TextView)findViewById(R.id.textView);
        button=(Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(networkstate(getApplicationContext()))
                {

                }
            }
        });


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

}
