package foi.appchallenge.brainwriting.database;

import java.util.ArrayList;
import java.util.List;

import foi.appchallenge.brainwriting.types.Idea;
import foi.appchallenge.brainwriting.types.Note;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Adapter for working with idea and note tables in DB.
 * 
 * @author Josip
 * 
 */
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

	/**
	 * Searches notes for specific words (query).
	 * 
	 * @param query
	 *            Search keywords
	 * @return List of notes that contains keyword
	 */
	public List<Note> searchNotes(String query) {

		List<Note> result = new ArrayList<Note>();

		String[] columns = new String[] { KEY_ID, "id", "note_text", "idea_id",
				"x", "y" };

		Cursor cursor = sqLiteDatabase.query(TABLE, columns,
				"note_text like '%" + query + "%'", null, null, null, null);

		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {

			int id = cursor.getInt(cursor.getColumnIndex("id"));

			String noteText = cursor.getString(cursor
					.getColumnIndex("note_text"));

			int x = cursor.getInt(cursor.getColumnIndex("x"));
			int y = cursor.getInt(cursor.getColumnIndex("y"));

			int ideaId = cursor.getInt(cursor.getColumnIndex("idea_id"));

			Note note = new Note(id, noteText, ideaId, x, y);

			result.add(note);
		}
		cursor.close();

		return result;
	}

	/**
	 * Inserts single note in DB table note.
	 * 
	 * @param note
	 *            Note to insert
	 * @return True if inserted successful, false otherwise
	 */
	public boolean insertNote(Note note) {
		ContentValues cv = new ContentValues();
		// note id is auto increment
		cv.put("note_text", note.getNoteText());
		cv.put("x", note.getX());
		cv.put("y", note.getY());
		cv.put("idea_id", note.getIdeaId());

		if (sqLiteDatabase.insert("note", null, cv) != -1) {
			return true;
		}
		return false;
	}

	/**
	 * Inserts notes i DB table note.
	 * 
	 * @param notes
	 *            Notes to insert.
	 * @return True if inserted successful, false otherwise
	 */
	public boolean insertNotes(ArrayList<Note> notes) {
		boolean allInsertedOk = true;
		for (int i = 0; i < notes.size(); i++) {
			if (!insertNote(notes.get(i))) {
				allInsertedOk = false;
			}
		}
		return allInsertedOk;
	}

	/**
	 * Inserts idea (including ideas notes) in DB.
	 * 
	 * @param idea
	 *            Idea to insert
	 * @return True if inserted successful, false otherwise
	 */
	public boolean insertIdea(Idea idea) {
		boolean ideaInsertedOk = false;
		boolean notesInsertedOk = false;

		ContentValues cv = new ContentValues();
		// note id is auto increment
		cv.put("creator", idea.getCreator());
		cv.put("img_height", idea.getImageHeight());
		cv.put("img_width", idea.getImageWidth());
		cv.put("img_name", idea.getImageName());

		if (sqLiteDatabase.insert("idea", null, cv) != -1) {
			ideaInsertedOk = true;
		}

		if (insertNotes(idea.getNotes())) {
			notesInsertedOk = true;
		}

		return (ideaInsertedOk && notesInsertedOk);
	}

	/**
	 * Return last (MAX) id from DB for table idea.
	 * 
	 * @return Table idea MAX id
	 */
	public int getLastId() {
		int lastId = -1;
		Cursor cursor = sqLiteDatabase.rawQuery(
				"SELECT id FROM idea WHERE id=(SELECT MAX(id) FROM idea);",
				null);
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			lastId = cursor.getInt(cursor.getColumnIndex("id"));
		}
		cursor.close();
		return lastId;
	}
}
