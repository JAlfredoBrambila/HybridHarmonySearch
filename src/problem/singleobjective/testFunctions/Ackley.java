/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package problem.singleobjective.testFunctions;

import problem.singleobjective.Problem;
import solution.real.Individual;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class Ackley implements Problem {
    double a = 20;
    double b = 0.2;
    double c = 2 * Math.PI;
    
    private double[] loweBound;
    private double[] upperBound;
            
    private int nVars;
    private int nObj;
    
    String name;
    
    
    
    public Ackley() {
        this.nVars = 30;
        nObj = 1;
        name = "Ackley_Function";
        loweBound = new double[nVars];
        upperBound = new double[nVars];
        for(int i=0; i<nVars; i++) {
            //loweBound[i] = -32.768;
            //upperBound[i] = 32.768;
            //
            loweBound[i] = -32.0;
            upperBound[i] = 32.0;
        }
        
        //loweBound = 
        //upperBound
    }
    
    @Override
    public void costFunction(Individual ind) {
        int d = ind.getPosition().length;
        double[] f = new double[this.nObj];
        f[0] = 0.0;
        
        double sum1 = 0.0;
        double sum2 = 0.0;
        for(int i=0; i<d; i++) {
            sum1 += Math.pow(ind.getPosition()[i], 2);
            sum2 += Math.cos(c * ind.getPosition()[i]);
        }
        
        f[0] = (-1 * a) * Math.exp((-1 * b) * Math.sqrt(sum1/d)) + (-1 * Math.exp(sum2/d)) + a + Math.exp(1);
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
