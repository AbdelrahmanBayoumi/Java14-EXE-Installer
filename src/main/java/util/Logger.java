package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Used to log Exceptions and Information
 *
 * @author Abdelrahman Bayoumi
 */
public class Logger {

    //========= Helper Objects =========
    private static volatile SimpleDateFormat DATE_TIME_FORMAT = null;
    private static final Object LOCK = new Object();
    private static volatile PrintWriter PRINT_WRITER = null;

    private Logger() {
        if(PRINT_WRITER != null){
            throw new RuntimeException("Use init() method to create");
        }
    }
    /**
     * initialize printWriter object to log data in a file
     */
    public static void init() {
        try {
            if (PRINT_WRITER == null) {
                synchronized (Logger.class) {
                    if (PRINT_WRITER == null) {
                        PRINT_WRITER = new PrintWriter(new FileWriter("logs.txt", true));
                        DATE_TIME_FORMAT = new SimpleDateFormat("[dd-MM-yyyy] [hh:mm:ss a]");
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }

    public static void info(String msg) {
        new Thread(() -> {
            synchronized (LOCK) {
                String DataAndTime = DATE_TIME_FORMAT.format(new Date());
                System.out.println(msg);
                PRINT_WRITER.println(DataAndTime + " => " + msg);
                PRINT_WRITER.flush();
            }
        }).start();
    }

    public static void error(String msg, Throwable throwable, String CLASS_NAME) {
        new Thread(() -> {
            synchronized (LOCK) {
                String DataAndTime = DATE_TIME_FORMAT.format(new Date());
                String m = DataAndTime + " => "
                        + "Exception[ " + throwable.getLocalizedMessage() + " ] in => "
                        + CLASS_NAME;
                m += (msg != null) ? (" => " + msg) : "";
                System.err.println(m);
                PRINT_WRITER.println(m);
                PRINT_WRITER.flush();
            }
        }).start();
    }
}
