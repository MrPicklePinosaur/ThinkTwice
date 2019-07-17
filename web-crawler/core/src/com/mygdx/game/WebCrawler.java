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
    public static final int LINKS_PER_PAGE = 50;

    private ArrayList<String> visited = new ArrayList<String>();

    public WebCrawler() {

    }

    public ArrayList<String> getGoogleSearchResults(String search) {
        ArrayList<String> toVisit = new ArrayList<String>();
        String query = null;
        try {
            query = SEARCH_ENGINE+"q="+URLEncoder.encode(search, SEARCH_CHARSET)+SEARCH_PARAMS;
        } catch(UnsupportedEncodingException ex) { System.out.println("Error when generating query: "+ex); }

        assert (query != null && !query.equals(null)): "Error when generating query";

        try {
            Document search_page = Jsoup.connect(query).get();

            //pull all links from site
            Elements links = search_page.getElementsByClass("r");

            for (Element link : links) {
                String nextLink = link.select("a").attr("href");

                toVisit.add(nextLink);
            }
        } catch(IOException ex) { System.out.println("Error when constructing search results: "+ex); }

        return toVisit;
    }

    public void start_crawl(String search) {
        this.visited.clear();

        //generate google search url
        ArrayList<String> toVisit = this.getGoogleSearchResults(search);
        System.out.println(toVisit);
        this.start_crawl(toVisit,1);
    }
    public void start_crawl(ArrayList<String> toVisit,int depth) {

        for (String curUrl : toVisit) {
            System.out.println("CURRENT PAGE: " + curUrl);

            if (depth > SEARCH_DEPTH) { System.out.println("terminated at depth: " + depth); return; } //if the max search depth has been reached
            else if (this.visited.contains(curUrl)) { System.out.println("Page already visited"); continue; }

            ArrayList<String> newToVisit = new ArrayList<String>();
            this.visited.add(curUrl); //we have visited the page

            try {
                Document doc = Jsoup.connect(curUrl).timeout(6000).get(); //retrieve html from website

                //pull all links from site
                Elements links = doc.getElementsByTag("a");
                for (Element link : links) {
                    String newLink = link.attr("href");

                    if (newToVisit.size() >= LINKS_PER_PAGE) {
                        System.out.println("too many links per page");
                        break;
                    }

                    newToVisit.add(newLink);
                    System.out.println(newLink);
                }

            } catch (IOException ex) { System.out.println("Error when connecting to website: " + ex); }
            catch (IllegalArgumentException ex) { System.out.println("Null link"); }


            //System.out.println(newToVisit.toString());
            this.start_crawl(newToVisit,depth++);
        }
    }



}
