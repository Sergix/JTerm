package jterm.command;

import java.time.LocalDate;
import java.util.ArrayList;

public class Date {
    public Date(ArrayList<String> options) {
        if (options.contains("-h")) {
            System.out.println("Command syntax:\n\techo [-h] input\n\nPrints the specified input to the console."); // Options
            return;
        }
        System.out.println("The Current Date is " + LocalDate.now() + " (yyyy-mm-dd)");
    }
}
