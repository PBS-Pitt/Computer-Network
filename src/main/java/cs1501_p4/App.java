/**
 * A driver for CS1501 Project 4
 * @author	Dr. Farnan
 */
package cs1501_p4;

import java.io.FileNotFoundException;

public class App {
    public static void main(String[] args) throws FileNotFoundException {
        NetAnalysis na = new NetAnalysis("build/resources/main/network_data2.txt");
    }
}
