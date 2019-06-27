package nz.ac.vuw.swen301.assignment3.client;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Resthome4LogsAppender extends AppenderSkeleton {
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private TimeZone tz = TimeZone.getTimeZone("GMT");

    @Override
    protected void append(LoggingEvent loggingEvent) {
        List<LogEvent> currentLogs = new ArrayList<>();
        df.setTimeZone(tz);
        URI uri = null;
        URI uri2 = null;
        try {
            uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("localhost")
                    .setPort(8080)
                    .setPath("resthome4logs/logs")
                    .build();

            uri2 = new URIBuilder()
                    .setScheme("http")
                    .setHost("localhost")
                    .setPort(8080)
                    .setPath("resthome4logs/stats")
                    .build();

            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(uri);
            HttpPost httpPost2 = new HttpPost(uri2);
            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();
            String message = loggingEvent.getMessage().toString();
            String time = df.format(new Date(loggingEvent.getTimeStamp()));
            String thread = loggingEvent.getThreadName();
            String logger = loggingEvent.getLoggerName();
            String logLevel = loggingEvent.getLevel().toString();
            String errorDetails = "String";

            LogEvent logs = new LogEvent(randomUUIDString,message,time,thread,logger,logLevel,errorDetails);
            currentLogs.add(logs);
            String jsonString = new Gson().toJson(currentLogs);
            StringEntity entity = new StringEntity(jsonString);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            client.execute(httpPost);
            httpPost2.setEntity(entity);
            httpPost2.setHeader("Accept", "application/vnd.ms-excel");
            httpPost2.setHeader("Content-type", "application/vnd.ms-excel");
            client.execute(httpPost2);
            client.close();

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }


}
