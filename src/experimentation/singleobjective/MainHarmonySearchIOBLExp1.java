/**
 * Archivo MAIN HS-IOBL
 * MainHarmonySearchIOBLExp1.java
 * @version: 1.0
 * @autor: J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 * @autor: Miguel Angel Garcia Morales <talivan12@hotmail.com>
 * @autor: Hector Fraire Huacuja <hector.fraire2014@gmail.com>
 * Proyecto Biblioteca de clases para MO-JFramework 
 * Enero de 2023
 * 
 * 
 */

package experimentation.singleobjective;


import algorithm.singleobjective.SingleobjectiveAlgorithm;
import algorithm.singleobjective.hs.*;
import problem.singleobjective.Problem;
import problem.singleobjective.testFunctions.Ackley;
import problem.singleobjective.testFunctions.F12;
import problem.singleobjective.testFunctions.F13;
import problem.singleobjective.testFunctions.F16;
import problem.singleobjective.testFunctions.F17;
import problem.singleobjective.testFunctions.F4;
import problem.singleobjective.testFunctions.F7;
import problem.singleobjective.testFunctions.Griewank;
import problem.singleobjective.testFunctions.Rastrigin;
import problem.singleobjective.testFunctions.Rosenbrock;
import problem.singleobjective.testFunctions.RotatedHyperEllipsoid;
import problem.singleobjective.testFunctions.Schwefel_2_22;
import problem.singleobjective.testFunctions.Schwefel_2_26;
import problem.singleobjective.testFunctions.Sphere;
import problem.singleobjective.testFunctions.Step;
import solution.real.Individual;


public class MainHarmonySearchIOBLExp1 {

    public static void main(String[] args) {

        // [ Sphere | Schwefel_2_22 | Step | Rosenbrock | Schwefel_2_26 | Rastrigin | Ackley | Griewank | RotatedHyperEllipsoid ]
        int Num_Exp = 30;

        Problem problem = new F7();

        for (int i = 0; i < Num_Exp; i++) {
            System.out.println("Run experiment: " + (i + 1));
            SingleobjectiveAlgorithm algoritm = new HarmonySearchIOBL(
                    problem,// Problem
                    100, // Maximum Number of Iterations
                    5, // Harmony Memory Size
                    20, // Number of New Harmonies
                    0.95, // Harmony Memory Consideration Rate
                    0.7, // Pitch Adjustment Rate
                    0.2, // Constant to calculate bw -> Fret Width (Bandwidth) 
                    0.995, // Fret Width Damp Ratio
                    true
            );

            algoritm.execute();
        }
    }
}
