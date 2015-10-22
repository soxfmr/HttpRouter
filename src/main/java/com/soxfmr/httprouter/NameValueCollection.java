package com.soxfmr.httprouter;

import java.util.AbstractCollection;
import java.util.HashMap;
import java.util.Iterator;

public class NameValueCollection extends AbstractCollection<NameValuePair> {

    private HashMap<String, String> pairList;

    public NameValueCollection() {
        this(null);
    }

    public NameValueCollection(HashMap<String, String> pairList) {
        if (pairList == null) {
            pairList = new HashMap<>();
        }

        this.pairList = pairList;
    }

    @Override
    public Iterator<NameValuePair> iterator() {
        // Give the iterator from pair list
        final Iterator<String> pairIterator = pairList.keySet().iterator();
        // Resolve the String iterator to NameValuePair
        return new Iterator<NameValuePair>() {

            @Override
            public boolean hasNext() {
                return !pairList.isEmpty() && pairIterator.hasNext();
            }

            @Override
            public NameValuePair next() {
                String name = pairIterator.next();
                String value = pairList.get(name);

                return new NameValuePair(name, value);
            }
        };
    }

    public void add(String key, String value) {
        pairList.put(key, value);
    }

    @Override
    public boolean add(NameValuePair nameValuePair) {
        if (nameValuePair == null)
            return false;

        pairList.put(nameValuePair.getName(), nameValuePair.getValue());
        return true;
    }

    public String get(String key) {
        return pairList.get(key);
    }

    @Override
    public boolean remove(Object key) {
        pairList.remove(key);
        return true;
    }

    @Override
    public int size() {
        return pairList.size();
    }

    @Override
    public void clear() {
        pairList.clear();
    }

    @Override
    public boolean isEmpty() {
        return pairList.isEmpty();
    }

}
