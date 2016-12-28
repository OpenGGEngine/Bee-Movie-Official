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
 
public class CellData {
	private double g;
	private double rhs;
	private double cost;

	/**
	 * Returns a blank CellInfo with the default cost.
	 */
	public CellData() {
		super();
		this.cost = Cell.DEFAULT_COST;
	}
        
        /*
	 * RHS value.
	 * RHS is the g value + the estimated cost to move to this Cell.
	 */
	public double getRhs() {
		return rhs;
	}
        
	/*
	 * G is cost of the path from the start Cell to this Cell.
	 */
	public double getG() {
		return g;
	}

	public void setG(double g) {
		this.g = g;
	}

	

	public void setRhs(double rhs) {
		this.rhs = rhs;
	}
	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "CellInfo [g=" + g + ", rhs=" + rhs + ", cost=" + cost + "]";
	}

}
