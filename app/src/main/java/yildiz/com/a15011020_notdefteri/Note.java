package yildiz.com.a15011020_notdefteri;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Note implements Serializable {
    static final long serialVersionUID =5;
    String Title,Note;
    List<String> FileNames;
    List<String> FilePaths;
    String Date,Hour,RemindDate,RemindHour;
    String Oncelik;

    int id;
    static int ID_Number=0;

    int Freq;
    public Note(String Title,String Note,List<String> FileNames,List<String> FilePaths,String Date,String Hour,String Oncelik){
        this.Title=Title;
        this.Note=Note;
        this.FileNames=FileNames;
        this.FilePaths=FilePaths;
        this.Hour=Hour;
        this.Date=Date;
        this.Oncelik=Oncelik;
        this.id=ID_Number;
        ID_Number+=1;
    }
    public void setReminTimes(String Date,String Hour){
        RemindDate=Date;
        RemindHour=Hour;
    }



}
