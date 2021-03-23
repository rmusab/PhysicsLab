package kz.develop.physicslab;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class Form extends SherlockActivity {

    WebView webView;
	
	private int calculateScale(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int picWidth = options.outWidth;
 		Double val = 1d;
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                val = Double.valueOf(reqWidth) / Double.valueOf(picWidth);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                val = Double.valueOf(reqHeight) / Double.valueOf(picWidth);
                break;
        }
		val = val * 100d;
		return val.intValue();
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
        setLocale();

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= 11)
            webView.getSettings().setDisplayZoomControls(false);
        webView.setPadding(0, 0, 0, 0);
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        Intent intent = getIntent();
        int pos = intent.getExtras().getInt("image");
        SharedPreferences mSettings = getSharedPreferences(PrefActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean method = mSettings.getBoolean(PrefActivity.APP_PREFERENCES_METHOD, false);
        if (method) {
            webView.getSettings().setSupportZoom(false);
            webView.getSettings().setBuiltInZoomControls(false);
            setWebImage(pos);
        } else setHtml(pos);

        String title = getResources().getStringArray(R.array.buttons)[pos];
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setWebImage(int pos) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int dwidth = metrics.widthPixels;
        int dheight = metrics.heightPixels;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        try {
            InputStream is = getAssets().open("GIF/" + getResources().getStringArray(R.array.formulas_gif)[pos]);
            BitmapFactory.decodeStream(is, null, options);
        } catch (IOException e) {
            return;
        }
        webView.setInitialScale(calculateScale(options, dwidth, dheight));
        webView.loadUrl("file:///android_asset/GIF/" + getResources().getStringArray(R.array.formulas_gif)[pos]);
    }

    private void setHtml(int pos) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/" + getResources().getStringArray(R.array.formulas)[pos]);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			  this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setLocale() {
		SharedPreferences mSettings = getSharedPreferences(PrefActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
		String lang = mSettings.getString(PrefActivity.APP_PREFERENCES_LANG, getResources().getConfiguration().locale.toString());
		Locale myLocale = new Locale(lang); 
		Locale.setDefault(myLocale);
		Resources res = getResources(); 
		DisplayMetrics dm = res.getDisplayMetrics(); 
		Configuration conf = res.getConfiguration(); 
		conf.locale = myLocale; 
		res.updateConfiguration(conf, dm);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		setLocale();
		super.onSaveInstanceState(outState);
	}
	
}
