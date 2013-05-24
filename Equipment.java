/*
    Copyright (C) 2013  Ferran Fàbregas (ferri.fc@gmail.com)

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

public class Equipment {
	private Object lefthand;
	private Object righthand;
	private Object head;
	private Object vest;
	private Object legs;
	private Object foot;
	public Equipment () {
		
	}
	
	public int set_lefthand(Object obj) {
		this.lefthand=obj;
		return 0;
	}
	
	public Object get_lefthand() {
		return this.lefthand;
	}
}
