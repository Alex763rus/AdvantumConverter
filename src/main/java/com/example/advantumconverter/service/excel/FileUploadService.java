package com.example.advantumconverter.service.excel;

import com.example.advantumconverter.config.BotConfig;
import lombok.val;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;

@Service
public class FileUploadService {

    @Autowired
    BotConfig botConfig;

    public File uploadFile(String file_name, String file_id) throws IOException {
        val fulFileName = botConfig.getInputFilePath() + " " + file_name;
        val url = new URL("https://api.telegram.org/bot" + botConfig.getBotToken() + "/getFile?file_id=" + file_id);
        val in = new BufferedReader(new InputStreamReader(url.openStream()));
        val res = in.readLine();
        val file_path = new JSONObject(res).getJSONObject("result").getString("file_path");
        val downoload = new URL("https://api.telegram.org/file/bot" + botConfig.getBotToken() + "/" + file_path);
        val fos = new FileOutputStream(fulFileName);
        val rbc = Channels.newChannel(downoload.openStream());
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
        return new File(fulFileName);
    }

    public XSSFWorkbook uploadXlsx(String file_name, String file_id) throws Exception {
        return (XSSFWorkbook) (WorkbookFactory.create(uploadFile(file_name, file_id)));
    }
}
