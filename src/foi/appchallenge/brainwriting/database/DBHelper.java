package foi.appchallenge.brainwriting.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "database.db";
	private static final int DATABASE_VERSION = 1;
	//private Context context;
	//private SQLiteDatabase database;

	public DBHelper(Context c) {
		super(c, DATABASE_NAME, null, DATABASE_VERSION);
		//context = c;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//database = db;

		db.execSQL("CREATE TABLE idea (id INTEGER PRIMARY KEY, creator VARCHAR(50), img_width INTEGER, img_height INTEGER, img_name VARCHAR(100))");
		db.execSQL("CREATE TABLE note (id INTEGER PRIMARY KEY, note_text TEXT, x INTEGER, y INETEGER, idea_id INTEGER)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
}