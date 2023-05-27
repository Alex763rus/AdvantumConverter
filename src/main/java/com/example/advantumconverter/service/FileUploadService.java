package com.example.advantumconverter.service;

import com.example.advantumconverter.config.BotConfig;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@Service
public class FileUploadService {

    @Autowired
    BotConfig botConfig;

    String upPath = "C:\\Users\\grigorevap\\Desktop\\excel\\result_";

    public File uploadFile(String file_name, String file_id) throws IOException {
        URL url = new URL("https://api.telegram.org/bot" + botConfig.getBotToken() + "/getFile?file_id=" + file_id);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String res = in.readLine();
        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        URL downoload = new URL("https://api.telegram.org/file/bot" + botConfig.getBotToken() + "/" + file_path);

        FileOutputStream fos = new FileOutputStream(upPath + file_name);
        System.out.println("Start upload");
        ReadableByteChannel rbc = Channels.newChannel(downoload.openStream());
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
        System.out.println("Uploaded!");
        return new File(upPath + file_name);
    }
}
