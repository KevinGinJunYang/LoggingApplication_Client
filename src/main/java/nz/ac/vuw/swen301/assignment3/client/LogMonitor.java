package nz.ac.vuw.swen301.assignment3.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

public class LogMonitor {

    public static void main(String args[]) throws IOException {
        JFrame f = new JFrame("LogMonitor");
        JPanel finalPanel = new JPanel();
        JPanel tophalf = new JPanel();
        JPanel bottomHalf = new JPanel();
        JLabel levelLabel = new JLabel("min level:");
        JLabel limitLabel = new JLabel("limit:");
        String[] types = {"ALL","DEBUG","INFO","WARN","ERROR","FATAL","TRACE","OFF"};
        JComboBox cb = new JComboBox(types);
        JTextField limit = new JTextField("10",5);
        JButton log, stats;
        log = new JButton("FETCH DATA");
        stats = new JButton("DOWNLOAD STATS");

        Vector<Vector<String>> dataList = new Vector<>();
        Vector<String> columnNames = new Vector<>();
        columnNames.add("TIME");
        columnNames.add("LEVEL");
        columnNames.add("LOGGER");
        columnNames.add("THREAD");
        columnNames.add("MESSAGE");
        JTable jt = new JTable(dataList, columnNames);
        jt.setBounds(30,40,200,100);
        JScrollPane sp=new JScrollPane(jt);

        DefaultTableModel tableModel = (DefaultTableModel) jt.getModel();

        log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String limitValue = limit.getText();
                    String levelValue = cb.getItemAt(cb.getSelectedIndex()).toString();
                    dataList.clear();
                    URIBuilder builder = new URIBuilder();
                    builder.setScheme("http")
                            .setHost("localhost")
                            .setPort(8080)
                            .setPath("/resthome4logs/logs")
                            .setParameter("limit", "" + limitValue)
                            .setParameter("level", "" + levelValue);


                    URI uri = null;
                    uri = builder.build();

                    HttpClient client = HttpClientBuilder.create().build();
                    HttpGet request = new HttpGet(uri);
                    HttpResponse response = client.execute(request);
                    String s = EntityUtils.toString(response.getEntity());
                    JSONArray jsonArr = new JSONArray(s);

                    for (int i = 0; i < jsonArr.length(); i++) {

                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        Vector<String> data = new Vector<>();

                        data.add(jsonObj.getString("timeStamp"));
                        data.add(jsonObj.getString("level"));
                        data.add(jsonObj.getString("logger"));
                        data.add(jsonObj.getString("thread"));
                        data.add(jsonObj.getString("message"));
                        dataList.add(data);

                    }

                    tableModel.fireTableDataChanged();

                } catch (URISyntaxException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        });



        stats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    URLConnection connection = new URL("http://localhost:8080/resthome4logs/stats").openConnection();

                    InputStream response = connection.getInputStream();
                    Workbook workbook = new HSSFWorkbook(response);
                    workbook.write(new FileOutputStream("LogStats.xls"));
                    workbook.close();
                }catch (Exception es){
                    es.printStackTrace();
                }

            }
        });
        tophalf.add(levelLabel);
        tophalf.add(cb);
        tophalf.add(limitLabel);
        tophalf.add(limit);
        tophalf.add(log);
        tophalf.add(stats);
        tophalf.setLayout(new FlowLayout());



        bottomHalf.add(sp);
        bottomHalf.setLayout(new FlowLayout());
        finalPanel.add(tophalf);
        finalPanel.add(bottomHalf);
        f.add(finalPanel);
        f.setSize(550,550);
        f.setVisible(true);



    }

}
