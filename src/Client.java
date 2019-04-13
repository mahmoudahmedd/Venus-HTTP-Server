/**
 *  @file    Client.java
 *  @authors Mahmoud Ahmed (mahmoudahmedd) - Mostafa Omar (MostafaOmar98) - Yousef okasha (yousefokasha61)
 *  @date    14/04/2019
 *  @version 1.0
 */

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client
{

    public static void main(String args[]) throws IOException
    {
        Socket socket = new Socket("127.0.0.1", 8080);
        Scanner userInput = new Scanner(System.in), socketInput = new Scanner(socket.getInputStream());
        PrintStream socketOutput = new PrintStream(socket.getOutputStream());
        System.out.println("What function would you like to do?:\n" +
                           "1- GET\n" +
                           "2- POST\n" +
                           "3- DELETE\n" +
                           "4- PUT\n");
        int choice;
        choice = userInput.nextInt();
        String URL, Request = "";
        if (choice == 1)
        {
            System.out.print("Enter URL: ");
            Request += "GET ";
            URL = userInput.next();
            Request += URL;
            Request += " HTTP/1.1";
            Request += "\r\n\r\n";
        }
        if (choice == 2)
        {
            System.out.println("Enter URL: ");
            Request += "POST ";
            URL = userInput.next();
            Request += URL;
            Request += " HTTP/1.1";
            Request += "\r\n";
            System.out.println("Enter Body (DONE! to stop): \n");
            String line;
            while(true)
            {
                line = userInput.nextLine();
                if (line.equals("DONE!"))
                    break;
                Request += line;
                Request += "\r\n";
            }
            Request += "\r\n\r\n";
        }
        else if (choice == 3)
        {
            System.out.print("Enter URL: ");
            Request += "DELETE ";
            URL = userInput.next();
            Request += URL;
            Request += " HTTP/1.1";
            Request += "\r\n\r\n";
        }
        else if (choice == 4)
        {
            System.out.println("Enter URL: ");
            Request += "PUT ";
            URL = userInput.next();
            Request += URL;
            Request += " HTTP/1.1";
            Request += "\r\n";
            System.out.println("Enter Body (DONE! to stop): \n");
            String line;
            while(true)
            {
                line = userInput.nextLine();
                if (line.equals("DONE!"))
                    break;
                Request += line;
                Request += "\r\n";
            }
            Request += "\r\n\r\n";

        }
        socketOutput.print(Request);
        while(socketInput.hasNextLine())
            System.out.println(socketInput.nextLine());

    }

}
