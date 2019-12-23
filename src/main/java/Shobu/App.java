/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package Shobu;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.*;
import java.lang.ProcessBuilder;
import java.lang.Process;

public class App {

    private static String getStdin() {
        Scanner in = new Scanner(System.in);
        String s = in.next();
        return s;
    }

    public static void main(String[] args) {
        System.out.println("Arguments: " + Arrays.asList(args));
        ListIterator<String> argListIter = Arrays.asList(args).listIterator();
        while (argListIter.hasNext()) {
            System.out.println(argListIter.next());
        }

        // Validate command line arguments
        Options programOptions = new Options(args);
        if (programOptions.isValid() == false) {
            System.out.println("Invalid arguments provided");
            return;
        }

        // Determine method to execute AI subprocesses (java, python3, etc)
        // Start subprocesses and open IO pipes to them
        AIController aiController = new AIController(programOptions.getProgramNames(), programOptions.getExtensions());

        // Initialize Shobu game
        // Send gamestate JSON to AI1
        // Get JSON turn response
        // Parse JSON turn response
        // Validate turn response
        // if valid: Game.takeTurn(parsedJson)
        // else: GOTO Send gamestate JSON to AI1
        // if game over quit, else continue

        // Send gamestate JSON to AI2
        // Get JSON turn response
        // Parse JSON turn response
        // Validate turn response
        // if valid: Game.takeTurn(parsedJson)
        // else: GOTO: Send gamestate JSON to AI2
        // if game over quit, else continue

        tryJson();
    }

    public static void tryJson() {

        // seems the json reader does not expect to consume multiple consecutive discrete json objects
        // so I'll have to deal with that myself.
        String json = "{\"brand\" : \"Toyota\", \"doors\" : 5}\n{\"brand\" : \"Mercedes\", \"doors\" : 2}";

        JsonReader jsonReader = new JsonReader(new StringReader(json));

        try {
            while(jsonReader.hasNext()){
                JsonToken nextToken = jsonReader.peek();
                System.out.println(nextToken);

                if(JsonToken.BEGIN_OBJECT.equals(nextToken)){

                    jsonReader.beginObject();

                } else if(JsonToken.NAME.equals(nextToken)){

                    String name  =  jsonReader.nextName();
                    System.out.println(name);

                } else if(JsonToken.STRING.equals(nextToken)){

                    String value =  jsonReader.nextString();
                    System.out.println(value);

                } else if(JsonToken.NUMBER.equals(nextToken)){

                    long value =  jsonReader.nextLong();
                    System.out.println(value);

                } else if (JsonToken.END_OBJECT.equals(nextToken)) {
                    //jsonReader.endObject();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
