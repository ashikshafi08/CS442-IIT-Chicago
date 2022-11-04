package com.example.testvoteapp;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataDownloader {

    private static final String TAG = "DataDownloader";

    private MainActivity mainActivity;
    private static RequestQueue newqueue;
    private static Civic civicObj;
    public static String location = "Denver, CO";

    private static final ArrayList<Civic> civicDataArrayList = new ArrayList<>();

    // API Key and the static URL
    private static final String civicUrl = "https://www.googleapis.com/civicinfo/v2/representatives";
    private static final String apiKey = "AIzaSyAA2ljMGIWfAYK9XEiTz4ZuiDC_PvWeaCM";

    public static void MainCivicDataDownloader(MainActivity mainActivity , String city){

        // Initialize the Volley
        RequestQueue queue = Volley.newRequestQueue(mainActivity);

        // Building the URL
        String urlBuild =  civicUrl + "?key=" + apiKey + "&address=" + location;

        Response.Listener<JSONObject> listener =
                response -> {
                    parseJSON(response.toString());
                    mainActivity.updateData(civicObj, civicDataArrayList);
                };

        Response.ErrorListener error =
                error1 -> Toast.makeText(mainActivity, "Didn't get the API Response" , Toast.LENGTH_SHORT).show();

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlBuild,
                        null, listener, error);

        queue.add(jsonObjectRequest);
    }

    private static void parseJSON(String s){
        try{

            //ArrayList<Civic> civicDataArrayList = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(s);

            // Initializing the object
            civicObj = new Civic();

            // Normalized Input
            JSONObject normalizedInputObj = jsonObject.getJSONObject("normalizedInput");

            // Logging the variables
            String city = normalizedInputObj.getString("city");
            String state = normalizedInputObj.getString("state");
            String zip = normalizedInputObj.getString("zip");


            // Grabbing the two JSON Array
            JSONArray officesArray = jsonObject.getJSONArray("offices");
            JSONArray officialsArray = jsonObject.getJSONArray("officials");



            // Getting each office Array by looping in
            for (int i = 0; i < officesArray.length(); i++){


                JSONObject officeJSONObj = (JSONObject) officesArray.get(i);
                String officeName = officeJSONObj.getString("name");

                // Setting the value of the OfficeName
                civicObj.setOfficeName(officeName);


                // Get the indices
                JSONArray officialIndicesArray = officeJSONObj.getJSONArray("officialIndices");
                for (int j = 0; j < officialIndicesArray.length(); j++) {

                    // Getting the index
                    int index = Integer.parseInt(officialIndicesArray.get(j).toString());


                    // Indexin the OfficialDetails with the above index
                    JSONObject officialDetailsArray = (JSONObject) officialsArray.get(index);

                    // Getting other values
                    String officialName = officialDetailsArray.getString("name");
                    civicObj.setOfficialName(officialName);

                    String partyName; if (officialDetailsArray.has("party")){
                        partyName = officialDetailsArray.getString("party");
                        civicObj.setParty(partyName);
                    }else{
                        partyName = "Unknown";
                        civicObj.setParty(partyName);
                    }



                    if (officialDetailsArray.has("phones")){
                        String phoneNumber = officialDetailsArray.getJSONArray("phones").getString(0);
                        civicObj.setPhoneNumber(phoneNumber);
                    }


                    if (officialDetailsArray.has("urls")){
                        String urlStr = officialDetailsArray.getJSONArray("urls").getString(0);
                        civicObj.setUrlString(urlStr);

                    }

                    if (officialDetailsArray.has("emails")){
                        String emailStr = officialDetailsArray.getJSONArray("emails").getString(0);
                        civicObj.setEmailId(emailStr);

                    }

                    if (officialDetailsArray.has("photoUrl")){
                        String photoUrlStr = officialDetailsArray.getString("photoUrl");
                        civicObj.setPhotoUrl(photoUrlStr);
                    }


                    if (officialDetailsArray.has("address")){



                        // Address is a Json Array
                        JSONArray addressArray = officialDetailsArray.getJSONArray("address");
                        JSONObject addressObj = (JSONObject) addressArray.get(0);
                        /*
                        Example below:
                        #TODO: Need to write if else and store the value based on the existence.

                        {"line1":"1600 Pennsylvania Avenue Northwest","city":"Washington","state":"DC","zip":"20500"}
                        {"line1":"1600 Pennsylvania Avenue Northwest","city":"Washington","state":"DC","zip":"20500"}
                         */

                         // Creating empty String
                        String line1 = "";
                        String line2 = "";
                        String cityLine = "";
                        String stateLine = "";
                        String zipCodeLine = "";

                        // If else for the String to store
                        if (addressObj.has("line1")){
                            line1 = addressObj.getString("line1");
                        }

                        if (addressObj.has("line2")){
                            line2 = addressObj.getString("line2");
                        }
                        if (addressObj.has("city")){
                            cityLine = addressObj.getString("city");
                        }
                        if (addressObj.has("state")){
                            stateLine = addressObj.getString("state");
                        }
                        if (addressObj.has("zip")){
                            zipCodeLine = addressObj.getString("zip");
                        }

                        // Appending the Strings together
                        String firstAddressLine = line1 + " " +  line2;
                        String secondAddressLine = cityLine + "," + stateLine + " " + zipCodeLine;
                        String completeAddress = firstAddressLine + ", " + secondAddressLine;

                        civicObj.setAddress(completeAddress);


                        // Log.d(TAG , completeAddress);


                    }

                    // Social Media channels
                    if (officialDetailsArray.has("channels")){
                        JSONArray channelsArray = officialDetailsArray.getJSONArray("channels");
                        for (int k = 0; k < channelsArray.length(); k++) {
                            JSONObject channelObj = (JSONObject) channelsArray.get(k);

                            String channelType = channelObj.getString("type");
                            String channelhandleId = channelObj.getString("id");

                            // Switch cases to parse depending on the type
                            switch (channelType){
                                case "Facebook":
                                    String fbId = "Facebook";
                                    civicObj.setFacebookId(fbId);
                                    break;

                                case "Twitter":
                                    String twitterId = "Twitter";
                                    civicObj.setTwitterId(twitterId);
                                    break;

                                case "Youtube":
                                    String youtubeId = "Youtube";
                                    civicObj.setYoutubeId(youtubeId);
                                    break;

                                default:
                                    Log.d(TAG , "process: Can't find the appropriate channel type");
                            }


                        }
                    }

                }

                civicDataArrayList.add(new Civic(civicObj.getOfficeName() ,civicObj.getOfficialName() ,
                        civicObj.getAddress() , civicObj.getParty(),
                        civicObj.getPhoneNumber() , civicObj.getUrlString() ,
                        civicObj.getEmailId() , civicObj.getPhotoUrl() ,
                        civicObj.getFacebookId() , civicObj.getTwitterId() , civicObj.getYoutubeId()));

//                civicObj = new Civic(civicObj.getOfficeName() ,civicObj.getOfficialName() ,
//                        civicObj.getAddress() , civicObj.getParty(),
//                        civicObj.getPhoneNumber() , civicObj.getUrlString() ,
//                        civicObj.getEmailId() , civicObj.getPhotoUrl() ,
//                        civicObj.getFacebookId() , civicObj.getTwitterId() , civicObj.getYoutubeId());
//





            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
