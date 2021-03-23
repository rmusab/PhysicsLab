package kz.develop.physicslab;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class AboutActivity extends SherlockActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
        setLocale();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
		
	public void GoogleClick(View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=kz.develop.physicslab"));
		startActivity(intent);		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		setLocale();
		super.onSaveInstanceState(outState);
	}
}
