package com.example.thinktwice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

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

            String query = String.format("search="+URLEncoder.encode(search,Globals.SEARCH_CHARSET)); //encode the search
            URLConnection connection = new URL(Globals.SEARCH_ENGINE+"?"+query).openConnection();

        }
        catch (UnsupportedEncodingException ex) { System.out.println("Error when sending http request: "+ex); }
        catch (MalformedURLException ex) { System.out.println("Error when sending http request: "+ex); }
        catch (IOException ex) { System.out.println("Error when sending http request: "+ex); }
    }

}
