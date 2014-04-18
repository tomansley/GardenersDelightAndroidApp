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
import com.gdelight.request.RequestHelper;
import com.gdelight.utils.constants.Constants;
import com.gdelight.widget.adapter.WantAutoCompleteAdapter;

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

public class HaveHomeActivity extends AbstractGDelightActivity implements OnItemSelectedListener, OnItemLongClickListener, OnClickListener {
    
	private AutoCompleteTextView autoComplete = null;
	private WantAutoCompleteAdapter autoCompleteAdapter = null;
	private ArrayAdapter<String> amountSpinnerAdapter = null;
	private ArrayAdapter<String> listViewAdapter = null;
	private List<String> adapterDisplayValues = new ArrayList<String>();
	private List<String> adapterItemValues = new ArrayList<String>();
	private List<String> adapterAmountValues = new ArrayList<String>();
	private Button addButton = null;
	private Button searchButton = null;
	private Spinner amountSpinner = null;
	private RequestHelper requestHelper = null;

    public HaveHomeActivity() {
    	
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
        listViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, adapterDisplayValues);

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
	            adapterItemValues.remove(which);
	            adapterAmountValues.remove(which);
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
	            if (!adapterItemValues.contains(autoComplete.getText().toString())) {
	            	
	            	adapterItemValues.add(autoComplete.getText().toString());
	            	adapterAmountValues.add(amountSpinner.getSelectedItem().toString());
	            	
	            	listViewAdapter.add(autoComplete.getText().toString().trim() + " - " + amountSpinner.getSelectedItem().toString());
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
				group.setName(getUser().getFirstName() + "'s Have List - " + formatter.format(new Date()));
				for (int i = 0; i < adapterItemValues.size(); i++) {
					Item item = new Item();
					item.setName(adapterItemValues.get(i));
					item.setAmount(adapterAmountValues.get(i));
					group.addItem(item);
				}
				
				//create the request
				HaveAvailableRequestBean requestBean = new HaveAvailableRequestBean();
				requestBean.setUserId(getUser().getEmail());
				requestBean.setToken(getUser().getToken());
				requestBean.setAvailable(group);
				
				requestHelper = new RequestHelper();
				requestHelper.makeRequest(HaveHomeActivity.this, requestBean);

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

	@Override
	public void handleServerRequest() {
						
		HaveAvailableResponseBean responseBean = (HaveAvailableResponseBean) requestHelper.getResponse();
		
    	autoComplete.setText("");

    	Intent intent = new Intent();
		intent.setClassName("com.gdelight", "com.gdelight.activity.HomePageActivity");

		Bundle b = new Bundle();
		b.putSerializable(Constants.USER_BEAN, responseBean.getUser());

		intent.putExtras(b);
		startActivity(intent);

		Toast.makeText(getApplicationContext(), R.string.have_success, Toast.LENGTH_LONG).show();

	}


}
