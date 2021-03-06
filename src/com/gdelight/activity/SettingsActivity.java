package com.gdelight.activity;

import java.util.List;

import com.gdelight.R;
import com.gdelight.domain.user.UserBean;
import com.gdelight.utils.constants.Constants;
import com.nullwire.trace.ExceptionHandler;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {
	
	private UserBean user = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //send trace back to base to be able to track issues
        ExceptionHandler.register(this, "http://www.tomansley.com/gdelight/trace.php"); 

        //get the user
        Bundle bundle = this.getIntent().getExtras();
        user = (UserBean) bundle.getSerializable(Constants.USER_BEAN);

		// Add a button to the header list.
		//if (hasHeaders()) {
			//Button button = new Button(this);
			//button.setText("Some action");
			//setListFooter(button);
		//}
	}

	/**
	 * Populate the activity with the top-level headers.
	 */
	@Override
	public void onBuildHeaders(List<Header> target) {
		//loadHeadersFromResource(R.xml.preference_headers, target);
	}


}