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
import com.opengg.core.engine.AudioController;
import com.opengg.core.engine.EngineInfo;
import com.opengg.core.engine.OpenGG;
import com.opengg.core.engine.RenderEngine;
import com.opengg.core.engine.WorldManager;
import com.opengg.core.io.input.keyboard.KeyboardController;
import com.opengg.core.io.input.keyboard.KeyboardListener;
import com.opengg.core.math.Vector3f;
import com.opengg.core.movement.MovementLoader;
import com.opengg.core.render.objects.ObjectCreator;
import com.opengg.core.render.texture.Cubemap;
import com.opengg.core.render.window.DisplayMode;
import com.opengg.core.render.window.GLFWWindow;
import static com.opengg.core.render.window.RenderUtil.endFrame;
import static com.opengg.core.render.window.RenderUtil.startFrame;
import com.opengg.core.render.window.Window;
import static com.opengg.core.util.GlobalUtil.print;
import com.opengg.core.world.Camera;
import com.opengg.core.world.World;

/**
 *
 * @author Warren
 */
public class BeeMovie implements KeyboardListener{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BeeMovie beeMovie = new BeeMovie();
    }
    public float xrot, yrot;
    private final Window win;
    private Camera c;
    private AudioListener as;
    private Vector3f pos = new Vector3f();
    private Vector3f rot = new Vector3f();
    private World w;
    private boolean lock;
    
    public BeeMovie(){
        OpenGG.initializeOpenGG();
        win = new GLFWWindow(1280, 960, "Bee Movie", DisplayMode.WINDOWED);   
        KeyboardController.addToPool(this); 
        
        setup();
         
        while (!win.shouldClose()) {
            startFrame();
            update();
            render();
            endFrame(win);
        }
        OpenGG.closeEngine();
    }
    
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
        MovementLoader.setup(80);
        
        OpenGG.initializeRenderEngine(this);
        OpenGG.initializeAudioController();

        c = new Camera(pos, rot);
        c.setPos(pos);
        c.setRot(rot);

        print("Shader/VAO Loading and Generation Complete");
          
        as = new AudioListener();
        AudioController.setListener(as);
        Cubemap cb = new Cubemap();
        cb.loadTexture("C:/res/skybox/majestic");
        
        w = WorldManager.getDefaultWorld();
        EngineInfo.curworld = w;
        w.floorLev = -10;
        
        RenderEngine.setSkybox(ObjectCreator.createCube(1500f), cb);
    }
    
    public void render(){
        rot = new Vector3f(yrot, xrot, 0);
        if(lock){
            rot = MovementLoader.processRotation(15, false);
        }
        pos = MovementLoader.processMovement(pos, rot);
        
        as.setPos(pos);
        as.setRot(rot);
        AudioController.setListener(as);
        
        c.setPos(pos);
        c.setRot(rot);

        RenderEngine.controller.setLightPos(new Vector3f(40, 200, 40));
        RenderEngine.controller.setView(c);
        RenderEngine.controller.setPerspective(90, win.getRatio(), 1, 2500f);
        
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
