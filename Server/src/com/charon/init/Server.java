package com.charon.init;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * @description: 服务端调用代码(多线程) 采用内部类
 * @author: charon
 * @create: 2019-11-26 11:37
 **/
public class Server extends Thread {

    ServerSocket serverSocket=null;
    Socket socket=null;
    // 构造初始化ServerSocket
    public Server(int port){
        try {
            serverSocket=new ServerSocket(port);
        }catch (IOException e){

        }
    }
    // 连接客户端
    @Override
    public void run() {
        super.run();
        try {
            System.out.println("wait client connect...");
            socket=serverSocket.accept();
            new SendMessage().start();
            System.out.println(socket.getInetAddress().getHostAddress()+"SUCCESS TO CONNECT...");
            InputStream in=socket.getInputStream();
            int len=0;
            byte[] buf=new byte[1024];
            while ((len=in.read(buf))!=-1){
                System.out.println("client saying: "+new String(buf,0,len));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // 启动类
    public static void main(String[] args) {
        Server server=new Server(8888);
        server.start();
    }

    //  向客户端传输信息
    class SendMessage extends Thread {
        @Override
        public void run(){
            super.run();
            Scanner scanner=null;
            OutputStream out = null;
            try{
                if(socket != null){
                    scanner = new Scanner(System.in);
                    out = socket.getOutputStream();
                    String in = "";
                    do {
                        in = scanner.next();
                        out.write(("server saying: "+in).getBytes());
                        // 清空缓存区的内容
                        out.flush();
                    }while (!in.equals("q"));
                    scanner.close();
                    try{
                        out.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
