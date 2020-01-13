package BeatBox;

import java.io.*;
import java.net.*;
import java.util.*;

public class MusicServer {
    ArrayList<ObjectOutputStream> clientOutputStreams;
    public static void main (String[] args) {
        new MusicServer().go();
    }
    public class ClientHandler implements Runnable {
        ObjectInputStream in;
        Socket clientSocket;
        public ClientHandler(Socket socket) {
            try {
                clientSocket = socket;
                in = new ObjectInputStream(clientSocket.getInputStream());
            } catch(Exception ex) {ex.printStackTrace();}
        } // Закрываем конструктор

        public void run() {
            Object o2 = null;
            Object ol = null;
            try {
                while ((ol = in.readObject()) != null) {
                    o2 = in.readObject ();
                    System.out.println("read two objects");
                    tellEveryone(ol, o2);
                } // Закрываем цикл while
            } catch(Exception ex) {ex.printStackTrace();}
        } // Закрываем run
    } // Закрываем вложенный класс

    public void go() {
        clientOutputStreams = new ArrayList<ObjectOutputStream>();
        try {
            ServerSocket serverSock = new ServerSocket(4242) ;
            while(true) {
                Socket clientSocket = serverSock.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream()) ;
                clientOutputStreams.add(out);
                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start ();
                System.out.println ("got a connection11");
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    } // Закрываем go

    public void tellEveryone(Object one, Object two) {
        Iterator it = clientOutputStreams.iterator();
        while(it.hasNext()) {
            try {
                ObjectOutputStream out = (ObjectOutputStream) it.next();
                out.writeObject(one);
                out.writeObject(two);
            }catch(Exception ex) {ex.printStackTrace();}
        }
    } // Закрываем tellEveryone
} // Закрываем класс



