package com.moviemagic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

public class OMDbApi {
    private static HttpURLConnection connection;
    public static void main(String[] args) {
        //I wanted to create a simple menu 
        System.out.println("   Movie Genre search!   "); 
        System.out.println("-------------------------");
        String cont = "y"; //String that will work as my boolean variable
        Scanner movieTitle = new Scanner(System.in); //Create a scanner so that the user can put in their movie
        String key = "REDACTED"; //Put your own API key here, the code on my desktop has my key in replace of REDACTED
        String Title;
        while (cont.equals("y")) { //While loop so the user can make multiple searches
        System.out.println("What movie do you want to look up?");
        Title=movieTitle.nextLine(); //Read the user input
        BufferedReader reader; //Create a reader to read the url
        String line; // Generate the line we want to use
        StringBuffer responseContent = new StringBuffer(); //Create a StringBuffer
        //We use try and catch to access the web
        try {
        URL url = new URL("https://www.omdbapi.com/?t=" + Title + "&apikey=" + key); //Create a url obj using the API's endpoint
        connection = (HttpURLConnection) url.openConnection(); 
            //Request setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); //This equates to 5 seconds
            connection.setReadTimeout(5000);
            int status=connection.getResponseCode(); //Fetch the status
        //If we get a status of 200, it means we are connected!  
        if (status == 200) {
        //If we are connected, then we can fetch the movie title and genre
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); //Create a reader
             while((line=reader.readLine())!=null) { //While the line or data is readable...
                responseContent.append(line); //Add data to responseContent
            }
            reader.close(); //Close the reader 
            }   
            // Otherwise we show an error
            else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            while((line=reader.readLine())!=null) {
                responseContent.append(line);
            }
            reader.close();
        }  
        parse(responseContent.toString()); //Parse the data
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            connection.disconnect();
        } 
    System.out.println("-----------------");
    System.out.println("Do you want to continue? y/n ");
    Scanner _cont = new Scanner(System.in);
    cont = _cont.nextLine();
    }
System.out.println("Goodbye!");
}
public static String parse(String responseBody) {
    JSONObject movie = new JSONObject(responseBody); //Create a JSONOBject that is made from the responseBody
    String title = movie.getString("Title"); //Get the tile
    String genre = movie.getString("Genre"); //Get the genre
    System.out.println("Movie Title: " + title + "\n"  + //Print out the data 
                        "Genre: "+genre);
    return null; 
}
}
