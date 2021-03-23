package kz.develop.physicslab;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.DisplayMetrics;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

import java.util.Locale;

/**
 * Created by Steve Fox on 15.08.2014.
 */
public class PrefActivity extends SherlockPreferenceActivity {

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_LANG = "language";
    public static final String APP_PREFERENCES_METHOD = "method";

    private void setLocale() {
        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String lang = mSettings.getString(APP_PREFERENCES_LANG, getResources().getConfiguration().locale.toString());
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale();
        addPreferencesFromResource(R.xml.pref);
        getPreferenceManager().setSharedPreferencesName(APP_PREFERENCES);
        SharedPreferences sp = getPreferenceManager().getSharedPreferences();

        CheckBoxPreference chMethod = (CheckBoxPreference) findPreference(APP_PREFERENCES_METHOD);
        chMethod.setChecked(sp.getBoolean(APP_PREFERENCES_METHOD, false));
        ListPreference listLang = (ListPreference) findPreference(APP_PREFERENCES_LANG);
        String lang = sp.getString(APP_PREFERENCES_LANG, getResources().getConfiguration().locale.toString());
        if ((lang.equalsIgnoreCase("ru")) || (lang.equalsIgnoreCase("ru_ru")))
            listLang.setValueIndex(1);
         else listLang.setValueIndex(0);
        listLang.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                setResult(RESULT_OK);
                finish();
                return true;
            }
        });

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        setLocale();
        super.onSaveInstanceState(outState);
    }
}
