package com.skyousuke.libgdx.example.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.skyousuke.libgdx.example.GdxExample;
import com.skyousuke.libgdx.example.GdxExamples;

import javax.swing.*;

public class DesktopLauncher extends JFrame implements SampleLauncher {
	private static final int GAME_SCREEN_WIDTH = 1280;
	private static final int GAME_SCREEN_HEIGHT = 720;

	public DesktopLauncher() {
		super("Gdx Example Collection");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(new SampleList(this));
		pack();
		setSize(Math.max(getWidth(), 600), 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main (String[] arg) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new DesktopLauncher();
	}

	@Override
	public boolean launchSample(String sampleName) {
		GdxExample sample = GdxExamples.newSample(sampleName);

		if (sample != null) {
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.width = GAME_SCREEN_WIDTH;
			config.height = GAME_SCREEN_HEIGHT;
			config.title = sampleName;
			config.forceExit = false;

			new LwjglApplication(sample, config);

			dispose();

			return true;
		}

		return false;
	}



}
