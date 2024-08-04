package com.admiral.client.db;

import com.admiral.client.swing.CDialog;
import com.admiral.kernel.base.db.ADConnection;

import java.awt.*;
import java.awt.event.ActionListener;

public class ADConnectionDialog extends CDialog implements ActionListener {
    public ADConnectionDialog() {
        this(null);
    }

    public ADConnectionDialog(ADConnection cc) {}
}
