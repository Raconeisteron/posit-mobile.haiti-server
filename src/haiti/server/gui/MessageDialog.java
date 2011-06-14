/*
 * File: MessageDialog.java
 * 
 * Copyright (C) 2011 The Humanitarian FOSS Project (http://hfoss.org).
 * 
 * This file is part of POSIT-Haiti Server for ACDI/VOCA.
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

    public MessageDialog(Frame parent, String title, String message) {
        super(parent, title, true);
        setLayout(new BorderLayout());
        if (message==null) message = "";
	    label = new MultiLineLabel(message);
		label.setFont(new Font("Dialog", Font.PLAIN, 12));
        add("Center", label);
        button = new Button("  OK  ");
		button.addActionListener(this);
        Panel p = new Panel();
        p.setLayout(new FlowLayout());
        p.add(button);
        add("South", p);
        pack();
        DataEntryGUI.centerWindow(this);
        this.setVisible(true);
    }
    

	public void actionPerformed(ActionEvent e) {
		setVisible(false);
		dispose();
	}

}
