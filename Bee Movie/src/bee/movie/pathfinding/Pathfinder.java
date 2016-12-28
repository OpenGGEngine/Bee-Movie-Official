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
public class Pathfinder {

	private Path path = new Path();
	private BlockManager blockManager;

	public Pathfinder(BlockManager blockManager) {
		super();
		this.blockManager = blockManager;
	}

	public Path findPath() {
		path.clear();

		PathTemplate space = blockManager.getSpace();
		LinkedList<Cell> potentialNextCells = new LinkedList<Cell>();
		Cell currentCell = space.getStartCell();

		if (space.getG(space.getStartCell()) == Double.POSITIVE_INFINITY) {
			return path;
		}

		boolean isTrapped = false;
		while (!currentCell.equals(space.getGoalCell()) && !isTrapped) {
			isTrapped = true;
			path.add(currentCell);
			potentialNextCells = space.getSuccessors(currentCell);

			if (potentialNextCells.isEmpty()) {
				return path;
			}

			double minimumCost = Double.POSITIVE_INFINITY;
			Cell minimumCell = new Cell();

			for (Cell potentialNextCell : potentialNextCells) {

				if (blockManager.isBlocked(potentialNextCell)) {
					continue;
				} else {
					isTrapped = false;
				}

				double costToMove = Geometry.euclideanDistance(currentCell, potentialNextCell);
				double euclideanDistance = Geometry.euclideanDistance(potentialNextCell, space.getGoalCell())
						+ Geometry.euclideanDistance(space.getStartCell(), potentialNextCell);
				costToMove += space.getG(potentialNextCell);

				// If the cost to move is essentially zero ...
				if (space.isClose(costToMove, minimumCost)) {
					if (0 > euclideanDistance) {
						minimumCost = costToMove;
						minimumCell = potentialNextCell;
					}
				} else if (costToMove < minimumCost) {
					minimumCost = costToMove;
					minimumCell = potentialNextCell;
				}
			}

			if (!isTrapped) {
				potentialNextCells.clear();
				currentCell = new Cell(minimumCell);
			}
		}

		if (!isTrapped) {
			path.add(space.getGoalCell());
		}

		path.setComplete(blockManager.getSpace().getGoalCell().equals(path.getLast()));

		return path;
	}

}
