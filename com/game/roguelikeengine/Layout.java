package com.game.roguelikeengine;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Layout {
	private BufferedImage menu_img;
    private BufferedImage backgroundtext_img;
	// screen methods
    public Layout() {
    	try {
			// tiles
			menu_img= ImageIO.read(new File("img/menu.png"));
			backgroundtext_img= ImageIO.read(new File("img/text_background.png"));  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	public BufferedImage getmenubackground() {
		return menu_img;
	}
	public BufferedImage gettextbackground() {
		return backgroundtext_img;
	}
	public void drawString(Graphics g, String text, int x, int y) { // this function overrides drawstring and add newline functionality
		for (String line : text.split("\n"))
			g.drawString(line, x, y += g.getFontMetrics().getHeight());
	}

}
