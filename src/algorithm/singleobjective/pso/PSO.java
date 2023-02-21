/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package algorithm.singleobjective.pso;

import algorithm.singleobjective.SingleobjectiveAlgorithm;
import java.util.LinkedList;
import problem.singleobjective.Problem;
import problem.singleobjective.testFunctions.Sphere;

import solution.real.Individual;
import solution.real.Particle;
import utils.DoubleUtils;
import utils.PSOUtils;
import utils.Plot;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class PSO  implements SingleobjectiveAlgorithm<Particle>{

    Problem problem;
    int nVar;
    double[] varMin;
    double[] varMax;
    int nObj;
    int MaxIt; // Maximum Number of Iterations
    int nPop; // Population Size (Swarm Size)
    // PSO Parameters
    double w; // Inertia Weight
    double wdamp; // Inertia Weight Damping Ratio
    double c1; // Personal Learning Coefficient
    double c2; // Global Learning Coefficient
    double velMin;
    double velMax;
    LinkedList<Particle> particles;
    
    //double GlobalBestCost;
    Particle GlobalBest;
    
    public PSO(Problem problem, int maxIt, int npop, double w, double wdamp, double c1, double c2, double velC) {
        this.problem = problem;
        //System.out.println("Selected problem: " + problem.getName());
        this.nVar = problem.getnVars();
        this.varMin = problem.getLowerBound();
        this.varMax = problem.getUpperBound();
        this.nObj = problem.getnObj(); // numero de objetivos
        this.MaxIt = maxIt;
        this.nPop = npop;
        this.w = w;
        this.wdamp = wdamp;
        this.c1 = c1;
        this.c2 = c2;
        // Velocity Limits
        this.velMax = calculateVelMax(velC,varMin, varMax); //0.1*(varMax-varMin);
        this.velMin = -velMax;
        this.particles = new LinkedList<Particle>();
        
        //GlobalBestCost = Double.POSITIVE_INFINITY;
        GlobalBest = new Particle();
        double[] initialCost = new double[this.nObj];
        initialCost[0] = Double.POSITIVE_INFINITY;
        GlobalBest.setBestCost(initialCost);
        GlobalBest.setCost(initialCost);
    }
    
    public PSO() {
        problem = new Sphere();
        System.out.println("Selected problem: " + problem.getProblemName());
        nVar = problem.getnVars();
        varMin = problem.getLowerBound();
        varMax = problem.getUpperBound();
        nObj = problem.getnObj(); // numero de objetivos
        MaxIt = 1000;
        nPop = 100;
        w = 1;
        wdamp = 0.99;
        c1 = 1.5;
        c2 = 2.0;
        // Velocity Limits
        velMax = calculateVelMax(0.1,varMin, varMax); //0.1*(varMax-varMin);
        velMin = -velMax;
        particles = new LinkedList<Particle>();
        
        //GlobalBestCost = Double.POSITIVE_INFINITY;
        GlobalBest = new Particle();
        double[] initialCost = new double[this.nObj];
        initialCost[0] = Double.POSITIVE_INFINITY;
        GlobalBest.setBestCost(initialCost);
        GlobalBest.setCost(initialCost);
    }
    
    public void execute() {
        this.generateParticles();
        System.out.println("Particles inici");
        for(Particle p : particles) {
            System.out.println(p);
        }
        System.out.println("");
        double[][] bestCost = new double[this.MaxIt][this.nObj];
        
        for(int it=0; it<this.MaxIt; it++) {
            for(int i=0; i<this.nPop; i++) {
                // Update Velocity
                for(int j=0; j<this.nVar; j++) {
                    this.particles.get(i).getVelocity()[j] = w * this.particles.get(i).getVelocity()[j] 
                            + c1 * Math.random() * (particles.get(i).getBestPosition()[j] - particles.get(i).getPosition()[j]) 
                            + c2 * Math.random() * (this.GlobalBest.getPosition()[j] - particles.get(i).getPosition()[j]);
                }
                
                // Apply Velocity Limits
                for(int j=0; j<this.nVar; j++) {
                    particles.get(i).getVelocity()[j] = Math.max(particles.get(i).getVelocity()[j], this.velMin);
                    particles.get(i).getVelocity()[j] = Math.min(particles.get(i).getVelocity()[j], this.velMax);
                }
                
                //Update Position
                for(int j=0; j<this.nVar; j++) {
                    particles.get(i).getPosition()[j] = particles.get(i).getPosition()[j] + particles.get(i).getVelocity()[j];
                }
                //System.out.println("Ante: " + particles.get(i));
                //Velocity Mirror Effect
                //
                //Apply Position Limits
                //Update Position
                for(int j=0; j<this.nVar; j++) {

                    particles.get(i).getPosition()[j] = Math.max(particles.get(i).getPosition()[j], varMin[j]);
                    particles.get(i).getPosition()[j] = Math.min(particles.get(i).getPosition()[j], varMax[j]);
                }

                
                //Evaluation
                problem.costFunction(particles.get(i));
                
                // Update Personal Best
                if(particles.get(i).getCost()[0] < particles.get(i).getBestCost()[0]) {
                    particles.get(i).setBestPosition(particles.get(i).getPosition());
                    particles.get(i).setBestCost(particles.get(i).getCost());
                    // Update Global Best
                    if(particles.get(i).getBestCost()[0] < this.GlobalBest.getCost()[0] ) {
                         //this.GlobalBestCost = particles.get(i).getBestCost()[0];
                         this.GlobalBest.clone(particles.get(i));
                    }
                }
            }
            //
            bestCost[it][0] = this.GlobalBest.getCost()[0];
            w = w * wdamp;
        }
        
        System.out.println("");
        System.out.println("Resultados: ");
        PSOUtils.printList3(particles);
        System.out.println("\nBest Costs:");
        PSOUtils.printMatrix(bestCost);
        Plot.plotMTXAG(bestCost,"PSO");
        System.out.println("");
    }
    
    public Particle getBestParticle() {
        this.generateParticles();
        /*System.out.println("Particles inici");
        for(Particle p : particles) {
            System.out.println(p);
        }
        System.out.println("");*/
        //double[][] bestCost = new double[this.MaxIt][this.nObj];
        
        for(int it=0; it<this.MaxIt; it++) {
            for(int i=0; i<this.nPop; i++) {
                // Update Velocity
                for(int j=0; j<this.nVar; j++) {
                    this.particles.get(i).getVelocity()[j] = w * this.particles.get(i).getVelocity()[j] 
                            + c1 * Math.random() * (particles.get(i).getBestPosition()[j] - particles.get(i).getPosition()[j]) 
                            + c2 * Math.random() * (this.GlobalBest.getPosition()[j] - particles.get(i).getPosition()[j]);
                }
                
                // Apply Velocity Limits
                for(int j=0; j<this.nVar; j++) {
                    particles.get(i).getVelocity()[j] = Math.max(particles.get(i).getVelocity()[j], this.velMin);
                    particles.get(i).getVelocity()[j] = Math.min(particles.get(i).getVelocity()[j], this.velMax);
                }
                
                //Update Position
                for(int j=0; j<this.nVar; j++) {
                    particles.get(i).getPosition()[j] = particles.get(i).getPosition()[j] + particles.get(i).getVelocity()[j];
                }
                //System.out.println("Ante: " + particles.get(i));
                //Velocity Mirror Effect
                //
                //Apply Position Limits
                //Update Position
                for(int j=0; j<this.nVar; j++) {
//                    System.out.println("");
//                    System.out.println("Max of " + particles.get(i).getPosition()[j] + " and " + varMin[j] + " = " + Math.max(particles.get(i).getPosition()[j], varMin[j]));
//                    System.out.println("Min of " + particles.get(i).getPosition()[j] + " and " + varMax[j] + " = " + Math.min(particles.get(i).getPosition()[j], varMax[j]));
//                    System.out.println("");
                    particles.get(i).getPosition()[j] = Math.max(particles.get(i).getPosition()[j], varMin[j]);
                    particles.get(i).getPosition()[j] = Math.min(particles.get(i).getPosition()[j], varMax[j]);
                }
                //System.out.println("Post: " + particles.get(i));
                //System.out.println("");
                
                //Evaluation
                problem.costFunction(particles.get(i));
                
                // Update Personal Best
                if(particles.get(i).getCost()[0] < particles.get(i).getBestCost()[0]) {
                    particles.get(i).setBestPosition(particles.get(i).getPosition());
                    particles.get(i).setBestCost(particles.get(i).getCost());
                    // Update Global Best
                    if(particles.get(i).getBestCost()[0] < this.GlobalBest.getCost()[0] ) {
                         //this.GlobalBestCost = particles.get(i).getBestCost()[0];
                         this.GlobalBest.clone(particles.get(i));
                    }
                }
            }
            //
            //bestCost[it][0] = this.GlobalBest.getCost()[0];
            w = w * wdamp;
        }
        
        /*System.out.println("");
        System.out.println("Resultados: ");
        PSOUtils.printList3(particles);
        System.out.println("\nBest Costs:");
        PSOUtils.printMatrix(bestCost);
        Plot.plotMTXAG(bestCost,"PSO");
        System.out.println("");*/
        return GlobalBest;
    }
    
    public void generateParticles() {
        for(int i=0; i<this.nPop; i++) {
            Particle particle = new Particle();
            particle.setPosition(DoubleUtils.getRandomPos(varMin, varMax, nVar));
            particle.setVelocity(zeros(problem.getnVars()));
            problem.costFunction(particle);
            //
            particle.setBestPosition(particle.getPosition());
            particle.setBestCost(particle.getCost());
            
            //MonoObjetive
            if(particle.getBestCost()[0] < this.GlobalBest.getCost()[0]) {
                //this.GlobalBestCost = particle.getBestCost()[0];
                this.GlobalBest.clone(particle);
            }
            
            this.particles.add(particle);
        }
    }
    
    public double[] zeros(int n) {
        double[] a = new double[n];
        for(int i=0; i<n; i++) {
            a[i] = 0.0;
        }
        return a;
    }
    
    public double calculateVelMax(double v, double[] l, double [] u) {
        //0.02 * (varMax - varMin)
        double fw = 0;
        for(int i=0; i<l.length; i++) {
            fw += (u[i] - l[i]);
        }
        
        fw = fw/l.length;
        fw = fw * v;
        
        return fw;
    }

    @Override
    public void setProblem(Problem problem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMaxIt(int MaxIt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
