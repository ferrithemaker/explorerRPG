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

	import java.awt.Component; 
	import java.awt.event.*; 

	/** 
	 * Makes handling input a lot simpler 
	 */ 
	public class Inputhandler implements KeyListener 
	{        
	        /** 
	         * Assigns the newly created InputHandler to a Component 
	         * @param c Component to get input from 
	         */
			private boolean keys[];
	        public Inputhandler(Component c) 
	        { 
	                c.addKeyListener(this); 
	                keys = new boolean[256];
	        } 
	        
	        /** 
	         * Checks whether a specific key is down 
	         * @param keyCode The key to check 
	         * @return Whether the key is pressed or not 
	         */ 
	        public boolean isKeyDown(int keyCode) 
	        { 
	                if (keyCode > 0 && keyCode < 256) 
	                { 
	                        return keys[keyCode]; 
	                } 
	                
	                return false; 
	        } 
	        
	        /** 
	         * Called when a key is pressed while the component is focused 
	         * @param e KeyEvent sent by the component 
	         */ 
	        public void keyPressed(KeyEvent e) 
	        { 
	                if (e.getKeyCode() > 0 && e.getKeyCode() < 256) 
	                { 
	                        keys[e.getKeyCode()] = true; 
	                } 
	        } 

	        /** 
	         * Called when a key is released while the component is focused 
	         * @param e KeyEvent sent by the component 
	         */ 
	        public void keyReleased(KeyEvent e) 
	        { 
	                if (e.getKeyCode() > 0 && e.getKeyCode() < 256) 
	                { 
	                        keys[e.getKeyCode()] = false; 
	                } 
	        } 

	        /** 
	         * Not used 
	         */ 
	        public void keyTyped(KeyEvent e){}
	}
