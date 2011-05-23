/*
 * File: SmsReader.java
 * 
 * Copyright (C) 2011 Humanitarian FOSS Project
 * 
 * This file is part of POSIT-Haiti Server.
 *
 * POSIT-Haiti Server is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation; either version 3.0 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not visit http://www.gnu.org/licenses/gpl.html.
 *
 */

package haiti.server.gui;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**  
 * A command-line program to read a SMS message file, one message per line.
 *
 *  <BR>To compile: javac -classpath ../../haiti-server.jar:. SmsReader.java
 *  <BR>To run:     java -classpath ../../haiti-server.jar:. SmsReader <filename> [encoding]
 *
 */

public class SmsReader {
	public static final String TAG = "SmsReader";
	
	private String filename;
	private String encoding;
	private ArrayList<String> messages;
	
	public SmsReader(String filename) { 
		this(filename, System.getProperty("file.encoding"));
	}
	
	public SmsReader(String filename, String encoding) {
		log("Encoding = " + encoding);
		this.filename = filename;
		this.encoding = encoding;
	}
	
	  /** 
	   * Reads the file line by line into the arraylist. 
	   */
	public void readFile() throws IOException {
	    log("Reading from file.");
	    messages = new ArrayList<String>();
	    Scanner scanner = new Scanner(new FileInputStream(filename), encoding);
	    //Scanner scanner = new Scanner(new FileInputStream(filename));

	    try {
	      while (scanner.hasNextLine()){
	    	  messages.add(scanner.nextLine());
	      }
	    }
	    finally{
	      scanner.close();
	    }
	    log("Text read from " + filename + " : " + System.getProperty("line.separator") + toString());
	  }
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public ArrayList<String> getMessages() {
		return messages;
	}
	
	public String[] getMessagesAsArray() {
		String arr[] = new String[messages.size()];
		for (int k = 0; k < arr.length; k++) 
			arr[k] = messages.get(k);
		return arr;
	}

	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < messages.size(); k++) 
			sb.append(messages.get(k) + System.getProperty("line.separator"));
		return sb.toString();
	}
	
	private void log(String s) {
		System.out.println(TAG + " " + s);
	}
   
    public static void main (String args[]) throws Exception {
    	SmsReader reader = null;
    	
        if (args.length < 1) {
            System.out.println("Usage: java SmsReader </path/to/smsfile.txt> [encoding]");
           return;
        } else if (args.length < 2) {
        	reader = new SmsReader(args[0]);  // Uses default encoding
        } else {
            reader = new SmsReader(args[0], args[1]);
        }
        reader.readFile();
        
    }
}

