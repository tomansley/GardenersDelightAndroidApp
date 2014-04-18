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
import com.nullwire.trace.ExceptionHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class ForgotLoginActivity extends Activity implements OnClickListener {
    
    public ForgotLoginActivity() {
    	
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //send trace back to base to be able to track issues
        ExceptionHandler.register(this, "http://www.tomansley.com/gdelight/trace.php"); 

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.forgot_login);

        ((Button) findViewById(R.id.forgotLoginSendPasswordButton)).setOnClickListener(this);

    }

    /**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    OnClickListener mLoginListener = new OnClickListener() {
        public void onClick(View v) {
        	
        	
        }
    };

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.forgotLoginSigninButton: {
			Toast.makeText(getApplicationContext(), R.string.forgot_login_signin_button, Toast.LENGTH_LONG).show();
			break;
		}
		case R.id.forgotLoginSendPasswordButton: {
			Toast.makeText(getApplicationContext(), R.string.forgot_login_send_password_button, Toast.LENGTH_LONG).show();
			break;
		}
		default:
			throw new RuntimeException("Unknow button ID");
	}
	}


}
