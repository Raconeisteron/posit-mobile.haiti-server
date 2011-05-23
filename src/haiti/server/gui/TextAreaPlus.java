/*
 * File: TextAreaPlus.java
 * 
 * Copyright (C) 2009 R. Morelli
 * 
 * This file is part of CryptoToolJ.
 *
 * CryptoToolJ is free software; you can redistribute it and/or modify
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
 * <P>Description: Implements a Java 1.1 version of a GUI tool for using and analyzing
 *  historical ciphers.
 * Credits: CryptoToolJ is modeled after the ImageJ program which is written by
 *  Wayne Rasband of the National Institutes of Health. ImageJ is in the public domain.
 *
 * Modifications:
 * 7/29/02 Modified the openFile() and saveFile() methods to allow the file's
 *  encoding method to be set by the calling method. -- RAM
 */
package haiti.server.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/**
 * <p>This class implements a TextArea for CryptoToolJ. It manages the
 *  opening, closing, saving, and renaming of the file associated with
 *  the TextArea.
 */
public class TextAreaPlus extends TextArea implements TextListener, FocusListener {

    private File file = null;
    private boolean dirty = false;
    private boolean fileOpened = false;
    private boolean initializedText = false;
    private boolean inFocus = false;
    private Frame frame;
    private TitledBorder mBorder;
    
    public TextAreaPlus(){}

    public TextAreaPlus(Frame parent, String name, TitledBorder border) {
        super();
        frame = parent;
        setName(name);
        setText(name);
        addTextListener(this);  
        addFocusListener(this); 
        mBorder = border;
	dirty = false;
    }
    
    public void setFile(File f) {
        file = f;
    }
    
    //    public void openFile(){
    public void openFile(String encoding){
        FileDialog fd = new FileDialog(frame, "Open File", FileDialog.LOAD);
        fd.show();
        String fileName = fd.getFile();
        String dirName = fd.getDirectory();
        if (fileName != null) {
            try {
                File file = new File(dirName, fileName);
                setFile(file);
		InputStreamReader inStream = null;
		if (encoding.startsWith("default"))
		    inStream = new InputStreamReader(new FileInputStream(file));
		else
		    inStream = new InputStreamReader(new FileInputStream(file), encoding);

                int length = (int)file.length();
		//                byte[] input = new byte[length];
                char[] input = new char[length];
                inStream.read(input);

		/**********8
		System.out.println("\n\nFile length = " + length + " bytes");
		for (int k = 0; k < input.length; k++)
		    System.out.print(input[k] + " ");
		System.out.println("\nChar array as int ");
		for (int k = 0; k < input.length;  k++)
		    System.out.print((int)input[k] + " ");
		System.out.println();
		***********/

		String encodedS = new String(input);
		setText(encodedS);

		/*************
		System.out.println("\nEncodedS length = " + encodedS.length());
		for (int k = 0; k < encodedS.length();  k++)
		    System.out.print(encodedS.charAt(k) + "," + (int)encodedS.charAt(k) + " ");
		************/

		String txt = getText();

		/**************
		System.out.println("\nText length = " + txt.length());
		System.out.println("\nText as int ");
		for (int k = 0; k < txt.length();  k++)
		    System.out.print(txt.charAt(k) + "," + (int)txt.charAt(k)+ " ");
		System.out.println();
		***************/

		
                inStream.close();
                fileOpened = true;     // Flags textValueChanged ()        
           } catch (FileNotFoundException e) {
                     e.printStackTrace();
           } catch (IOException e) {
                     System.err.println("IOERROR: " + e.getMessage());
                     e.printStackTrace();
           } 
        }        
    }
    
    public void setText(String s) { // OVerrides default
	//	System.out.println("setText(): ");
	super.setText("");
	for (int k = 0; k < s.length(); k++) {
	    char ch = s.charAt(k);
	    //	    System.out.print((int)ch+ " ");
	    int chint = (int)s.charAt(k);
	    if (chint <= 127)
		append(""+ch);
	    else
		append("\\u"+ Integer.toHexString(chint));
	}
    }

    public String getText() { // Overrides default
	String s = super.getText();
	//	System.out.println("getText(): ");
	//	for (int k = 0; k < s.length(); k++)
	//	    System.out.print(s.charAt(k) + " ");
	//	System.out.println();
	return s;
    }

    public int saveToFile() {
        FileSaveMessageDialog fsmd = new FileSaveMessageDialog(frame, "Save " + getName() + " to File?");
        int result = fsmd.getResult();
        fsmd.dispose();
        return result;
    }
    
    //    public boolean save(boolean rename) {
    public boolean save(String file_encoding, boolean rename) {
        FileDialog fd = new FileDialog(frame, "Save " + getName() + "?", FileDialog.SAVE);
        if (file != null) 
            fd.setFile(file.getName());
        fd.show();
        
        String fileName = fd.getFile();
        String dirName = fd.getDirectory();
        if (fileName == null)
            return false;
        
        if (fileName != null) {
            try {
                file = new File(dirName, fileName);
		OutputStreamWriter outStream = null;
		if (file_encoding.startsWith("Default"))
		    outStream = new OutputStreamWriter(new FileOutputStream(file));
		else {
		    System.out.println("Using file encoding: " + file_encoding);
		    outStream = new OutputStreamWriter(new FileOutputStream(file), file_encoding);
		}
		//		OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(file));
		//                FileOutputStream outStream = new FileOutputStream(file);
                String text = getText();
		//                byte[] output = text.getBytes();
                outStream.write(text, 0, text.length());
		//                outStream.write(output);
                outStream.close();
                dirty = false;
            } catch (FileNotFoundException e) {
                     e.printStackTrace();
            } catch (IOException e) {
                     System.err.println("IOERROR: " + e.getMessage());
                     e.printStackTrace();
            }        
        }   
        return true;     
    }
    
    public boolean isDirty() {
        return dirty;
    }
    
    public synchronized void markFileSaved() {
        dirty = false;
//        System.out.println("Saved");
    }

    public boolean isInFocus() {
        return inFocus;
    }
    public File getFile() {
        return file;
    }
    
    /**
     * This method is part of TextListener interface. It is
     *  called each time the text in a TextArea is changed.
     */
    public synchronized void textValueChanged(TextEvent e) {
	if (initializedText) 
	    dirty = true;
	else
	    initializedText = true;
        if (fileOpened) {
            dirty = false;
            fileOpened = false;
        }            
//        System.out.println("TextChanged");
    }
    
    /**
     * focusLost changes the value of inFocus to false when the TextAreaPlus object is no longer active.
     * The color of titled border object is then set to black and the (Active) string is removed from the
     * end of the title string, if it is there.
     */
    
    public void focusLost(FocusEvent e) {
        inFocus = false;
        mBorder.setTitleColor(Color.black);
        mBorder.setTitle(mBorder.getTitle().substring(0,mBorder.getTitle().indexOf("(")));
        frame.repaint();
    }
    /**
     * focusGained changes the value of inFocus to true when the TextAreaPlus object becomes active.
     * The title of the titled border object is then set to green and an (Active) substring is then 
     * added to the end of the title string.
     */
    public void focusGained(FocusEvent e) {
        inFocus = true;
        mBorder.setTitleColor(Color.green);
        frame.repaint();
        mBorder.setTitle(mBorder.getTitle()+"(Active)");
     }
}
