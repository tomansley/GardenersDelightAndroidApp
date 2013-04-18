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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gdelight.R;
import com.gdelight.domain.item.Item;
import com.gdelight.domain.item.ItemGroup;
import com.gdelight.domain.request.FindAvailableRequestBean;
import com.gdelight.domain.response.FindAvailableResponseBean;
import com.gdelight.domain.user.UserBean;
import com.gdelight.request.RequestHelper;
import com.gdelight.utils.constants.Constants;
import com.gdelight.utils.location.LocationHelper;
import com.gdelight.widget.adapter.WantAutoCompleteAdapter;
import com.nullwire.trace.ExceptionHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class WantHomeActivity extends Activity implements OnSeekBarChangeListener, OnItemLongClickListener, OnClickListener, LocationListener, DialogInterface.OnClickListener {
    
	private AutoCompleteTextView autoComplete = null;
	private WantAutoCompleteAdapter autoCompleteAdapter = null;
	private TextView seekBarValue = null;
	private ArrayAdapter<String> adapter = null;
	private List<String> adapterValues = new ArrayList<String>();
	private Button addButton = null;
	private Button searchButton = null;
	private SeekBar seekBar = null;
	private UserBean user = null;

    public WantHomeActivity() {
    	
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //send trace back to base to be able to track issues
        ExceptionHandler.register(this, "http://www.tomansley.com/gdelight/trace.php"); 

        //get the user
        Bundle bundle = this.getIntent().getExtras();
        user = (UserBean) bundle.getSerializable(Constants.USER_BEAN);
        
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.want_screen);
        
        //---------------------
        //handle auto complete 
        autoComplete = (AutoCompleteTextView) findViewById(R.id.wantAutoCompleteText);
        autoCompleteAdapter = new WantAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line); 
        autoComplete.setAdapter(autoCompleteAdapter);
        
        //---------------------
        //handle seek bar
        seekBar = (SeekBar) findViewById(R.id.wantDrivingDistanceSeekBar);
        seekBar.setMax(100);
        seekBar.setProgress(2);
        seekBar.setOnSeekBarChangeListener(this);

        seekBarValue = (TextView) findViewById(R.id.wantDrivingDistanceValueTextView);
        seekBarValue.setText("2");
        
        //---------------------
        //handle list view
        ListView listView = (ListView) findViewById(R.id.wantListView);
        listView.setOnItemLongClickListener(this);
        
        // First parameter - Context - Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written - Fourth - the Array of data
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, adapterValues);

        // Assign adapter to ListView
        listView.setAdapter(adapter);
        
        //---------------------
        //handle add button click
        addButton = (Button) findViewById(R.id.wantAutoCompleteAddButton);
        addButton.setOnClickListener(this);
        
        //------------------------
        //handle search button click
        searchButton = (Button) findViewById(R.id.wantSubmitButton);
        searchButton.setOnClickListener(this);

    }

    /**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (progress != 100)
			seekBarValue.setText(String.valueOf(progress));
		else
			seekBarValue.setText("Unlimited");
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
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
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.wantAutoCompleteAddButton: {
	            if (!adapterValues.contains(autoComplete.getText().toString())) {
	            	adapter.add(autoComplete.getText().toString().trim());
	            	autoComplete.setText("");
	            } else {
	            	Toast.makeText(getApplicationContext(), R.string.want_already_in_shopping_list, Toast.LENGTH_LONG).show();
	            }
				break;
			}
			case R.id.wantSubmitButton: {

				if (adapter.getCount() == 0) {
					adapter.add(autoComplete.getText().toString());
				}

				//get the current location
				Location location = LocationHelper.getCurrentLocation(this, this);
				
				if (location != null) {
									
					double currentLatitude = location.getLatitude();
					double currentLongitude = location.getLongitude();
	
					//get the items to search on
					List<Item> items = new ArrayList<Item>();
					for (int i = 0; i < adapter.getCount(); i++) {
						Item item = new Item();
						item.setName(adapter.getItem(i));
						items.add(item);
					}
					
					//create the request
					FindAvailableRequestBean request = new FindAvailableRequestBean();
					request.setLatitude(currentLatitude);
					request.setLongitude(currentLongitude);
					request.setUserId(user.getEmail());
					request.setToken(user.getToken());
					request.setRadius(seekBar.getProgress());
					request.setFindItems(items);
					
					FindAvailableResponseBean responseBean = (FindAvailableResponseBean) RequestHelper.makeRequest(WantHomeActivity.this, request);
					
					List<ItemGroup> availableItems = responseBean.getItems();
					
					if (availableItems.size() > 0) {
					
		            	autoComplete.setText("");
	
		            	Intent intent = new Intent();
						intent.setClassName("com.gdelight", "com.gdelight.activity.WantMapActivity");
		
						Bundle b = new Bundle();
						b.putSerializable(Constants.USER_BEAN, responseBean.getUser());
						b.putSerializable(Constants.FIND_ITEMS, (Serializable) availableItems);
		
						intent.putExtras(b);
						startActivity(intent);
						
					} else {
						
						new AlertDialog.Builder(this)
					    .setTitle(R.string.want_find_no_results)
					    .setMessage(R.string.want_find_no_results_message)
					    .setPositiveButton(R.string.want_ok, this)
					    .create().show();
	
					}
				
				} else {
					Toast.makeText(getApplicationContext(), R.string.want_no_gps_coordinates, Toast.LENGTH_LONG).show();
				}

				break;
			}
			default:
				throw new RuntimeException("Unknown button ID");
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}

}
