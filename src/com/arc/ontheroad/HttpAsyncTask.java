package com.arc.ontheroad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;



public class HttpAsyncTask extends AsyncTask<String, Void, String> {
	
    static String addressid = "";
    static String cityid = "";
    static String villageid = "";
    static String streetid = "";
	JSONArray jsonArrayMain = null;
    
 @Override
protected String doInBackground(String... urls) 
{
   	jsonArrayMain = GET(urls[0]); 
    return "OK";
}
 
 // onPostExecute displays the results of the AsyncTask.
 @Override
protected void onPostExecute(String result) 
 {
	 
 }

private static String convertInputStreamToString(InputStream inputStream) throws IOException{
    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
    String line = "";
    String result = "";
    while((line = bufferedReader.readLine()) != null)
        result += line;

    inputStream.close();
    return result;

}

public static JSONArray  GET(String url)
{
    InputStream inputStream = null;
    String result = "";
    JSONArray jsonArray = null;
    try {

        // create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // make GET request to the given URL
        HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

        // receive response as inputStream
        inputStream = httpResponse.getEntity().getContent();

        // convert inputstream to string
        if(inputStream != null)
            result = convertInputStreamToString(inputStream);
        else
            result = "Did not work!";
    } catch (Exception e) {
        Log.d("InputStream", e.getLocalizedMessage());
    }
    
 // 讀取回應
 		try {
 			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf8"),9999999);
 			//99999為傳流大小，若資料很大，可自行調整
 			StringBuilder sb = new StringBuilder();
 			String line = null;
 			while ((line = reader.readLine()) != null) {
 				//逐行取得資料
 				sb.append(line + "\n");
 			}
 			inputStream.close();
 			result = sb.toString();
 		} catch(Exception e) {
 			e.printStackTrace();
 		}
 
 	    try {
 	    JSONObject jsnJsonObject = new JSONObject(result);
 	    JSONObject contacts = jsnJsonObject.getJSONObject("atLocation");

   
 	           
 	          cityid = contacts.getString("city");
 	          villageid = contacts.getString("village");
 	          streetid = contacts.getString("street");
 	            Log.i("Parsed data is",":"+villageid);
 	        

 	    } catch (JSONException e) {
        	Log.i("ARC","3");
 	        e.printStackTrace();
 	    }
 		//轉換文字為JSONArray
 		/*try {
 			JSONObject jsnJsonObject = new JSONObject(result);
 		} catch(JSONException e) {
 			Log.i("ARC","2C" + e.getMessage());
 			e.printStackTrace();
 		}*/

    return jsonArray;
    }
}