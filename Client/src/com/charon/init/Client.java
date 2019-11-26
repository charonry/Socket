package com.charon.init;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @description: 客户端
 * @author: charon
 * @create: 2019-11-26 13:41
 **/
public class Client extends Thread {
    Socket socket=null;

    public Client(String host,int port){
        try {
            socket=new Socket(host,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // 客户端连接服务器写入数据
        new sendMessage().start();
        super.run();
        try {
            InputStream input=socket.getInputStream();
            byte[] buf=new byte[1024];
            int len=0;
            while ((len=input.read(buf))!=-1){
                System.out.println(new String(buf,0,len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 启动类
    public static void main(String[] args) {
        Client client=new Client("127.0.0.1", 8888);
        client.start();
    }

    // 往Socket里面写数据，需要新开一个线程
    class sendMessage extends Thread{
        @Override
        public void run() {
            super.run();
            Scanner scanner = null;
            OutputStream out = null;
            String in="";
            try {
                scanner=new Scanner(System.in);
                out=socket.getOutputStream();
                do{
                    in=scanner.next();
                    out.write((""+in).getBytes());
                    out.flush();
                }while (!"bye".equals(in));
            } catch (IOException e) {
                e.printStackTrace();
            }
            scanner.close();
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
