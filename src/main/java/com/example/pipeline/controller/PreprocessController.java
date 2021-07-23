package com.example.pipeline.controller;

import com.example.pipeline.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Controller
@RequestMapping("/preprocess")
public class PreprocessController {
    private static String path = "D:\\java\\source\\imdb\\src\\main\\resources\\static\\files";

    private final String command = "D:\\java\\source\\imdb\\src\\main\\java\\com\\example\\pythonscript\\cutAndAlign.py";

    @PostMapping("/post")
    public Result post(@RequestParam("file") MultipartFile[] file,
                       @RequestParam("anomalyProcess") String anomalyProcess,
                       @RequestParam("normalization") String normalization
    ) throws IOException, InterruptedException {
        String[] filename = saveFile(file);
        if(anomalyProcess.equals("true")){
            anomalyProcess(filename);
        }
        if(normalization.equals("true")){
            normalize(filename);
        }
        cutAndAlign(filename);

        return new Result("ok");//TODO:修改传回
    }

    /*
    TODO:修改传回
     */
    @GetMapping("/get")
    public String get(){
        return "HelloWorld";
    }

    private String[] saveFile(MultipartFile[] files){
        String[] filenames = new String[files.length];
        if (files.length > 0) {
            //循环获取file数组中得文件
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                //保存文件
                if(!file.isEmpty()){
                    String fileName = file.getOriginalFilename();
                    File dest = new File(new File(path).getAbsolutePath()+ "/" + fileName);
                    filenames[i] = dest.getAbsolutePath();
                    try {
                        // 转存文件
                        file.transferTo(dest);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return filenames;
    }

    public void anomalyProcess(String[] file){

    }
    public void normalize(String[] file){

    }
    public void cutAndAlign(String[] file) throws IOException, InterruptedException {
        String exe = "C:\\Users\\zjy19\\anaconda3\\python.exe";
        String[] cmdArr = new String[] {exe, command};//TODO:修改通过inputstream传入文件名
        //String[] cmdArr = new String[]{exe, command};
        Process process = Runtime.getRuntime().exec(cmdArr);
        int result = process.waitFor();
        System.out.println(result);
    }
}
