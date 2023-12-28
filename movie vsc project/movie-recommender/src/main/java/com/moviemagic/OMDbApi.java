package com.moviemagic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;
 
public class OMDbApi {
    private static HttpURLConnection connection;
    public static Scanner watched = new Scanner(System.in);
    public static Scanner _cont = new Scanner(System.in);
    public static Scanner movieTitle = new Scanner(System.in);
    public static Scanner movieYear = new Scanner(System.in);
    public static String date;
    //Parsing through the movie data of the selected movie. 
    public static String parse(String responseBody){ 
    JSONObject movie = new JSONObject(responseBody);
    String title = movie.getString("Title");
    String genre = movie.getString("Genre");
    String release = movie.getString("Released");
    System.out.println("Movie Title: " + title + "\n"  + 
                        "Genre: "+genre + "\n" +
                        "Release date: " + release);
    System.out.println(" ");
    watched(title);  
    return null; 
    }

    // Watchlist creation This isn't working and needs to be redone. ~~My guess is that I should call watched within the main function.~~
    //Issue is that global variables aren't a thing. Fixed by making it public static 
    //watched() serves as the function to ask the user if they have watched the selected movie
    public static void watched(String title) {
        String watch = "n";
        System.out.println("Have you watched this movie before? y/n");
        watch = watched.nextLine();
        //If they have, write the title of the movie into watchlist.txt
        if (watch.equals("y")) {
            try (BufferedWriter watchWriter = new BufferedWriter(new FileWriter("watchList.txt", true))) {
                if(!date.equals("")) {
                watchWriter.write(title + " (" + date + ")\n");
                }
                else {
                    watchWriter.write(title + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //This function does the browsing of the omdbAPI 
   public static void browseCataloge() {  
        String cont = "y";
        String key = "4c87bee2";
        String Title;
        String year;
        while (cont.equals("y")) {
        System.out.println("What movie do you want to look up?");
        Title=movieTitle.nextLine();
         System.out.println("Enter a year of release? If not, hit the enter key.");
        year = movieYear.nextLine(); 
        date = year;
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer(); 
        try {
            //Figuring out how I can implement a year function as well
            if(!year.isEmpty() && year.matches(".*\\d.*")) {
            URL url = new URL("https://www.omdbapi.com/?t=" + Title + "+&y=" + year + "&apikey=" + key);
                connection = (HttpURLConnection) url.openConnection();
        }
        else {
            date ="";
            URL url = new URL("https://www.omdbapi.com/?t=" + Title + "&apikey=" + key);
                connection = (HttpURLConnection) url.openConnection();
        }
            //Request setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int status=connection.getResponseCode();
        //If we get a status of 200, it means we are connected!  
        if (status == 200) {
            
        //If we are connected, we create a buffered reader to collect the selected movie data
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
        //This function allows us to parse the data
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
    cont = _cont.nextLine();   

}
System.out.println("Goodbye!");
   }
//displayList() purpose is to display watchList.txt
   public static void displayList() {
    try {
       BufferedReader reader = new BufferedReader(new FileReader("watchList.txt")); 
    String line;
    while((line=reader.readLine()) !=null) {
       System.out.println(line);
    }
     System.out.println("-------------------------"); 
    reader.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}   
//Main function ties all of the functions togeather
public static void main(String[] args) throws FileNotFoundException {
        Scanner scan = new Scanner(System.in);
        //Creation and checking for a watchist.txt
        File file = new File("watchList.txt");
        try (BufferedWriter watchWriter = new BufferedWriter(new FileWriter("watchList.txt", true))) {
            if(file.length()==0) {    
            watchWriter.append("Watchlist:");
            }
                watchWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        int decision = 0;
         //This is the menu that the user interacts with
        System.out.println("   Movie search!   ");
        System.out.println("-------------------------"); 
        while (decision !=3) { 
        System.out.println("Menu: \n" +
                             "1) Browse movie catalog \n"
                             +"2) See watched movie list \n"
                             +"3) Quit \n" + "-------------------------"); 
        decision = scan.nextInt();
    if(decision ==1) {
        browseCataloge();
    }                        
    else if (decision == 2) {
        displayList(); 
    }    
    else if (decision != 1 && decision !=2 && decision!=3) {
        System.out.println("Wrong input detected, please try again");
        System.out.println("-------------------------"); 
    }
}
System.out.println("Goodbye!");    
scan.close();    
    watched.close();
    _cont.close();
movieTitle.close();
movieYear.close();   
}
}   
