/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bee.movie.pathfinding;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Warren
 */
public class CellSpace {

	private HashMap<Cell, CellInfo> cellHash = new HashMap<Cell, CellInfo>();
	private double kM = 0.0;
	private Cell startCell;
	private Cell goalCell;

	/**
	 * Returns an empty CellSpace
	 */
	public CellSpace() {
		super();
	}

	/**
	 * Returns the specified Cell's CellInfo
	 * 
	 * @param cell
	 * @return
	 */
	public CellInfo getInfo(Cell cell) {
		return cellHash.get(cell);
	}

	/**
	 * Update the specified Cell's cost using the specified double.
	 * 
	 * @param cell
	 * @param cost
	 */
	public void updateCellCost(Cell cell, double cost) {
		if (cell == null) {
			return;
		}

		cellHash.get(cell).setCost(cost);
	}

	/**
	 * Get the g value of the specified Cell.
	 * 
	 * The g value, as specified by
	 * <a href="http://idm-lab.org/bib/abstracts/papers/aaai02b.pdf">Sven
	 * Koenig</a>, is the cost of the path from the start Cell to this Cell.
	 * 
	 * @param cell
	 * @return
	 */
	public double getG(Cell cell) {
		if (cell == null) {
			return 0.0;
		}

		CellInfo info = cellHash.get(cell);

		if (info == null) {
			return 0.0;
		}

		return info.getG();
	}

	/**
	 * Build a Cell in the CellSpace using the specified x, y, z coordinates.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public Cell makeNewCell(int x, int y, int z) {
		return makeNewCell(x, y, z, null);
	}

	/**
	 * Build a Cell in the CellSpace using the specified x, y, and z coordinates
	 * plus the specified Costs.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param k
	 * @return
	 */
	public Cell makeNewCell(int x, int y, int z, Costs k) {
		Cell state = new Cell();
            
		state.setX(x);
		state.setY(y);
		state.setZ(z);
		state.setKey(k);

		return makeNewCell(state);
	}

	/**
	 * Build a Cell in the CellSpace which is a copy of the specified Cell
	 * 
	 * @param cell
	 * @return
	 */
	public Cell makeNewCell(Cell cell) {
		if (cellHash.get(cell) != null) {
			return cell;
		}

		CellInfo cellInfo = new CellInfo();

		if (goalCell == null) {
			throw new RuntimeException("Goal cell not set");
		}

		double costToGoal = Geometry.euclideanDistance(cell, goalCell);
		cellInfo.setRhs(costToGoal);
		cellInfo.setG(costToGoal);
		cellHash.put(cell, cellInfo);

		Costs key = cell.getKey();
		if (key != null && !key.equals(new Costs(-1.0, -1.0))) {
			updateVertex(cell);
		}

		calculateKey(cell);

		return cell;
	}

	/**
	 * Set this CellSpace's start Cell.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setStartCell(int x, int y, int z) {
		Cell cell = new Cell();
		cell.setX(x);
		cell.setY(y);
		cell.setZ(z);
		this.startCell = cell;

		CellInfo startCellInfo = new CellInfo();
		double totalPathCost = Geometry.euclideanDistance(startCell, goalCell);
		startCellInfo.setRhs(totalPathCost);
		startCellInfo.setG(totalPathCost);
		cellHash.put(startCell, startCellInfo);

		this.startCell = calculateKey(startCell);
	}

	/**
	 * Get this CellSpace's start Cell
	 * 
	 * @return
	 */
	public Cell getStartCell() {
		return startCell;
	}

	/**
	 * Set this CellSpace's goal Cell.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setGoalCell(int x, int y, int z) {
		Cell cell = new Cell();
		cell.setX(x);
		cell.setY(y);
		cell.setZ(z);

		this.goalCell = cell;
		this.cellHash.put(goalCell, new CellInfo());
	}

	/**
	 * Get this CellSpace's goal Cell.
	 * 
	 * @return
	 */
	public Cell getGoalCell() {
		return goalCell;
	}

	protected boolean isClose(double var1, double var2) {
		if (var1 == Double.POSITIVE_INFINITY && var2 == Double.POSITIVE_INFINITY) {
			return true;
		}

		return (Math.abs(var1 - var2) < 0.00001);
	}

	private void updateVertex(Cell cell) {
		LinkedList<Cell> successors = new LinkedList<Cell>();

		if (!cell.equals(getGoalCell())) {
			successors = getSuccessors(cell);
			double tmp = Double.POSITIVE_INFINITY;
			double tmp2;

			for (Cell successor : successors) {
				tmp2 = getG(successor) + Geometry.euclideanDistance(cell, successor);
				if (tmp2 < tmp) {
					tmp = tmp2;
				}
			}

			if (!isClose(getRHS(cell), tmp)) {
				setRHS(cell, tmp);
			}
		}

		if (!isClose(getG(cell), getRHS(cell))) {
			insertCell(cell);
		}
	}

	private void setRHS(Cell state, double rhs) {
		makeNewCell(state);
		cellHash.get(state).setRhs(rhs);
	}

	private double getRHS(Cell state) {
		if (goalCell == null) {
			throw new RuntimeException("Goal cell not set");
		}

		if (state == goalCell) {
			return 0;
		}

		if (cellHash.get(state) == null) {
			return Geometry.euclideanDistance(state, goalCell);
		}

		return cellHash.get(state).getRhs();
	}

	private void insertCell(Cell cell) {
		cell = calculateKey(cell);
	}

	// TODO Refactor with predeccesors.
	public LinkedList<Cell> getSuccessors(Cell state) {
		LinkedList<Cell> successors = new LinkedList<Cell>();
		Cell tempState;

		// Generate the successors, starting at the immediate right and moving
		// in a clockwise manner
		tempState = makeNewCell(state.getX() + 1, state.getY(), state.getZ(), new Costs(-1.0, -1.0));
		successors.addFirst(tempState);

		tempState = makeNewCell(state.getX(), state.getY() + 1, state.getZ(), new Costs(-1.0, -1.0));
		successors.addFirst(tempState);

		tempState = makeNewCell(state.getX() - 1, state.getY(), state.getZ(), new Costs(-1.0, -1.0));
		successors.addFirst(tempState);

		tempState = makeNewCell(state.getX(), state.getY() - 1, state.getZ(), new Costs(-1.0, -1.0));
		successors.addFirst(tempState);

		// Up one z level
		tempState = makeNewCell(state.getX(), state.getY(), state.getZ() + 1, new Costs(-1.0, -1.0));
		successors.addFirst(tempState);

		// Down one z level
		tempState = makeNewCell(state.getX(), state.getY(), state.getZ() - 1, new Costs(-1.0, -1.0));
		successors.addFirst(tempState);

		return successors;
	}

	public LinkedList<Cell> getPredecessors(Cell state) {
		LinkedList<Cell> predecessors = new LinkedList<Cell>();
		Cell tempState;

		tempState = makeNewCell(state.getX() + 1, state.getY(), state.getZ(), new Costs(-1.0, -1.0));
		predecessors.addFirst(tempState);

		tempState = makeNewCell(state.getX(), state.getY() + 1, state.getZ(), new Costs(-1.0, -1.0));
		predecessors.addFirst(tempState);

		tempState = makeNewCell(state.getX() - 1, state.getY(), state.getZ(), new Costs(-1.0, -1.0));
		predecessors.addFirst(tempState);

		tempState = makeNewCell(state.getX(), state.getY() - 1, state.getZ(), new Costs(-1.0, -1.0));
		predecessors.addFirst(tempState);

		tempState = makeNewCell(state.getX(), state.getY(), state.getZ() + 1, new Costs(-1.0, -1.0));
		predecessors.addFirst(tempState);

		tempState = makeNewCell(state.getX(), state.getY(), state.getZ() - 1, new Costs(-1.0, -1.0));
		predecessors.addFirst(tempState);

		return predecessors;
	}

	private Cell calculateKey(Cell state) {
		Cell startCell = getStartCell();

		if (startCell == null) {
			throw new RuntimeException("Start cell not set");
		}

		double cost = Math.min(getRHS(state), getG(state));

		Costs key = state.getKey();

		if (key == null) {
			key = new Costs(0.0, 0.0);
		}

		key.setCostPlusHeuristic(cost + Geometry.euclideanDistance(state, startCell) + kM);
		key.setCost(cost);

		return state;
	}
}
