package com.as4.galaxyfunpark;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User HP on 4/9/2016.
 */
public class session {

    public static String userID = "0";
    public static String name = "0";
    public static String place = "0";
    public static String SPID = "0";
    public static String SName = "0";
    public static String Date="0";

    public static String url  = "http://hiwashportal.com/GFPAPI/glaxypark.php";
    public static List<String> items =  new ArrayList<>();
    public static List<Integer> itemsCount =  new ArrayList<>();
    public static List<item_object> listBasket= new ArrayList<item_object>();


    public static boolean mIsConnected = false;

    public static String PHeader  =
            "********************************\n" +
            "*       Galaxy Fun Park        *\n" +
            "********************************\n" ;;
    public static String PFooter  =
            "********************************\n" +
            "Thank You For Using Our Services\n" +
            "********************************\n" +
            "      Tel : +974 44087338       \n" +
            "      Fax : +974 44087334       \n" +
            "********************************\n" ;;


}

