package com.genie97.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {

    @SerializedName("meta")
    ResultMeta meta;

    @SerializedName("documents")
    List<ResultDocuments> documents;

    public ResultMeta getResultMeta(){
        return meta;
    }

    public List<ResultDocuments> getResultDocuments(){
        return documents;
    }

    public class ResultMeta{
        @SerializedName("total_count") int total_count;
        @SerializedName("pageable_count") int pageable_count;
        @SerializedName("is_end") boolean is_end;

        public int getTotal_count(){
            return total_count;
        }

        public int getPageable_count(){
            return pageable_count;
        }

        public boolean getIs_end(){
            return is_end;
        }
    }

    public class ResultDocuments{
        @SerializedName("collection") String collection;
        @SerializedName("thumbnail_url") String thumbnail_url;
        @SerializedName("image_url") String image_url;
        @SerializedName("width") int width;
        @SerializedName("height") int height;
        @SerializedName("display_sitename") String display_sitename;
        @SerializedName("doc_url") String doc_url;
        @SerializedName("datetime") String date_time;

        public String getCollection(){
            return collection;
        }
        public String getThumbnail_url(){
            return thumbnail_url;
        }
        public String getImage_url(){ return image_url; }
        public int getWidth(){
            return width;
        }
        public int getHeight(){
            return height;
        }
        public String getDisplay_sitename(){
            return display_sitename;
        }
        public String getDoc_url(){
            return doc_url;
        }
        public String getDate_time(){
            return date_time;
        }
    }
}