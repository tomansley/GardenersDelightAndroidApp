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
import com.gdelight.domain.request.LoginRequestBean;
import com.gdelight.domain.response.LoginResponseBean;
import com.gdelight.request.RequestHelper;
import com.gdelight.utils.constants.Constants;
import com.gdelight.utils.string.StringHelper;
import com.nullwire.trace.ExceptionHandler;

import android.app.Activity;
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
public class LoginActivity extends AbstractGDelightActivity implements OnClickListener {

	EditText username = null;
	EditText password = null;
	Activity activity = this;
	private RequestHelper requestHelper = null;

	public LoginActivity() {

	}

	/** Called with the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//send trace back to base to be able to track issues
		ExceptionHandler.register(this, "http://www.tomansley.com/gdelight/trace.php"); 

		// Inflate our UI from its XML layout description.
		setContentView(R.layout.login);

		((Button) findViewById(R.id.loginLoginButton)).setOnClickListener(this);
		username = (EditText) findViewById(R.id.loginEmailEditText);
		password = (EditText) findViewById(R.id.loginPassword);
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
		case R.id.loginLoginButton: {
			if (password.equals("") || username.equals("")) {
				Toast.makeText(getApplicationContext(), R.string.login_error_please_enter_values, Toast.LENGTH_LONG).show();
			} else {

				//get the appropriate request bean and populate.
				LoginRequestBean requestBean = new LoginRequestBean();
				requestBean.setUserId(username.getText().toString());
				requestBean.setToken(StringHelper.encryptPassword(password.getText().toString()));

				requestHelper = new RequestHelper();
				requestHelper.makeRequest(this, requestBean);

			}

			break;
		}
		default:
			throw new RuntimeException("Unknown button ID");
		}
	}

	@Override
	public void handleServerRequest() {
		
		LoginResponseBean responseBean = (LoginResponseBean) requestHelper.getResponse();
				
		//if error
		if (responseBean.getStatus().equals(STATUS_TYPE.FAILED)) {
			Toast.makeText(getApplicationContext(), R.string.login_error_invalid_credentials, Toast.LENGTH_LONG).show();

			//else go to home page for new user.
		} else {

			Intent intent = new Intent();
			intent.setClassName("com.gdelight", "com.gdelight.activity.HomePageActivity");

			Bundle b = new Bundle();
			b.putSerializable(Constants.USER_BEAN, responseBean.getUser());

			intent.putExtras(b);
			startActivity(intent);
		}
	}


}
