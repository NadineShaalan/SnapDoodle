package long1.shaalan.snapdoodle;

import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;



public class MainActivity extends Activity implements OnClickListener {
	
	private DrawingView drawView;
	private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, picBtn;
	private float smallBrush, mediumBrush, largeBrush;
	static final int REQUEST_IMAGE_CAPTURE = 1;

	
	//CAMERA THINGS
	//private static String logtag = "CameraApp3";
    // tells us which camera to take a picture from
    //private static int TAKE_PICTURE = 1;
    // empty variable to hold our image Uri once we store it
  //  private Uri imageUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		drawView = (DrawingView)findViewById(R.id.drawing);
		LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
		currPaint = (ImageButton)paintLayout.getChildAt(0);
		currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
		
		
		//instantiate brush sizes
		smallBrush = getResources().getInteger(R.integer.small_size);
		mediumBrush = getResources().getInteger(R.integer.medium_size);
		largeBrush = getResources().getInteger(R.integer.large_size);
		
		//retrieve button reference from layout
		drawBtn = (ImageButton)findViewById(R.id.draw_btn);
		
		drawBtn.setOnClickListener(this);
		
		drawView.setBrushSize(mediumBrush);
		
		eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
		eraseBtn.setOnClickListener(this);
		
		newBtn = (ImageButton)findViewById(R.id.new_btn);
		newBtn.setOnClickListener(this);
		
		saveBtn = (ImageButton)findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(this);
		
		//picBtn = (ImageButton)findViewById(R.id.pic_btn);
		//picBtn.setOnClickListener(this);
		
		 // look for the button we set in the view
        picBtn = (ImageButton)findViewById(R.id.pic_btn);
        // set a listener on the button         
      //image button for the camera button
        picBtn.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
        openCamera();
        }
        });
	}
	
	//for the camara to be run 
	public void openCamera()
	{
	//create a new intent object that runs the camera
	//Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	//use the intent to send it over
	  // startActivityForResult(intent, 0);
	   
	   Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    } 
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
	super.onActivityResult(requestCode, resultCode, data);
	//create a bitmap, from the information inside the intent
		Bitmap bp = (Bitmap) data.getExtras().get("data");
	//send the bitmap over to the custom view 
		drawView.cameraChange(bp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void paintClicked(View view){
	    //use chosen color
		drawView.setErase(false);
		drawView.setBrushSize(drawView.getLastBrushSize());
		if(view!=currPaint){
			//update color
			ImageButton imgView = (ImageButton)view;
			String color = view.getTag().toString();
			drawView.setColor(color);
			imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
			currPaint=(ImageButton)view;
			}
	}
	
	@Override
	public void onClick(View view){
	//respond to clicks  
		if(view.getId()==R.id.draw_btn){
		    //draw button clicked
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Brush size:");
			brushDialog.setContentView(R.layout.brush_chooser);
			
			ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setBrushSize(smallBrush);
			        drawView.setLastBrushSize(smallBrush);
			        drawView.setErase(false);
			        brushDialog.dismiss();
			    }
			});
			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setBrushSize(mediumBrush);
			        drawView.setLastBrushSize(mediumBrush);
			        drawView.setErase(false);
			        brushDialog.dismiss();
			    }
			});
			 
			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setBrushSize(largeBrush);
			        drawView.setLastBrushSize(largeBrush);
			        drawView.setErase(false);
			        brushDialog.dismiss();
			    }
			});
			
			brushDialog.show();
		}
		else if(view.getId()==R.id.erase_btn){
		    //switch to erase - choose size
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Eraser size:");
			brushDialog.setContentView(R.layout.brush_chooser);
			
			ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setErase(true);
			        drawView.setBrushSize(smallBrush);
			        brushDialog.dismiss();
			    }
			});
			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setErase(true);
			        drawView.setBrushSize(mediumBrush);
			        brushDialog.dismiss();
			    }
			});
			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener(){
			    @Override
			    public void onClick(View v) {
			        drawView.setErase(true);
			        drawView.setBrushSize(largeBrush);
			        
			        brushDialog.dismiss();
			    }
			});
			
			brushDialog.show();
		}
		else if(view.getId()==R.id.new_btn){
		    //new button
			AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
			newDialog.setTitle("New drawing");
			newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
			newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			        drawView.startNew();
			        dialog.dismiss();
			    }
			});
			newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			        dialog.cancel();
			    }
			});
			newDialog.show();
		}
		else if(view.getId()==R.id.save_btn){
            //save drawing
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
			saveDialog.setTitle("Save drawing");
			saveDialog.setMessage("Save drawing to device Gallery?");
			saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			        //save drawing
			    	drawView.setDrawingCacheEnabled(true);
			    	String imgSaved = MediaStore.Images.Media.insertImage(
			    		    getContentResolver(), drawView.getDrawingCache(),
			    		    UUID.randomUUID().toString()+".png", "drawing");
			    	if(imgSaved!=null){
			    	    Toast savedToast = Toast.makeText(getApplicationContext(), 
			    	        "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
			    	    savedToast.show();
			    	}
			    	else{
			    	    Toast unsavedToast = Toast.makeText(getApplicationContext(), 
			    	        "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
			    	    unsavedToast.show();
			    	}
			    	drawView.destroyDrawingCache();
			    }
			});
			saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which){
			        dialog.cancel();
			    }
			});
			saveDialog.show();
		}
		// If Picture Button is clicked
		else if(view.getId()==R.id.pic_btn){
			//
			
		}
		
		
	}
}
