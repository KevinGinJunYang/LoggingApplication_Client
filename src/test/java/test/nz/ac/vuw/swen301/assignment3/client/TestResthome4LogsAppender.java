package test.nz.ac.vuw.swen301.assignment3.client;

import nz.ac.vuw.swen301.assignment3.client.Resthome4LogsAppender;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.*;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class TestResthome4LogsAppender {
    private Logger logger = Logger.getLogger(Resthome4LogsAppender.class);
    private Resthome4LogsAppender appender;
    private static final String TEST_HOST = "localhost";
    private static final int TEST_PORT = 8080;
    private static final String TEST_PATH = "/resthome4logs"; // as defined in pom.xml
    private static final String SERVICE_PATH = TEST_PATH + "/logs"; // as defined in pom.xml and web.xml
    private static final String STATS_PATH = TEST_PATH + "/stats";

    @BeforeClass
    public static void setup() {
        BasicConfigurator.configure();
    }

    @Before
    public void setUp() {
        appender = new Resthome4LogsAppender();;
        logger.addAppender(appender);
    }

    @After
    public void clean() {
        appender.close();
    }

    private HttpResponse get(URI uri) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);
        return httpClient.execute(request);
    }

    private boolean isServerReady() throws Exception {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(TEST_PATH);
        URI uri = builder.build();
        try {
            HttpResponse response = get(uri);
            boolean success = response.getStatusLine().getStatusCode() == 200;

            if (!success) {
                System.err.println("Check whether server is up and running, request to " + uri + " returns " + response.getStatusLine());
            }

            return success;
        } catch (Exception x) {
            System.err.println("Encountered error connecting to " + uri + " -- check whether server is running and application has been deployed");
            return false;
        }
    }

    @Test
    public void testAppender() throws Exception {
        Assume.assumeTrue(isServerReady());

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH).setParameter("limit", "11").setParameter("level","WARN");
        URI uri = builder.build();

        logger.setLevel(Level.WARN);
        logger.warn("warn message");

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);

        HttpResponse response = client.execute(request);
        String responseData = EntityUtils.toString(response.getEntity());
        System.out.println("server response: " + responseData);

        assertEquals(200,response.getStatusLine().getStatusCode());
        assertTrue(responseData.contains("warn message"));
    }

    @Test
    public void testAppender2() throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger log3 = Logger.getLogger("test");
        log3.addAppender(appender);
        log3.setLevel(Level.FATAL);
        log3.trace("teeeeeeeeeeeest");
        log3.fatal("fatal error");

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH).setParameter("limit", "11").setParameter("level","FATAL");
        URI uri = builder.build();

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);

        HttpResponse response = client.execute(request);
        String responseData = EntityUtils.toString(response.getEntity());
        System.out.println("server response: " + responseData);

        assertEquals(200,response.getStatusLine().getStatusCode());
        assertFalse(responseData.contains("teeeeeeeeeeeest"));
        assertTrue(responseData.contains("fatal error"));

        client.close();
    }

    @Test
    public void testAppender3() throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger log4 = Logger.getLogger("test3");
        log4.addAppender(appender);
        log4.setLevel(Level.ALL);
        log4.trace("trace");
        log4.debug("debug");
        log4.info("info");
        log4.warn("warn");
        log4.trace("error");
        log4.fatal("fatal");

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH).setParameter("limit", "11").setParameter("level","TRACE");
        URI uri = builder.build();

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);

        HttpResponse response = client.execute(request);
        String responseData = EntityUtils.toString(response.getEntity());
        System.out.println("server response: " + responseData);

        assertEquals(200,response.getStatusLine().getStatusCode());
        assertTrue(responseData.contains("trace"));
        assertTrue(responseData.contains("debug"));
        assertTrue(responseData.contains("info"));
        assertTrue(responseData.contains("warn"));
        assertTrue(responseData.contains("error"));
        assertTrue(responseData.contains("fatal"));

    }

    @Test
    public void testAppender4() throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger log5 = Logger.getLogger("test222");
        log5.addAppender(appender);
        log5.setLevel(Level.ALL);
        log5.warn("warn message");

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH).setParameter("limit", "11").setParameter("level","WARN");
        URI uri = builder.build();

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);

        HttpResponse response = client.execute(request);
        String responseData = EntityUtils.toString(response.getEntity());
        System.out.println("server response: " + responseData);

        assertEquals(200,response.getStatusLine().getStatusCode());
        assertTrue(responseData.contains("test222"));
    }

    @Test
    public void testAppender5() throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger log6 = Logger.getLogger("test22222222222222");
        log6.addAppender(appender);
        log6.setLevel(Level.ALL);
        log6.warn("warn message");

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH).setParameter("limit", "11").setParameter("level","SS");
        URI uri = builder.build();

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);

        HttpResponse response = client.execute(request);
        String responseData = EntityUtils.toString(response.getEntity());
        System.out.println("server response: " + responseData);

        assertEquals(400,response.getStatusLine().getStatusCode());
    }

    @Test
    public void testLayout(){
        assertFalse(appender.requiresLayout());
    }

    @Test
    public void testStats() throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger log7 = Logger.getLogger("test222");
        log7.addAppender(appender);
        log7.setLevel(Level.ALL);
        log7.warn("warn message");

        URLConnection connection = new URL("http://localhost:8080/resthome4logs/stats").openConnection();

        String data = IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8.name());
        assertTrue(connection.getContentType().startsWith("application/vnd.ms-excel"));
        Assert.assertTrue(data.contains("Thread Count"));


    }

    @Test
    public void testStats2() throws Exception {
        Assume.assumeTrue(isServerReady());
        URLConnection connection = new URL("http://localhost:8080/resthome4logs/stats").openConnection();

        InputStream response23 = connection.getInputStream();
        Workbook workbook2 = new HSSFWorkbook(response23);
        workbook2.write(new FileOutputStream("LogStats.xls"));
        workbook2.close();

        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream("LogStats.xls"));
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow date = sheet.getRow(0);
        HSSFRow name = sheet.getRow(1);
        HSSFRow level = sheet.getRow(2);
        HSSFRow thread = sheet.getRow(3);
        HSSFRow logs1 = sheet.getRow(4);
        HSSFRow logs2 = sheet.getRow(5);
        HSSFRow logs3 = sheet.getRow(6);

        assertEquals("Date:", date.getCell(0).getStringCellValue());
        assertEquals("Logger Name:", name.getCell(0).getStringCellValue());
        assertEquals("Logger Level:", level.getCell(0).getStringCellValue());
        assertEquals("Log threads:", thread.getCell(0).getStringCellValue());
        assertEquals("Logger Count:", logs1.getCell(0).getStringCellValue());
        assertEquals("Level Count:", logs2.getCell(0).getStringCellValue());
        assertEquals("Thread Count:", logs3.getCell(0).getStringCellValue());

    }

    @Test
    public void testStats3() throws Exception {
        Assume.assumeTrue(isServerReady());
        Logger log666 = Logger.getLogger("yoyoyoyoyo");
        log666.addAppender(appender);
        log666.setLevel(Level.ALL);
        log666.warn("warn message");

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(STATS_PATH);
        URI uri = builder.build();

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);

        HttpResponse response = client.execute(request);

        assertEquals(200,response.getStatusLine().getStatusCode());
    }


}
