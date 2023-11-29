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
        System.out.println("   Movie Genre search!   ");
        System.out.println("-------------------------");
        String cont = "y";
        while (cont.equals("y")) {
        System.out.println("What movie do you want to look up?");
        Scanner movieTitle = new Scanner(System.in);
        String key = "4c87bee2";
        String Title=movieTitle.nextLine();
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        //We use try and catch to access the web
        try {
        URL url = new URL("https://www.omdbapi.com/?t=" + Title + "&apikey=" + key);
        connection = (HttpURLConnection) url.openConnection();
            //Request setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int status=connection.getResponseCode();
        //If we get a status of 200, it means we are connected!  
        if (status == 200) {
            
        //If we are connected, we follow the else statement, but we could rewrite it such that if status ==200)    
       
            reader =new BufferedReader(new InputStreamReader(connection.getInputStream()));
             while((line=reader.readLine())!=null) {
                responseContent.append(line);
            }
            reader.close();
            }   
            // Otherwise we show an error
            else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            while((line=reader.readLine())!=null) {
                responseContent.append(line);
            }
            reader.close();
        }  
        parse(responseContent.toString());
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
    JSONObject movie = new JSONObject(responseBody);
    String title = movie.getString("Title");
    String genre = movie.getString("Genre");
    System.out.println("Movie Title: " + title + "\n"  + 
                        "Genre: "+genre);
    return null; 
}
}