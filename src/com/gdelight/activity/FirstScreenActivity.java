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
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class FirstScreenActivity extends Activity {
    
    private EditText mEditor;
    
    public FirstScreenActivity() {
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.first_screen);

        // Find the text editor view inside the layout, because we
        // want to do various programmatic things with it.
        //mEditor = (EditText) findViewById(R.id.editor);

        // Hook up button presses to the appropriate event handler.
        ((Button) findViewById(R.id.sign_in)).setOnClickListener(mSignInListener);
        ((Button) findViewById(R.id.sign_up)).setOnClickListener(mSignUpListener);
        
    }

    /**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    OnClickListener mSignInListener = new OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClassName("com.gdelight", "com.gdelight.activity.LoginActivity");
            startActivity(intent);
        }
    };

    OnClickListener mSignUpListener = new OnClickListener() {
        public void onClick(View v) {
            mEditor.setText("");
        }
    };
}
