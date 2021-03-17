package com.ivory.isasa.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * describe: 本次存储操作
 *
 * @Author: Aaron
 * @Date: 2021/3/16 15:31
 */
public class SharedPreferencesUtil {
    private static SharedPreferences sharedPreferences;

    public static void putBoolean(Context context,String key,Boolean value){
        if (sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putBoolean(key,value).apply();
    }

    public static boolean getBoolean(Context context, String key){
        if (sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key,false);
    }

    public  static void putString(Context context,String key,String value){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putString(key, value).apply();
    }

    public  static String getString(Context context,String key){ if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(key, "");
    }

    public  static void putInt(Context context,String key,int value){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public  static int getInt(Context context,String key){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sharedPreferences.getInt(key, 0);
    }

    public static void remove(Context context,String key){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().remove(key).apply();
    }

    public static void clear(Context context){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().clear().apply();
    }
}
