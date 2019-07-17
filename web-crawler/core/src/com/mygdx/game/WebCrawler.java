package com.mygdx.game;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;


public class WebCrawler {

    public static final String SEARCH_ENGINE = "https://www.google.ca/search?";
    public static final String SEARCH_CHARSET = "UTF-8";
    public static final String SEARCH_PARAMS = "&num=25&safe=active";
    public static final int SEARCH_DEPTH = 2;

    private ArrayList<String> visited = new ArrayList<String>();

    public WebCrawler() {

    }

    public static String generateQuery(String search) {
        String query = null;
        try {
            query = SEARCH_ENGINE+"q="+URLEncoder.encode(search, SEARCH_CHARSET)+SEARCH_PARAMS;
        } catch(UnsupportedEncodingException ex) { System.out.println("Error when generating query: "+ex); }

        assert (query != null && !query.equals(null)): "Error when generating query";
        return query;
    }

    public void start_crawl(String search) {
        //create queue of urls to visit
        ArrayList<String> toVisit = new ArrayList<String>();
        this.visited.clear();

        //generate google search url
        String rootUrl = WebCrawler.generateQuery(search);
        toVisit.add(rootUrl);

        this.start_crawl(toVisit,1);
    }
    public void start_crawl(ArrayList<String> toVisit,int depth) {

        for (String curUrl : toVisit) {
            System.out.println("CURRENT PAGE: "+curUrl);
            ArrayList<String> newToVisit = new ArrayList<String>();

            if (depth > SEARCH_DEPTH) { System.out.println("terminated at depth: "+depth); return; }

            try {
                Document doc = Jsoup.connect(curUrl).get(); //retrieve html from website

                //pull all links from site
                Elements links = doc.getElementsByClass("r");

                for (Element link : links) {
                    String nextLink = link.select("a").attr("href");
                    System.out.println(nextLink);
                    //dont visit the site if we have already seen it
                    //if (visited.contains(curUrl)) { continue; }
                    newToVisit.add(nextLink);

                }

            } catch (IOException ex) { System.out.println("Error when connecting to website: "+ex); }

            System.out.println(newToVisit.toString());
            this.start_crawl(newToVisit,depth++);
        }
    }

    /*
    public ArrayList<String> getGoogleSearchResults(String search) {

    }
    */

}
