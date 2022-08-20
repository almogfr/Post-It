package com.example.postit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

    private String host;
    private ImageView imageView;
    private ProgressBar progress;

    public ImageDownloader(String host, ImageView imageView) {
        this.host = host;
        this.imageView = imageView;
    }

    public ImageDownloader(String host, ImageView imageView, ProgressBar progress) {
        this.host = host;
        this.imageView = imageView;
        this.progress = progress;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(host);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            return bitmap;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            if(progress != null) {
                progress.setVisibility(View.GONE);
            }
        }
    }

}