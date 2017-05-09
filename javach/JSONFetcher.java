package javach;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//Given a JSON endpoint returns either JSONObject or JSONArray
public class JSONFetcher {

public static Object vomit(String url){
    JSONParser parser = new JSONParser();
    
    try {        
        URL oracle = new URL(url); // URL to Parse
    
        URLConnection yc = oracle.openConnection();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        Object o = parser.parse(in);

        in.close();
        
        return o;
    
    } catch (FileNotFoundException e) { //any exception results in a n ull return
        return null;                    //may be changed as needed
    } catch (IOException e) {
        return null;
    } catch (ParseException e) {
        return null;
    }  
    
    
}  



}