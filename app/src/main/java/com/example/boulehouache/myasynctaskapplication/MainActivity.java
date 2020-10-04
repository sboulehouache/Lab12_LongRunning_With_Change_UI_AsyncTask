package com.example.boulehouache.myasynctaskapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static String TAG ="ThreadingAsynTask";      private ImageView mImageView;
    private ProgressBar mProgressBar;                          private int mDelay=1000;
    boolean nonStop=true;       ArrayList<Integer> arrayListimages=new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView =(ImageView)findViewById(R.id.imageView);
        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
        arrayListimages.add(new Integer(R.drawable.image1));
        arrayListimages.add(new Integer(R.drawable.image2));
        arrayListimages.add(new Integer(R.drawable.image3));
        arrayListimages.add(new Integer(R.drawable.image4));
        final Button btnStartDiaporama=(Button) findViewById(R.id.btnStartbutton);
        btnStartDiaporama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadImagesTask().execute(arrayListimages);
                Toast.makeText(MainActivity.this, "Diaporama is started", Toast.LENGTH_SHORT).show();
            }
        });
        final Button btnStopDiaporama=(Button)findViewById(R.id.btnStopButton);
        btnStopDiaporama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonStop=false;
                Toast.makeText(MainActivity.this, "Diaporama is stopped", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class LoadImagesTask extends AsyncTask<ArrayList<Integer> , Object, Bitmap>{
        @Override
        protected void onPreExecute(){
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }
        @Override
        protected Bitmap doInBackground(ArrayList<Integer> ... params) {
            ArrayList<Integer>  images = params[0];
            Bitmap tmp=null;
            while (nonStop) {  // Infinite loop
                for (int i = 0; i < images.size(); i++) {
                    sleep();
                    int resId = images.get(i);
                    tmp = BitmapFactory.decodeResource(getResources(), resId);
                    publishProgress((i +1)* 100/images.size(), tmp);
                }
            }
            nonStop=true;
            return tmp;
        }
        @Override
        protected void onProgressUpdate(Object... values){
            mProgressBar.setProgress((Integer)values[0]);
            mImageView.setImageBitmap((Bitmap)values[1]);
        }
        @Override
        protected void onPostExecute(Bitmap result){
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }
        private void sleep(){
            try{ Thread.sleep(mDelay); }
            catch(InterruptedException e){ Log.e(TAG, e.toString());  }
        }
    } // End of LoadImagesTask Class

    @Override
    protected void onPause(){
        super.onPause();
        nonStop=false;
    }
}

