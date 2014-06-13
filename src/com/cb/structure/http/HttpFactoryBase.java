package com.cb.structure.http;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public abstract class HttpFactoryBase<T> {

	private HttpDownloadTask task;
	private HttpEventHandler<T> httpEventHandler;

	private static final String USER_AGENT = "Android/1.0";
	private static final int HTTP_TIMEOUT_MS = 15000;

	private static final String HTTP_TIMEOUT = "http.connection-manager.timeout";
	private AndroidHttpClient mHttpClient;

	public void setHttpEventHandler(HttpEventHandler<T> httpEventHandler) {
		this.httpEventHandler = httpEventHandler;
	}

	public void DownloaDatas(Object... args) {
		cancel();
		task = new HttpDownloadTask();
		task.execute(args);

	}

	public void cancel() {
		if (task != null) {
			task.cancel(true);
			task.abort();
			task = null;
		}
	}

	protected int getConnectTimeout() {
		return 15000;
	}

	protected ArrayList<NameValuePair> getPostArgs(Object... args) {
		return null;
	}

	protected abstract String CreateUri(Object... args);

	protected abstract T AnalysisContent(String responseContent) throws IOException;

	private class HttpDownloadTask extends AsyncTask<Object, Integer, T> {

		public void abort() {
			if (mHttpClient != null) {
				mHttpClient.close();
			}
		}

		@Override
		protected T doInBackground(Object... params) {
			if (isCancelled()) {
				return null;
			}
			try {
				mHttpClient = AndroidHttpClient.newInstance(USER_AGENT);
				HttpParams httpParams = mHttpClient.getParams();
				httpParams.setLongParameter(HTTP_TIMEOUT, HTTP_TIMEOUT_MS);
				String uri = CreateUri(params);
				Log.e("httpfactorybase uri", uri);
				ArrayList<NameValuePair> nameValuePairs = getPostArgs(params);
				HttpUriRequest request;
				if (nameValuePairs != null) {
					HttpPost httpPost = new HttpPost(uri);
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
					request = httpPost;
				} else {
					HttpGet httpGet = new HttpGet(uri);
					request = httpGet;
				}

				HttpResponse response = mHttpClient.execute(request);
				if (response.getStatusLine().getStatusCode() == 200) {
					String responseContent = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
					Log.e("post response", responseContent);
				    return AnalysisContent(responseContent);
				} else {
					return null;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(T result) {
			if (isCancelled()) {
				return;
			}
			if (result == null) {
				if (httpEventHandler != null) {
					httpEventHandler.HttpFailHandler();
				}
			} else {
				if (httpEventHandler != null) {
					httpEventHandler.HttpSucessHandler(result);
				}
			}
		}
	}
}
