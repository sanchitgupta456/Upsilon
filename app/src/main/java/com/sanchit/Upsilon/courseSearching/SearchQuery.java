package com.sanchit.Upsilon.courseSearching;

import android.location.Location;

public class SearchQuery {
    String keywords = "";

    rankBy rank = rankBy.RATING;

    public String getKeywords() {return keywords;}

    public rankBy getRankMethod() {return rank;}

    public void setRankMethod(rankBy method) {rank = method;}

    public void setQuery(String query) {keywords = query;}
}
