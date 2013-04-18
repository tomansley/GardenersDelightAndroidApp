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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.gdelight.R;
import com.gdelight.domain.item.Item;
import com.gdelight.domain.item.ItemGroup;
import com.gdelight.domain.request.HaveAvailableRequestBean;
import com.gdelight.domain.response.HaveAvailableResponseBean;
import com.gdelight.domain.user.UserBean;
import com.gdelight.request.RequestHelper;
import com.gdelight.utils.constants.Constants;
import com.gdelight.widget.adapter.WantAutoCompleteAdapter;
import com.nullwire.trace.ExceptionHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class HaveHomeActivity extends Activity implements OnItemSelectedListener, OnItemLongClickListener, OnClickListener {
    
	private AutoCompleteTextView autoComplete = null;
	private WantAutoCompleteAdapter autoCompleteAdapter = null;
	private ArrayAdapter<String> amountSpinnerAdapter = null;
	private ArrayAdapter<String> listViewAdapter = null;
	private List<String> adapterValues = new ArrayList<String>();
	private Button addButton = null;
	private Button searchButton = null;
	private Spinner amountSpinner = null;
	private UserBean user = null;

    public HaveHomeActivity() {
    	
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
        setContentView(R.layout.have_screen);
        
        //---------------------
        //handle auto complete 
        autoComplete = (AutoCompleteTextView) findViewById(R.id.haveAutoCompleteText);
        autoCompleteAdapter = new WantAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line); 
        autoComplete.setAdapter(autoCompleteAdapter);
        
        //------------------------
        //handle amount drop down
        amountSpinner = (Spinner) findViewById(R.id.haveAmountSpinner);
        
        //item amounts
        Resources res = getResources();
        String[] itemAmounts = res.getStringArray(R.array.item_amount_array);
        
    	amountSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemAmounts);
    	amountSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	amountSpinner.setAdapter(amountSpinnerAdapter);
        
        amountSpinner.setOnItemSelectedListener(this);
        
        //---------------------
        //handle list view
        ListView listView = (ListView) findViewById(R.id.haveListView);
        listView.setOnItemLongClickListener(this);
        
        // First parameter - Context - Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written - Fourth - the Array of data
        listViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, adapterValues);

        // Assign adapter to ListView
        listView.setAdapter(listViewAdapter);
        
        //---------------------
        //handle add button click
        addButton = (Button) findViewById(R.id.haveAutoCompleteAddButton);
        addButton.setOnClickListener(this);
        
        //------------------------
        //handle search button click
        searchButton = (Button) findViewById(R.id.haveSubmitButton);
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
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
		
		new AlertDialog.Builder(this)
	    .setTitle("Delete entry")
	    .setMessage("Delete item?")
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            String item = listViewAdapter.getItem(arg2);
	            listViewAdapter.remove(item);
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
			case R.id.haveAutoCompleteAddButton: {
	            if (!adapterValues.contains(autoComplete.getText().toString())) {
	            	listViewAdapter.add(autoComplete.getText().toString().trim());
	            	autoComplete.setText("");
	            } else {
	            	Toast.makeText(getApplicationContext(), R.string.have_already_in_have_list, Toast.LENGTH_LONG).show();
	            }
				break;
			}
			case R.id.haveSubmitButton: {

				if (listViewAdapter.getCount() == 0) {
					listViewAdapter.add(autoComplete.getText().toString());
				}

				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
				
				//get the items to search on
				ItemGroup group = new ItemGroup();
				group.setLocation("Main Location");
				group.setName(user.getFirstName() + "'s Have List - " + formatter.format(new Date()));
				for (int i = 0; i < listViewAdapter.getCount(); i++) {
					Item item = new Item();
					item.setName(listViewAdapter.getItem(i));
					group.addItem(item);
				}
				
				//create the request
				HaveAvailableRequestBean request = new HaveAvailableRequestBean();
				request.setUserId(user.getEmail());
				request.setToken(user.getToken());
				request.setAvailable(group);
				
				HaveAvailableResponseBean responseBean = (HaveAvailableResponseBean) RequestHelper.makeRequest(HaveHomeActivity.this, request);
				
            	autoComplete.setText("");

            	Intent intent = new Intent();
				intent.setClassName("com.gdelight", "com.gdelight.activity.HaveMapActivity");

				Bundle b = new Bundle();
				b.putSerializable(Constants.USER_BEAN, responseBean.getUser());
				//b.putSerializable(Constants.FIND_ITEMS, (Serializable) availableItems);

				intent.putExtras(b);
				startActivity(intent);
					
				break;
			}
			default:
				throw new RuntimeException("Unknown button ID");
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
