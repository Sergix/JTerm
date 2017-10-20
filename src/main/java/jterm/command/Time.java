package jterm.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Time {
    public Time(ArrayList<String> options) {
        if (options.contains("-h")) {
            System.out.println("Command syntax:\n\techo [-h] input\n\nPrints the specified input to the console."); // Options
            return;
        }
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss , z");
        System.out.println("The Current Time is: " + dateFormat.format(date));
    }
}