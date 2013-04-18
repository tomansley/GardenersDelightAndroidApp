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
import com.gdelight.domain.base.BaseResponseBean.STATUS_TYPE;
import com.gdelight.domain.request.SignupRequestBean;
import com.gdelight.domain.response.SignupResponseBean;
import com.gdelight.domain.user.UserBean;
import com.gdelight.request.RequestHelper;
import com.gdelight.utils.constants.Constants;
import com.gdelight.utils.string.StringHelper;
import com.nullwire.trace.ExceptionHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
public class SignupActivity extends Activity implements OnClickListener, DialogInterface.OnClickListener {

	private EditText username = null;
	private EditText password = null;
	private EditText password2 = null;
	private EditText firstName = null;
	private AlertDialog alertDialog = null;
	private SignupResponseBean responseBean = null;
	private UserBean user = null;

	public SignupActivity() {
	}

	/** Called with the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //send trace back to base to be able to track issues
        ExceptionHandler.register(this, "http://www.tomansley.com/gdelight/trace.php"); 

        // Inflate our UI from its XML layout description.
		setContentView(R.layout.signup);

		((Button) findViewById(R.id.signupSignupButton)).setOnClickListener(this);
		username = (EditText) findViewById(R.id.signupEmailEditText);
		password = (EditText) findViewById(R.id.signupPasswordEditText);
		password2 = (EditText) findViewById(R.id.signupPassword2EditText);
		firstName = (EditText) findViewById(R.id.signupFirstNameEditText);

		alertDialog = new AlertDialog.Builder(this)
	    .setTitle(R.string.signup_error)
	    .setMessage(R.string.signup_email_exists)
	    .setNegativeButton(R.string.signup_login, this)
	    .setPositiveButton(R.string.signup_resend_password, this)
	    .create();

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
			case R.id.signupSignupButton: {
				
				//1.  Check passwords are the same.
				//2.  Try create user.
				//3.  Start intent if successful
	
				if (!password.getText().toString().equals(password2.getText().toString())) {
					Toast.makeText(getApplicationContext(), R.string.signup_error_password_mismatch, Toast.LENGTH_LONG).show();
				} else if (firstName.getText().toString().length() == 0) {
					Toast.makeText(getApplicationContext(), R.string.signup_error_no_firstname, Toast.LENGTH_LONG).show();
				} else if (username.getText().toString().length() == 0) {
					Toast.makeText(getApplicationContext(), R.string.signup_error_no_valid_email, Toast.LENGTH_LONG).show();
				} else {
	
					//get the appropriate request bean and populate.
					SignupRequestBean requestBean = new SignupRequestBean();
					requestBean.setUserId(username.getText().toString());
					requestBean.setToken(StringHelper.encryptPassword(password.getText().toString()));
					requestBean.setFirstName(firstName.getText().toString());
	
					responseBean = (SignupResponseBean) RequestHelper.makeRequest(SignupActivity.this, requestBean);
					
					user = responseBean.getUser();
					
					//if error
					if (responseBean.getStatus().equals(STATUS_TYPE.FAILED)) {
						alertDialog.show();				
					
					//else go to home page for new user.
					} else {
						
						new AlertDialog.Builder(this)
					    .setTitle(R.string.signup_success_title)
					    .setMessage(R.string.signup_success_message)
					    .setNeutralButton(R.string.signup_ok, this)
					    .create().show();
						
					}
				}
				break;
			}
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		
		switch(which) {
			case DialogInterface.BUTTON_POSITIVE: {
				Intent intent = new Intent();
				intent.setClassName("com.gdelight", "com.gdelight.activity.HomePageActivity");
				startActivity(intent);
				break;
			}
			case DialogInterface.BUTTON_NEGATIVE: {
				Intent intent = new Intent();
				intent.setClassName("com.gdelight", "com.gdelight.activity.LoginActivity");
				startActivity(intent);	
				break;
			}
			case DialogInterface.BUTTON_NEUTRAL: {
				Intent intent = new Intent();
				intent.setClassName("com.gdelight", "com.gdelight.activity.HomePageActivity");
				Bundle b = new Bundle();
				b.putSerializable(Constants.USER_BEAN, user);

				intent.putExtras(b);
				startActivity(intent);
				break;
			}
		}
	}

}
