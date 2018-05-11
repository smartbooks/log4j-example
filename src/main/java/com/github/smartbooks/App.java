package com.github.smartbooks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class App {

    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) throws Exception {

        logger.info("info test");
        logger.debug("debug test");
        logger.warn("warn test");
        logger.error("error test");
        logger.fatal("fatal test");
        logger.trace("trace test");

        Process process = Runtime.getRuntime().exec("python C:\\work\\github-work\\log4j-example\\src\\main\\resources\\Pystd.py");

        new Thread(() -> {
            try {
                //子进程的错误流(读取流)，即子进程的错误输出流
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.debug(String.format("ERR-Data:%s", line));
                }
                reader.close();
            } catch (Exception e) {
                logger.error(e);
            }
        }).start();

        new Thread(() -> {
            try {
                //子进程的输入流(读取流)，即子进程的标准输出流
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.debug(String.format("OUT-Data:%s", line));
                }
                reader.close();
            } catch (Exception e) {
                logger.error(e);
            }
        }).start();

        new Thread(() -> {
            try {
                //子进程的输出流(写入流)，即子进程的标准输入流
                OutputStream out = process.getOutputStream();
                //此处\n是必须的,对应python中的readline
                String content = "666\n";
                out.write(content.getBytes("utf-8"));
                out.flush();
            } catch (Exception e) {
                logger.error(e);
            }
        }).start();

        int wait = process.waitFor();
        process.destroy();

        Boolean isAlive = process.isAlive();
        int exit = process.exitValue();

        logger.debug(String.format("wait=%s exit=%s isAlive=%s", wait, exit, isAlive));
    }
}