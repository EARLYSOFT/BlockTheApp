package kr.co.earlysoft.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

import kr.co.earlysoft.blocktheapp.EarlyBlockTheApp;
import kr.co.earlysoft.blocktheapp.EarlyBlockTheAppReceiver;

public class MainActivity extends AppCompatActivity {

    private EarlyBlockTheAppReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            TedPermission.with(this)
                    .setPermissionListener(permissionlistener)
                    .setPermissions(Manifest.permission.FOREGROUND_SERVICE)
                    .check();
        }
        mBroadcastReceiver = new EarlyBlockTheAppReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "EarlyBlockTheAppReceiver:" + intent.getStringExtra(EarlyBlockTheApp.EXTRA_PACKAGENAME), Toast.LENGTH_LONG).show();
                Log.d("EARLY", intent.getStringExtra(EarlyBlockTheApp.EXTRA_PACKAGENAME));
            }
        };
        EarlyBlockTheApp.callbackHandler = mHandler;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == EarlyBlockTheApp.MESSAGE_BLOCKTHEAPP) {
                Toast.makeText(getApplicationContext(), "callbackHandler:" + msg.obj.toString(), Toast.LENGTH_LONG).show();
                EarlyBlockTheApp.instance().stopLifeCycleService(getApplicationContext());
            }

        }
    };

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };

    private void checkPermisstion() {
        if (!EarlyBlockTheApp.instance().isGrantedUsageStats(this)) {
            new AlertDialog.Builder(this)
                    .setMessage("사용자정보 접근을 허용하시겠습니까?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            EarlyBlockTheApp.instance().requestPermisstion(MainActivity.this);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mBroadcastReceiver),
                new IntentFilter(EarlyBlockTheApp.ACTION_EARLY_BLOCKTHEAPP)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermisstion();
    }

}
