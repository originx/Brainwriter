package foi.appchallenge.brainwriting;

import java.io.File;
import java.io.FileOutputStream;

import foi.appchallenge.helpers.HSVColorPickerDialog;
import foi.appchallenge.helpers.HSVColorPickerDialog.OnColorSelectedListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class IdeaMakerActivity extends ActionBarActivity  {

	private Context context;
	private RadioGroup rgDrawOptions;
	private ImageButton colorPicker;

	private ScrollView sv;
	private HorizontalScrollView hsv;

	private GestureDetector gestureDetector;

	private ImageView ivCanvas;

	// TODO get real values from MainActivity through extras in onCreate
	private int CANVAS_PX_WIDTH = 1024;
	private int CANVAS_PX_HEIGHT = 1024;

	private Bitmap bmp;

	private Canvas canvas;
	private Paint paint;
	private Matrix matrix;
	private float downx = 0;
	private float downy = 0;
	private float upx = 0;
	private float upy = 0;

	// TODO create Settings for this so it can be changed
	private int canvasBackgroundColorId = 0xffffffff;
	private int brushColorId;
	private int brushStrokeWidth = 5;

	// coordinates to shift the view by
	private float shiftX = 0f;
	private float shiftY = 0f;
	
	// used for getting coordinates of tap in gestureDetector
	// (in onDown listener)
	private float x = -1; // -1 means coordinate is not OK to use
	private float y = -1;

	//previous idea on which we worked on
	private int previousIdea=0;
	private boolean imageLoaded=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_idea_maker);
		context = this;

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayOptions(actionBar.getDisplayOptions()
				^ ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		final String[] ideas = { "Idea 1", "Idea 2", "Idea 3" };
		SpinnerAdapter mSpinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, ideas);

		OnNavigationListener mOnNavigationListener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int position, long itemId) {
				Toast.makeText(context, ideas[position].toString(),
						Toast.LENGTH_SHORT).show();

				//TODO save  text
				manageIdeas(position);
				return true;
			}
			
			/**
			 * Saves current idea and loads picked one if exists
			 * @param position current position of idea
			 */
			private void manageIdeas(int position) {
				//if we switched idea
				if(previousIdea!=position){
				//get root folder
				String root = Environment.getExternalStorageDirectory().toString();
				File myDir = new File(root + "/Brainwriter/my_ideas");    
				myDir.mkdirs();
				//create new image file
				String fname = "image"+ String.valueOf(previousIdea+1) +".png";
				File file = new File (myDir, fname);
				//if that file exists delete it to create new one
				if (file.exists ()) file.delete (); 
				try {
						//save file
				       FileOutputStream out = new FileOutputStream(file);
				       bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
				       out.flush();
				       out.close();
				} catch (Exception e) {
				       e.printStackTrace();
				}
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				//load file if exists
				File imgFile = new  File(myDir+"/image"+ String.valueOf(position+1) +".png");
				if(!imgFile.exists()){
					//if there isn't a file to load prepare empty canvas
					prepareEmptyCanvas();				
				}else{
					//if there is a file prepare canvas with loaded image
					 Bitmap mutableBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
					 bmp = mutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
					prepareLoadedCanvas(bmp);
				}
				}
				imageLoaded=true;//sets that we loaded image
				//save previous position
				previousIdea=position;
			}
		};

		actionBar.setListNavigationCallbacks(mSpinnerAdapter,
				mOnNavigationListener);
		shiftX = 0f;
		shiftY = 0f;
		
		ivCanvas = (ImageView) this.findViewById(R.id.iv_canvas);

		sv = (ScrollView) findViewById(R.id.sv);
		hsv = (HorizontalScrollView) findViewById(R.id.hsv);

		if(!imageLoaded || bmp==null){//if image isnt loaded create new bmp
			prepareEmptyCanvas();
		}


		
		colorPicker.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HSVColorPickerDialog cpd = new HSVColorPickerDialog( IdeaMakerActivity.this, brushColorId, new OnColorSelectedListener() {
					
					public void colorSelected(Integer color) {
						colorPicker.setBackgroundColor(color);
						brushColorId = color;
						paint.setColor(brushColorId);
					}
				});
				//cpd.setTitle("Pick a color");
				cpd.show();
				
			}
		});
		
		rgDrawOptions = (RadioGroup) findViewById(R.id.rg_draw_options);
		rgDrawOptions
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group,
							int checkedRadioButtonId) {
						switch (checkedRadioButtonId) {
						case R.id.rb_hand:
							Toast.makeText(context, "HAND", Toast.LENGTH_SHORT)
									.show();
							break;
						case R.id.rb_brush:
							Toast.makeText(context, "BRUSH", Toast.LENGTH_SHORT)
									.show();
							break;
						case R.id.rb_text:
							Toast.makeText(context, "TEXT", Toast.LENGTH_SHORT)
									.show();
							break;
						case R.id.rb_eraser:
							Toast.makeText(context, "ERASER",
									Toast.LENGTH_SHORT).show();
							break;
						
						default:
							break;
						}
					}
				});

		GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onDown(MotionEvent e) {
				// used for drawing on canvas
				if (rgDrawOptions.getCheckedRadioButtonId() == R.id.rb_brush) {
					x = e.getX();
					y = e.getY();
					canvas.drawPoint(x, y, paint);
					ivCanvas.invalidate();
					Log.d("gestureDetector:onDown", "x: " + x + " y:" + y);
					//downx = x;
					//downy = y;
					//Toast.makeText(context,  "x: " + x + " y: " + y, Toast.LENGTH_SHORT)
					//.show();
				} else { // if not in brush MOD
					x = -1;
					y = -1;
				}
				return super.onDown(e);
			}
		};

		gestureDetector = new GestureDetector(getBaseContext(), gestureListener);

		// 'turn off' inner ScrollView
		sv.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (rgDrawOptions.getCheckedRadioButtonId() == R.id.rb_hand) {
					
					return false;
				} else if (rgDrawOptions.getCheckedRadioButtonId() == R.id.rb_brush) {
					int action = event.getAction();
					switch (action) {
					case MotionEvent.ACTION_DOWN:
						break;
					case MotionEvent.ACTION_MOVE:
						upx = event.getX();
						upy = event.getY();

						
						if(isUpXOk()){
							drawOnCanvas();
						}
						break;
					case MotionEvent.ACTION_UP:
						break;
					case MotionEvent.ACTION_CANCEL:
						break;
					default:
						break;
					}
				//return false;
				}
				return true; // no scroll case
			}
		});

		// outer HorizontalScrollView handles all scrolling
		// including vertical so that there is decent diagonal scroll
		hsv.setOnTouchListener(new View.OnTouchListener() {
			private float mx, my, curX, curY;
			private boolean started = false;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (rgDrawOptions.getCheckedRadioButtonId() == R.id.rb_hand) {
					curX = event.getX();
					curY = event.getY();
					int dx = (int) (mx - curX);
					int dy = (int) (my - curY);
					switch (event.getAction()) {
					case MotionEvent.ACTION_MOVE:
						if (started) {
							sv.scrollBy(0, dy);
							hsv.scrollBy(dx, 0);
							//shiftX += dx;// moves the shifting variables
							//shiftY += dy;// in the direction of the finger
						} else {
							started = true;
						}
						mx = curX;
						my = curY;
						break;
					case MotionEvent.ACTION_UP:
						sv.scrollBy(0, dy);
						hsv.scrollBy(dx, 0);
						started = false;
						break;
					}
					return true;
				} else if (rgDrawOptions.getCheckedRadioButtonId() == R.id.rb_brush) {
					int action = event.getAction();
					switch (action) {
					case MotionEvent.ACTION_DOWN:
						
						break;
					case MotionEvent.ACTION_MOVE:
						upx = event.getX();
						upy = event.getY();

						if(isUpXOk()){
							drawOnCanvas();
						}
						break;
					case MotionEvent.ACTION_UP:
						//Toast.makeText(context, "action up", Toast.LENGTH_SHORT)
						//.show();
						 //upx = event.getX();
						 //upy = event.getY();
						 //drawOnCanvas();
						 //canvas.drawLine(downx, downy, upx, upy, paint);
						 //ivCanvas.invalidate();
						break;
					case MotionEvent.ACTION_CANCEL:
						break;
					default:
						break;
					}
				}

				return true; // if MOD is not hand
			}
		});

		// Touch events on ivCanvas are handled with gesture detector
		ivCanvas.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				gestureDetector.onTouchEvent(event);
				return true;
			}
		});

	}

	private void prepareEmptyCanvas() {
		// for smaller bitmap use different config
				Bitmap.Config conf = Bitmap.Config.ARGB_8888;

				// this creates a MUTABLE bitmap
				bmp = Bitmap.createBitmap(CANVAS_PX_WIDTH, CANVAS_PX_HEIGHT, conf);
		canvas = new Canvas(bmp);

		// Fill with some background color
		canvas.drawColor(canvasBackgroundColorId);
		colorPicker = (ImageButton)findViewById(R.id.ib_color);
		colorPicker.setBackgroundColor(0xFF4488CC);
		brushColorId = 0xFF4488CC;
		paint = new Paint();
		// a lot of options
		paint.setColor(brushColorId);
		paint.setStrokeWidth(brushStrokeWidth);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setAntiAlias(true);

		matrix = new Matrix();
		canvas.drawBitmap(bmp, matrix, paint);

		// attach the canvas to the ImageView
		ivCanvas.setImageDrawable(new BitmapDrawable(getResources(), bmp));
	}
	private void prepareLoadedCanvas(Bitmap bmp) {
		
		canvas = new Canvas(bmp);

		// Fill with some background color
		//canvas.drawColor(canvasBackgroundColorId);
		colorPicker = (ImageButton)findViewById(R.id.ib_color);
		colorPicker.setBackgroundColor(0xFF4488CC);
		brushColorId = 0xFF4488CC;
		paint = new Paint();
		// a lot of options
		paint.setColor(brushColorId);
		paint.setStrokeWidth(brushStrokeWidth);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setAntiAlias(true);

		matrix = new Matrix();
		canvas.drawBitmap(bmp, matrix, paint);

		// attach the canvas to the ImageView
		ivCanvas.setImageDrawable(new BitmapDrawable(getResources(), bmp));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.idea_maker, menu);
		MenuItem search = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat
				.getActionView(search);
		searchView.setQueryHint(context.getResources().getString(
				R.string.action_bar_search_hint));
		searchView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		/*
		 * case R.id.action_search: Toast.makeText(context, "SEARCH",
		 * Toast.LENGTH_SHORT).show(); return true;
		 */
		case R.id.action_send:
			Toast.makeText(context, "SEND", Toast.LENGTH_SHORT).show();
			//TODO upload images and wait for another round
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Draw on canvas with brush.
	 */
	void drawOnCanvas() {
		Log.d("drawOnCanvas:ACTION_MOVE", "downx: " + downx + 
				" upx: " + upx + " downy" + downy + " upy: " + upy + " shiftX" + shiftX
				+ " shiftY: " + shiftY);

		// canvas.drawLine(downx, downy, upx, upy, paint);
		if (x != -1 && y != -1) {
			shiftX = x - upx;
			shiftY = y - upy;
			//checkAndSetScrollShift();
			
			
			Log.d("drawOnCanvas:ACTION_MOVE2", "downx: " + downx
					+ " upx: " + upx + " downy" + downy + " upy: " + upy + " shiftX" + shiftX
					+ " shiftY: " + shiftY);
			//canvas.drawLine(x, y, upx + shiftX, upy + shiftY, paint);
			x = -1;
			y = -1;
		} else {
			canvas.drawLine(downx + shiftX, downy + shiftY, upx + shiftX, upy
					+ shiftY, paint);
		}

		ivCanvas.invalidate();
		downx = upx;
		downy = upy;
	}
	
	public boolean isUpXOk(){
	//	int tolerance = 50;
	//	if((x != -1) || (upx > (downx - (tolerance)) && upx < (downx + tolerance))){
			return true;
	//	}
	//	return false;
	}


}
