package com.fidenz.dev.crmdemo.Models;

/**
 * Created by fidenz on 4/13/18.
 */

public class Notes {

    String noteid;
    String notetext;
    String time;
    String noteimage;

    public Notes() {
    }

    public Notes(String noteid, String notetext, String time, String noteimage) {
        this.noteid = noteid;
        this.notetext = notetext;
        this.time = time;
        this.noteimage = noteimage;
    }

    public String getNoteid() {
        return noteid;
    }

    public String getNotetext() {
        return notetext;
    }

    public String getTime() {
        return time;
    }

    public String getNoteimage() {
        return noteimage;
    }
}
