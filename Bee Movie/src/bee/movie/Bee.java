/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bee.movie;

import bee.movie.pathfinding.Cell;
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
}
