/**
 * CrossoverOperator.java
 * @version: 1.0
 * @autor: J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 * @autor: Miguel Angel Garcia Morales <talivan12@hotmail.com>
 * @autor: Hector Fraire Huacuja <hector.fraire2014@gmail.com>
 * Proyecto Biblioteca de clases para MO-JFramework 
 * Enero de 2022
 * 
 * 
 * 
 */

package operator.crossover;

import java.util.LinkedList;
import solution.real.Individual;

/**
 *
 * @author al_x3
 */
public interface CrossoverOperator<T> {
    public LinkedList<T> execute(LinkedList<T> parents);
    public T executeOnlyChild(LinkedList<T> parents);
    public void setCurrentIndividual(T c);
}
