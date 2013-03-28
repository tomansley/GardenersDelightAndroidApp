package com.gdelight.utils.json;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class TestRequest extends AsyncTask<String, Integer, String> {

	private String response = "";
	private ProgressDialog dialog;
	
	public TestRequest(ProgressDialog dialog) {
		this.dialog = dialog;
	}
	
	protected void onPreExecute()
    {

    }

	protected String doInBackground(String... json) {

		try { Thread.sleep(10000); } catch (InterruptedException e) { e.printStackTrace(); }
		
		return response;
	}

	protected void onProgressUpdate(Integer... progress) {
	}
	
	protected void onPostExecute(String result) {
		dialog.dismiss();
	}

}
