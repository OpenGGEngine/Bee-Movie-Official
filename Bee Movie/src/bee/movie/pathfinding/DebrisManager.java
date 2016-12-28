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
public class DebrisManager extends BlockManager {

	public DebrisManager(PathTemplate space) {
		super(space);
	}

	public void blockCell(Cell blockedCell) {
		PathTemplate space = super.getSpace();

		if ((blockedCell.equals(space.getStartCell())) || (blockedCell.equals(space.getGoalCell()))) {
			return;
		}

		space.makeNewCell(blockedCell);
		space.updateCellCost(blockedCell, -1);
	}

        @Override
	public boolean isBlocked(Cell cell) {
		PathTemplate space = super.getSpace();
		CellData info = space.getInfo(cell);

		if (info == null) {
			return false;
		}

		return (info.getCost() < 0);
	}

}
