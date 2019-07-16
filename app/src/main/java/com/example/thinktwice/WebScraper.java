package com.example.thinktwice;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

public class WebScraper {

    public static void getLinks(final String url) {

            new Thread(new Runnable() { //for some reason networking calls must be made on separate threads

                @Override public void run() {
                    Thread.currentThread().setName("WebScraper");
                    try {

                        Document doc = Jsoup.connect(url).timeout(6000).get(); //connect to website

                        Elements links = doc.getElementsByTag("a"); //grab all links
                        for (Element link : links) {
                            System.out.println(link.attr("href"));
                        }

                    } catch(IOException ex) { System.out.println("Error in connecting to website: "+ex); }
                }
            }).start();
    }

    public static void sendRequest(String search) {
        try {

            String query = String.format("search="+URLEncoder.encode(search, Global.SEARCH_CHARSET)); //encode the search
            final URLConnection connection = new URL(Global.SEARCH_ENGINE+"?"+query).openConnection();
            connection.setRequestProperty("Accept-Charset", Global.SEARCH_CHARSET);

            //Listen for response
            new Thread(new Runnable() {

                @Override public void run() {

                    try {
                        InputStream response = connection.getInputStream();
                        Scanner webin = new Scanner(response);
                        String responseBody = webin.useDelimiter("\\A").next();
                        //System.out.println("HTTP_RESPONSE: "+responseBody);

                        //Convert response to json
                        JSONObject json = new JSONObject(responseBody);
                        System.out.println(json);


                    } catch(IOException ex) { System.out.println("Error in http request thread: " + ex);
                    } catch(JSONException ex) { System.out.println("Error parsing json: "+ex); }
                }
            }).start();

    }
        catch (UnsupportedEncodingException ex) { System.out.println("Error when sending http request: "+ex); }
        catch (MalformedURLException ex) { System.out.println("Error when sending http request: "+ex); }
        catch (IOException ex) { System.out.println("Error when sending http request: "+ex); }
    }

}
