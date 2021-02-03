package com.sanchit.Upsilon.courseSearching;

import org.bson.Document;

import java.util.Comparator;

public class LocationSorter implements Comparator<Document>
{
    //Distance under which rating is preferred over distance (in km)
    Double overrideDistance = 5.00;
    @Override
    public int compare(Document o1, Document o2) {
        if (Math.abs(o1.getDouble("courseDistance")-(o2.getDouble("courseDistance"))) > overrideDistance){
            return o1.getDouble("courseDistance").compareTo(o2.getDouble("courseDistance"));
        }
        else{
            try {
                return o1.getDouble("courseRating").compareTo(o2.getDouble("courseRating"));
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Double x;
                    x = Double.parseDouble(String.valueOf(o1.get("courseRating")));
                    return x.compareTo(Double.parseDouble(String.valueOf(o1.get("courseRating"))));
                } catch (NumberFormatException numberFormatException) {
                    numberFormatException.printStackTrace();
                    return 1;
                }
            }
        }
    }
}