package com.mygdx.game;

import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;


public class WebCrawler {

    public static final String SEARCH_ENGINE = "https://www.google.ca/search?";
    public static final String SEARCH_CHARSET = "UTF-8";
    public static final String SEARCH_PARAMS = "&num=25&safe=active";
    public static final String DESCRIPTION = "";
    public static final int SEARCH_DEPTH = 1;

    public WebCrawler() {

    }

    public ArrayList<String> getGoogleSearchResults(String search) {
        ArrayList<String> toVisit = new ArrayList<String>();
        String query = null;
        try {
            query = SEARCH_ENGINE + "q=" + URLEncoder.encode(search, SEARCH_CHARSET) + SEARCH_PARAMS;
        } catch (UnsupportedEncodingException ex) {
            System.out.println("Error when generating query: " + ex);
        }

        assert (query != null && !query.equals(null)) : "Error when generating query";

        try {
            Document search_page = Jsoup.connect(query).get();

            //pull all links from site
            Elements links = search_page.getElementsByClass("r");
            for (Element link : links) {
                String nextLink = link.select("a").attr("href");
                toVisit.add(nextLink);
            }
        } catch (IOException ex) {
            System.out.println("Error when constructing search results: " + ex);
        }

        return toVisit;
    }

    public void start_crawl(String search) {
        ArrayList<String> visited = new ArrayList<String>();

        ArrayList<Pair<String, Integer>> toVisit = new ArrayList<Pair<String, Integer>>();
        ArrayList<String> search_results = this.getGoogleSearchResults(search); //generate google search url
        for (String url : search_results) {
            toVisit.add(new Pair<String, Integer>(url, 1)); //store the url and the depth
        }

        while (toVisit.size() > 0) {
            Pair<String, Integer> data = toVisit.get(0);
            String url = data.getKey();
            int depth = data.getValue();

            //assert (depth > SEARCH_DEPTH) : "Max search depth exceeded";

            try {
                Document doc = Jsoup.connect(url).timeout(6000).get(); //retrieve html from website

                //pull all links from site
                Elements links = doc.getElementsByTag("a");
                for (Element link : links) {
                    String newLink = link.attr("href");
                    System.out.println(depth+" "+newLink);

                    if (depth > SEARCH_DEPTH || visited.contains(newLink)) {
                        break;
                    }

                    toVisit.add(new Pair<String, Integer>(newLink, depth + 1));
                }

            } catch (IOException ex) { System.out.println("Error connecting to page: " + ex); }
            catch (IllegalArgumentException ex) { System.out.println("Malformed URL: "+ex); }

            visited.add(toVisit.remove(0).getKey());
        }
    }

    /*
    public void start_crawl(String search) {
        this.visited.clear();

        //generate google search url
        ArrayList<String> toVisit = this.getGoogleSearchResults(search);
        System.out.println(toVisit);
        this.start_crawl(toVisit,1);
    }
    public void start_crawl(ArrayList<String> toVisit,int depth) {

        LinkedList<ArrayList<String>> branches = new LinkedList<ArrayList<String>>();

        for (String curUrl : toVisit) {
            //System.out.println("CURRENT PAGE: " + curUrl);

            if (depth > SEARCH_DEPTH) { System.out.println("terminated at depth: " + depth); return; } //if the max search depth has been reached
            else if (this.visited.contains(curUrl)) { System.out.println("Page already visited: "+curUrl); continue; }

            ArrayList<String> newToVisit = new ArrayList<String>();
            this.visited.add(curUrl); //we have visited the page

            try {
                Document doc = Jsoup.connect(curUrl).timeout(6000).get(); //retrieve html from website

                //pull all links from site
                Elements links = doc.getElementsByTag("a");
                for (Element link : links) {
                    String newLink = link.attr("href");

                    if (newToVisit.size() >= LINKS_PER_PAGE) {
                        System.out.println("too many links per page: "+curUrl);
                        break;
                    }

                    newToVisit.add(newLink);
                    System.out.println(newLink);
                }

            } catch (IOException ex) { System.out.println("Error when connecting to website: " + ex); }
            catch (IllegalArgumentException ex) { System.out.println("Null link"); }

            branches.add(newToVisit);
        }

        for (ArrayList<String> newToVisit : branches) {
            this.start_crawl(newToVisit,depth+1);
        }
    }
    */
}
