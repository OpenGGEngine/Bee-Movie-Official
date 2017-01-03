/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bee.movie;

import bee.movie.pathfinding.Cell;
import bee.movie.pathfinding.DebrisManager;
import bee.movie.pathfinding.Path;
import bee.movie.pathfinding.PathTemplate;
import bee.movie.pathfinding.Pathfinder;
import com.opengg.core.audio.AudioListener;
import com.opengg.core.engine.GGApplication;
import com.opengg.core.engine.OpenGG;
import com.opengg.core.engine.RenderEngine;
import com.opengg.core.engine.UpdateEngine;
import static com.opengg.core.io.input.keyboard.Key.KEY_E;
import static com.opengg.core.io.input.keyboard.Key.KEY_F;
import static com.opengg.core.io.input.keyboard.Key.KEY_Q;
import static com.opengg.core.io.input.keyboard.Key.KEY_R;
import com.opengg.core.io.input.keyboard.KeyboardController;
import com.opengg.core.io.input.keyboard.KeyboardListener;
import com.opengg.core.math.Vector3f;
import com.opengg.core.model.Model;
import com.opengg.core.model.ModelLoader;
import com.opengg.core.movement.MovementLoader;
import com.opengg.core.render.drawn.InstancedDrawnObject;
import com.opengg.core.render.objects.ObjectCreator;
import com.opengg.core.render.shader.ShaderController;
import com.opengg.core.render.texture.Cubemap;
import com.opengg.core.render.texture.Texture;
import com.opengg.core.render.window.WindowInfo;
import com.opengg.core.render.window.WindowOptions;
import static com.opengg.core.render.window.WindowOptions.GLFW;
import com.opengg.core.world.Camera;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 *
 * @author Warren
 */
public final class BeeMovie extends GGApplication implements KeyboardListener {

    BeeComponent b;
    static WindowInfo w;
    static FileParser f;

    InstancedDrawnObject blocks;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        f = new FileParser();
        w = new WindowInfo();
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
    public float xrot, yrot, rot1, rot2;
    private final Camera c = new Camera();
    private AudioListener as;
    private Vector3f pos = new Vector3f(199, 139, -139);
    private Vector3f rot = new Vector3f();
    Texture t1;
    private boolean lock;
    Random rand = new Random();

    public Path process(Vector3f goal,Vector3f start) {
        PathTemplate space = new PathTemplate();
        space.setGoalCell((int) Math.floor(goal.x), (int) Math.floor(goal.y),
        (int) Math.floor( goal.z));
        space.setStartCell((int) Math.floor(start.x), (int) Math.floor(start.y), (int) Math.floor(start.z));
        DebrisManager blockManager = new DebrisManager(space);
        f.getDebeeArray();
        int numbar = (int) (f.cubeSize * 0.30);
        List<Vector3f> temp = new ArrayList<>();
        for (int i = 0; i < f.stupid.length; i++) {
            for (int i2 = 0; i2 < f.stupid[i].length; i2++) {
                for (int i3 = 0; i3 < f.stupid[i][i2].length; i3++) {
                    if (f.stupid[i][i2][i3]) {
                        blockManager.blockCell(space.makeNewCell(i, i2, i3));
                    } else {
                        if (numbar > 0) {
                            if (rand.nextBoolean() && f.notInBee(new Vector3f(i, i2, i3)) && f.notInHive(new Vector3f(i, i2, i3))) {
                                blockManager.blockCell(space.makeNewCell(i, i2, i3));
                                numbar--;
                            }

                        }
                    }
                }
            }
        }
        Pathfinder ps = new Pathfinder(blockManager);
        return ps.findPath();
      
    }

    @Override
    public void setup() {
        KeyboardController.addToPool(this);
        MovementLoader.setup(80);       

        ArrayList<Path> moarpaths = new ArrayList<>();
        int s = 0;
        
        
        for (Vector3f bpos : f.bees) {
            int index = 0;
            int tempup = 0;
            float low = Float.MAX_VALUE;
            for (Vector3f goalpos : f.hive) {
                float yup = bpos.getDistance(goalpos);
                if (low > yup) {
                    low = yup;
                    index = tempup;
                    //System.out.println(low);
                }
                tempup++;
            }
           Path p = process(f.hive.get(index),bpos);
            
            moarpaths.add(p);
            f.hive.remove(index);

        }
        int path = 1;
        int moves = 0;
        
        List<Bee> bees = new ArrayList<>();
        for (Path c : moarpaths) {
            //System.out.println("Path " + path);
            for(Cell cyto:c){
                
            //System.out.println(cyto.toString());
               moves++;
            }
             //System.out.println("______________");
            path++;

            bees.add(new Bee(c));
        }
        
        System.out.println("Num moves " + moves);
        t1 = Texture.get("C:\\res\\bbbenson.jpg");
        //Model m = ModelLoader.loadModel("C:\\res\\bigbee\\model.bmf");
        
        b = new BeeComponent(bees);
        RenderEngine.addRenderable(b);
        UpdateEngine.addObjects(b);
        
        RenderEngine.setSkybox(ObjectCreator.createCube(1500f), Cubemap.get("C:/res/skybox/majestic"));
    }

    @Override
    public void render() {
        rot = new Vector3f(yrot, xrot, 0);
        pos = MovementLoader.processMovement(pos, rot);
        //pos = b.bees.get(0).pos;
        c.setPos(pos);
        c.setRot(rot);

        ShaderController.setLightPos(new Vector3f(40, 200, 40));
        ShaderController.setView(c);
        ShaderController.setPerspective(90, OpenGG.window.getRatio(), 1, 2500f);
        t1.useTexture(0);
        t1.useTexture(1);
        t1.useTexture(2);
        t1.useTexture(3);
        t1.useTexture(4);
        t1.useTexture(5);
        RenderEngine.setCulling(false);
        RenderEngine.draw();
    }

    @Override
    public void update() {
        UpdateEngine.update();
        xrot -= rot1 * 7;
        yrot -= rot2 * 7;
    }

    @Override
    public void keyPressed(int key) {
        if (key == KEY_Q) {
            rot1 += 0.3;
        }
        if (key == KEY_E) {
            rot1 -= 0.3;
        }
        if (key == KEY_R) {
            rot2 += 0.3;
        }
        if (key == KEY_F) {
            rot2 -= 0.3;
        }
    }

    @Override
    public void keyReleased(int key) {
        if (key == KEY_Q) {
            rot1 -= 0.3;
        }
        if (key == KEY_E) {
            rot1 += 0.3;
        }
        if (key == KEY_R) {
            rot2 -= 0.3;
        }
        if (key == KEY_F) {
            rot2 += 0.3;
        }
    }

}
