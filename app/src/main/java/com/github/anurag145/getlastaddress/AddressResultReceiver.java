
package com.github.anurag145.getlastaddress;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

/**
 * Created by Anurag145 on 9/9/2016.
 */
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

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",resultData.getString(RESULT_DATA_KEY));
                    setResult(location,returnIntent);
                    finish();

                }
            });
        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",resultData.getString(RESULT_DATA_KEY));
                    setResult(location,returnIntent);
                    finish();

                }
            });
        }
    }
}

