/*
 * File: FileSaveMessageDialog.java
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
 * 
 * <P>Credits: CryptoToolJ is modeled after the ImageJ program which is written by
 *  Wayne Rasband of the National Institutes of Health. ImageJ is in the public domain.
 *
 */

package haiti.server.gui;

import java.awt.*;
import java.awt.event.*;

/**
 * Implements a dialog window for the Save and SaveAs options.
 */ 

public class FileSaveMessageDialog extends Dialog implements ActionListener {
    public static final int CANCEL_OPTION = 0;  // Possible results
    public static final int NOSAVE_OPTION = 1;
    public static final int SAVE_OPTION = 2;
    public static final int WIDTH = 300;
    public static final int HEIGHT = 150;
    
    
    private Button saveButton, dontSaveButton, cancelButton;
    private Panel buttonPanel;
    private TextArea display;
    
    private int result = SAVE_OPTION;  // Default
    
    public FileSaveMessageDialog(Frame parent, String title) {
        super(parent, title, true);
        
        saveButton = new Button("Save");
        saveButton.addActionListener(this);
        dontSaveButton = new Button("Don't Save");
        dontSaveButton.addActionListener(this);
        cancelButton = new Button("Cancel");
        cancelButton.addActionListener(this);
        
        display = new TextArea("", 4, 50, TextArea.SCROLLBARS_NONE);
	display.setEditable(false);
        display.setText("Changes made to this text area will be  \n" +
                        "discarded if it is not saved. Do you want\n" +
                        "to save it before closing?");
        display.requestFocus();
    
        buttonPanel = new Panel();
        buttonPanel.add(dontSaveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        this.add(display,"North");
        this.add(buttonPanel, "Center");
        
        setSize(WIDTH, HEIGHT);
        Tools.centerWindow(this);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) 
            result = CANCEL_OPTION;
        else if (e.getSource() == dontSaveButton)
            result = NOSAVE_OPTION;
        else 
            result = SAVE_OPTION;
        setVisible(false);
    }
    
    public int getResult() { return result; }
}
