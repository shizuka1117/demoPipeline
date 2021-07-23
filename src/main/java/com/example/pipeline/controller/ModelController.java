package com.example.pipeline.controller;


import com.example.pipeline.common.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

@Controller
@RequestMapping("/model")
public class ModelController {
    /*
    validation_split=0.33, epochs=50, batch_size=10
     */
    private final String command = "D:\\java\\source\\imdb\\src\\main\\java\\com\\example\\pythonscript\\model.py";

    @PostMapping("/post")
    public Result post(@RequestParam("split") String split,
                       @RequestParam("epochs") String epochs,
                       @RequestParam("batch_size") String batch_size
    ) throws InterruptedException {
        String exe = "C:\\Users\\zjy19\\anaconda3\\python.exe";
        String[] cmdArr = new String[] {exe, command, split, epochs, batch_size};//TODO:传入保存path
        try {
            Process process = Runtime.getRuntime().exec(cmdArr);//执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            int result = process.waitFor();
            System.out.println("训练模型结果:"+result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new Result("ok");
    }
}
