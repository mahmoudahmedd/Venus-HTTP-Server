/**
 *  @file    SingleThread.java
 *  @authors Mahmoud Ahmed (mahmoudahmedd) - Mostafa Omar (MostafaOmar98) - Yousef okasha (yousefokasha61)
 *  @date    14/04/2019
 *  @version 1.0
 */

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;

public class SingleThread extends Thread
{

    private Socket socket;
    // Reads and prints to socket streams
    private BufferedReader in;
    private PrintWriter out;
    private String htdocsPath = "E:/Venus Server/htdocs";
    final static String CRLF = "\r\n";

    public SingleThread(Socket clientSocket)
    {
        this.socket = clientSocket;
    }

    public void run()
    {
        try
        {
            //create a buffer reader
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //create a PrintWriter
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            String line;
            String httpHeader = "";
            while (((line = in.readLine()) != null) && !line.equals("") && !line.equals(CRLF))
            {
                httpHeader += line;
                httpHeader += CRLF;
            }
            if (!httpHeader.equals(""))
            {
                String[] request = httpHeader.split(" ");
                System.out.println("Client HTTP Request Header: " + CRLF + httpHeader);
                if (request[0].equals("GET")) // if line contains get
                {

                    //extract the html filename
                    if (request[1].equals("/"))
                    {
                        request[1] = "/index.html";
                    }

                    processRequestGET(new java.net.URI(request[1]).getPath()); // process the request
                }
                else if (request[0].equals("POST"))
                {
                    String requestBody = "";
                    while (((line = in.readLine()) != null) && !line.equals("") && !line.equals(CRLF))
                    {
                        requestBody += line;
                        requestBody += CRLF;
                    }

                    System.out.println("Client HTTP Request Body: " + CRLF + requestBody);
                    processRequestPOST(new java.net.URI(request[1]).getPath(), requestBody);
                }
                else if (request[0].equals("DELETE"))
                {
                    //System.out.println("INSIDE DELETE: " + FilePath);
                    File file = new File(htdocsPath + new java.net.URI(request[1]).getPath());
                    if (file.delete())
                    {
                        System.out.println(htdocsPath + new java.net.URI(request[1]).getPath() + " has been deleted");
                    }
                    else
                    {
                        System.out.println("File cannot be found");
                    }
                }
                else if (request[0].equals("PUT"))
                {
                    String requestBody = "";

                    while (((line = in.readLine()) != null) && !line.equals("") && !line.equals(CRLF))
                    {
                        requestBody += line;
                        requestBody += CRLF;
                    }

                    System.out.println("Client HTTP Request Body: " + CRLF + requestBody);
                    processRequestPUT(new java.net.URI(request[1]).getPath(), requestBody);
                }
            }
            closeConnection(); // close the socket connection and input/output buffers
        }
        catch (Exception e)   //print error stack trace
        {
            e.printStackTrace();
        }
    }

    public void processRequestGET(String fileName) throws Exception
    {

        File file = new File(htdocsPath + fileName); //create a file variable
        URLConnection connection = file.toURL().openConnection();
        String mimeType = connection.getContentType();


        if(file.isDirectory())
        {
            //sent the HTTP head (404 Not Found)
            out.print("HTTP/1.1 200 Not Found" + CRLF);
            Date date = new Date();
            out.print("Date: " + date.toString() + CRLF);
            out.print("Server: Venus Web Server" + CRLF);
            out.print("Connection: close" + CRLF);
            out.println("Content-Type: text/html; charset=UTF-8" + CRLF);
            //end of http header

            File[] listOfFiles = file.listFiles();
            
            for (int i = 0; i < listOfFiles.length; i++)
                if (listOfFiles[i].isDirectory())
                    out.println("<a style=\"background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAd5JREFUeNqMU79rFUEQ/vbuodFEEkzAImBpkUabFP4ldpaJhZXYm/RiZWsv/hkWFglBUyTIgyAIIfgIRjHv3r39MePM7N3LcbxAFvZ2b2bn22/mm3XMjF+HL3YW7q28YSIw8mBKoBihhhgCsoORot9d3/ywg3YowMXwNde/PzGnk2vn6PitrT+/PGeNaecg4+qNY3D43vy16A5wDDd4Aqg/ngmrjl/GoN0U5V1QquHQG3q+TPDVhVwyBffcmQGJmSVfyZk7R3SngI4JKfwDJ2+05zIg8gbiereTZRHhJ5KCMOwDFLjhoBTn2g0ghagfKeIYJDPFyibJVBtTREwq60SpYvh5++PpwatHsxSm9QRLSQpEVSd7/TYJUb49TX7gztpjjEffnoVw66+Ytovs14Yp7HaKmUXeX9rKUoMoLNW3srqI5fWn8JejrVkK0QcrkFLOgS39yoKUQe292WJ1guUHG8K2o8K00oO1BTvXoW4yasclUTgZYJY9aFNfAThX5CZRmczAV52oAPoupHhWRIUUAOoyUIlYVaAa/VbLbyiZUiyFbjQFNwiZQSGl4IDy9sO5Wrty0QLKhdZPxmgGcDo8ejn+c/6eiK9poz15Kw7Dr/vN/z6W7q++091/AQYA5mZ8GYJ9K0AAAAAASUVORK5CYII=) left top no-repeat;padding-left: 25px; \" href=\""+ fileName+'/'+listOfFiles[i].getName()+"\">" + listOfFiles[i].getName() + "</a><br>");
            
            for (int i = 0; i < listOfFiles.length; i++)
                if (listOfFiles[i].isFile())
                    out.println("<a style=\"background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAIAAACQkWg2AAAABnRSTlMAAAAAAABupgeRAAABHUlEQVR42o2RMW7DIBiF3498iHRJD5JKHurL+CRVBp+i2T16tTynF2gO0KSb5ZrBBl4HHDBuK/WXACH4eO9/CAAAbdvijzLGNE1TVZXfZuHg6XCAQESAZXbOKaXO57eiKG6ft9PrKQIkCQqFoIiQFBGlFIB5nvM8t9aOX2Nd18oDzjnPgCDpn/BH4zh2XZdlWVmWiUK4IgCBoFMUz9eP6zRN75cLgEQhcmTQIbl72O0f9865qLAAsURAAgKBJKEtgLXWvyjLuFsThCSstb8rBCaAQhDYWgIZ7myM+TUBjDHrHlZcbMYYk34cN0YSLcgS+wL0fe9TXDMbY33fR2AYBvyQ8L0Gk8MwREBrTfKe4TpTzwhArXWi8HI84h/1DfwI5mhxJamFAAAAAElFTkSuQmCC) left top no-repeat;padding-left: 25px; \" href=\""+ fileName+'/'+listOfFiles[i].getName()+"\">" + listOfFiles[i].getName() + "</a><br>");

        }
        else if (file.exists() && (fileName.contains(".html") || fileName.contains(".txt"))) // if file exists
        {
            Scanner reader = new Scanner(new File(htdocsPath + fileName));
            //sent the HTTP head (HTTP 200 OK)
            out.print("HTTP/1.0 200 OK" + CRLF); // HTTP/1.1 results in a failure in showing the content of the file
            Date date = new Date();
            out.print("Date: " + date.toString() + CRLF);
            out.print("Server: Venus Web Server" + CRLF);
            out.print("Content-Length: " + file.length() + CRLF);
            out.println("Content-Type: " + mimeType + "; charset=UTF-8" + CRLF);
            //end of http header


            String line = "";
          
            while (reader.hasNextLine()) //read a line from the html file
            {
                line = reader.nextLine();
                out.println(line); //write the line to the socket connection
            }
            reader.close(); // must close reader to allow further access of file
        }
        else if (!file.exists())   //if file does not exists
        {
            //sent the HTTP head (404 Not Found)
            out.print("HTTP/1.1 404 Not Found" + CRLF);
            Date date = new Date();
            out.print("Date: " + date.toString() + CRLF);
            out.print("Server: Venus Web Server" + CRLF);
            out.print("Connection: close" + CRLF);
            out.println("Content-Type: text/html; charset=UTF-8" + CRLF);
            //end of http header

            //send file not found message
            file = new File(htdocsPath + "/404.html");
            Scanner reader = new Scanner(new FileReader(file));
            String line;
            while (reader.hasNextLine())
            {
                line = reader.nextLine();
                out.println(line);
            }
            reader.close();
        }
        else
        {
            //sent the HTTP head (415 Unsupported Media Type)
            out.print("HTTP/1.1 415 Unsupported Media Type" + CRLF);
            Date date = new Date();
            out.print("Date: " + date.toString() + CRLF);
            out.print("Server: Venus Web Server" + CRLF);
            out.print("Connection: close" + CRLF);
            out.println("Content-Type: text/html; charset=UTF-8" + CRLF);
            //end of http header

            //send file not found message
            file = new File(htdocsPath + "/415.html");
            Scanner reader = new Scanner(new FileReader(file));
            String line;
            while (reader.hasNextLine())
            {
                line = reader.nextLine();
                out.println(line);
            }
            reader.close();
        }
    }

    public void processRequestPOST(String fileName, String RequestBody) throws Exception
    {
        File file = new File(htdocsPath + fileName);
        file.createNewFile();
        FileWriter writer = new FileWriter(htdocsPath + fileName, true);
        writer.write(RequestBody);
        writer.flush();
        processRequestGET(fileName);
        writer.close();
    }

    public void processRequestPUT(String fileName, String RequestBody) throws Exception
    {
        File file = new File(htdocsPath + fileName);
        FileWriter writer = new FileWriter(htdocsPath + fileName);
        if (!file.createNewFile())
        {
            writer.flush();
        }
        writer.write(RequestBody);
        writer.flush();
        processRequestGET(fileName);
        writer.close();
    }

    private void closeConnection()
    {
        try
        {
            out.close(); // close output stream
            in.close(); // close input stream
            socket.close(); //close socket
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }
}
