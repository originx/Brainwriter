package foi.appchallenge.brainwriting.types;

import java.util.ArrayList;

/**
 * Idea type of data.
 * 
 * @author Josip
 * 
 */
public class Idea {
	private int id;
	private String creator;
	private int imageWidth;
	private int imageHeight;
	private String imageName;
	private ArrayList<Note> notes;

	public Idea(int id, String creator, int imageWidth, int imageHeight,
			String imageName, ArrayList<Note> notes) {
		this.id = id;
		this.creator = creator;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.imageName = imageName;
		this.notes = notes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public ArrayList<Note> getNotes() {
		return notes;
	}

	public void setNotes(ArrayList<Note> notes) {
		this.notes = notes;
	}

}
