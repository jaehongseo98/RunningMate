package com.example.project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class simpleDB {
    private static Map<String, ArticleVO> db;

    public static void addArticle(String index, ArticleVO articleVO) {
        db.put(index, articleVO);
    }

    public static ArticleVO getArticle(String index) {
        return db.get(index);
    }

    public static List<String> getArticle() {
        Iterator<String> keys = db.keySet().iterator();

        List<String> keyList = new ArrayList<String>();
        String key = "";
        while(keys.hasNext()) {
            key = keys.next();
            keyList.add(key);
        }

        return keyList;
    }
}