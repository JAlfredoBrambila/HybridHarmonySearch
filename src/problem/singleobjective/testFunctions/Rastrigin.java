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
public class Rastrigin implements Problem{
    double A = 10;
    
    private double[] loweBound;
    private double[] upperBound;
            
    private int nVars;
    private int nObj;
    
    String name;
    
    public Rastrigin() {
        this.nVars = 30;
        nObj = 1;
        name = "Rastrigin_Function";
        loweBound = new double[nVars];
        upperBound = new double[nVars];
        for(int i=0; i<nVars; i++) {
            loweBound[i] = -5.12;
            upperBound[i] = 5.12;
        }
        
        //loweBound = 
        //upperBound
    }
    
    
    @Override
    public void costFunction(Individual ind) { 
        int n = ind.getPosition().length;
        //double sum = 0.0;
        double[] f = new double[this.nObj];
        f[0] = 0.0;
        for(int i=0; i<n; i++) {
            f[0] += Math.pow(ind.getPosition()[i], 2) - (A * Math.cos(2 * Math.PI * ind.getPosition()[i]));
        }
        
        f[0] = (A * n) + f[0];
        //ind.getCost()[0] = (A * n) + sum;
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
