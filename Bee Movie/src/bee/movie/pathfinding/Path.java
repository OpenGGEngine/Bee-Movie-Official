/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bee.movie.pathfinding;

import java.util.LinkedList;

/**
 *
 * @author Warren
 */
public class Path extends LinkedList<Cell> {

	private static final long serialVersionUID = -5572661613938583005L;
	private boolean isComplete = false;

	/**
	 * Return true if the path concludes at the CellSpace's goal Cell.
	 * 
	 * @return
	 */
	public boolean isComplete() {
		return isComplete;
	}

	protected void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

}
