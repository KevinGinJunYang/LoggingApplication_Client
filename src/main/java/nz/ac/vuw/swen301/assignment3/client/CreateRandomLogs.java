package nz.ac.vuw.swen301.assignment3.client;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

public class CreateRandomLogs {

    public static void main(String[] args) throws InterruptedException {
        Logger logger = Logger.getLogger("RandomLogger");
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        logger.addAppender(appender );
        Random random = new Random();
        String[] types = {"ALL","DEBUG","INFO","WARN","ERROR","FATAL","TRACE","OFF"};

        while(true){
            Thread.sleep(1000);
            int randomIndex = random.nextInt(types.length);
            int length = 10;
            boolean useLetters = true;
            boolean useNumbers = false;
            String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
            logger.setLevel(Level.toLevel(types[randomIndex]));
            logger.warn(generatedString);
            logger.debug(generatedString);
            logger.error(generatedString);
            logger.fatal(generatedString);
            logger.info(generatedString);
            logger.trace(generatedString);
       }
    }
}
