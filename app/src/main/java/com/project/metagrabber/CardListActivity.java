package com.project.metagrabber;

/**
 * Created by AshishKenjale on 23/4/15.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CardListActivity extends Activity implements CustomListAdapter.customButtonListener {

    private static final String TAG = "CardListActivity";

    private List<LinkPreview> linksList = new ArrayList<LinkPreview>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    public void onButtonClickListner(int position, String value) {
        Toast.makeText(CardListActivity.this, "Opening " + value,
                Toast.LENGTH_SHORT).show();

        Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
        myWebLink.setData(Uri.parse("http://"+value));
        startActivity(myWebLink);
    }

    @Override
    public void onButtonClickListner2(int position, String value) {
        Toast.makeText(CardListActivity.this, "You LIKE " + value + ".",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        listView = (ListView) findViewById(R.id.card_listView);
        adapter = new CustomListAdapter(this, linksList);
        adapter.setCustomButtonListner(CardListActivity.this);
        listView.setAdapter(adapter);

        GetWebsiteInfoTask getInfo1 = new GetWebsiteInfoTask();
        getInfo1.execute("www.google.com");

        GetWebsiteInfoTask getInfo2 = new GetWebsiteInfoTask();
        getInfo2.execute("www.imdb.com");

        GetWebsiteInfoTask getInfo3 = new GetWebsiteInfoTask();
        getInfo3.execute("www.thinkdigit.com");

    }


    private class GetWebsiteInfoTask extends AsyncTask<String, String, List<LinkPreview>> {

        private List<LinkPreview> linkItems = new ArrayList<LinkPreview>();
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CardListActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected List<LinkPreview> doInBackground(String... params) {

            try {
                Document doc = Jsoup.connect("http://"+params[0]).get();
                String title = doc.title();

                String description;

                description = doc.select("meta[name=description]").attr("content");

                if (description.isEmpty())
                {
                    description = "Description not available.";
                }

                String img_url = "http://www.google.com/s2/favicons?domain="+params[0];

                Log.e(TAG, "\nTitle: " + title + "\nDescription: "+ description + "\nImage URL: "+ img_url);

                linkItems.add(new LinkPreview(params[0], title, description, img_url));
            }
            catch(IOException e) {
                e.printStackTrace();
            }

            return linkItems;
        }

        @Override
        protected void onPostExecute(List<LinkPreview> links) {
            super.onPostExecute(links);

            progressDialog.dismiss();

            if(!links.isEmpty()){
                linksList.addAll(links);
                Toast.makeText(getApplicationContext(), "Retrieved website info. :)", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();

                for (LinkPreview lp : linksList) {
                    // START LOADING IMAGES FOR EACH LINK
                    lp.loadImage(adapter);
                }
            }
        }
    }
}
