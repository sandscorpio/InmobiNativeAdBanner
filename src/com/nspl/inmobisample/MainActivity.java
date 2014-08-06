package com.nspl.inmobisample;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMNative;
import com.inmobi.monetization.IMNativeListener;
import com.koushikdutta.urlimageviewhelper.*;

import android.R.color;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	InMobiNativeAd newAd;
	AdView adView;
	SavedValues savedValues;
	boolean showAdsFromBothProviders;
	boolean simulateInMobiFailure;
	IMNative nativeAd;
	
	Button buttonToast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		savedValues = SavedValues.Instance(this);
		
		showAdsFromBothProviders = savedValues.loadKeyValue(getString(R.string.key_show_ads_from_both_providers), false);
		simulateInMobiFailure = savedValues.loadKeyValue(getString(R.string.key_simulate_inmobi_failure), false);

		if (simulateInMobiFailure)		
			InMobi.initialize(this, "7a71816df2914e75bbf936856dc10939");   //incorrect Property Id will cause it to fail
		else
			InMobi.initialize(this, "7a71816df2914e75bbf936856dc10936");

		nativeAd = new IMNative(nativeListener);
		RelativeLayout relativeLayoutInMobiAd = (RelativeLayout) findViewById(R.id.relativeLayoutInMobiAd);
		relativeLayoutInMobiAd.setVisibility(View.GONE);		
		nativeAd.loadAd();
		
		nativeAd.attachToView((ViewGroup) relativeLayoutInMobiAd);
		
		if (showAdsFromBothProviders){
			loadAdMob();			
		}
		Log.v("TAG", "Activity loaded and native add asked for");
		
		buttonToast = (Button) findViewById(R.id.buttonToast);
		buttonToast.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.showToast(getApplicationContext(), MainActivity.this, "Custom Toast Loaded", Toast.LENGTH_LONG);
			}
		});
		
		
		
	}
	
	IMNativeListener nativeListener = new IMNativeListener() {
		@Override
		public void onNativeRequestSucceeded(final IMNative nativeAd) {		
			
			
			if (nativeAd != null){				
				RelativeLayout relativeLayoutInMobiAd = (RelativeLayout) findViewById(R.id.relativeLayoutInMobiAd);
				relativeLayoutInMobiAd.setVisibility(View.VISIBLE);
				
				Log.v("TAG", "Add is loaded, Content asked for");
				String content = nativeAd.getContent();
				Gson gson = new Gson();
				newAd = gson.fromJson(content, InMobiNativeAd.class);

				Log.v("new Add title", newAd.title);
				Log.v("new Add description", newAd.description);

				TextView textViewTitle = (TextView) findViewById(R.id.textViewAdTitle);
				TextView textViewDescription = (TextView) findViewById(R.id.textViewAdDescription);
				//				TextView textViewRating = (TextView) findViewById(R.id.textViewAdRating);

				ImageView imageViewIcon = (ImageView) findViewById(R.id.imageViewAdIcon);
				RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBarAppRating);
				Button buttonAction = (Button) findViewById(R.id.buttonAction);

				//				ImageView imageViewScreeshot = (ImageView) findViewById(R.id.imageViewAdScreenshot);

				ratingBar.setRating(Float.parseFloat(newAd.rating));
				//				LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
				//				stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
				//				stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);


				buttonAction.setText(newAd.cta);
				textViewTitle.setText(newAd.title);
				textViewDescription.setText(newAd.description);
				//				textViewRating.setText(newAd.rating);

				buttonAction.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (newAd.landing_url != null){
							Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(newAd.landing_url));
							browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(browserIntent);
						}else{
							Log.v("newAd.landing_url", "null");
						}
					}
				});
				//				
				UrlImageViewHelper.setUrlDrawable((ImageView) imageViewIcon,  newAd.icon.url, R.drawable.ic_launcher, new UrlImageViewCallback() {					
					@Override
					public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url, boolean loadedFromCache) {
						if (!loadedFromCache) {
							ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
							scale.setDuration(300);
							scale.setInterpolator(new OvershootInterpolator());
							imageView.startAnimation(scale);
						}
					}
				});

				//				UrlImageViewHelper.setUrlDrawable((ImageView) imageViewScreeshot,  newAd.screenshots.url, R.drawable.ic_launcher, new UrlImageViewCallback() {					
				//	                @Override
				//	                public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url, boolean loadedFromCache) {
				//	                    if (!loadedFromCache) {
				//	                        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
				//	                        scale.setDuration(300);
				//	                        scale.setInterpolator(new OvershootInterpolator());
				//	                        imageView.startAnimation(scale);
				//	                    }
				//	                }
				//	            });


				Log.v("TAG", content);
			}else{
				RelativeLayout relativeLayoutInMobiAd = (RelativeLayout) findViewById(R.id.relativeLayoutInMobiAd);
				relativeLayoutInMobiAd.setVisibility(View.GONE);
				Log.v("TAG", "Add is null");
				if (!showAdsFromBothProviders)
					loadAdMob();
			}
		}
		@Override
		public void onNativeRequestFailed(IMErrorCode errorCode) {
			Log.v("TAG", "Add loading failed");
			android.util.Log.e("TAG", "native ad failed");
			RelativeLayout relativeLayoutInMobiAd = (RelativeLayout) findViewById(R.id.relativeLayoutInMobiAd);
			relativeLayoutInMobiAd.setVisibility(View.GONE);
			if(!showAdsFromBothProviders)
				loadAdMob();
		}
	};

	@Override
	protected void onStop() {
		if (nativeAd != null)
			nativeAd.detachFromView();
		super.onStop();
	};
	
	private void loadAdMob(){
	
		AdRequest request = new AdRequest.Builder()	    
	    .addTestDevice(Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID))  // My Galaxy Nexus test phone
	    .build();
		
		adView = (AdView) findViewById(R.id.adView);
		
		if (isTablet(getApplicationContext())){
			adView.setAdSize(AdSize.LEADERBOARD);
		}
		
		adView.loadAd(request);
		
		//.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub		
		//showToast(getApplicationContext(), this, "Custom Toast Loaded", Toast.LENGTH_LONG);
		super.onBackPressed();
	}
	
	public boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem menuItem = menu.findItem(R.id.ShowBothAds);
		menuItem.setChecked(savedValues.loadKeyValue(getString(R.string.key_show_ads_from_both_providers), false));
		MenuItem menuItem2 = menu.findItem(R.id.SimulateInMobiFailure);
		menuItem2.setChecked(savedValues.loadKeyValue(getString(R.string.key_simulate_inmobi_failure), false));
		Log.v("onPrepareOptionsMenuSelected", "true");
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.ShowBothAds) {
			Log.v("ShowBothAds Clicked", "true");
			item.setChecked(!item.isChecked());
			savedValues.saveKeyValue(getString(R.string.key_show_ads_from_both_providers), item.isChecked());
			Intent mainIntent = new Intent(this, MainActivity.class);
			this.startActivity(mainIntent);
			finish();
			return true;
		}else if (id == R.id.SimulateInMobiFailure) {
			Log.v("Simulate InMobi Error Clicked", "true");
			item.setChecked(!item.isChecked());
			savedValues.saveKeyValue(getString(R.string.key_simulate_inmobi_failure), item.isChecked());
			
			Intent mainIntent = new Intent(this, MainActivity.class);
			this.startActivity(mainIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		nativeAd = null;

	}
	
	private static void showToast(final Context context, final Activity activity, String text, int duration){
		LayoutInflater inflater = activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_custom,
		                               (ViewGroup) activity.findViewById(R.id.toast_layout_root));

		TextView textView = (TextView) layout.findViewById(R.id.text);
		textView.setText(text);
		
		ImageView imageViewLogo = (ImageView) layout.findViewById(R.id.imageViewToastLogo);
		imageViewLogo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView textViewSponsored = (TextView) activity.findViewById(R.id.textViewAdSponsored);
				Log.v("Toast Clicked","true");
				if (textViewSponsored.getText().equals("Sponsored")){
					textViewSponsored.setTextColor(Color.RED);
					textViewSponsored.setText("Sponsored_");
				}else{
					textViewSponsored.setTextColor(Color.GREEN);
					textViewSponsored.setText("Sponsored_");
				}				
			}
		});

		Toast toast = new Toast(context);
		toast.setGravity(Gravity.BOTTOM, 0, 250);
		toast.setDuration(duration);
		toast.setView(layout);
		toast.show();
	}
}
