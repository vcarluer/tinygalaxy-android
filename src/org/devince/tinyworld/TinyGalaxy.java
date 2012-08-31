
package org.devince.tinyworld;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class TinyGalaxy extends AndroidApplication implements IActivityRequestHandler {
	private static final String SGS_VCR = "703A6FB6180B55E158105A7D9481857A"; // 354795046635436
	private static final String AdMobPublisherId = "a15040cbf0bde5c";
	private static final boolean AdTest = true;
	
	private RelativeLayout layout;
	private View gameView;
	private AdView adView;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useWakelock = true;
		config.useGL20 = true;
		
		// initialize(TinyWorld.get(), config);
				
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		
		this.layout = new RelativeLayout(this);
		this.gameView = initializeForView(TinyWorld.get(this), config);
		this.adView = new AdView(this, AdSize.BANNER, AdMobPublisherId);
		this.nextAdInternal();
		this.showAds(false);
		
		this.layout.addView(this.gameView);
		        
        FrameLayout.LayoutParams adsParams =new FrameLayout.LayoutParams(
        		FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);        
        this.adView.setLayoutParams(adsParams);
        this.adView.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
		
		this.layout.addView(this.adView);
		
		setContentView(layout);
		
//		 final TelephonyManager tm =(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//
//		 String deviceid = tm.getDeviceId();
	}
	
	private void nextAdInternal() {
    	// Create an ad request.
        AdRequest adRequest = new AdRequest();
        if (AdTest) {
        	adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
        	adRequest.addTestDevice(SGS_VCR);        	
        }
        // Fill out ad request.       

        // Start loading the ad in the background.        
        this.adView.loadAd(adRequest);
    }
	
	private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;

    protected Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_ADS:
                {
                    adView.setVisibility(View.VISIBLE);
                    break;
                }
                case HIDE_ADS:
                {
                    adView.setVisibility(View.GONE);
                    break;
                }
            }
        }
    };

	public void showAds(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}
}
