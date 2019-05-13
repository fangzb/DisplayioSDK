package io.display.integration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.display.sdk.Controller;
import io.display.sdk.listeners.SdkInitListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnInterstitial, btnBanner;

    private static final String TAG = "MainActivity";
    private static final PlacementListItem[] data = {
            new PlacementListItem("5271", PlacementListItem.Type.BANNER), // Html
            new PlacementListItem("5272", PlacementListItem.Type.INTERSTITIAL), // Video
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initial SDK
        Controller.getInstance().init(this, Config.APPID, new SdkInitListener() {
            @Override
            public void onInit() {
                Log.i(TAG, "Controller initialized");
            }

            @Override
            public void onInitError(String msg) {
                Log.e(TAG, msg);
            }
        });

        btnInterstitial = findViewById(R.id.btn_interstitial);
        btnBanner = findViewById(R.id.btn_banner);
        btnInterstitial.setOnClickListener(this);
        btnBanner.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_interstitial:
                InterstitialActivity.launch(this, data[1].id);
                break;
            case R.id.btn_banner:
                BannerActivity.launch(this, data[0].id);
                break;
        }
    }
}
