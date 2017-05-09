package javach;

import org.json.simple.JSONObject;

public class Post { //represents a 4chan post
    
    private JSONObject data; //the JSONObject containing post metadata
    private Thread thread; //reference to it's thread
    private File file;     //reference to it's file
    
    public Post(Thread thread, JSONObject data){ 
        this.data = data;
        this.thread = thread;
        
        if(this.hasFile()){
            file = new File(this);
        }
    }
    
    public int getPostID(){ //returns postID
        return (int) (long) data.get("no");
    }
    public String getPosterID(){ //returns poster ID (may be null)
        
        return (String) data.get("id");
    }
    public String getPosterName(){ //returns poster name (may be null)
        return (String) data.get("name");
    }
    public String getText(){ //returns text of post. May be null if blank
        return (String) data.get("com");
    }
    public int timestamp(){ //returns timestamp
        return (int) (long) data.get("tim");
    }
    public boolean isOP(){ //checks if the post is the OP of the thread
        return thread.OriginalPost.equals(this);
    }
    public String trip(){ //returns tripcode of poster. may be null
        return (String) data.get("trip");
    }
    public String subject(){ //returns subject of post. may be null
        return (String) data.get("sub"); 
    }
    public String date(){   //date and time. ex: 12\/24\/16(Sat)14:10:39
        return (String) data.get("now");
    }
    public JSONObject getData(){ //returns data as a JSON obj
        return data;
    }
    
    //File related methods
    public boolean hasFile(){
        return data.containsKey("filename");
    }
    
    public File getFile(){
        return file;
    }
    
    //URL related
    public String url(){
        return thread.url() + "#p" + data.get("no");
    }
    public String semanticUrl(){
        return thread.semanticUrl() + "#p" + data.get("no");
    }
    
    //misc accessors
    
    public Thread containingThread(){
        return thread;
    }
    
    public File file(){
        return file;
    }
}