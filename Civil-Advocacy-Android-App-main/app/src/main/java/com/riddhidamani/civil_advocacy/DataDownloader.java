package com.riddhidamani.civil_advocacy;

import android.net.Uri;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class DataDownloader implements Runnable {

    private MainActivity mainActivity;
    private String location;
    private static final String TAG = "DataDownloader";
    public static HashMap<String, String> officialMapping = new HashMap<>();
    private static final String API_URL = "";

    public DataDownloader(MainActivity mainActivity, String location) {
        this.mainActivity = mainActivity;
        this.location = location;
    }

    @Override
    public void run() {
        Uri.Builder uriBuilder = Uri.parse(API_URL + location).buildUpon();
        String urlToUse = uriBuilder.toString();
        Log.d(TAG, "run: " + urlToUse);

        StringBuilder stringBuilder = new StringBuilder();

        try{
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTPResponseCode is not ok" + conn.getResponseCode());
                return;
            }
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while( (line = reader.readLine()) != null ) {
                stringBuilder.append(line).append("\n");
            }
            Log.d(TAG, "run: "+ stringBuilder.toString());

        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

        process(stringBuilder.toString());
    }

    private void process(String str){
        try
        {
            JSONObject officialsObj = new JSONObject(str);
            JSONObject normalizedInput = officialsObj.getJSONObject("normalizedInput");
            String city = normalizedInput.getString("city");
            String state = normalizedInput.getString("state");
            String zip = normalizedInput.getString("zip");
            Office newOffice;

            JSONArray offices = officialsObj.getJSONArray("offices");
            JSONArray officials =  officialsObj.getJSONArray("officials");

            for(int i = 0; i < offices.length(); i++) {

                JSONObject office = (JSONObject) offices.get(i);
                final String officeName = office.getString("name");
                JSONArray officialIndices =  office.getJSONArray("officialIndices");

                for(int j = 0; j < officialIndices.length(); j++) {
                    int index = Integer.parseInt(officialIndices.get(j).toString());

                    JSONObject officialDetails = (JSONObject) officials.get(index);
                    final String officialName = officialDetails.getString("name");
                    newOffice = new Office(officeName, officialName);

                    if(officialDetails.has("address")) {
                        JSONArray address = officialDetails.getJSONArray("address");
                        JSONObject startAddress = (JSONObject) address.get(0);
                        String line1 = "";
                        String line2 = "";
                        String cityLine = "";
                        String stateLine = "";
                        String zipLine = "";
                        if(startAddress.has("line1")){
                            line1 = startAddress.getString("line1");
                        }
                        if(startAddress.has("line2")) {
                            line2 = startAddress.getString("line2");
                        }
                        if(startAddress.has("city")) {
                            cityLine = startAddress.getString("city");
                        }
                        if(startAddress.has("state")) {
                            stateLine = startAddress.getString("state");
                        }
                        if(startAddress.has("zip")) {
                            zipLine = startAddress.getString("zip");
                        }

                        String line1F = line1 + line2;
                        String line2F = cityLine + ", " + stateLine + " " + zipLine;
                        String completeAddress = line1F + ", " + line2F;
                        newOffice.setAddress(completeAddress);
                    }

                    if(officialDetails.has("party")) {
                        String party = officialDetails.getString("party");
                        newOffice.setParty(party);
                    }

                    if(officialDetails.has("phones")) {
                        JSONArray phoneArr = officialDetails.getJSONArray("phones");
                        String phoneNum = (String) phoneArr.get(0);
                        newOffice.setPhoneNum(phoneNum);
                    }

                    if(officialDetails.has("urls")){
                        JSONArray urlArr = officialDetails.getJSONArray("urls");
                        String url = (String) urlArr.get(0);
                        newOffice.setWebURL(url);
                    }


                    if(officialDetails.has("emails")){
                        JSONArray emailArr = officialDetails.getJSONArray("emails");
                        String email = (String) emailArr.get(0);
                        newOffice.setEmailID(email);
                    }

                    if(officialDetails.has("photoUrl")){
                        String photoUrl = officialDetails.getString("photoUrl");
                        newOffice.setPhotoURL(photoUrl);
                    }

                    if(officialDetails.has("channels")){
                        JSONArray channelArr = officialDetails.getJSONArray("channels");
                        for(int z = 0; z < channelArr.length(); z++){
                            JSONObject channel = (JSONObject) channelArr.get(z);
                            String channelType = channel.getString("type");
                            String channelId = channel.getString("id");
                            switch (channelType){
                                case "Facebook":
                                    newOffice.setFbID(channelId);
                                    break;
                                case "Twitter":
                                    newOffice.setTwitterID(channelId);
                                    break;
                                case "YouTube":
                                    newOffice.setYoutubeID(channelId);
                                    break;
                                default:
                                    // error message
                                    Log.d(TAG, "process: Not Appropriate Channel Type");
                            }
                        }
                    }

                    final Office newOfficeF = newOffice;
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.addOfficialData(newOfficeF);
                            String str = officeName + " " + officialName;
                            Log.d(TAG, "run: Official Added Complete  " + str);
                        }
                    });
                }
            }

        }
        catch(Exception exception) {
            Log.d(TAG, "process: In the process exception");
            exception.printStackTrace();
        }
    }
}
