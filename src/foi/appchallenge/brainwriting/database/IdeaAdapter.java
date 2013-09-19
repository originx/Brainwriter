package foi.appchallenge.brainwriting.database;


import java.util.ArrayList;
import java.util.List;

import foi.appchallenge.brainwriting.types.Note;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class IdeaAdapter {
	public static final String TABLE = "idea";
	public static final String KEY_ID = "id";

	private DBHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;
	private Context context;

	public IdeaAdapter(Context c) {
		context = c;
	}

	public IdeaAdapter openToRead() throws android.database.SQLException {
		sqLiteHelper = new DBHelper(context);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;
	}

	public IdeaAdapter openToWrite() throws android.database.SQLException {
		sqLiteHelper = new DBHelper(context);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		sqLiteHelper.close();
	}
	
	public List<Note> searchNotes(String query){
		
		List<Note> result = new ArrayList<Note>();

		String[] columns = new String[] { KEY_ID, "id",
				"note_text", "note_id",
				"x", "y" };

		Cursor cursor = sqLiteDatabase.query(TABLE, columns, "note_text like '%"
				+ query + "%'", null,
				null, null, null);
		
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {

			int id = cursor.getInt(cursor
					.getColumnIndex("id"));
			
			String noteText = cursor.getString(cursor.getColumnIndex("note_text"));

			int x = cursor.getInt(cursor
					.getColumnIndex("x"));
			int y = cursor.getInt(cursor
					.getColumnIndex("y"));

			int ideaId = cursor.getInt(cursor.getColumnIndex("idea_id"));

			Note note = new Note(id, noteText, ideaId, x, y);
			
			result.add(note);
		}
		cursor.close();
		
		return result;
	}
}
