package com.example.kobac.myapplication.details;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by kobac on 6.9.17..
 */

public class FontClass {

    public static Typeface getHelvetica(Context c){
        return Typeface.createFromAsset(c.getAssets(), "fonts/HLT.ttf");
    }

    public static Typeface getRobotoLight(Context c){
        return Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-Light.ttf");
    }

}
