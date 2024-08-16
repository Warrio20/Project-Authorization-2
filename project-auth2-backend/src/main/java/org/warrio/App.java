package org.warrio;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.javalin.Javalin;
import org.warrio.route.Router;
import org.warrio.utils.EnvReader;

import java.util.Map;


public class App
{
    public static MongoDatabase db;
    public static Map<String, String> dotenv;

    public static void main( String[] args ) {
        dotenv = EnvReader.readFile(".env");
        String dbUrl = dotenv.get("DB_URL");
        String dbName = dotenv.get("DB_NAME");
        MongoClient mongoClient = MongoClients.create(dbUrl);
        db = mongoClient.getDatabase(dbName);
        int port = Integer.parseInt(dotenv.get("PORT"));
        Javalin app = Router.Route();
        app.start(port);
    }
}
