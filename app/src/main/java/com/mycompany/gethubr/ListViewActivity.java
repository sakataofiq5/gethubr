package com.mycompany.gethubr;

import android.app.Activity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.ProgressDialog;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListViewActivity extends Activity {
	
    private static final String TAG = ListViewActivity.class.getSimpleName();
    private ListView mListView;
    private ProgressDialog dialog;
    private ListViewAdapter mListAdapter;
    private ArrayList <ListItem> mListData;
    
	//this is the url that returns our JSON objects
	private String GITHUB_API_URL = "https://api.github.com/search/users?q=location:lagos+language:java";
	
	//a method to empty the gridview for reload
	public void emptylistview() {
        mListData = new ArrayList<>();
        mListAdapter = new ListViewAdapter(this, R.layout.list_item_layout, mListData);
        mListView.setAdapter(mListAdapter);
	}
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mListView = (ListView) findViewById(R.id.listView);
     
        //Initialize with empty data
		emptylistview();
		
        //Start download
        new AsyncHttpTask().execute(GITHUB_API_URL);
        
}
	


	  //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(ListViewActivity.this);

          //While AsyncClass is running downloads
			dialog.setMessage("please wait");
			dialog.setTitle("Loading Users");
			dialog.show();
			dialog.setCancelable(false);
		}
    
		
        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    parseResult(response);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }


        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result == 1) {
                mListAdapter.setListData(mListData);
            } else {
                Toast.makeText(ListViewActivity.this, "Failed to fetch data, seems your are offline", Toast.LENGTH_SHORT).show();
            }
            dialog.cancel();
			
        }
    }

    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;
    }

    /**
     * Parsing the feed results and get the list
     * @param result
     */
    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray results = response.optJSONArray("items");
            ListItem item;
            for (int i = 0; i < results.length(); i++) {
                JSONObject post = results.optJSONObject(i);
                String userName = post.optString("login");
				String userImage = post.optString("avatar_url");
				String profileUrl = post.optString("html_url");
				
                item = new ListItem();
                item.setName(userName);
                item.setImage(userImage);
				item.setProfileUrl(profileUrl);
				
                mListData.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
		// Send Users details to next activity
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

					//Get item at position
					ListItem item = (ListItem) parent.getItemAtPosition(position);

					//Pass the user name,profile url and Picture to UserDetailsActivity
					Intent intent = new Intent(ListViewActivity.this, UserDetailActivity.class);
					intent.putExtra("login", item.getName());
					intent.putExtra("avatar_url", item.getImage());
                    intent.putExtra("html_url", item.getProfileUrl());
					
					//Start users details activity intent
					startActivity(intent);
				}
			});
		
    }
}
