package urlsApp.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class currentURL {
    private String prefix;
    private URL address;
    private String link;
    private int sizeOfImages;
    private int nrOfImages;
    private int nrOfLinks;
    private ConcurrentHashMap<myLink,myLink> setOfLinks;
    private ConcurrentHashMap<myImage,myImage> setOfImages;

    public currentURL(String link) throws Exception {
        if( !rightLink(link))
            throw new Exception("wrong link");
        this.link=link;
        try {
            this.address=new URL(link);
        } catch (MalformedURLException e) {
        }

        this.prefix=address.getProtocol()+"://"+address.getHost();

        this.nrOfImages=0;
        this.nrOfLinks=0;
        this.sizeOfImages=0;

        this.setOfImages=new ConcurrentHashMap<myImage, myImage>();
        this.setOfLinks=new ConcurrentHashMap<myLink,myLink>();
    }

    private boolean rightLink(String link){
        if ((link==null || link.length()==0
                ||link.equals("http://")|| link.equals("https://")
                || link.contains(" ")))
            return false;
        return true;

    }

    private boolean isImage(String link){
        return(link.endsWith(".jpg")||link.endsWith(".jpeg")||link.endsWith(".png") ||
                link.endsWith(".gif"));
    }

    private void addImage(Element newImage){
        if(!(newImage.tagName().equals("img")))
            return;
        int imgSize=0;
        URLConnection connection;
        myImage img=new myImage(newImage.attr("src"),this.prefix);
        try {
            connection = new URL(img.getLink()).openConnection();
        } catch (IOException e) {    return;    }

        imgSize=connection.getContentLength();
        img.setSize(imgSize);

        setOfImages.put(img,img);
    }


    public void parseURL() throws IOException {
        Document doc= Jsoup.connect(link).get();
        Elements allLinks=doc.select("a[href]");
        Elements allImages=doc.select("[src]");


        for(Element elem :allLinks){
            String temp= elem.attr("href");
            if(!isImage(temp)){
                myLink tempLink =new myLink(elem.attr("href"),this.prefix);
                setOfLinks.put(tempLink,tempLink);
            }
        }

        final CountDownLatch counter=new CountDownLatch(allImages.size());

        for(final Element elem:allImages){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    addImage(elem);
                    counter.countDown();
                }
            }).start();
        }

        try {
            counter.await();
        } catch (InterruptedException e) {}

        nrOfLinks=setOfLinks.size();
        nrOfImages=setOfImages.size();

        for(myImage key : setOfImages.keySet()){
            sizeOfImages+=key.getSize();
        }


    }


    public int getSizeOfImages(){
        return sizeOfImages;
    }

    public int getNrOfImages(){
        return nrOfImages;
    }

    public int getNrOfLinks(){
        return nrOfLinks;
    }


    public ConcurrentHashMap<myLink,myLink> getSetOfLinks(){
        return this.setOfLinks;
    }


}
