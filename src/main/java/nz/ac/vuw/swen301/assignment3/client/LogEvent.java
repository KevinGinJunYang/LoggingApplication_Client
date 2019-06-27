package nz.ac.vuw.swen301.assignment3.client;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class LogEvent {

    private String id;
    private String message;
    private String thread;
    private String timeStamp;
    private String logger;
    private String level;
    private String errorDetails;

    public LogEvent(String id, String message, String timestamp, String thread,
                    String logger, String level, String errorDetails) {
        this.id = id;
        this.message = message;
        this.timeStamp = timestamp;
        this.thread = thread;
        this.logger = logger;
        this.level = level;
        this.errorDetails = errorDetails;
    }


    public LogEvent() {
    }

    private String getId() {
        return id;
    }

    private String getMessage() {
        return message;
    }

    private String getTimeStamp() {
       return timeStamp;
    }

    private String getThread() {
        return thread;
    }

    private String getLogger() {
        return logger;
    }

    private String getLevel() {
        return level;
    }

    private String getErrorDetails() {
        return errorDetails;
    }


    public String toString() {
        return "" + this.getId() + "\n"
                + this.getMessage() + "\n"
                + getTimeStamp() + "\n"
                + this.getThread() + "\n"
                + this.getLogger() + "\n"
                + this.getLevel() + "\n"
                + this.getErrorDetails() + "";
    }

}
