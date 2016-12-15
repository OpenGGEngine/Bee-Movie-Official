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
import com.opengg.core.audio.AudioListener;
import com.opengg.core.engine.GGApplication;
import com.opengg.core.engine.OpenGG;
import com.opengg.core.engine.RenderEngine;
import com.opengg.core.io.input.keyboard.KeyboardController;
import com.opengg.core.io.input.keyboard.KeyboardListener;
import com.opengg.core.math.Vector3f;
import com.opengg.core.movement.MovementLoader;
import com.opengg.core.render.objects.ObjectCreator;
import com.opengg.core.render.texture.Cubemap;
import com.opengg.core.render.window.WindowInfo;
import com.opengg.core.render.window.WindowOptions;
import static com.opengg.core.render.window.WindowOptions.GLFW;
import com.opengg.core.world.Camera;

/**
 *
 * @author Warren
 */
public final class BeeMovie extends GGApplication implements KeyboardListener{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WindowInfo w = new WindowInfo();
        w.displaymode = WindowOptions.WINDOWED;
        w.height = 1024;
        w.width = 1280;
        w.resizable = false;
        w.type = GLFW;
        w.vsync = true;
        w.name = "Bee Project";
        OpenGG.initialize(new BeeMovie(), w);
        OpenGG.run();
    }
    public float xrot, yrot;
    private final Camera c = new Camera();
    private AudioListener as;
    private Vector3f pos = new Vector3f();
    private Vector3f rot = new Vector3f();
    private boolean lock;
    
    public void process(){
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
    
    public void setup(){
        KeyboardController.addToPool(this); 
        MovementLoader.setup(80);
        
        RenderEngine.setSkybox(ObjectCreator.createCube(1500f), Cubemap.get("C:/res/skybox/majestic"));
    }
    
    public void render(){
        rot = MovementLoader.processRotation(5, false);
        pos = MovementLoader.processMovement(pos, rot);

        c.setPos(pos);
        c.setRot(rot);

        RenderEngine.controller.setLightPos(new Vector3f(40, 200, 40));
        RenderEngine.controller.setView(c);
        RenderEngine.controller.setPerspective(90, OpenGG.window.getRatio(), 1, 2500f);
        
        RenderEngine.draw();
    }
    
    public void update(){ 
        
    }

    @Override
    public void keyPressed(int key) {
        
    }

    @Override
    public void keyReleased(int key) {
        
    }
    
}
