package com.gdelight.utils.taskmanager;

import com.gdelight.R;

import android.content.res.Resources;
import android.os.AsyncTask;

public abstract class Task extends AsyncTask<String, String, String> {

	protected final Resources mResources;
	private String mResult;
	private String mProgressMessage;
	private IProgressTracker mProgressTracker;

	/* UI Thread */
	public Task(Resources resources) {
		// Keep reference to resources
		mResources = resources;

		// Initialise initial pre-execute message
		mProgressMessage = resources.getString(R.string.task_manager_task_starting);
	}

	/* UI Thread */
	public void setProgressTracker(IProgressTracker progressTracker) {
		// Attach to progress tracker
		mProgressTracker = progressTracker;
		// Initialise progress tracker with current task state
		if (mProgressTracker != null) {
			mProgressTracker.onProgress(mProgressMessage);
			if (mResult != null) {
				mProgressTracker.onComplete();
			}
		}
	}

	/* UI Thread */
	@Override
	protected void onCancelled() {
		// Detach from progress tracker
		mProgressTracker = null;
	}

	/* UI Thread */
	@Override
	protected void onProgressUpdate(String... values) {
		// Update progress message 
		mProgressMessage = values[0];
		// And send it to progress tracker
		if (mProgressTracker != null) {
			mProgressTracker.onProgress(mProgressMessage);
		}
	}

	/* UI Thread */
	@Override
	protected void onPostExecute(String result) {
		// Update result
		mResult = result;
		// And send it to progress tracker
		if (mProgressTracker != null) {
			mProgressTracker.onComplete();
		}
		// Detach from progress tracker
		mProgressTracker = null;
	}

	/* Separate Thread */
	@Override
	abstract protected String doInBackground(String... arg0);
	abstract protected Object[] getInput();
	abstract protected Object[] getOutput();
	
}