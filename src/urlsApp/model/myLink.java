package urlsApp.model;

public class myLink {
    private String wholeLink;

    public myLink(String partLink, String source){
        if(!(partLink.startsWith("http://")||partLink.startsWith("https://"))){
            this.wholeLink=source+partLink;
        }else
            this.wholeLink=partLink;
    }

    public boolean equals(Object o){
        if (!(o instanceof myLink))
            return false;
        if (o == this)
            return true;
        myLink oLink=(myLink) o;
        return (oLink.wholeLink.equals(this.wholeLink));
    }

    @Override
    public int hashCode(){
        return this.wholeLink.hashCode();
    }

    public String getWholeLink(){
        return this.wholeLink;
    }


    @Override
    public String toString(){
        return wholeLink;
    }
}
