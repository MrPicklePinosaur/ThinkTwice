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
    public static final String SEARCH_CHARSET = "UTF-8";
    public static final String SEARCH_PARAMS = "&num=100";

    private HashSet<String> visited = new HashSet<String>();
    private LinkedList<String> toVisit = new LinkedList<String>();

    public WebCrawler() {

    }

    public String generateQuery(String search) {
        String query = null;
        try {
            query = SEARCH_ENGINE+"q="+URLEncoder.encode(search, SEARCH_CHARSET)+SEARCH_PARAMS;
        } catch(UnsupportedEncodingException ex) { System.out.println("Error when generating query: "+ex); }

        assert (query != null && !query.equals(null)): "Error when generating query";
        return query;
    }

    public void start_crawl(String search) {
        this.visited.clear();
        this.toVisit.clear();

        //generate google search url
        String rootUrl = this.generateQuery(search);
        toVisit.add(rootUrl);
        System.out.println(rootUrl);

        while (toVisit.size() > 0) {

            String curUrl = toVisit.get(0);
            //dont visit the site if we have already seen it
            if (visited.contains(curUrl)) { continue; }

            try {
                Document doc = Jsoup.connect(curUrl).get(); //retrieve html from website

                this.pull_links(doc);
                /*
                for (Element link : links) {

                }
                */


            } catch (IOException ex) { System.out.println("Error when connecting to website: "+ex); }

            toVisit.remove(0); //Possibly reconsider using linkedlist, as remove is O(n)
        }
    }

    public void pull_links(Document doc) {
        Elements links = doc.getElementsByClass("r");

        for (Element link : links) {
            System.out.println(link.select("a").attr("href"));
            //System.out.println(link.attr("href"));
        }
    }

    public void addUrl(String url) { this.toVisit.add(url); }
    public void popUrl() { this.visited.add(this.toVisit.pop()); }
}
