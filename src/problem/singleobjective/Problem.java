/**
 * Problem.java
 * @version: 0.1
 * @autor: J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 * @autor: Miguel Angel Garcia Morales <talivan12@hotmail.com>
 * @autor: Hector Fraire Huacuja <hector.fraire2014@gmail.com>
 * Proyecto Biblioteca de clases para MO-JFramework 
 * Enero de 2022
 * Puede hacer uso total o parcial de este codigo dandole credito a los autores
 */
package problem.singleobjective;

import solution.real.Individual;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public interface Problem {
    public void costFunction(Individual ind);
    public double[] getLowerBound();
    public double[] getUpperBound();
    public int getnVars();
    public int getnObj();
    public String getProblemName();
    public int getNumVars();
    public int getNumObjectives();
    public int getNumEvals();
}
