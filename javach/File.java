package javach;

import org.json.simple.JSONObject;

public class File {
    //represents a file objects on a specific post
    
    private Post post; //parent post 
    private JSONObject data;    //parent post's data
    
    public File(Post post){
        this.post = post;
        this.data = post.getData();
    }
    
    public String md5(){ //md5 of the file
        return (String) data.get("md5");
    }
    public String extension(){ //extension of the file
        return (String) data.get("ext");
    }
    public int filesize(){      //filesize in bytes
        return (int) (long) data.get("fsize");
    }
    public int width(){         //in pixels
        return (int) (long) data.get("w");
    }
    public int height(){
        return (int) (long) data.get("h");
    }
    public boolean deleted(){ //was the file later deleted?
        return data.containsKey("filedeleted") && (int) (long) data.get("filedeleted") == 1;
    }
    public String filenameOriginal(){ //original filename 
        return (String) data.get("filename") + extension();
    }
    public String filename(){       //new filename
        return Long.toString((long)data.get("tim")) + extension();
    }
    
    public String url(){ //format: http(s)://i.4cdn.org/board/tim.ext
                        
        return URL.fileURL("https://", this);
        
    }
    
    public String thumbName(){  //name of the thumbnail
        return Long.toString((long) data.get("tim")) + "s.jpg";
    }
    public String thumbUrl(){ //url of the thumbnail
        return URL.thumbURL("https://", this);
        
    }

    public int thumbnailWidth(){ //in pixels
        return (int) (long) data.get("tn_w");
    }
    public int thumbnailHeight(){
        return (int) (long) data.get("tn_h");
    }
    
    public Post post(){ //note to self - possibly pointless method
        return post;
    }
}