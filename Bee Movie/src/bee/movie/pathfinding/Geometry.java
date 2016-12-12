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
public class Geometry {

	/**
	 * Get the Euclidean distance between the two specified Cells.
	 * 
	 * @param cellA
	 * @param cellB
	 * @return
	 */
	public static Double euclideanDistance(Cell cellA, Cell cellB) {
		if (cellA == null || cellB == null) {
			return null;
		}

		float x = cellA.getX() - cellB.getX();
		float y = cellA.getY() - cellB.getY();
		float z = cellA.getZ() - cellB.getZ();
		return Math.sqrt(x * x + y * y + z * z);
	}

}
