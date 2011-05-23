/*
 * File: TextManager.java
 * 
 * Copyright (C) 2009 R. Morelli
 * 
 * This file is part of DatEntryGUI.
 *
 * DatEntryGUI is free software; you can redistribute it and/or modify
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


import java.awt.*;
import java.awt.datatransfer.*;

/** 
 * <p>Manages the cutting, pasting, copying of text for all windows 
 */
public class TextManager implements ClipboardOwner {

    private static String appletClip = null;

    public static void copyText(boolean cut) {
    	DataEntryFrame cf = (DataEntryFrame)WindowManager.getActiveCipherFrame();
        DataEntryGUI ctj = WindowManager.getCryptoTool();
    	
    	TextArea tf = null;
    	String selection = null;
    	if (cf != null) {    	    
            tf = cf.getTextAreaWithFocus();
            if (tf == null) 
                return;
        }
        else if (cut) {     // Don't cut text from DatEntryGUI     
            ctj.showStatus("Cuts not allowed in DatEntryGUI");
            return;
        }
        else {
            boolean isActiveWindow = ctj.getFocusOwner()!=null;
            if (!isActiveWindow) {
                ctj.showStatus("CTJ not in focus");
                return;
            }
            tf = ctj.display;
        }

    	Clipboard clip = ctj.getToolkit().getSystemClipboard();
    	if (clip==null) {
    		ctj.showStatus("Clipboard null");            
    		return;
    	}
    	selection = tf.getSelectedText();
    	StringSelection cont = new StringSelection(selection);
    	clip.setContents(cont, ctj);
  
        if (selection.length() > 0) {
            ctj.showStatus(selection.length() + " characters copied to clipboard");
        }
        if (cut) 
            tf.replaceRange("", tf.getSelectionStart(), tf.getSelectionEnd());
    }
        
    public static void pasteText() {
    	DataEntryFrame cf = (DataEntryFrame)WindowManager.getActiveCipherFrame();
        DataEntryGUI ctj = WindowManager.getCryptoTool();
        String text;
        TextArea tf = null; 
        if (cf != null)
            tf = cf.getTextAreaWithFocus();
        else
            tf = ctj.display;
        if (tf == null) return;
 
        Clipboard clip = ctj.getToolkit().getSystemClipboard();
        if (clip==null) return;
        Transferable contents = clip.getContents(ctj);
        try {
        	text = (String)(contents.getTransferData(DataFlavor.stringFlavor));
        	tf.replaceRange(text, tf.getSelectionStart(), tf.getSelectionEnd());
        } 
        catch (Exception e) {}
    }

    public static void selectAll() {
        TextArea tf = null;
    	DataEntryFrame cf = (DataEntryFrame)WindowManager.getActiveCipherFrame();
        DataEntryGUI ctj = WindowManager.getCryptoTool();
        if (cf != null)
            tf = cf.getTextAreaWithFocus();
        else
            tf = ctj.display;
        if (tf == null) return;
        tf.selectAll();
    }

    public void lostOwnership (Clipboard clip, Transferable cont) {}


}
