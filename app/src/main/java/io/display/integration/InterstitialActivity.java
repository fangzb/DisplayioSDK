package io.display.integration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.display.sdk.AdProvider;
import io.display.sdk.AdRequest;
import io.display.sdk.Controller;
import io.display.sdk.Placement;
import io.display.sdk.ads.Ad;
import io.display.sdk.exceptions.DioSdkException;
import io.display.sdk.listeners.AdEventListener;
import io.display.sdk.listeners.AdLoadListener;
import io.display.sdk.listeners.AdRequestListener;

public class InterstitialActivity extends AppCompatActivity {

    private static String TAG = "InterstitialActivity";

    private Button loadButton;
    private Button showButton;

    private String placementId;
    private Ad loadedAd;

    public static void launch(Context context,String placementId){
        Intent intent = new Intent(context,InterstitialActivity.class);
        intent.putExtra("placementId",placementId);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        placementId = getIntent().getStringExtra("placementId");

        loadButton = findViewById(R.id.button_load_interstitial);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd();
            }
        });
do
        showButton = findViewById(R.id.button_show_interstitial);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAd();
            }
        });
    }

    private void loadAd() {
        loadButton.setEnabled(false);

        Placement placement;
        try {
            placement = Controller.getInstance().getPlacement(placementId);
        } catch (DioSdkException e) {
            Log.e(TAG, e.getLocalizedMessage());
            return;
        }

        AdRequest adRequest = placement.newAdRequest();
        adRequest.setAdRequestListener(new AdRequestListener() {
            @Override
            public void onAdReceived(AdProvider adProvider) {

                adProvider.setAdLoadListener(new AdLoadListener() {
                    @Override
                    public void onLoaded(Ad ad) {
                        loadedAd = ad;
                        showButton.setEnabled(true);
                    }

                    @Override
                    public void onFailedToLoad() {
                        Toast.makeText(InterstitialActivity.this, "Ad for placement " + placementId + " failed to load", Toast.LENGTH_LONG).show();
                    }
                });

                try {
                    adProvider.loadAd();
                } catch(DioSdkException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }

            @Override
            public void onNoAds() {
                Toast.makeText(InterstitialActivity.this, "No Ads placement " + placementId, Toast.LENGTH_LONG).show();
            }
        });

        adRequest.requestAd();
    }

    private void showAd() {
        showButton.setEnabled(false);

        loadedAd.setEventListener(new AdEventListener() {
            @Override
            public void onShown(Ad ad) {
                Log.e(TAG, "onShown");
            }

            @Override
            public void onFailedToShow(Ad ad) {
                Log.e(TAG, "onFailedToShow");
            }

            @Override
            public void onClicked(Ad ad) {
                Log.e(TAG, "onClicked");
            }

            @Override
            public void onClosed(Ad ad) {
                Log.e(TAG, "onClosed");
            }

            @Override
            public void onAdCompleted(Ad ad) {
                Log.e(TAG, "onAdCompleted");
            }
        });

        loadedAd.showAd(this);
    }
}
