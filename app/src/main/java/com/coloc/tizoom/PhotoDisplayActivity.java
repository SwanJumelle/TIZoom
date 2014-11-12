package com.coloc.tizoom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


public class PhotoDisplayActivity extends Activity implements ZoomController.PanAndZoomListener {

    protected static final int REQUEST_OK = 1;
    private static final String TAG = "PhotoDisplayActivity";
    private ZoomController mZoomController;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);
        mImageView = (ImageView) findViewById(R.id.image);
        mZoomController = new ZoomController(this);
        findViewById(R.id.mainLayout).setOnTouchListener(mZoomController);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.launchSpeech) {
            // TODO lancer la reconnaissance vocale
        }
        return super.onOptionsItemSelected(item);
    }

    public void startSpeechRecognition(View v) {
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
            // TODO si le mot prononcé est "zoom" ou "unzoom", dire au PanAndZoomController de zoomer / dézoomer.
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
}
