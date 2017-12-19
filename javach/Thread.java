package javach;

import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Thread implements Comparable<Thread> { //represents a 4chan thread
    
    private JSONArray posts; //array of posts
    private LinkedList<Post> postCache = new LinkedList<>();    //cache of Post objects
    
    private String board = ""; //name of the board. ex: a, g, tg
    private long id; //the thread id
    private boolean is404 = false;      //has the thread been closed?
    private boolean isStickied = false; //stickied?
    private boolean archived = false;   //archived?
    private boolean bumpLimit = false;  //has it hit the bumplimit
    private boolean imageLimit = false; //has it hit the imagelimit
    
    private Post OriginalPost;      //the OP of the thread
    
    public Thread(String board, long id){
        this.id = id;
        this.board = board; 
        
        populate();
    }
    
    public long getID(){
        return id;
    }
    public String getBoard(){
        return board;
    }
    public Post getOP() {
        return OriginalPost;
    }
    
    //populate all the metadata of the thread (should be used only once for efficiency)
    private void populate(){ 
        JSONObject metadata = (JSONObject) JSONFetcher.vomit("https://a.4cdn.org/" + board + "/thread/" + id + ".json");
        posts = (JSONArray) metadata.get("posts");
        
        for(Object o: posts){ //add posts to cache
            Post p = new Post(this, (JSONObject) o);
            postCache.add(p);
        }
        
        if(((JSONObject) posts.get(0)).containsKey("sticky")){
            
            isStickied = true;
        }
        if(((JSONObject) posts.get(0)).containsKey("closed")){
            
            is404 = true;
        }
        if(((JSONObject) posts.get(0)).containsKey("archived")){
            
            archived = true;
        }
        if(((JSONObject) posts.get(0)).containsKey("imagelimit")){
            
            imageLimit = true;
        }
        if(((JSONObject) posts.get(0)).containsKey("bumplimit")){
            
            bumpLimit = true;
        }
        OriginalPost = new Post(this, (JSONObject) posts.get(0));
    }
    
     //add all new posts needed, returns number of new posts, updates metadata
    public int refresh(){
        JSONObject metadata = (JSONObject) JSONFetcher.vomit("https://a.4cdn.org/" + board + "/thread/" + id + ".json");
        posts = (JSONArray) metadata.get("posts");
        if(posts.size() == postCache.size()) { //no changes
            return 0;
        }
        
        int newPosts = 0;
        for(newPosts = 0; postCache.size() < posts.size(); newPosts++) { //adds all posts
            int i = posts.size();
            postCache.add(new Post(this, (JSONObject) posts.get(i))); //add new post
        }
        
        if(((JSONObject) posts.get(0)).containsKey("sticky")){
            
            isStickied = true;
        }
        if(((JSONObject) posts.get(0)).containsKey("closed")){
            
            is404 = true;
        }
        if(((JSONObject) posts.get(0)).containsKey("archived")){
            
            archived = true;
        }
        if(((JSONObject) posts.get(0)).containsKey("imagelimit")){
            
            imageLimit = true;
        }
        if(((JSONObject) posts.get(0)).containsKey("bumplimit")){
            
            bumpLimit = true;
        }
        
        return newPosts;
    }
    
    public boolean isStickied(){
        if(posts == null){
            populate();
        }
        return isStickied;
    }
    public String board(){
        return board;
    }
    public long ID(){
        return id;
    }
    public boolean is404(){
        if(posts == null){
            populate();
        }
        return is404;
    }
    public boolean isArchived(){
        if(posts == null){
            populate();
        }
        return archived;
    }
    public boolean bumpLimit(){
        if(posts == null){
            populate();
        }
        return bumpLimit;
    }
    public boolean imageLimit(){
        if(posts == null){
            populate();
        }
        return imageLimit;
    }
    
    @SuppressWarnings("unchecked")
    public int customSpoiler(){ //amount of custom spoilers
        if(posts == null){
            populate();
        }
        return (int) ((JSONObject) posts.get(0)).getOrDefault("custom_spoiler", 0);
    }
    
    //File related accessors
    public List<String> thumbUrls(){
        List<String> urls = new LinkedList<>();
        for(Post p: postCache){
            if(p.hasFile()){
                urls.add(p.getFile().thumbUrl());
            }
        }
        return urls;
    }
    
    public List<String> fileUrls(){
        List<String> urls = new LinkedList<>();
        for(Post p: postCache){
            if(p.hasFile()){
                urls.add(p.getFile().url());
            }
        }
        return urls;
    }
    
    public List<File> fileList(){
        List<File> fileList = new LinkedList<>();
        for(Post p : postCache){
            if(p.hasFile()){
                fileList.add(p.getFile());
            }
        }
        return fileList;
    }
    
    public List<String> filenames(){
        List<String> filenames = new LinkedList<>();
        for(Post p: postCache){
            if(p.hasFile()){
                filenames.add(p.getFile().filename());
            }
            
        }
        
        return filenames;
    }
    
    public List<String> thumbnames(){
        List<String> thumbnames = new LinkedList<>();
        for(Post p: postCache){
            if(p.hasFile()){
                thumbnames.add(p.getFile().thumbName());
            }
        }
        return thumbnames;
    }

    @Override
    public int compareTo(@NotNull Thread comparesTo) {
        int comparison = (int) ((Thread)comparesTo).getID();

        return comparison - (int)this.id;
    }

    public LinkedList<Post> getPosts() {
        return postCache;
    }
    
    //URL accessors
    public String url(){
        return URL.threadURL("https://", this);
    }
    public String semanticUrl(){
        return url() + "/" + semanticSlug();
    }
    public String semanticSlug(){
        return (String) ((JSONObject) posts.get(0)).get("semantic_url");
    }
}