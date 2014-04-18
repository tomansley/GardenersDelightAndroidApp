/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gdelight.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdelight.R;
import com.gdelight.utils.constants.Constants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class HomePageActivity extends AbstractGDelightActivity implements OnClickListener, OnItemLongClickListener  {

	private ArrayAdapter<String> adapter = null;
	private List<String> adapterValues = new ArrayList<String>();

	public HomePageActivity() {
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //---------------------
        // handle buttons
        setContentView(R.layout.home_page);
        ((Button) findViewById(R.id.homeWantButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.homeHaveButton)).setOnClickListener(this);

        
        //*****************************************************
        // THIS IS WHERE WE GET ALL THE HAVE LISTS FOR THE USER
        // I THINK WE SHOULD HAVE THEM BEING PASSED DOWN ALREADY!
        //********************************************************
        
        
        //---------------------
        //handle list view
        ListView listView = (ListView) findViewById(R.id.homeHaveListView);
        listView.setOnItemLongClickListener(this);
        
        // First parameter - Context - Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written - Fourth - the Array of data
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, adapterValues);

        // Assign adapter to ListView
        listView.setAdapter(adapter);
        
    }

    /**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.homeWantButton: {
		        Intent intent = new Intent();
		        intent.setClassName("com.gdelight", "com.gdelight.activity.WantHomeActivity");
				Bundle b = new Bundle();
				b.putSerializable(Constants.USER_BEAN, getUser());
				intent.putExtras(b);
		        startActivity(intent);
				break;
			}
			case R.id.homeHaveButton: {
		        Intent intent = new Intent();
		        intent.setClassName("com.gdelight", "com.gdelight.activity.HaveHomeActivity");
				Bundle b = new Bundle();
				b.putSerializable(Constants.USER_BEAN, getUser());
				intent.putExtras(b);
		        startActivity(intent);
				break;
			}
			default:
				throw new RuntimeException("Unknown button ID");
		}	
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
		
		new AlertDialog.Builder(this)
	    .setTitle("Delete entry")
	    .setMessage("Delete item?")
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            String item = adapter.getItem(arg2);
	            adapter.remove(item);
	            
	            //************************************************
	            //THIS IS WHERE WE DELETE A HAVE LIST
	            //************************************************
	            
	        }
	     })
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	     .show();
		
		return false;
	}

	@Override
	public void handleServerRequest() {
		// TODO Auto-generated method stub
		
	}


}
