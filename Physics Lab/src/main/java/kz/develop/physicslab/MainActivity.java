package kz.develop.physicslab;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity implements com.actionbarsherlock.app.ActionBar.TabListener {
	
	private final int FRAGMENT_ONE = 0;
    private final int FRAGMENT_TWO = 1;
    private final int FRAGMENT_THREE = 2;
    private final int FRAGMENTS = 3;
    private FragmentPagerAdapter _fragmentPagerAdapter;
    private final List<Fragment> _fragments = new ArrayList<Fragment>();
    private final List<com.actionbarsherlock.app.ActionBar.Tab> _tabs = new ArrayList<com.actionbarsherlock.app.ActionBar.Tab>();
    private ViewPager _viewPager;
		
	private ImageView imgLogo;
		
	private RelativeLayout splash;
	private boolean loading;
	private static final int STOPSPLASH = 0;
	private static final long SPLASHTIME = 3000;

    public static SharedPreferences myPreferences;
				
	private Handler splashHandler = new Handler() 
	{
		@Override
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			    case STOPSPLASH:
				    splash.setVisibility(View.GONE);
				    _viewPager.setVisibility(View.VISIBLE);
				    getSupportActionBar().show();
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	public static void fixBackgroundRepeat(View view) {
	    Drawable bg = view.getBackground();
	    if (bg != null) {
	        if (bg instanceof BitmapDrawable) {
	            BitmapDrawable bmp = (BitmapDrawable) bg;
	            bmp.mutate();
	            bmp.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
	        }
	    }
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLocale();
		setContentView(R.layout.main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		splash = (RelativeLayout) findViewById(R.id.rlSplash);
		imgLogo = (ImageView) findViewById(R.id.imgLogo);
		loading = true;

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        doFragments();

        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	}
	
	private void doFragments() {
		_fragments.add(FRAGMENT_ONE, new MenuActivity());
        _fragments.add(FRAGMENT_TWO, new ConvertActivity());
        _fragments.add(FRAGMENT_THREE, new ConstActivity());
        _fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return FRAGMENTS;
            }

            @Override
            public Fragment getItem(final int position) {
                return _fragments.get(position);
            }
            
            @Override
            public CharSequence getPageTitle(int position) {
            	return getResources().getStringArray(R.array.pages)[position];
            }
        };
        _viewPager = (ViewPager) findViewById(R.id.pager);
        _viewPager.setAdapter(_fragmentPagerAdapter);
        _viewPager.setOffscreenPageLimit(2);
        _viewPager.setCurrentItem(FRAGMENT_ONE);
        _viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ConvertActivity.btnCalc.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setSelectedNavigationItem(position);
            }
        });

        _tabs.add(getSupportActionBar().newTab().setText(getResources().getStringArray(R.array.pages)[FRAGMENT_ONE]));
        _tabs.add(getSupportActionBar().newTab().setText(getResources().getStringArray(R.array.pages)[FRAGMENT_TWO]));
        _tabs.add(getSupportActionBar().newTab().setText(getResources().getStringArray(R.array.pages)[FRAGMENT_THREE]));
        getSupportActionBar().addTab(_tabs.get(FRAGMENT_ONE).setTabListener(this));
        getSupportActionBar().addTab(_tabs.get(FRAGMENT_TWO).setTabListener(this));
        getSupportActionBar().addTab(_tabs.get(FRAGMENT_THREE).setTabListener(this));
        getSupportActionBar().setSelectedNavigationItem(FRAGMENT_ONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (loading) {
			_viewPager.setVisibility(View.GONE);
        	getSupportActionBar().hide();
			Message msg = new Message();
            msg.what = STOPSPLASH;
            splashHandler.sendMessageDelayed(msg, SPLASHTIME);
            loading = false;
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.myanim);
            imgLogo.startAnimation(anim);
		}
	}
		
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("loading", false);
        setLocale();
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		loading = savedInstanceState.getBoolean("loading");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
			
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itmAbout:
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.itmPreferences:
            Intent prefIntent = new Intent(this, PrefActivity.class);
            startActivityForResult(prefIntent, 0);
			break;
		case R.id.itmQuit:
			ConstActivity.db.close();
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onTabSelected(com.actionbarsherlock.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        _viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(com.actionbarsherlock.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(com.actionbarsherlock.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
}
