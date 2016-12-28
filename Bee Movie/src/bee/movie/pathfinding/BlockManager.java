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

	protected PathTemplate space;

	public BlockManager(PathTemplate space) {
		super();
		this.space = space;
	}

	public abstract boolean isBlocked(Cell cell);

	public PathTemplate getSpace() {
		return space;
	}

}
