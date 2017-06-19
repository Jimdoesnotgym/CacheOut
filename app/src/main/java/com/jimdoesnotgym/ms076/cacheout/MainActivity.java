package com.jimdoesnotgym.ms076.cacheout;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wang.avi.AVLoadingIndicatorView;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private AVLoadingIndicatorView avi;
    private Button mBtnClearCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Log.d(TAG, "version >= marshmallow");
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Error").
                    setMessage("You're phone is currently using Android " +
                            Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName() +
                            " (" + Build.VERSION.RELEASE + ")" +
                            ", and is unable to use the features in this app. The app will now close.").
            setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        mBtnClearCache = (Button) findViewById(R.id.btn_clear_cache);

        avi.smoothToHide();

        mBtnClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearCacheOperation operation = new ClearCacheOperation();
                operation.execute();
            }
        });
    }

    private class ClearCacheOperation extends AsyncTask<String, Void, String> {

        PackageManager pm;

        @Override
        protected void onPreExecute() {
            avi.smoothToShow();
            pm = getPackageManager();
        }

        @Override
        protected String doInBackground(String... strings) {
            Method[] methods = pm.getClass().getDeclaredMethods();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (Method m : methods) {
                if (m.getName().equals("freeStorage")) {
                    try {
                        long desiredFreeStorage = Long.MAX_VALUE;
                        m.invoke(pm, desiredFreeStorage, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return "execute";
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("execute")) {
                avi.smoothToHide();
            }
        }
    }
}
