package javach;
import org.json.simple.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//General purpose class
public class Board {
    
    //automatically retrieved metadata for all boards
    private static JSONObject Allmetadata = (JSONObject) JSONFetcher.vomit("https://a.4cdn.org/boards.json");
    
    //returns List of Board objects given List of board names
    public static Object getBoards(List<String> list){  
        
        List<specBoard> myList = new LinkedList<>();
        for (String s : list){
            specBoard board = new specBoard(s);
            myList.add(board);
        }
        return myList;
    }
    
    public static Object allBoardsJSON(){ //returns the 4chan api's JSON representation of all boards
        if(Allmetadata == null) {
            Allmetadata = (JSONObject) JSONFetcher.vomit("https://a.4cdn.org/boards.json");
        }
        return Allmetadata;
    }
    
    public static List<specBoard> allBoards(){ //returns List of all boards
        
        LinkedList<specBoard> boardsList = new LinkedList<>();
        
        JSONObject allboards = (JSONObject) allBoardsJSON();
        JSONArray boards = (JSONArray) allboards.get("boards");
        for (Object o : boards) {
            JSONObject board = (JSONObject) o;
            specBoard customBoard = new specBoard((String) board.get("board"));
            boardsList.add(customBoard);
        }
        
        return boardsList;
    }
    
    public static List<Integer> getAllThreadIds(String id){ //Returns list of all thread IDs given board name
        
        LinkedList<Integer> threadIDs = new LinkedList<>();
        specBoard board = new specBoard(id);
        JSONArray jsonobj = (JSONArray) JSONFetcher.vomit(board.getURL() + "/threads.json");
        for(Object o: jsonobj) {
            JSONObject page = (JSONObject) o;
            JSONArray pageArray = (JSONArray) page.get("threads");
            for (Object t: pageArray) {
                JSONObject thread = (JSONObject) t;
                threadIDs.add((int)(long) thread.get("no"));
            }
        }
        return threadIDs;
        
    }
    
    public static class specBoard { //inner class representing individual board
        
        JSONObject metadata;    //will contain the board metadata
        private String name;    //the name. ex: g, fit, etc
        String protocol = "https://";   
        private String url;
        
        public HashMap<Integer, Thread> cache = new HashMap<>();   //constant cache of threads
        
        public specBoard(String name){
            this(true, name);   
        }
        
        public specBoard(boolean https, String name){ //given protocol and name of board
            
            if(!https) { //swap protocols if necessary
                protocol = "http://";
            }
            this.name = name;
            this.url = URL.boardURL(protocol, name);
            
            JSONObject jsonObj = Allmetadata; //get data for all boards
            JSONArray boards = (JSONArray) jsonObj.get("boards");
            
            for (Object o : boards) { //narrow it down to our board
                metadata = (JSONObject) o;
                if (metadata.get("board").equals(name)){
                    break;
                }
            }
        }
        
        public boolean hasThread(int id) { //checks if thread currently exists on the board
            JSONObject value = (JSONObject)JSONFetcher.vomit(url + "/thread/" + id + ".json");
            if (value == null){
                return false;
            }
            if (value.containsKey("closed")){
                if ((int)value.get("closed") == 1){
                    return false;
                }
            }
            return true;
        }
        
        public Thread getThread(int id, boolean updateCached){ //returns thread object, updates cache if needed
            
            Thread cachedThread = cache.get(new Integer(id));
            if(cachedThread == null){ //not cached
                
                Thread newThread = new Thread(name, id);
                cache.put(id, newThread);
                
                return cache.get(id);
            }
            if(!updateCached){ //just return the cached thread if necessary
                return cachedThread;
            }
            Thread newThread = new Thread(name, id);
            if(newThread.is404()){ //return null on a 404ed thread
                cache.remove(new Integer(id)); //remove thread from cache
                return null;
            }
            //if here, the cache needs to be updated and the thread is not closed
            
            cache.put(new Integer(id), newThread);
            return newThread;
        }
        
        public List<Thread> getThreads(){ //returns List of threads on page 1 (default setting)
            return getThreads(1);
        }
        public List<Thread> getThreads(int page){  //get all threads on specific page
            if(page <= 0 || pageCount() < page) { //invalid page returns null
                return null;
            }
            List<Thread> myList = new LinkedList<>();
            JSONObject jsonobj = (JSONObject) JSONFetcher.vomit(url + "/" + page + ".json");
            
            JSONArray threads = (JSONArray) jsonobj.get("threads");
            for (Object t: threads) {
                JSONArray posts = (JSONArray) ((JSONObject) t).get("posts");
                myList.add(new Thread(name, (long)((JSONObject)posts.get(0)).get("no")));
                
            }
            return myList;
        }
        
        public List<Thread> getAllThreads(){    //list of all thread objects in board
            List<Thread> myList = new LinkedList<>();
            JSONArray jsonobj = (JSONArray) JSONFetcher.vomit(url + "/threads.json");
            
            for(Object o: jsonobj) {
                JSONObject page = (JSONObject) o;
                JSONArray pageArray = (JSONArray) page.get("threads");
                for (Object t: pageArray) {
                    JSONObject thread = (JSONObject) t;
                    
                    Long id = (Long) thread.get("no");
                    
                    
                    Thread newThread = new Thread(name, (long) thread.get("no"));
                    myList.add(newThread);
                    cache.put(new Integer((int) (long) id), newThread);//adds thread to cache
                    
                    
                    
                }
            }
            return myList;
            
        }
        
        public List<Thread> getCachedThreads() {
            return new ArrayList<Thread>(cache.values());
        }
        
        public List<Integer> getAllThreadIds(){ //get list of all thread id's
            LinkedList<Integer> threadIDs = new LinkedList<>();
            JSONArray jsonobj = (JSONArray) JSONFetcher.vomit(url + "/threads.json");
            for(Object o: jsonobj) {
                JSONObject page = (JSONObject) o;
                JSONArray pageArray = (JSONArray) page.get("threads");
                for (Object t: pageArray) {
                    JSONObject thread = (JSONObject) t;
                    threadIDs.add((int) (long) thread.get("no"));
                }
            }
            return threadIDs;
        }
        
        public String getName(){
            return this.name;
        }
        public String getURL(){
            return this.url;
        }
        
        
        
        public boolean isWorksafe(){ //is this nsfw
            //metadata should never be null
            return (long) metadata.get("ws_board") == 1;
        }
        public int pageCount(){ //how many pages does this board have
            return (int)(long) metadata.get("pages");
        }
        public int threadsPerPage(){  //max threads per page?
            return (int) (long) metadata.get("per_page");
        }
        public String title(){ //what is the title ex: Fitness
            return (String) metadata.get("title");
        }
        public String description(){ //ex:" /fit/ - Fitness is for exercise"
           
            return (String) metadata.get("meta_description");
        }
        
        public void refreshCache(){
            clearCache();
            getAllThreads();
            
        }
        
        public void clearCache(){
            cache = new HashMap<Integer, Thread>();
        }
    }
}