package foi.appchallenge.brainwriting.types;

/**
 * Note type of data.
 * 
 * @author Josip
 * 
 */
public class Note {
	private int id;
	private String noteText;
	private int ideaId;
	private int x;
	private int y;

	public Note(int id, String noteText, int ideaId, int x, int y) {
		this.id = id;
		this.noteText = noteText;
		this.ideaId = ideaId;
		this.x = x;
		this.y = y;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNoteText() {
		return noteText;
	}

	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}

	public int getIdeaId() {
		return ideaId;
	}

	public void setIdeaId(int ideaId) {
		this.ideaId = ideaId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
