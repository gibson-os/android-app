package de.wollis_page.gibsonos.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

@SuppressWarnings("unused")
public class DataStore {
	private HashMap<String, String> params = new HashMap<>();
	
	private boolean useCache = false;
	private long cacheTTL = 2 * 60 * 1000;
	
	final String SEPERATOR = "/";
	final String DELIMITER = "/";
	final String URL_SEPERATOR = "/";
	private static final char PARAMETER_DELIMITER = '&';
	private static final char PARAMETER_EQUALS_CHAR = '=';
	
	private int timeout = 20000;
	
	private String deviceId;
	private String url;
	
	private String module, task, action;
	
	private Context context;
	
	
	public DataStore(Context context, String url) {
		
		this.context = context;

		if (!url.endsWith("/")) {
			url += '/';
		}

		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "http://" + url;
		}

		this.url = url;
		deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		
		//HttpParams httpParameters = new BasicHttpParams();
		//HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
		//HttpConnectionParams.setSoTimeout(httpParameters, timeout);
		
		//httpClient = new DefaultHttpClient(httpParameters);
		
		//params = new ArrayList<NameValuePair>();
		
		addParam("device", deviceId);
	}
	
	public void addParamEncoded(String key, String value) {
		value = value.trim();
		try {
			value = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		params.put(key, value);
	}
	
	public void addParam(String key, String value) {
		value = value.trim();
		params.put(key, value);
	}
	
	public void addParam(String key, Float value) {
		params.put(key, value.toString());
	}
	
	public void addParam(String key, double value) {
		params.put(key, Double.valueOf(value).toString());
	}
	
	public void addParam(String key, int value) {
		params.put(key, Integer.valueOf(value).toString());
	}
	
	public void addParam(String key, boolean value) {
		params.put(key, Boolean.valueOf(value).toString());
	}
	
	public void clearParams() {
		params.clear();
	}
	
	public void removeParam(String key) {
		params.remove(key);
	}
	
	public String getData() {
	    if (isOnline()) {
	        return _execute();
	    }
	    	
	    return null;
	}
	
	private String _getParams() {
		StringBuilder result = new StringBuilder();
		if (params != null) {
			boolean first = true;
			for (String key : params.keySet()) {
				if (!first) {
					result.append(PARAMETER_DELIMITER);
	            }
				
				try {
					result.append(key).append(PARAMETER_EQUALS_CHAR).append(URLEncoder.encode(params.get(key), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				first = false;
			}
		}
		
		return result.toString();
	}
	
	private String _execute() {
		
		String requestUrl = getUrl();
		Log.i(Config.LOG_TAG, requestUrl);
		
    	try {
    		
    		URL url = new URL(requestUrl);
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		conn.addRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(timeout);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(_getParams());

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();
            StringBuilder result = new StringBuilder();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                	result.append(line);
                }
            }
            else {
            	result = new StringBuilder();
            }
            
            Log.i(Config.LOG_TAG, result.toString());
    		
    		try {
				JSONObject jsonObject = new JSONObject(result.toString());
				if (!jsonObject.getBoolean("success")) {
					JSONObject data = jsonObject.getJSONObject("data");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
    		return result.toString();
    		
        } catch (Exception e) {
    		e.printStackTrace();
		}
    	
    	return null;
	}
	
	private String getUrl() {
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
	
    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;

		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setRoute(String module, String task, String action) {
	    setModule(module);
	    setTask(task);
	    setAction(action);
    }
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public boolean getCache() {
		return useCache;
	}

	public void setCache(boolean useCache) {
		this.useCache = useCache;
	}
	
	public void setCacheTimeToLive(int ttl) {
		cacheTTL = ttl;
	}
	
}