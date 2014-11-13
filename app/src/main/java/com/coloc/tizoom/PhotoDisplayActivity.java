package com.coloc.tizoom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.OnDoubleTapListener;
import android.support.v4.view.GestureDetectorCompat;

import java.util.ArrayList;


public class PhotoDisplayActivity extends Activity implements ZoomController.PanAndZoomListener, OnGestureListener, OnDoubleTapListener {

    protected static final int REQUEST_OK = 1;
    private static final String TAG = "PhotoDisplayActivity";
    private ZoomController mZoomController;
    private ImageView mImageView;
    private GestureDetectorCompat mDetector;
    private boolean doubleTapped = false;
    private RelativeLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);
        mImageView = (ImageView) findViewById(R.id.image);
        mZoomController = new ZoomController(this);
        mLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        mDetector = new GestureDetectorCompat(this, this);
        mDetector.setOnDoubleTapListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_display, menu);
        for (int i = 1; i <= 7; i++) {
            menu.add(Menu.NONE, i, Menu.NONE, "Momo " + i);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.launchSpeech) {
            startSpeechRecognition();
        } else if (id == R.id.photoZoom) {
            mZoomController.onPlus();
        } else if (id == R.id.photoUnZoom) {
            mZoomController.onMinus();
        } else {
        	switch (id) {
        		case 1:
					mImageView.setImageResource(R.drawable.momo1);
					break;
				case 2:
					mImageView.setImageResource(R.drawable.momo2);
					break;
				case 3:
					mImageView.setImageResource(R.drawable.momo3);
					break;
				case 4:
					mImageView.setImageResource(R.drawable.momo4);
					break;
				case 5:
					mImageView.setImageResource(R.drawable.momo5);
					break;
				case 6:
					mImageView.setImageResource(R.drawable.momo6);
					break;
				case 7:
					mImageView.setImageResource(R.drawable.momo7);
					break;
				default:
					break;
        	}
        }
        return super.onOptionsItemSelected(item);
    }

    public void startSpeechRecognition() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {
            startActivityForResult(i, REQUEST_OK);
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_OK  && resultCode==RESULT_OK) {
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.i(TAG,thingsYouSaid.get(0));
            for(String thing : thingsYouSaid){
                if(thing.equalsIgnoreCase("plus"))
                    mZoomController.onVoiceCommand(true);
            }
            if(thingsYouSaid.get(0).equalsIgnoreCase("moins")){
                mZoomController.onVoiceCommand(false);
            }
        }
    }

    @Override
    public void onPanAndZoom() {
        int xOffset = mZoomController.zoomAndTranslateX(0);
        int yOffset = mZoomController.zoomAndTranslateY(0);
        float zoomFactor = mZoomController.getZoomFactor();
        Log.d(TAG, "x" + xOffset + " y" + yOffset + " z" + zoomFactor);
        Matrix matrix = new Matrix();
        matrix.postScale(zoomFactor, zoomFactor);
        matrix.postTranslate(xOffset, yOffset);
        mImageView.setImageMatrix(matrix);
        mImageView.setScaleType(ImageView.ScaleType.MATRIX);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);

        if (!this.mDetector.onTouchEvent(event)) {
            mZoomController.onTouch(mLayout, event);
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        mZoomController.onDoubleTap(doubleTapped);
        doubleTapped = !doubleTapped;
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return true;
    }
}
