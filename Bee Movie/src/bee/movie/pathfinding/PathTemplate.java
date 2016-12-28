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

//Holds level data.

public class PathTemplate {

	private HashMap<Cell, CellData> cellHash = new HashMap<Cell, CellData>();
	private double kM = 0.0;
	private Cell startCell;
	private Cell goalCell;

	public PathTemplate() {
		super();
	}
	public CellData getInfo(Cell cell) {
		return cellHash.get(cell);
	}

	public void updateCellCost(Cell cell, double cost) {
		if (cell == null) {
			return;
		}

		cellHash.get(cell).setCost(cost);
	}
	public double getG(Cell cell) {
		if (cell == null) {
			return 0.0;
		}

		CellData info = cellHash.get(cell);

		if (info == null) {
			return 0.0;
		}

		return info.getG();
	}

	public Cell makeNewCell(int x, int y, int z) {
		return makeNewCell(x, y, z, null);
	}

	public Cell makeNewCell(int x, int y, int z, Costs k) {
		Cell state = new Cell();
            
		state.setX(x);
		state.setY(y);
		state.setZ(z);
		state.setKey(k);

		return makeNewCell(state);
	}

	public Cell makeNewCell(Cell cell) {
		if (cellHash.get(cell) != null) {
			return cell;
		}

		CellData cellInfo = new CellData();

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

	public void setStartCell(int x, int y, int z) {
		Cell cell = new Cell();
		cell.setX(x);
		cell.setY(y);
		cell.setZ(z);
		this.startCell = cell;

		CellData startCellInfo = new CellData();
		double totalPathCost = Geometry.euclideanDistance(startCell, goalCell);
		startCellInfo.setRhs(totalPathCost);
		startCellInfo.setG(totalPathCost);
		cellHash.put(startCell, startCellInfo);

		this.startCell = calculateKey(startCell);
	}

	public Cell getStartCell() {
		return startCell;
	}
        
	public void setGoalCell(int x, int y, int z) {
		Cell cell = new Cell();
		cell.setX(x);
		cell.setY(y);
		cell.setZ(z);

		this.goalCell = cell;
		this.cellHash.put(goalCell, new CellData());
	}

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
		LinkedList<Cell> successors = new LinkedList<>();
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
		LinkedList<Cell> predecessors = new LinkedList<>();
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
