/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bee.movie;

import com.opengg.core.math.Quaternionf;
import com.opengg.core.math.Vector3f;
import com.opengg.core.render.drawn.Drawable;
import com.opengg.core.render.drawn.InstancedDrawnObject;
import com.opengg.core.render.objects.ObjectCreator;
import com.opengg.core.world.components.Component;
import com.opengg.core.world.components.Renderable;
import com.opengg.core.world.components.Updatable;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Javier
 */
public class BeeComponent implements Renderable, Updatable{
    List<Bee> bees = new ArrayList<>();
    InstancedDrawnObject r;
    
    public BeeComponent(List<Bee> bees){
        this.bees = bees;
        Buffer[] b = ObjectCreator.createQuadPrismBuffers(new Vector3f(-1,-1,-1), new Vector3f(1,-1, 1));
        r = new InstancedDrawnObject((FloatBuffer)b[0], (IntBuffer)b[1], Vector3f.listToBuffer(new Vector3f()));
    }
    
    @Override
    public void render() {
        Vector3f[] vs = new Vector3f[bees.size()];
        int i = 0;
        for(Bee ps : bees){
            vs[i] = ps.pos;
            i++;
        }
        r.setPositions(Vector3f.listToBuffer(vs), bees.size());
        r.draw();
    }

    @Override
    public Drawable getDrawable() {
        return r;
    }

    @Override
    public void setPosition(Vector3f pos) {
        
    }

    @Override
    public void setRotation(Quaternionf rot) {
        
    }

    @Override
    public Vector3f getPosition() {
        return new Vector3f();
    }

    @Override
    public Quaternionf getRotation() {
        return new Quaternionf();
    }

    @Override
    public void setParentInfo(Component parent) {
        
    }

    @Override
    public void update(float delta) {
        bees.stream().forEach((Bee b) -> {
            if(b.complete)
                return;
            b.percent += delta/1000;
            if(b.percent > 1){
                b.current = b.next;
                b.pos = new Vector3f(b.current.getX(), b.current.getY(), b.current.getZ());
                b.lpos++;
                try{
                    b.next = b.path.get(b.lpos);
                }catch(IndexOutOfBoundsException e){
                    b.complete = true;
                }
            }
        });
    }

    @Override
    public void setScale(Vector3f v) {}

    @Override
    public Vector3f getScale() {
        return new Vector3f();
    }
}
