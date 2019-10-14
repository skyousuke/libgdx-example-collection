package com.skyousuke.libgdx.example.desktop;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.files.FileHandle;
import com.skyousuke.libgdx.example.GdxExamples;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SampleList extends JPanel {

    private SampleLauncher launcher;

    public SampleList (SampleLauncher launcher) {
        this.launcher = launcher;

        setLayout(new BorderLayout());

        final JButton button = new JButton("Run Example");

        final JList list = new JList(GdxExamples.getNames().toArray());
        JScrollPane pane = new JScrollPane(list);

        DefaultListSelectionModel m = new DefaultListSelectionModel();
        m.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m.setLeadAnchorNotificationEnabled(false);
        list.setSelectionModel(m);

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked (MouseEvent event) {
                if (event.getClickCount() == 2) button.doClick();
            }
        });

        list.addKeyListener(new KeyAdapter() {
            public void keyPressed (KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) button.doClick();
            }
        });

        final Preferences prefs = new LwjglPreferences(new FileHandle(new LwjglFiles().getExternalStoragePath() + ".prefs/GdxExampleSkyousuke"));
        list.setSelectedValue(prefs.getString("last", null), true);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {
                String testName = (String)list.getSelectedValue();
                prefs.putString("last", testName);
                prefs.flush();
                SampleList.this.launcher.launchSample((testName));
            }
        });

        add(pane, BorderLayout.CENTER);
        add(button, BorderLayout.SOUTH);
    }
}