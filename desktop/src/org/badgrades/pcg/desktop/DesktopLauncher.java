package org.badgrades.pcg.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.badgrades.pcg.gameoflife.GOLAdapter;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "PcgSandbox";
		config.width = 1280;
		config.height = 736; // Easily rounds to 32px and 16px
		new LwjglApplication(new GOLAdapter(), config);
	}
}
