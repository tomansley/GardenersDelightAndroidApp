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

import com.gdelight.R;
import com.gdelight.domain.user.UserBean;
import com.gdelight.utils.constants.Constants;
import com.nullwire.trace.ExceptionHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class HomePageActivity extends Activity implements OnClickListener {
    
	private UserBean user = null;

	public HomePageActivity() {
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
        setContentView(R.layout.home_page);
        ((Button) findViewById(R.id.homeWantButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.homeHaveButton)).setOnClickListener(this);

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
				b.putSerializable(Constants.USER_BEAN, user);
				intent.putExtras(b);
		        startActivity(intent);
				break;
			}
			case R.id.homeHaveButton: {
		        Intent intent = new Intent();
		        intent.setClassName("com.gdelight", "com.gdelight.activity.HaveHomeActivity");
				Bundle b = new Bundle();
				b.putSerializable(Constants.USER_BEAN, user);
				intent.putExtras(b);
		        startActivity(intent);
				break;
			}
			default:
				throw new RuntimeException("Unknown button ID");
		}	
	}

}
