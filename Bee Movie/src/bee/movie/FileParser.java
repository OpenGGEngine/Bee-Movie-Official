/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bee.movie;

import com.opengg.core.math.Vector3f;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Warren
 */
public class FileParser{
    
    final int runNumber = 1;
    private BufferedReader br;
    public int cubeSize;
    public ArrayList<Vector3f> bees = new ArrayList<>();
    public ArrayList<Vector3f> hive = new ArrayList<>();
    
   public boolean[][][] stupid;
    
    public FileParser() throws FileNotFoundException, IOException {
        br = new BufferedReader(new FileReader("C:/res/beesetup"+runNumber+".txt"));
        br.readLine();
        StringTokenizer st = new StringTokenizer(br.readLine());
        cubeSize = Integer.parseInt(st.nextToken(","));
        for (int i = 0; i < 15; i++) {
            st = new StringTokenizer(br.readLine());
            bees.add(new Vector3f(Integer.parseInt(st.nextToken(",")),Integer.parseInt(st.nextToken(",")),Integer.parseInt(st.nextToken(","))));
        }
        for (int i = 0; i < 15; i++) {
            st = new StringTokenizer(br.readLine());
            hive.add(new Vector3f(Integer.parseInt(st.nextToken(",")),Integer.parseInt(st.nextToken(",")),Integer.parseInt(st.nextToken(","))));
        }
    }
    
    public boolean[][][] getBeeArray() {
        boolean arr[][][] = new boolean[cubeSize+2][cubeSize+2][cubeSize+2];
        for (Vector3f bee: bees) {
            arr[(int)bee.x][(int)bee.y][(int)bee.z] = true;
        }
        return arr;
    }
    
    public void getDebeeArray() {
        System.out.println(cubeSize);
        boolean arr[][][] = new boolean[cubeSize+2][cubeSize+2][cubeSize+2];
        for(int i = 0; i < cubeSize+2; i++) {
            for(int j = 0; j < cubeSize+2; j++) {
                arr[0][i][j] = true;
                arr[i][j][0] = true;
                arr[i][0][j] = true;
                arr[cubeSize+1][i][j] = true;
                arr[i][j][cubeSize+1] = true;
                arr[i][cubeSize+1][j] = true;
            }
        }
        stupid = arr;
    }
    
    public boolean[][][] getHiveArray() {
        boolean arr[][][] = new boolean[cubeSize+2][cubeSize+2][cubeSize+2];
        for (Vector3f h: hive) {
            arr[(int)h.x][(int)h.y][(int)h.z] = true;
        }
        return arr;
    }
    public boolean notInBee(Vector3f i){
        for(Vector3f i2: bees){
            if(i2.equals(i)){
                return false;
            }
        }
        return true;
    }
    public boolean notInHive(Vector3f i){
        for(Vector3f i2: hive){
            if(i2.equals(i)){
                return false;
            }
        }
        return true;
    }
    
}
