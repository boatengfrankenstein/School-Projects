package com.team_08.hi_run.hi_run.util;

import java.text.DecimalFormat;

/**
 * Created by Sohail
 */
public class Utils {


    public static String formatNumber( int value) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formatted = formatter.format(value);

        return formatted;
    }
}
