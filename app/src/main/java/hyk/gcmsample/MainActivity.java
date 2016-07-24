package hyk.gcmsample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends AppCompatActivity {

    private Button mButton;
    private ProgressBar mProgressBar;
    private TextView mTextView;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.TextView);
        mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        mProgressBar.setVisibility(ProgressBar.GONE);
        mButton = (Button) findViewById(R.id.registerButton);

        registBroadcastReceiver();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPlayServices()) {
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                    getInstanceIdToken();
                } else
                    Toast.makeText(getApplication(), "This device can not use GCM", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getInstanceIdToken() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

    //Google Play Service를 사용할 수 있는 환경이지를 체크한다.
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        1000).show();
            } else {
                Log.i("TAG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    //화면에서 앱이 동작할 때 LocalBoardcastManager를 등록합니다.
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("registrationComplete"));
    }

    //화면에서 사라지면 등록된 LocalBoardcast를 모두 해제합니다.
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    //BroadcastReceiver 함수입니다.
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(action.equals("registrationComplete")){
                    mProgressBar.setVisibility(ProgressBar.GONE);
                    mButton.setText("Registration Complete");
                    mButton.setTextColor(Color.RED);
                    mButton.setEnabled(false);
                    String token = intent.getStringExtra("token");
                    mTextView.setText(token);
                }
            }
        };
    }


}
