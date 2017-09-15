package com.mycompany.gethubr;

import android.content.ContentValues;
import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.view.Menu;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Button.OnClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import com.squareup.picasso.Picasso;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.content.Intent;
import android.net.Uri;


public class UserDetailActivity extends Activity

{
   //prepare views classes 
	private TextView nameTextView;
	private TextView profileurlTextView;
	private ImageView profileImageView;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);
		
		//get needed details as strings (username, profile url and picture from the listview activity)
		final String login = getIntent().getStringExtra("login");
        final String html_url = getIntent().getStringExtra("html_url");
		String user_image = getIntent().getStringExtra("avatar_url");
		
		//use the strings to compose the.message text our share to other apps button passes
		String share_text = "Checkout this awesome developer ";
		String space = " @  ";
		final String message = share_text+space+login+html_url;
		
		//Prepare the views to be loaded
		profileImageView = (ImageView) findViewById(R.id.list_item_image);
		nameTextView = (TextView) findViewById(R.id.list_item_name);  
		profileurlTextView = (TextView) findViewById(R.id.profileurl);
		
		//the the strings into our prepared text views and load the picture with Piccasso
		nameTextView.setText(login);
		profileurlTextView.setText(html_url);
        Picasso.with(this).load(user_image).resize(100,0).transform(new CircleTransform()).into(profileImageView);
		
		//Create the button object and method to open the profile url string in a browser through a browser intent
		Button shareOnBrowser = (Button) findViewById(R.id.browser_button);
		
		shareOnBrowser.setOnClickListener(new View.OnClickListener()   {             
				public void onClick(View v)  {
					
					Intent browserIntent = new Intent(Intent.ACTION_VIEW);
					browserIntent.setData(Uri.parse(html_url));
					startActivity(browserIntent);
					}
				    
			});
	

		//Create another button object and method to share the message text we prepared earlier to other apps that can view it
		Button shareToOtherApps = (Button) findViewById(R.id.share_button);

		shareToOtherApps.setOnClickListener(new View.OnClickListener()   {             
				public void onClick(View v)  {

					Intent shareIntent = new Intent(Intent.ACTION_SEND);
					shareIntent.setType("text/plain");
					shareIntent.putExtra(Intent.EXTRA_TEXT,message);
					startActivity(Intent.createChooser(shareIntent,"Share on"));
				}

			});
		
		}
		
	}
	
