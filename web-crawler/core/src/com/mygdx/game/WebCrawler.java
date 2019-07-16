package com.mygdx.game;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;


public class WebCrawler {

    public static final String SEARCH_ENGINE = "https://www.google.ca/search?";
    public static final String SEARCH_CHARSET =  "UTF-8";

    private HashSet<String> visited = new HashSet<String>();
    private LinkedList<String> toVisit = new LinkedList<String>();

    public WebCrawler(String root) {

    }

    public void generateQuery(String search) {
        try {
            String query = SEARCH_ENGINE+URLEncoder.encode(search, SEARCH_CHARSET);
            System.out.println(query);
        } catch(UnsupportedEncodingException ex) { System.out.println("Error when generating query: "+ex); }
    }

    public void start_crawl(String root) {
        this.visited.clear();
        this.toVisit.clear();

        toVisit.add(root);
        while (toVisit.size() > 0) {

            String curUrl = toVisit.get(0);
            //dont visit the site if we have already seen it
            if (visited.contains(curUrl)) { continue; }

            try {
                Document doc = Jsoup.connect(curUrl).get(); //retrieve html from website

                Elements links = doc.getElementsByTag("a");

                for (Element link : links) {

                }


            } catch (IOException ex) { System.out.println("Error when connecting to website: "+ex); }
        }
    }

    public void addUrl(String url) { this.toVisit.add(url); }
    public void popUrl() { this.visited.add(this.toVisit.pop()); }
}
