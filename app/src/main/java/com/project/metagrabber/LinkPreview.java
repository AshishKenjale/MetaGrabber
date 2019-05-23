package com.project.metagrabber;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by muditsaurabh on 23/4/15.
 */
public class LinkPreview {
    private String title;
    private String url;
    private String desc;
    private String img_url;
    private Bitmap image;
    private CustomListAdapter cla;

    public LinkPreview(String url, String title, String desc, String img_url) {
        this.url = url;
        this.title = title;
        this.desc = desc;
        this.img_url = img_url;
        this.image = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }




    public Bitmap getImage() {
        return image;
    }

    public CustomListAdapter getAdapter() {
        return cla;
    }

    public void setAdapter(CustomListAdapter sta) {
        this.cla = cla;
    }

    public void loadImage(CustomListAdapter cla) {
        // HOLD A REFERENCE TO THE ADAPTER
        this.cla = cla;
        Log.e("ImageLoadTask", "before if{}");
        if (img_url != null && !img_url.equals("")) {
            Log.e("ImageLoadTask", "in loadImage()");
            new ImageLoadTask().execute(img_url);
        }
    }


    private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            Log.i("ImageLoadTask", "Loading image...");
        }

        // PARAM[0] IS IMG URL
        protected Bitmap doInBackground(String... param) {
            Log.i("ImageLoadTask", "Attempting to load image URL: " + param[0]);
            try {
                Bitmap b = getBitmapFromURL(param[0]);
                return b;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onProgressUpdate(String... progress) {
            // NO OP
        }

        protected void onPostExecute(Bitmap ret) {
            if (ret != null) {
                Log.i("ImageLoadTask", "Successfully loaded " + title + " image");
                image = ret;
                if (cla != null) {
                    // WHEN IMAGE IS LOADED NOTIFY THE ADAPTER
                    cla.notifyDataSetChanged();
                }
            } else {
                Log.e("ImageLoadTask", "Failed to load " + title + " image");
            }
        }
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
