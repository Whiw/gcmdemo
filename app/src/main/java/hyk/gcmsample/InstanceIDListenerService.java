package hyk.gcmsample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by young on 2016-07-24.
 */

public class InstanceIDListenerService extends com.google.android.gms.iid.InstanceIDListenerService {
    private static final String TAG = "MyInstanceIDLS";
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
