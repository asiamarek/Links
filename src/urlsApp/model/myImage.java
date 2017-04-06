package urlsApp.model;

public class myImage {
    private int size;
    private String link;

    public myImage(String link, String sourceBeg){
        this.size=0;
        if(!(link.startsWith("http://")||link.startsWith("https://"))){
            this.link=sourceBeg+link;
        }else
            this.link=link;
    }

    public int getSize(){
        return size;
    }

    public void setSize(int size){
        this.size=size;
    }


    public String getLink(){
        return link;
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof myImage))
            return false;
        if (o == this)
            return true;
        myImage oImg=(myImage) o;
        return (oImg.link.equals(this.link));
    }

    @Override
    public int hashCode(){
        return this.link.hashCode();
    }

}
