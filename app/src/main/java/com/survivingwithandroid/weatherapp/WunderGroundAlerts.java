package com.survivingwithandroid.weatherapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Kendra on 4/14/2015.
 */
public class WunderGroundAlerts {

    private String alert;

    public String[] getAlerts(String location) {
        String[] alertData = new String[3];
        String url = "http://api.wunderground.com/api/f40be2e38cc014b7/alerts/q/OK/Norman.json";

        if(location.contains(", "))
        {
            String city = location.substring(0,location.indexOf(","));
            String state = location.substring(location.indexOf(",")+2, (location.length()));

            url = "http://api.wunderground.com/api/f40be2e38cc014b7/alerts/q/" + state + "/" + city + ".json";
        }
        else
        {
            alertData[0] = "Severe alerts can not be found for this location.";
            //return alertData;
        }


        HttpURLConnection con = null ;
        InputStream is = null;

        try {
            con = (HttpURLConnection) ( new URL(url /*+ location*/)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            alert = buffer.toString();
            is.close();
            con.disconnect();

            JSONObject jObj = new JSONObject(buffer.toString());
            JSONArray alerts = (JSONArray) jObj.get("alerts");

            if(alerts.get(0) != null)
            {
                JSONObject innerAlerts = (JSONObject) alerts.get(0);
                String date = innerAlerts.get("date").toString();
                String type = innerAlerts.get("type").toString();
                alertData[0] = innerAlerts.getString("description");
                alertData[1] = innerAlerts.getString("message");

                return alertData;
            }
            else
            {
                alertData[0] = "No severe weather alerts";
                return alertData;
            }

        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        alertData[0] = "No severe weather alerts";
        return alertData;
    }
    public String[] getAlerts(Double lat, Double lon) {
        String[] locationData = new String[3];


        String url = "http://api.wunderground.com/api/f40be2e38cc014b7/geolookup/q/";
        url += lat + "," + lon + ".json";

        HttpURLConnection con = null ;
        InputStream is = null;

        try {
            con = (HttpURLConnection) ( new URL(url /*+ location*/)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            alert = buffer.toString();
            is.close();
            con.disconnect();

            JSONObject jObj = new JSONObject(buffer.toString());
            JSONArray alerts = (JSONArray) jObj.get("geolookup");

            if(alerts.get(0) != null)
            {
                JSONObject innerAlerts = (JSONObject) alerts.get(0);
                String city = innerAlerts.get("city").toString();
                String state = innerAlerts.get("state").toString();
                locationData[0] = innerAlerts.getString("city");
                locationData[1] = innerAlerts.getString("state");

                return locationData;
            }
            else
            {
                locationData[0] = "No Alerts in your area.";
                return locationData;
            }


        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        locationData[0] = "No severe weather alerts";
        return locationData;
    }
}
