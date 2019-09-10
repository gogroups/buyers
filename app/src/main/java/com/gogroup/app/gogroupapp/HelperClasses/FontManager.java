package com.gogroup.app.gogroupapp.HelperClasses;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Sandeep on 18-May-17.
 */

public class FontManager {

    public static String path = "fonts/",IONICONFONT = "ionicons.ttf", FONTAWESOME_ICON = "fontawesome-webfont.ttf";

    public static Typeface getTypeFaceFromFontName(Context context, String name){
        return  Typeface.createFromAsset(context.getAssets(),path+name);
    }
}
