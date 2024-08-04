package com.admiral.client.db;

import com.admiral.client.swing.CDialog;
import com.admiral.kernel.base.db.ADConnection;
import com.admiral.kernel.base.db.Database;
import com.admiral.kernel.base.db.Database_PostgreSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ADConnectionDialog extends CDialog implements ActionListener {
    private static Logger log = LoggerFactory.getLogger(ADConnectionDialog.class);

    private ADConnection m_cc = null;
    private ADConnection m_ccResult = null;
    private boolean 		m_updating = false;
    private boolean     	m_saved = false;

    // components
    private JPanel mainPanel = new JPanel();
    private BorderLayout mainLayout = new BorderLayout();
    private JPanel centerPanel = new JPanel();
    private JPanel southPanel = new JPanel();
    private Button bOk = new Button("OK");
    private Button bCancel = new Button("Cancel");
    private FlowLayout soutLayout = new FlowLayout();
    private GridBagLayout centerLayout = new GridBagLayout();

    private JLabel nameLabel = new JLabel();
    private JTextField nameField = new JTextField();

    private JLabel dbTYpeLabel = new JLabel();
    private JComboBox dbTypeField = new JComboBox(Database.DATABASE_NAMES);
    private JLabel appHostLabel = new JLabel();
    private JTextField appHostField = new JTextField();
    private JLabel appPortLabel = new JLabel();
    private JTextField appPortField = new JTextField();
    private JButton bTestApps = new JButton();
    private JLabel dbUidLabel = new JLabel();
    private JTextField dbUidField = new JTextField();
    private JPasswordField dbPwdField = new JPasswordField();

    private boolean isCancel = true;

    private void jbInit() throws Exception {
        this.setTitle("Admiral Connection Dialog");
        mainPanel.setLayout(mainLayout);
        southPanel.setLayout(soutLayout);
        soutLayout.setAlignment(FlowLayout.RIGHT);
        centerPanel.setLayout(centerLayout);

    }

    public ADConnectionDialog() {
        this(null);
    }

    public ADConnectionDialog(ADConnection cc) {
        super((Frame)null, true);
    }
}
