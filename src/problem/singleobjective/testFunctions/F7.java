/**
 * Sphere.java
 * @version: 0.1
 * @autor: J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 * @autor: Miguel Angel Garcia Morales <talivan12@hotmail.com>
 * @autor: Hector Fraire Huacuja <hector.fraire2014@gmail.com>
 * Proyecto Biblioteca de clases para MO-JFramework 
 * Febrero de 2023
 * Puede hacer uso total o parcial de este codigo dandole credito a los autores
 */

package problem.singleobjective.testFunctions;

import problem.singleobjective.Problem;
import solution.real.Individual;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class F7 implements Problem {
    

    private double[] loweBound;
    private double[] upperBound;
            
    private int nVars;
    private int nObj;
    
    String name;
    
    public F7() {
        this.nVars = 30;
        nObj = 1;
        name = "F7_Function";
        
        loweBound = new double[nVars];
        upperBound = new double[nVars];
        for(int i=0; i<nVars; i++) {
            loweBound[i] = -128.0;
            upperBound[i] = 128.0;
        }

    }

    @Override
    public void costFunction(Individual ind) {
        //double sum = 0.0;
        double[] f = new double[this.nObj];
        f[0] = 0.0;

        for(int i=0; i<ind.getPosition().length; i++) {
            //f[0] += Math.floor(Math.pow((ind.getPosition()[i]), 2));
            //f[0] += i * Math.pow(ind.getPosition()[i], 4);
            f[0] += Math.pow(ind.getPosition()[i], 4);
        }
        
        f[0] += Math.random();
        
        ind.setCost(f);
        //ind.getCost()[0] = sum;
        nEvals++;
    }
  
    static int nEvals;
    @Override
    public int getNumEvals() {
        return nEvals;
    }
    

    /**
     * @return the loweBound
     */
    public double[] getLowerBound() {
        return loweBound;
    }

    /**
     * @param loweBound the loweBound to set
     */
    public void setLowerBound(double[] loweBound) {
        this.loweBound = loweBound;
    }

    /**
     * @return the upperBound
     */
    public double[] getUpperBound() {
        return upperBound;
    }

    /**
     * @param upperBound the upperBound to set
     */
    public void setUpperBound(double[] upperBound) {
        this.upperBound = upperBound;
    }

    /**
     * @return the nVars
     */
    public int getnVars() {
        return nVars;
    }

    /**
     * @param nVars the nVars to set
     */
    public void setnVars(int nVars) {
        this.nVars = nVars;
    }

    /**
     * @return the nObj
     */
    public int getnObj() {
        return nObj;
    }

    /**
     * @param nObj the nObj to set
     */
    public void setnObj(int nObj) {
        this.nObj = nObj;
    }

    @Override
    public String getProblemName() {
        return this.name;
    }

    @Override
    public int getNumVars() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNumObjectives() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
