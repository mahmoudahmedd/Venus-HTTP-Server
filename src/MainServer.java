/**
 *  @file    MainServer.java
 *  @authors Mahmoud Ahmed (mahmoudahmedd) - Mostafa Omar (MostafaOmar98) - Yousef okasha (yousefokasha61)
 *  @date    14/04/2019
 *  @version 1.0
 */

import java.io.*;
import java.net.*;

public class MainServer
{
    public MainServer() throws IOException
    {
        int portNumber = 8080;
        ServerSocket serverSocket = new ServerSocket(portNumber); //create a server socket object

        System.out.println("Running!");
        System.out.println("Point your browsers to http://localhost:" + portNumber + " \n");

        while(true)
        {
            Socket clientSocket = serverSocket.accept();
            SingleThread SingleThread = new SingleThread(clientSocket);//create a new thread for each client
            SingleThread.start();
        }
    }

    public static void main(String[] args)
    {
        try
        {
            new MainServer();
        }
        catch (IOException ioe)
        {
            System.err.println("Couldn't start server: " + ioe);
        }
    }
}
