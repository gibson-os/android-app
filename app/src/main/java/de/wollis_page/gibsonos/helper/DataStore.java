package de.wollis_page.gibsonos.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Objects;

import androidx.annotation.NonNull;
import de.wollis_page.gibsonos.exception.ResponseException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressWarnings("unused")
public class DataStore
{
	private HashMap<String, String> params = new HashMap<>();
	
	private boolean useCache = false;
	private long cacheTTL = 2 * 60 * 1000;
	
	final String SEPERATOR = "/";
	final String DELIMITER = "/";
	final String URL_SEPERATOR = "/";
	private static final char PARAMETER_DELIMITER = '&';
	private static final char PARAMETER_EQUALS_CHAR = '=';
	
	private int timeout = 20000;

	private String token;
	private String url;
	
	private String module, task, action;
	
	private Context context;

	OkHttpClient client = new OkHttpClient();

	public DataStore(Context context, String url, String token)
	{
		this.context = context;

		if (!url.endsWith("/")) {
			url += '/';
		}

		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "http://" + url;
		}

		this.url = url;
		this.token = token;
	}
	
	public void addParamEncoded(String key, String value)
	{
		value = value.trim();

		try {
			value = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		params.put(key, value);
	}
	
	public void addParam(String key, String value)
	{
		value = value.trim();
		params.put(key, value);
	}
	
	public void addParam(String key, Float value)
	{
		params.put(key, value.toString());
	}
	
	public void addParam(String key, double value)
	{
		params.put(key, Double.valueOf(value).toString());
	}
	
	public void addParam(String key, int value)
	{
		params.put(key, Integer.valueOf(value).toString());
	}
	
	public void addParam(String key, boolean value)
	{
		params.put(key, Boolean.valueOf(value).toString());
	}
	
	public void clearParams()
	{
		params.clear();
	}
	
	public void removeParam(String key)
	{
		params.remove(key);
	}
	
	public JSONObject getData() throws ResponseException {
	    if (isOnline()) {
	        return _execute();
	    }
	    	
	    return null;
	}
	
	private @NonNull RequestBody _getParams() {

		if (
			this.params == null ||
			this.params.size() == 0
		) {
			return RequestBody.create("", MediaType.get("application/json; charset=utf-8"));
		}

		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

		for (String key : this.params.keySet()) {
			builder.addFormDataPart(key, Objects.requireNonNull(this.params.get(key)));
		}

		return builder.build();
	}

	private JSONObject _execute() throws ResponseException {
		String url = this.getUrl();
		Log.i(Config.LOG_TAG, url);

		Request.Builder requestBuilder = new Request.Builder()
			.url(getUrl())
			.header("X-Requested-With", "XMLHttpRequest")
			.post(this._getParams())
		;

		if (this.token.length() > 0) {
			Log.i(Config.LOG_TAG, "X-Device-Token: " + this.token);
			requestBuilder.header("X-Device-Token", this.token);
		}

		try {
			Response response = client.newCall(requestBuilder.build()).execute();
			Log.i(Config.LOG_TAG, "Response code: " + response.code());
			String body = Objects.requireNonNull(response.body()).string();
			Log.i(Config.LOG_TAG, "Response body: " + body);

			JSONObject jsonResponse = new JSONObject(body);

			if (
				(jsonResponse.has("failure") && jsonResponse.getBoolean("failure")) ||
				(jsonResponse.has("success") && !jsonResponse.getBoolean("success"))
			) {
				String message = jsonResponse.has("message")
					? jsonResponse.getString("message")
					: "Fehler bei der Abfrage!"
				;

				throw new ResponseException(message, jsonResponse, response.code());
			}

			return jsonResponse;
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
    	
    	return null;
	}
	
	private String getUrl()
	{
		String url = this.url;

		if (module != null) {
			url += module + SEPERATOR;
		}

		if (task != null) { 
			url += task + SEPERATOR;
		}

		if (action != null) {
			url += action;
		}
		
		return url;
	}
	
    private boolean isOnline()
	{
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;

		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public String getModule()
	{
		return module;
	}

	public void setModule(String module)
	{
		this.module = module;
	}

	public String getTask()
	{
		return task;
	}

	public void setTask(String task)
	{
		this.task = task;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public void setRoute(String module, String task, String action)
	{
	    setModule(module);
	    setTask(task);
	    setAction(action);
    }
	
	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	public boolean getCache()
	{
		return useCache;
	}

	public void setCache(boolean useCache)
	{
		this.useCache = useCache;
	}
	
	public void setCacheTimeToLive(int ttl)
	{
		cacheTTL = ttl;
	}
	
}