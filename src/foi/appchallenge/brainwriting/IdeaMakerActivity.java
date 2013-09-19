package foi.appchallenge.brainwriting;

import foi.appchallenge.helpers.HSVColorPickerDialog;
import foi.appchallenge.helpers.HSVColorPickerDialog.OnColorSelectedListener;
import android.content.Context;
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
	// private Bitmap alteredBitmap; // example also had this for something

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

	// coordinates to shift the view by, work only for horizontal (hsv shift) for now :(
	// need better way to do it
	private float shiftX = 0f;
	private float shiftY = 0f;


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

		ivCanvas = (ImageView) this.findViewById(R.id.iv_canvas);

		sv = (ScrollView) findViewById(R.id.sv);
		hsv = (HorizontalScrollView) findViewById(R.id.hsv);

		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // for smaller bitmap use
														// different config
		bmp = Bitmap.createBitmap(CANVAS_PX_WIDTH, CANVAS_PX_HEIGHT, conf); // this
																			// creates
																			// a
																			// MUTABLE
																			// bitmap

		// TODO check why altered??
		// alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),
		// bmp.getConfig());

		canvas = new Canvas(bmp);

		// Fill with white
		canvas.drawColor(canvasBackgroundColorId);
		colorPicker = (ImageButton)findViewById(R.id.ib_color);
		colorPicker.setBackgroundColor(0xFF4488CC);
		brushColorId = 0xFF4488CC;
		paint = new Paint();
		paint.setColor(brushColorId);
		paint.setStrokeWidth(brushStrokeWidth);
		// paint.setDither(true);
		// paint.setStyle(Paint.Style.STROKE);
		// paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setAntiAlias(true);

		matrix = new Matrix();
		canvas.drawBitmap(bmp, matrix, paint);

		// canvas.drawBitmap(bmp, new Rect(0,0,bmp.getWidht, bmp.getheight),
		// thumbnailRectF, thumbCanvasPaint);

		// attach the canvas to the ImageView
		ivCanvas.setImageDrawable(new BitmapDrawable(getResources(), bmp));

		
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
					// Rect clipBounds_canvas = canvas.getClipBounds();
					downx = e.getX();// / 1 + clipBounds_canvas.left;
					downy = e.getY();// / 1 + clipBounds_canvas.top;

				}

				return super.onDown(e);
			}

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				// handleZoomRequest();
				Toast.makeText(
						getApplicationContext(),
						"double tap... canvas_width" + canvas.getWidth()
								+ "bmp width:" + bmp.getWidth(),
						Toast.LENGTH_LONG).show();
				return true;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				// showRoomDetails();
				return super.onSingleTapConfirmed(e);
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
						// downx = event.getX();
						// downy = event.getY();
						// won't come here as gesture detector action down is
						// stronger..
						break;
					case MotionEvent.ACTION_MOVE:
						// Rect clipBounds_canvas = canvas.getClipBounds();
						upx = event.getX();// / 1 + clipBounds_canvas.left;
						upy = event.getY();// / 1 + clipBounds_canvas.top;
						
						
						// canvas.drawLine(downx, downy, upx, upy, paint);
						canvas.drawLine(downx + shiftX, downy + shiftY, upx
								+ shiftX, upy + shiftY, paint);

						ivCanvas.invalidate();
						downx = upx;
						downy = upy;
						break;
					case MotionEvent.ACTION_UP:
						// upx = event.getX();
						// upy = event.getY();
						// canvas.drawLine(downx, downy, upx, upy, paint);
						// ivCanvas.invalidate();
						break;
					case MotionEvent.ACTION_CANCEL:
						break;
					default:
						break;
					}
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

							
							// for drawing with brush after scroll, don't really work
							// works only partially after horizontal scroll
							shiftX += dx;// moves the shifting variables
							shiftY += dy;// in the direction of the finger
											// movement
							
							

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
						// downx = event.getX();
						// downy = event.getY();
						// won't come here as gesture detector action down is
						// stronger..
						break;
					case MotionEvent.ACTION_MOVE:
						 upx = event.getX();
						 upy = event.getY();

						// canvas.drawLine(downx, downy, upx, upy, paint);
						canvas.drawLine(downx + shiftX, downy + shiftY, upx
								+ shiftX, upy + shiftY, paint);

						ivCanvas.invalidate();
						downx = upx;
						downy = upy;
						break;
					case MotionEvent.ACTION_UP:
						// upx = event.getX();
						// upy = event.getY();
						// canvas.drawLine(downx, downy, upx, upy, paint);
						// ivCanvas.invalidate();
						break;
					case MotionEvent.ACTION_CANCEL:
						break;
					default:
						break;
					}
				}

				return true; // if mod not hand
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



}
