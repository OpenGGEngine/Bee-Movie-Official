/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bee.movie;

import bee.movie.pathfinding.Cell;
import bee.movie.pathfinding.CellSpace;
import bee.movie.pathfinding.CostBlockManager;
import bee.movie.pathfinding.Path;
import bee.movie.pathfinding.Pathfinder;

/**
 *
 * @author Warren
 */
public class BeeMovie {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       CellSpace space = new CellSpace();

		// Cell goalCell = space.makeNewCell(1, 1, -1);
		space.setGoalCell(1, 1, -1);
		space.setStartCell(10, 7, 7);

		CostBlockManager blockManager = new CostBlockManager(space);
		blockManager.blockCell(space.makeNewCell(6, 6, 3));
		blockManager.blockCell(space.makeNewCell(6, 5, 4));

		// The following traps the pathfinder
		// blockManager.blockCell(space.makeNewCell(11, 7, 7));
		// blockManager.blockCell(space.makeNewCell(10, 8, 7));
		// blockManager.blockCell(space.makeNewCell(9, 7, 7));
		// blockManager.blockCell(space.makeNewCell(10, 6, 7));
		// blockManager.blockCell(space.makeNewCell(10, 7, 8));
		// blockManager.blockCell(space.makeNewCell(10, 7, 6));

		Pathfinder pathfinder = new Pathfinder(blockManager);

		// get and print the path
		Path path = pathfinder.findPath(); 
                
                for(Cell c: path){
                    System.out.println(c.toString());
                }
    }
    
}
