package com.nspl.inmobisample;

import com.google.gson.Gson;
import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMNative;
import com.inmobi.monetization.IMNativeListener;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.graphics.*;;

public class MainActivity extends ActionBarActivity {

	InMobiNativeAd newAd;

	IMNativeListener nativeListener = new IMNativeListener() {
		@Override
		public void onNativeRequestSucceeded(final IMNative nativeAd) {		
			if (nativeAd != null){				
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
				Log.v("TAG", "Add is null");
			}
		}
		@Override
		public void onNativeRequestFailed(IMErrorCode errorCode) {
			Log.v("TAG", "Add loading failed");
			android.util.Log.e("TAG", "native ad failed");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		InMobi.initialize(this, "f5ecc6679216457b87cd953195c1890c");
		//InMobi.initialize(this, "f71cf2b4513749a89e81ff4704680e14");

		IMNative nativeAd = new IMNative(nativeListener);

		nativeAd.loadAd();
		Log.v("TAG", "Activity loaded and native add asked for");
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
}
