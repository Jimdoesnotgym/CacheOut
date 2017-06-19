package com.jimdoesnotgym.ms076.cacheout;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wang.avi.AVLoadingIndicatorView;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    AVLoadingIndicatorView avi;
    Button mBtnClearCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
