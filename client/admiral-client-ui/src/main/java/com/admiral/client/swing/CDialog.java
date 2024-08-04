package com.admiral.client.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CDialog extends JDialog implements ActionListener, MouseListener {
    public CDialog() throws HeadlessException
    {
        this((Frame)null, false);
    }
    public CDialog(Frame owner) throws HeadlessException
    {
        this (owner, false);
    }
    public CDialog(Frame owner, boolean modal) throws HeadlessException
    {
        this (owner, null, modal);
    }
    public CDialog(Frame owner, String title) throws HeadlessException
    {
        this (owner, title, false);
    }
    public CDialog(Frame owner, String title, boolean modal) throws HeadlessException
    {
        super(owner, title, modal);
        if(modal) {
            setModalityType(ModalityType.DOCUMENT_MODAL);
        }
    }
    public CDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc)
    {
        super(owner, title, modal, gc);
    }

    public CDialog(Dialog owner) throws HeadlessException
    {
        this (owner, false);
    }
    public CDialog(Dialog owner, boolean modal) throws HeadlessException
    {
        this(owner, null, modal);
    }
    public CDialog(Dialog owner, String title) throws HeadlessException
    {
        this(owner, title, false);
    }
    public CDialog(Dialog owner, String title, boolean modal) throws HeadlessException
    {
        super(owner, title, modal);
    }
    public CDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) throws HeadlessException
    {
        super(owner, title, modal, gc);
    }

    /**
     * 	Initialize.
     * 	Install ALT-Pause
     */
    protected void dialogInit()
    {
        super.dialogInit();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle(getTitle());	//	remove Mn
        //
        Container c = getContentPane();
        if (c instanceof JPanel)
        {
            JPanel panel = (JPanel)c;
            panel.getActionMap().put(ACTION_DISPOSE, s_dialogAction);
            panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(s_disposeKeyStroke, ACTION_DISPOSE);
        }
    }	//	init


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /** Dispose Action Name				*/
    protected static String			ACTION_DISPOSE = "CDialogDispose";
    /**	Action							*/
    protected static DialogAction	s_dialogAction = new DialogAction(ACTION_DISPOSE);
    /** ALT-EXCAPE						*/
    protected static KeyStroke		s_disposeKeyStroke =
            KeyStroke.getKeyStroke(KeyEvent.VK_PAUSE, InputEvent.ALT_MASK);

    static class DialogAction extends AbstractAction
    {
        /**
         *
         */
        private static final long serialVersionUID = -1502992970807699945L;

        DialogAction (String actionName)
        {
            super(actionName);
            putValue(AbstractAction.ACTION_COMMAND_KEY, actionName);
        }	//	DialogAction

        /**
         * 	Action Listener
         *	@param e event
         */
        public void actionPerformed (ActionEvent e)
        {
            if (ACTION_DISPOSE.equals(e.getActionCommand()))
            {
                Object source = e.getSource();
                while (source != null)
                {
                    if (source instanceof Window)
                    {
                        ((Window)source).dispose();
                        return;
                    }
                    if (source instanceof Container)
                        source = ((Container)source).getParent();
                    else
                        source = null;
                }
            }
            else
                System.out.println("Action: " + e);
        }	//	actionPerformed
    }	//	DialogAction
}
