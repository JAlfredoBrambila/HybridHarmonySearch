/**
 * SingleobjectiveAlgorithm.java
 * @version: 0.1
 * @autor: J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 * @autor: Miguel Angel Garcia Morales <talivan12@hotmail.com>
 * @autor: Hector Fraire Huacuja <hector.fraire2014@gmail.com>
 * Proyecto Biblioteca de clases para MO-JFramework 
 * Enero de 2022
 * 
 * 
 * 
 */

package algorithm.singleobjective;

import problem.singleobjective.Problem;

/**
 *
 * @author al_x3
 */
public interface SingleobjectiveAlgorithm<T> {
    public void execute();
    public void setProblem(Problem problem);
    public void setMaxIt(int MaxIt);
}
