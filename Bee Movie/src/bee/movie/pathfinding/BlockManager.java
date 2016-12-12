/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bee.movie.pathfinding;

/**
 *
 * @author Warren
 */
public abstract class BlockManager {

	protected CellSpace space;

	/**
	 * Returns an implementation of BlockManager. All BlockManagers should take
	 * a CellSpace as a parameter, as the BlockManager will determine which
	 * Cells in the CellSpace are blocked.
	 * 
	 * @param space
	 */
	public BlockManager(CellSpace space) {
		super();
		this.space = space;
	}

	/**
	 * True if the cell is impassable by the PathFinder. False otherwise.
	 * 
	 * @param cell
	 * @return
	 */
	public abstract boolean isBlocked(Cell cell);

	/**
	 * Get the CellSpace managed by this BlockManager.
	 * 
	 * @return
	 */
	public CellSpace getSpace() {
		return space;
	}

}
