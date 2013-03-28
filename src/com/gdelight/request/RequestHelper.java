package com.gdelight.request;

import android.app.Activity;
import android.util.Log;

import com.gdelight.domain.base.BaseRequestBean;
import com.gdelight.domain.base.BaseResponseBean;
import com.gdelight.domain.response.FindAvailableResponseBean;
import com.gdelight.domain.response.LoginResponseBean;
import com.gdelight.domain.response.SignupResponseBean;
import com.gdelight.utils.json.JsonRequest;
import com.gdelight.utils.json.JsonUtils;

public class RequestHelper {

	public static BaseResponseBean makeRequest(Activity activity, BaseRequestBean request) {
		Log.d("RequestHelper.makeRequest", "Starting method");
		
		//get the Json from the request bean
		String jsonDoc = JsonUtils.getJSonDocument(request);
		Log.d("", "REQUEST - " + jsonDoc);

		//make the request
		JsonRequest jsonRequest = (JsonRequest) new JsonRequest(activity).execute(jsonDoc);

		String responseStr = "";
		try {
			responseStr = jsonRequest.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		BaseResponseBean response = null;
		
		if (responseStr.contains("\"transactionType\":\"LOGIN\"")) {
			response = (LoginResponseBean) JsonUtils.parseJSonDocument(responseStr, LoginResponseBean.class);
		} else if (responseStr.contains("\"transactionType\":\"SIGNUP\"")) {
			response = (SignupResponseBean) JsonUtils.parseJSonDocument(responseStr, SignupResponseBean.class);
		} else if (responseStr.contains("\"transactionType\":\"FIND_AVAILABLE\"")) {
			response = (FindAvailableResponseBean) JsonUtils.parseJSonDocument(responseStr, FindAvailableResponseBean.class);
		} else if (responseStr.equals("")) {
			
		}

		Log.d("RequestHelper.makeRequest", "Ending method");
		return response;
	}
	
}