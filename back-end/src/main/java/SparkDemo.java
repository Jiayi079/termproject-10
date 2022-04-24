import static spark.Spark.*;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.HashMap;
import java.util.Map;
import org.bson.Document;

class LoginDto{
  String username;
  String password;
}

class LoginResponseDto{
  Boolean isLoggedIn;
  String error;

  public LoginResponseDto(Boolean isLoggedIn, String error) {
    this.isLoggedIn = isLoggedIn;
    this.error = error;
  }
}

public class SparkDemo {

  public static void main(String[] args) {
    port(1234);

    Map<String,String> users = new HashMap<>(); // TODO remove this and use mongo collection isntead
    // open connection
    MongoClient mongoClient = new MongoClient("localhost", 27017);
    // get ref to database
    MongoDatabase db = mongoClient.getDatabase("UsersDatabase");
    // get ref to collection
    MongoCollection<Document> usersCollection = db.getCollection("usersCollection");


    Gson gson = new Gson();
    post("/logIn", (req, res) -> {
      String body = req.body();
      LoginDto loginDto = gson.fromJson(body, LoginDto.class);
      // TODO swap the users data store with usersCollection
      if (users.get(loginDto.username) != null) {
        if (users.get(loginDto.username).equals(loginDto.password)) {
          return gson.toJson(new LoginResponseDto(true, null));
        } else {
          return gson.toJson(new LoginResponseDto(false, "Invalid password"));
        }
      } else {
        users.put(loginDto.username, loginDto.password);
        return gson.toJson(new LoginResponseDto(true, null));
      }
    });
  }
}
