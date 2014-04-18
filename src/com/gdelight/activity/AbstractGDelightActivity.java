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

import com.gdelight.domain.user.UserBean;
import com.gdelight.utils.constants.Constants;
import com.nullwire.trace.ExceptionHandler;

import android.app.Activity;
import android.os.Bundle;

public abstract class AbstractGDelightActivity extends Activity {
    
	private UserBean user = null;

	public AbstractGDelightActivity() {
    }

	public abstract void handleServerRequest();

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //send trace back to base to be able to track issues
        ExceptionHandler.register(this, "http://www.tomansley.com/gdelight/trace.php"); 
        
        //get the user
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && bundle.getSerializable(Constants.USER_BEAN) != null)
        	setUser((UserBean) bundle.getSerializable(Constants.USER_BEAN));
        
    }

	public final UserBean getUser() {
		return user;
	}

	public final void setUser(UserBean user) {
		this.user = user;
	}

}
