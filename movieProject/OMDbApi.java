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

public class OMDbApi {
    private static HttpURLConnection connection;
    public static Scanner scanner = new Scanner(System.in);
    public static String date;

    // Parsing through the movie data of the selected movie.
    // Since I know how the API handles calls, I can use regex instead of JSON, if it were more fragile, I would use JSON
public static String parse(String responseBody) {
    try {
        String title = responseBody.split("\"Title\":\"")[1].split("\"")[0];
        String genre = responseBody.split("\"Genre\":\"")[1].split("\"")[0];
        String release = responseBody.split("\"Released\":\"")[1].split("\"")[0];
        System.out.println("Movie Title: " + title + "\n" +
                "Genre: " + genre + "\n" +
                "Release date: " + release);
        System.out.println(" ");
        watched(title);
    } catch (Exception e) { //If it broke, give exception
        System.out.println("Could not parse movie information. Response was:");
        System.out.println(responseBody);
    }
    return null;
}

    // watched() serves as the function to ask the user if they have watched the selected movie
    public static void watched(String title) {
        String watch = "n";
        System.out.println("Have you watched this movie before? y/n");
        watch = scanner.nextLine();
        // If they have, write the title of the movie into watchlist.txt
        if (watch.equals("y")) {
            try (BufferedWriter watchWriter = new BufferedWriter(new FileWriter("watchList.txt", true))) {
                if (!date.equals("")) { // If we have a date, write the title and date, otherwise write the title only
                    watchWriter.write(title + " (" + date + ")\n");
                } else {
                    watchWriter.write(title + "\n");
                }
            } catch (IOException e) { // throw an error if printing out the movie causes an issue
                e.printStackTrace();
            }
        }
    }

    // This function does the browsing of the omdbAPI
    public static void browseCataloge() {
        String cont = "y";
        String key = "Insert_API_Key_Here"; // your API key
        String Title;
        String year;
        while (cont.equals("y")) {
            System.out.println("-------------------------");
            System.out.println("What movie do you want to look up?");
            Title = scanner.nextLine();
            System.out.println("-------------------------");
            System.out.println("Enter a year of release? If not, hit the enter key.");
            year = scanner.nextLine();
            System.out.println("-------------------------");
            date = year;
            BufferedReader reader;
            String line;
            StringBuffer responseContent = new StringBuffer();
            try {
                // Using Regex, I can see if the user included a year and check to see if the year matches up with the movie
                if (!year.isEmpty() && year.matches(".*\\d.*")) {
                    URL url = new URL("https://www.omdbapi.com/?t=" + Title + "+&y=" + year + "&apikey=" + key);
                    connection = (HttpURLConnection) url.openConnection();
                } else {
                    date = "";
                    URL url = new URL("https://www.omdbapi.com/?t=" + Title + "&apikey=" + key);
                    connection = (HttpURLConnection) url.openConnection();
                }
                // Request setup
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                int status = connection.getResponseCode();
                // If we get a status of 200, it means we are connected!
                if (status == 200) {

                    // If we are connected, we create a buffered reader to collect the selected movie data
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        responseContent.append(line);
                    }
                    reader.close();
                }
                // Otherwise, we show an error
                else {
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    while ((line = reader.readLine()) != null) {
                        responseContent.append(line);
                    }
                    reader.close();
                }
                // This function allows us to parse the data
                parse(responseContent.toString());
            } catch (MalformedURLException e) { // Throw exceptions if the url gets messed up or an error with input output
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect(); //disconnect the connection
            }
            System.out.println("-------------------------");
            System.out.println("Do you want to continue? y/n ");
            cont = scanner.nextLine();

        }
        System.out.println("Goodbye!");
        System.out.println("-------------------------");
    }

    // displayList() purpose is to display watchList.txt
    public static void displayList() {
        try { //try to read from the buffered reader
            BufferedReader reader = new BufferedReader(new FileReader("watchList.txt"));
            String line;
            while ((line = reader.readLine()) != null) { //while there is a line in the buffer, print it out
                System.out.println(line);
            }
            System.out.println("-------------------------");
            reader.close();
        } catch (IOException e) { 
            e.printStackTrace(); //otherwise through an exception
        }
    }

    // Main function ties all of the functions together
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scan = new Scanner(System.in);
        // Creation and checking for a watchist.txt
        File file = new File("watchList.txt");
        try (BufferedWriter watchWriter = new BufferedWriter(new FileWriter("watchList.txt", true))) {
            if (file.length() == 0) {
                watchWriter.append("Watchlist:");
            }
            watchWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int decision = 0;
        // This is the menu that the user interacts with
        System.out.println("   Movie search!   ");
        System.out.println("-------------------------");
        do {
            System.out.println("Menu: \n" +
                    "1) Browse movie catalog \n"
                    + "2) See watched movie list \n"
                    + "3) Quit \n" + "-------------------------");
            decision = scan.nextInt();
            if (decision == 1) {
                browseCataloge();
            } else if (decision == 2) {
                displayList();
            } else if (decision != 1 && decision != 2 && decision != 3) {
                System.out.println("Wrong input detected, please try again");
                System.out.println("-------------------------");
            }
        } while(decision !=3);
        System.out.println("Goodbye!");
        scan.close();
        scanner.close();
    }
}
