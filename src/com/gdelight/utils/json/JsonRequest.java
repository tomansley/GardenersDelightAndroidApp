package com.gdelight.utils.json;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Scanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class JsonRequest extends AsyncTask<String, Integer, String> {

	private static String contentType = "text/xml; charset=ISO-8859-1";
	private String response = "";
	private Activity activity = null;
	private ProgressDialog progressDialog;
	
	public JsonRequest(Activity activity) {
		this.activity = activity;
	}
	
	protected void onPreExecute()
    {
        progressDialog = ProgressDialog.show(activity, "", "Loading...");
        progressDialog.show();
    }

	protected String doInBackground(String... json) {

		try {

			URL url = new URL("http://gdelight.elasticbeanstalk.com/services/postService/");

			//you need to encode ONLY the values of the parameters
			String param="json=" + URLEncoder.encode(json[0],"UTF-8");

			Log.d("", param);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			//set the output to true, indicating you are outputting(uploading) POST data
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");

			//Android documentation suggested that you set the length of the data you are sending to the server, BUT
			// do NOT specify this length in the header by using conn.setRequestProperty("Content-Length", length);
			//use this instead.
			conn.setFixedLengthStreamingMode(param.getBytes().length);
			conn.setRequestProperty("Content-Type", contentType);   
			//send the POST out
			PrintWriter out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.close();

			//start listening to the stream
			Scanner inStream = new Scanner(conn.getInputStream());

			//process the stream and store it in StringBuilder
			while(inStream.hasNextLine()) {
				response = response + inStream.nextLine();
			}

			Log.d("", "RESPONSE BEFORE DECODING - " + response);

			//catch some error
		} catch(Exception ex){
			ex.printStackTrace();
			response = "Request Error";
		}

		response = URLDecoder.decode(response);

		Log.d("", "RESPONSE AFTER DECODING - " + response);
		
		return response;
	}

	protected void onProgressUpdate(Integer... progress) {
		if (progress.length > 0) {
			progressDialog.setProgress(progress[0]);
		}
	}
	
	protected void onPostExecute(String result) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
        }
	}

}
