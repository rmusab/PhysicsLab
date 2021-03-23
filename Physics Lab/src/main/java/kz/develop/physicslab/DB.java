package kz.develop.physicslab;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DB {
	
	private String db_name = "";
	private String table_name = "";
	private SQLiteDatabase mDB;
	private Context mContext;
	private DBHelper mDBHelper;
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TXT = "txt";
	public final int DB_VERSION = 1;
	
	public DB(Context context) {
		mContext = context;
	}
	
	public void open(String db, String table) {
		db_name = db;
		table_name = table;
		mDBHelper = new DBHelper(mContext, db_name, null, DB_VERSION);
		
		try {
			mDBHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}

		try {
			mDBHelper.openDataBase();
		}catch(SQLException sqle){
			throw sqle;
		}
	}
	
	public void close() {
		if (mDBHelper != null) {
		  mDBHelper.close();
		  mDB.close();
		}		
    }
	
	public Cursor getAllData() {
    	return mDB.query(table_name, null, null, null, null, null, null);
    }
	
	public Cursor getRec(long id) {
		String[] whereArgs = {String.valueOf(id)};
		return mDB.query(table_name, null, COLUMN_ID + " = ?", whereArgs, null, null, null);
	}
	
	public void addRec(String name, String txt) {
    	ContentValues cv = new ContentValues();
    	cv.put(COLUMN_NAME, name);
    	cv.put(COLUMN_TXT, txt);
    	mDB.insert(table_name, null, cv);
    }
	
	public void changeRec(long id, String name, String txt) { 
	    ContentValues cv = new ContentValues();
	    cv.put(COLUMN_NAME, name);
	    cv.put(COLUMN_TXT, txt); 
	    mDB.update(table_name, cv, COLUMN_ID + " = " + id, null); 
	}
	
	public void delRec(long id) {
    	String[] whereArgs = {String.valueOf(id)};
    	mDB.delete(table_name, COLUMN_ID + " = ?", whereArgs);
    }
	
	class DBHelper extends SQLiteOpenHelper {
		
		private File DB_PATH;
		
		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			DB_PATH = mContext.getDatabasePath(db_name);
		}
				
	    private boolean checkDataBase(){
	    	SQLiteDatabase checkDB = null;
	    	try{
	    		String myPath = DB_PATH.getAbsolutePath();
	    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	    	} catch (SQLiteException e) {

	    	}
	    	if (checkDB != null) {
	    		checkDB.close();
	    	}
	    	return checkDB != null ? true : false;
	    }

	    private void copyDataBase() throws IOException{
	    	InputStream myInput = mContext.getAssets().open(db_name);
	    	String outFileName = DB_PATH.getAbsolutePath();
	    	OutputStream myOutput = new FileOutputStream(outFileName);

	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = myInput.read(buffer))>0){
	    		myOutput.write(buffer, 0, length);
	    	}
	    	
	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
	    }

	    public void openDataBase() throws SQLException{
	    	String myPath = DB_PATH.getAbsolutePath();
	    	mDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	    }
		
		public void createDataBase() throws IOException{
	    	boolean dbExist = checkDataBase();
	    	if (dbExist) {

	    	} else {
	    		this.getReadableDatabase();
	        	try {
	    			copyDataBase();
	    		} catch (IOException e) {
	        		throw new Error("Error copying database");
	        	}
	    	}
	    }

		@Override
		public void onCreate(SQLiteDatabase db) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
				
	}

}
