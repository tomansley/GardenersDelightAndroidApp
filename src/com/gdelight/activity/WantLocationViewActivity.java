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
import com.gdelight.request.RequestHelper;
import com.gdelight.utils.constants.Constants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class WantLocationViewActivity extends AbstractGDelightActivity implements OnItemLongClickListener, OnClickListener, DialogInterface.OnClickListener {
    
	private AutoCompleteTextView autoComplete = null;
	private ArrayAdapter<String> adapter = null;
	private List<String> adapterValues = new ArrayList<String>();
	private Button emailButton = null;
	private Button phoneButton = null;
	private RequestHelper requestHelper = null;

    public WantLocationViewActivity() {
    	
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.want_location_view_screen);
        
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
        //handle email button click
        emailButton = (Button) findViewById(R.id.wantLocationViewEmailButton);
        emailButton.setOnClickListener(this);
        
        //------------------------
        //handle phone button click
        phoneButton = (Button) findViewById(R.id.wantLocationViewPhoneButton);
        phoneButton.setOnClickListener(this);

    }

    /**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
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

				//get the items to search on
				List<Item> items = new ArrayList<Item>();
				for (int i = 0; i < adapter.getCount(); i++) {
					Item item = new Item();
					item.setName(adapter.getItem(i));
					items.add(item);
				}
				
				//create the request
				FindAvailableRequestBean requestBean = new FindAvailableRequestBean();
				requestBean.setUserId(getUser().getEmail());
				requestBean.setToken(getUser().getToken());
				requestBean.setFindItems(items);
				
				requestHelper = new RequestHelper();
				requestHelper.makeRequest(WantLocationViewActivity.this, requestBean);

				break;
			}
			default:
				throw new RuntimeException("Unknown button ID");
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleServerRequest() {
		
		FindAvailableResponseBean responseBean = (FindAvailableResponseBean) requestHelper.getResponse();
		
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
	}

}
