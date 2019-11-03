/*
Brandon Franke
b00786125
This program will take crawl a website. It will take a website URL as an argument and then begin th print
all URLs found within that webpage. It will then crawl every URL within those webpages.
 */

import org.jsoup.Jsoup;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class WebCrawler {

    private static Queue queue = new Queue(); //queue to hold websites that need to be crawled
    private static ArrayList<String> visited = new ArrayList<>(); //arraylist to hold visited websites


    private static void crawl(String startingWebsite) throws IOException{

        //creating a writer so we can write to the file
        PrintWriter printWriter = new PrintWriter(new FileWriter("RESULT.txt")); //so we can write to a file
        //adding the first URL to the queue
        queue.enqueue(startingWebsite);

        /*
        Below algorithm works as follows. It will first grab the next item in the queue. It will then extract the HTML
        content of that URL using Jsoup. A Regular Expression is then used to extract the title and links in the page.
        The title of the page is printed, then every URL within that page is printed. Each URL is then added to the queue to be crawled (if it
        has not already been crawled).
         */
        int i = 0;
        while (!queue.isEmpty()) {
            String currentURL = queue.dequeue(); //removes the first element from the queue and assigns it

            //loops through to make sure no duplicate link is added to the queue
           while (visited.contains(currentURL) || visited.contains(currentURL + "/") || visited.contains(currentURL.substring(0, currentURL.length()-1))) {
               if (!queue.isEmpty()) {
                   currentURL = queue.dequeue();
               }else {
                   printWriter.close(); //closing the print writer as we no longer need it
                   return;
                   }
               }

            //while loop to loop through the currentURL until a suitable URL is found.
            String htmlContent;

           while (true) {
                try {
                    //creates a jsoup connection and retrieves the webpage as a string
                    htmlContent = Jsoup.connect(currentURL).userAgent("Mozilla").ignoreContentType(true).get().html();
                    visited.add(currentURL); //adds the current URL to the visited list so that it doesn't get crawled again

                    break;
                    //catches an exception if it occurs (this will happen if the URL is malformed, or if there is an error with
                    //the html connection)
                } catch (IOException io){
                    currentURL = queue.dequeue(); //adds the next url to the queue to be crawled and goes back through the loop
                }
            }



            //pattern to find a URL from the HTML content
            //RegEx pattern obtained from: http://urlregex.com
            Pattern patternURL = Pattern.compile("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
            Matcher matcherURL = patternURL.matcher(htmlContent);

            //pattern to find the title of the page from the HTML content
            Pattern patternTitle = Pattern.compile("<title>(.*?)</title>");
            Matcher matcherTitle = patternTitle.matcher(htmlContent);

            //if the matcher finds the title, assign it to the current title
            if (matcherTitle.find()){
                String currentTitle = matcherTitle.group(1);
                printWriter.print("\n" + currentTitle + " | URL: " + currentURL + "\n"); //prints the title of the current page being crawled
            }

            //loop to go through and find all the URLs within the HTML content
            while (matcherURL.find()) {
                String currentLink = matcherURL.group();


                if (currentLink.startsWith("https://csgs.cs.dal.ca")){ //if the link found has not already been visited
                    queue.enqueue(currentLink); //add it to the queue to be searched later
                }


                printWriter.print("\turl: " + currentLink + "\n"); //print the links within the page being crawled
            }

            //System.out.println(visited.get(i));
            i++;

        }
        printWriter.close(); //closing the writer as we no longer need it
    }

    public static void main (String [] args) throws IOException{
        crawl("https://csgs.cs.dal.ca/");
        System.out.println("done");
    }
}