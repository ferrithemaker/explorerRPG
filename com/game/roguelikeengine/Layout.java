package com.game.roguelikeengine;

/*
Copyright (C) 2013  Ferran Fabregas (ferri.fc@gmail.com)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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
