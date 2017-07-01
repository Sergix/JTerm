package main.java.jterm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Ping {

	public static void main(String[] args) {
		//pingHost("google.com",80,1000);
		System.out.println(pingHost("googdvcsde.com",80,1000));
	}
	
	public static boolean pingHost(String host, int port, int timeout) {
	    try (Socket socket = new Socket()) {
	        socket.connect(new InetSocketAddress(host, port), timeout);
	        return true;
	    } catch (IOException e) {
	        return false; // Either timeout or unreachable or failed DNS lookup.
	    }
	}
	
	public static void prePing() {
		Scanner pingScanner = new Scanner(System.in);
  		String hostAndPort = pingScanner.nextLine();
  		String[] host;
  		String[] port;
  		int portInt;
  		String hostStr;
  		if(hostAndPort.matches(".+ [0-9]{2}")) {
  			//System.out.println("PINGED");
  			host = hostAndPort.split(" ");
  			//System.out.println(host[0]);
  			port = hostAndPort.split(" ");
  			//System.out.println(port[1]);
  			portInt = Integer.parseInt(port[1]);
  			hostStr = host[0];
  			
  			if(pingHost(hostStr, portInt, 1000)) {
  				System.out.println("Ping Successful");
  			} else {
  				System.out.println("Ping Failed");
  			}
  			
  		} else {
  			System.out.println("Please Include Host & Port");
  		}
	}
	
}
