package foi.appchallenge.brainwriting;

import foi.appchallenge.helpers.HSVColorPickerDialog;
import foi.appchallenge.helpers.HSVColorPickerDialog.OnColorSelectedListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
	int selectedColor;

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
	private TextPaint textPaint;
	private Matrix matrix;
	private float downx = 0;
	private float downy = 0;
	private float upx = 0;
	private float upy = 0;

	// TODO create Settings for this so it can be changed
	private int canvasBackgroundColorId = 0xffffffff;
	private int brushStrokeWidth = 5;
	private int textSize = 20;
	String inputTextString = "";

	// coordinates to shift the view by
	private float shiftX = 0f;
	private float shiftY = 0f;
	
	// used for getting coordinates of tap in gestureDetector
	// (in onDown listener)
	private float x = -1; // -1 means coordinate is not OK to use
	private float y = -1;

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
				return true;
			}
		};

		actionBar.setListNavigationCallbacks(mSpinnerAdapter,
				mOnNavigationListener);

		
		shiftX = 0f;
		shiftY = 0f;
		
		ivCanvas = (ImageView) this.findViewById(R.id.iv_canvas);

		sv = (ScrollView) findViewById(R.id.sv);
		hsv = (HorizontalScrollView) findViewById(R.id.hsv);

		// for smaller bitmap use different config
		Bitmap.Config conf = Bitmap.Config.ARGB_8888;

		// this creates a MUTABLE bitmap
		bmp = Bitmap.createBitmap(CANVAS_PX_WIDTH, CANVAS_PX_HEIGHT, conf);

		canvas = new Canvas(bmp);

		// Fill with some background color
		canvas.drawColor(canvasBackgroundColorId);
		colorPicker = (ImageButton)findViewById(R.id.ib_color);
		selectedColor = 0xFF4488CC;
		colorPicker.setBackgroundColor(selectedColor);
		
		textPaint = new TextPaint();
		textPaint.setColor(selectedColor);
		textPaint.setTextSize(textSize);
		
		paint = new Paint();
		// a lot of options
		paint.setTextSize(20);
		paint.setColor(selectedColor);
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

		
		colorPicker.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HSVColorPickerDialog cpd = new HSVColorPickerDialog( IdeaMakerActivity.this, selectedColor, new OnColorSelectedListener() {
					
					public void colorSelected(Integer color) {
						colorPicker.setBackgroundColor(color);
						selectedColor = color;
						paint.setColor(selectedColor);
						textPaint.setColor(selectedColor);
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
							paint.setColor(selectedColor);
							paint.setStrokeWidth(brushStrokeWidth);
							break;
						case R.id.rb_text:
							textPaint.setColor(selectedColor);
							break;
						case R.id.rb_eraser:
							paint.setColor(Color.WHITE);
							paint.setStrokeWidth(brushStrokeWidth*5);
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
				if (rgDrawOptions.getCheckedRadioButtonId() == R.id.rb_brush || rgDrawOptions.getCheckedRadioButtonId() == R.id.rb_eraser) {
					x = e.getX();
					y = e.getY();
					canvas.drawPoint(x, y, paint);
					ivCanvas.invalidate();
					//Log.d("gestureDetector:onDown", "x: " + x + " y:" + y);
				}  else if (rgDrawOptions.getCheckedRadioButtonId() == R.id.rb_text){
					x = e.getX();
					y = e.getY();
					showDialogInputText(x,y);

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
				} else if (rgDrawOptions.getCheckedRadioButtonId() == R.id.rb_brush || rgDrawOptions.getCheckedRadioButtonId() == R.id.rb_eraser) {
					int action = event.getAction();
					switch (action) {
					case MotionEvent.ACTION_DOWN:
						break;
					case MotionEvent.ACTION_MOVE:
						float[] coords= getRelativeCoords((Activity) context, event);
				        
						upx = coords[0];//event.getX();
						upy = coords[1]; //event.getY();

						
							drawOnCanvas();
						
						

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
				} else if (rgDrawOptions.getCheckedRadioButtonId() == R.id.rb_brush || rgDrawOptions.getCheckedRadioButtonId() == R.id.rb_eraser) {
					int action = event.getAction();
					switch (action) {
					case MotionEvent.ACTION_DOWN:
						
						break;
					case MotionEvent.ACTION_MOVE:
						
						float[] coords= getRelativeCoords((Activity) context, event);
				        
						upx = coords[0];
						upy = coords[1]; 

							drawOnCanvas();
						
						break;
					case MotionEvent.ACTION_UP:
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
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void showDialogInputText(final float xPath, final float yPath){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_text_input, null);
		alert.setView(dialogView);
		final EditText textInput = (EditText)dialogView.findViewById(R.id.et_text_input);

		alert.setTitle(context.getResources().getString(R.string.dialog_input_text_title));
		alert.setPositiveButton(context.getResources().getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						inputTextString = textInput.getText().toString();
						
						StaticLayout mTextLayout = new StaticLayout(inputTextString, textPaint, canvas.getWidth(), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
						canvas.save();
						canvas.translate(xPath, yPath);
						mTextLayout.draw(canvas);
						canvas.restore();
		
						//canvas.drawText(inputTextString,xPath, yPath, paint);
						ivCanvas.invalidate();
					}
				});
		alert.setNegativeButton(
				context.getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Toast.makeText(context, "CANCEL", Toast.LENGTH_SHORT)
						.show();
					}
				});
		alert.show();
	}
	
	/**
	 * Draw on canvas with brush.
	 */
	void drawOnCanvas() {
		Log.d("drawOnCanvas:ACTION_MOVE", "downx: " + downx + 
				" upx: " + upx + " downy" + downy + " upy: " + upy + " shiftX" + shiftX
				+ " shiftY: " + shiftY);

		if (x != -1 && y != -1) {
			shiftX = x - upx;
			shiftY = y - upy;
			
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
	
	/**
	 * Used to get relative coordinates of view.
	 * @param activity
	 * @param e
	 * @return X and Y coordinates
	 */
	public static float[] getRelativeCoords(Activity activity, 
		    MotionEvent e){
		    // MapView
		    View contentView= activity.getWindow().
		        findViewById(Window.ID_ANDROID_CONTENT);
		    return new float[] {
		        e.getRawX() - contentView.getLeft(),
		        e.getRawY() - contentView.getTop()};
		}


}
