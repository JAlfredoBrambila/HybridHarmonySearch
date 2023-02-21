/**
 * Rosenbrock.java
 * @version: 0.1
 * @autor: J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 * @autor: Miguel Angel Garcia Morales <talivan12@hotmail.com>
 * @autor: Hector Fraire Huacuja <hector.fraire2014@gmail.com>
 * Proyecto Biblioteca de clases para MO-JFramework 
 * Enero de 2022
 * Puede hacer uso total o parcial de este codigo dandole credito a los autores
 */

package problem.singleobjective.testFunctions;

import problem.singleobjective.Problem;
import solution.real.Individual;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class Rosenbrock implements Problem{
    
    private double[] loweBound;
    private double[] upperBound;
            
    private int nVars;
    private int nObj;
    
    String name;
    
    public Rosenbrock() {
        this.nVars = 30;
        nObj = 1;
        name = "Rosenbrock_Function";
        loweBound = new double[nVars];
        upperBound = new double[nVars];
        for(int i=0; i<nVars; i++) {
            loweBound[i] = -30.0;
            upperBound[i] = 30.0;
        }
        
        //loweBound = 
        //upperBound
    }

    @Override
    public void costFunction(Individual ind) {
        double[] f = new double[nObj];
        f[0] = 0.0;
        double x1,x2 = 0.0;
        //double z = 0.0;
        // 6.60103844782264, -3.713312632580859, -7.894108847593529, 2.976340291660179, -4.785419806564358
        for(int i=0; i<ind.getPosition().length-1; i++) {
            x1 = ind.getPosition()[i];
            x2 = ind.getPosition()[i+1];
            f[0] += 100 * Math.pow((x2 - Math.pow(x1, 2)), 2) + Math.pow((x1-1), 2);
        }
        /*for(int i=0; i<ind.getPosition().length-1; i++) {
            x1 = ind.getPosition()[i];
            x2 = ind.getPosition()[i+1];
            sum += 100 * Math.pow((Math.pow(x1, 2) - x2), 2) + Math.pow((1-x1), 2);
        }*/
        //ind.getCost()[0] = sum;
        ind.setCost(f);
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
