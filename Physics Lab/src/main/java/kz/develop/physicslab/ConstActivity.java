package kz.develop.physicslab;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

public class ConstActivity extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private Context context;
	View view;
	
	private final String DB_NAME = "dbConsts";
	private String TABLE_NAME;
	private final int ITEM_DEL_ID = 1;
	private final int ITEM_VALUE_ID = 2;
    private final int ITEM_CLIP_NAME = 3;
    private final int ITEM_CLIP_VALUE = 4;
			
	public static DB db;
	public static ListView lvConst;
	SimpleCursorAdapter scAdapter;
	public static long menu_id = -1;
	    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.const_layout, container, false);
        context = view.getContext();
        lvConst = (ListView) view.findViewById(R.id.lvConst);

		db = new DB(context);
		TABLE_NAME = getResources().getString(R.string.table_name);
		db.open(DB_NAME, TABLE_NAME);
								
		String[] from = {DB.COLUMN_NAME, DB.COLUMN_TXT}; 
	    int[] to = {R.id.tvName, R.id.tvTxt};
        scAdapter = new SimpleCursorAdapter(context,
				R.layout.item, null, from, to, 0);
        View v = inflater.inflate(R.layout.footer, null);
        lvConst.addFooterView(v, null, true);
		lvConst.setAdapter(scAdapter);
		getActivity().getSupportLoaderManager().initLoader(0, null, this);

        lvConst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (l == -1) {
                    Dialog_add dlgAdd = new Dialog_add();
                    dlgAdd.show(getActivity().getSupportFragmentManager(), "dialog_add");
                }
            }
        });
		registerForContextMenu(lvConst);
		
		LinearLayout llConst = (LinearLayout) view.findViewById(R.id.llConst);
		MainActivity.fixBackgroundRepeat(llConst);

        return view;
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
        if (((AdapterContextMenuInfo)menuInfo).position == lvConst.getCount()-1) return;
        switch (v.getId()) {
		case R.id.lvConst:
				menu.add(0, ITEM_DEL_ID, 0, R.string.item_del_text);
				menu.add(0, ITEM_VALUE_ID, 0, R.string.item_value_text);
                menu.add(0, ITEM_CLIP_NAME, 0, R.string.item_clip_name_text);
                menu.add(0, ITEM_CLIP_VALUE, 0, R.string.item_clip_value_text);
			break;
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		menu_id = menuInfo.id;
				
		if (item.getItemId() == ITEM_DEL_ID) {
			db.delRec(menu_id);
			getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
		}
		if (item.getItemId() == ITEM_VALUE_ID) {
			DialogFragment dialog_edit = new Dialog_edit();
			dialog_edit.show(getActivity().getSupportFragmentManager(), "dialog_edit");
		}
        if (item.getItemId() == ITEM_CLIP_NAME) {
            Cursor item_c = db.getRec((int) menu_id);
            if (item_c.moveToFirst()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    ClipboardManager ClipMan = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", item_c.getString(item_c.getColumnIndex(DB.COLUMN_NAME)));
                    ClipMan.setPrimaryClip(clip);
                } else {
                    android.text.ClipboardManager ClipMan = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipMan.setText(item_c.getString(item_c.getColumnIndex(DB.COLUMN_NAME)));
                }
                Toast.makeText(context, R.string.toast_clipboard, Toast.LENGTH_SHORT).show();
            }
            item_c.close();
        }
        if (item.getItemId() == ITEM_CLIP_VALUE) {
            Cursor item_c = db.getRec((int) menu_id);
            if (item_c.moveToFirst()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    ClipboardManager ClipMan = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", item_c.getString(item_c.getColumnIndex(DB.COLUMN_TXT)));
                    ClipMan.setPrimaryClip(clip);
                } else {
                    android.text.ClipboardManager ClipMan = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipMan.setText(item_c.getString(item_c.getColumnIndex(DB.COLUMN_TXT)));
                }
                Toast.makeText(context, R.string.toast_clipboard, Toast.LENGTH_SHORT).show();
            }
            item_c.close();
        }
		return super.onContextItemSelected(item);
	}
	
	@Override
	public android.support.v4.content.Loader<Cursor> onCreateLoader(int id,
			Bundle bndl) {
		return new MyCursorLoader(context, db);
	}

	@Override
	public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader,
			Cursor cursor) {
		scAdapter.swapCursor(cursor);		
	}

	@Override
	public void onLoaderReset(android.support.v4.content.Loader<Cursor> arg0) {
	}
	
	static class MyCursorLoader extends CursorLoader {

	    DB db;
	    
	    public MyCursorLoader(Context context, DB db) {
	      super(context);
	      this.db = db;
	    }
	    
	    @Override
	    public Cursor loadInBackground() {
	      Cursor cursor = db.getAllData();
	      return cursor;
	    }
	    
	}

}
