package com.fstakem.research.attendalitics;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class RangingActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "Fred";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        beaconManager.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Tell the Application not to pass off ranging updates to this activity
        //(BeaconReferenceApplication)this.getApplication()).setRangingActivity(null);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Tell the Application to pass off ranging updates to this activity
        //((BeaconReferenceApplication)this.getApplication()).setRangingActivity(this);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect()
    {
        Log.i("FREDLOG", "Beacon connect!");

        beaconManager.setRangeNotifier(new RangeNotifier()
        {
            @Override
            public void didRangeBeaconsInRegion(Collection beacons, Region region)
            {
                Log.i("FREDLOG", "Beacon range");

                if (beacons.size() > 0)
                {
                    while(beacons.iterator().hasNext())
                    {
                        Object b = beacons.iterator().next();
                        Beacon bb = (Beacon)b;
                        Log.i("FREDLOG", "Beacon " + bb.toString() + " " + bb.getDistance() + " meters away.");
                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }
}
