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
import com.gdelight.domain.request.SignupRequestBean;
import com.gdelight.utils.json.JsonRequest;
import com.gdelight.utils.json.JsonUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
public class SignupActivity extends Activity {

	private EditText username = null;
	private EditText password = null;
	private EditText password2 = null;
	private Activity activity = this;

	public SignupActivity() {
	}

	/** Called with the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate our UI from its XML layout description.
		setContentView(R.layout.signup);

		((Button) findViewById(R.id.signupButton)).setOnClickListener(mLoginListener);
		username = (EditText) findViewById(R.id.loginUsername);
		password = (EditText) findViewById(R.id.loginPassword);
		password2 = (EditText) findViewById(R.id.loginPassword2);

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

			//1.  Check passwords are the same.
			//2.  Try create user.
			//3.  Start intent if successful

			if (password.getText().toString().equals(password2.getText().toString())) {

				SignupRequestBean s = new SignupRequestBean();
				s.setUserId(username.getText().toString());
				s.setToken(password.getText().toString());

				String jsonDoc = JsonUtils.getJSonDocument(s);

				Log.d("", jsonDoc);

				JsonRequest request = (JsonRequest) new JsonRequest(activity).execute(jsonDoc);

				while (request.getResponse().equals("")) {
					
				}
				
				if (request.getResponse().equals("Request Error")) {
					Toast.makeText(getApplicationContext(), request.getResponse(), Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), request.getResponse(), Toast.LENGTH_LONG).show();
				}
				
				Intent intent = new Intent();
				intent.setClassName("com.gdelight", "com.gdelight.activity.HomePageActivity");
				startActivity(intent);

			} else {

				Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();

			}


		}
	};

}
