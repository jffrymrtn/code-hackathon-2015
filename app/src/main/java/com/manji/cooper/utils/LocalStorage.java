package com.manji.cooper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.manji.cooper.custom.CSVData;
import com.manji.cooper.custom.ItemInfo;
import com.manji.cooper.managers.DataManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public class LocalStorage {

    private final static String TAG = LocalStorage.class.getSimpleName();

    public final static String STORAGE_KEY = "storage_key";
    public final static String DATA_SETS_TAG = "data_sets";
    public final static String ALL_ITEMS_TAG = "all_items";


    protected SharedPreferences sharedPref;
    protected SharedPreferences.Editor editor;

    public LocalStorage(String preferencesKey) {

        this.sharedPref = Utility.activity.getSharedPreferences(preferencesKey,
                Context.MODE_PRIVATE);
        this.editor = this.sharedPref.edit();
    }

    /**
     * Serializes object and stores it to shared preferences
     */
    public void serializeAndStore(String tag, Serializable obj) {

        try {
            editor.putString(tag, ObjectSerializer.serialize(obj));
            commit();
        } catch (IOException e){ e.printStackTrace();}
    }

    @SuppressWarnings("unchecked")
    public Object retrieveObject(String tag){
        if (containsKey(tag)){
            try{
                return ObjectSerializer.deserialize(getString(tag));
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Retrieve a string value associated with this settings.
     */
    public String getString(String key) {
        return sharedPref.getString(key, "");
    }

    /**
     * Set a string value associated with the key.
     */
    public void setString(String key, String value) {
        editor.putString(key, value);
    }

    /**
     * @return Returns true if the key exists in the preferences
     */
    public boolean containsKey(String key) {
        return sharedPref.getAll().keySet().contains(key);
    }

    public void clear() {
        editor.clear();
    }

    /**
     *
     * @return Returns true if successful
     */
    public boolean commit() {
        return editor.commit();
    }

    public void storeData(String tag, HashMap<Integer, CSVData> data){
        HashMap<Integer, CSVData> obj = new HashMap();

        if (tag == DATA_SETS_TAG){
            for (Integer k: data.keySet()){
                obj.put(k, data.get(k));
            }
        }

        serializeAndStore(tag, obj);
    }

    public void storeItems(String tag, HashMap<String, ItemInfo> data){
        HashMap<String, ItemInfo> obj = new HashMap();

        if (tag == ALL_ITEMS_TAG){
            for (String k: data.keySet()){
                obj.put(k, data.get(k));
            }
        }

        serializeAndStore(tag, obj);
    }

}