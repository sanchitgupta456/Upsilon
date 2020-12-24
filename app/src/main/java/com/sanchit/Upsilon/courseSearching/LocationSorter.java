package com.sanchit.Upsilon.courseSearching;

import org.bson.Document;

import java.util.Comparator;

public class LocationSorter implements Comparator<Document>
{
    @Override
    public int compare(Document o1, Document o2) {
        return o1.getDouble("courseDistance").compareTo(o2.getDouble("courseDistance"));
    }
}