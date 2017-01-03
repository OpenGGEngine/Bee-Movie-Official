/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bee.movie;

import bee.movie.pathfinding.Cell;
import bee.movie.pathfinding.Path;
import com.opengg.core.math.Vector3f;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Javier
 */
public class Bee {
    Cell current;
    Cell next;
    float percent;
    int lpos;
    Vector3f pos;
    String name;
    boolean complete;
    List<Cell> path = new ArrayList<>();
    
    public Bee(Path p){
        this.path = p;
        complete = false;
        lpos = 0;
        current = p.get(lpos);
        next = p.get(lpos+1);
        percent = 0;
        pos = new Vector3f(current.getX(), current.getY(), current.getZ());
        name = "Barry B Benson";
    }
}
