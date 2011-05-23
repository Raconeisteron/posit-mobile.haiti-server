/*
 * File: MessageDialog.java
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
 */

package haiti.server.gui;

import java.awt.*;
import java.awt.event.*;

/** 
  <p>Implements a modal dialog box that displays information. 
  Based on the 	InfoDialogclass from "Java in a Nutshell" by David Flanagan. 
*/
public class MessageDialog extends Dialog implements ActionListener {
    protected Button button;
    protected MultiLineLabel label;
//    protected Label label;

    public MessageDialog(Frame parent, String title, String message) {
        super(parent, title, true);
        setLayout(new BorderLayout());
        if (message==null) message = "";
	    label = new MultiLineLabel(message);
//	    label = new Label(message);
		label.setFont(new Font("Dialog", Font.PLAIN, 12));
        add("Center", label);
        button = new Button("  OK  ");
		button.addActionListener(this);
        Panel p = new Panel();
        p.setLayout(new FlowLayout());
        p.add(button);
        add("South", p);
        pack();
//		GUI.center(this);
        show();
    }
    

	public void actionPerformed(ActionEvent e) {
		setVisible(false);
		dispose();
	}

}
