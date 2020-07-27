package com.example.mynotes;

import android.content.Context;

/**
 * Created by SUDA on 16-10-2017.
 */

public class GetTimeAgo {
    private static final int seconds = 1000;
    private static final int minutes = 60 * seconds;
    private static final int hours = 60 * minutes;
    private static final int days = 24 * hours;

    public static String GetTimeAgo(long time, Context ctx){

        if (time < 1000000000000L) {

            // If timestamp given in seconds, convert to millis
            time *= 1000;

        }

        long now = System.currentTimeMillis();

        if (time > now || time <= 0){

            return null;

        }

        // TODO: localize
        final long diff = now - time;

        if (diff < minutes){
            return "just now";
        } else if (diff < 2 * minutes) {
            return "a minute ago";
        } else if (diff < 50 * minutes){
            return (diff / minutes + " minutes ago");
        } else if (diff < 90 * minutes) {
            return "an hour ago";
        } else if (diff < 24 * hours){
            return (diff / hours + " hours ago");
        } else if (diff < 48 * hours){
            return "yesterday";
        } else {
            return diff / hours + " days ago";
        }

    }
}
