# Movie-web-scraper
In my in-progress Java project by using API, I hope to be able to search movies via genre and other factors. Currently, the only part of this project completed was a connection test to an API and the ability to search for a movie and get its genres. 
The current API I am using is OMDb API, not created by me, only utilized by me.
HTTP connection template modified from Coding Master's video "How to Send HTTP Requests and Parse JSON Data Using Java".
Link to video: https://www.youtube.com/watch?v=qzRKa8I36Ww

Update 12/28/23
This Java project has taken a life of its own. Instead of being a simple search and receive program, the purpose of this program is to catalog movies into a watched list stored in watchList.txt. How this works is by utilizing OMDBapi's movie search function, the user is provided a prompt to provide a movie title and optionally a release date. The program then browses OMDB's movie database and picks the movie closest to the selected title and date. Afterwards, the selected movie's title, genre, and year of day of release is given to the user to which they are then prompted to select if they have seen this movie. If they select yes, then the movie title is submitted to watchList.txt. Additionally, I have also added a menu on startup such that the user can access their watchlist. 

Additional features to highlight is how I have changed the location of my functions such that the main function has only the necessary implementations and function calls. This includes the creation of a watchList.txt file on the first run of the program. I have also moved the HTTP connection to a separate function and changed up the if statement within the function to ask directly if a signal of 200 was received. I also cleared out any vestigial public static scanners and variables and changed the location of where they are closed to be at the end of the main function. 

If there were to be any more updates to this project, they would include streamlining my functions as well as implementing a favorites feature in the watched list and a way to remove movies from the watched list and favorites. 
