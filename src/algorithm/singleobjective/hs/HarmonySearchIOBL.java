/**
 * HarmonySearch-IOBL.java
 * @version: 0.1
 * @autor: J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 * @autor: Miguel Angel Garcia Morales <talivan12@hotmail.com>
 * @autor: Hector Fraire Huacuja <hector.fraire2014@gmail.com>
 * Proyecto Biblioteca de clases para MO-JFramework 
 * Enero de 2022
 * Puede hacer uso total o parcial de este codigo dandole credito a los autores
 * 
 * 
 */

package algorithm.singleobjective.hs;

import algorithm.singleobjective.SingleobjectiveAlgorithm;
import algorithm.singleobjective.pso.PSO;
import algorithm.singleobjective.pso.PSOIOBL;
import java.nio.file.Paths;
import java.util.LinkedList;
import operator.crossover.CrossoverOperator;
import operator.crossover.SBXCrossover;
import operator.mutation.MutationOperator;
import operator.mutation.PolinomialMutation;
import problem.singleobjective.Problem;
import problem.singleobjective.testFunctions.Sphere;
import solution.real.Harmony;
import solution.real.Individual;
import solution.real.Particle;
import utils.DoubleUtils;
import utils.Plot;


/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class HarmonySearchIOBL implements SingleobjectiveAlgorithm<Individual> {
    // Problem Definition
    int nVar;
    double[] varMin;
    double[] varMax;
    private Problem problem;
    int nObj;
    //Harmony Search Parameters
    private int MaxIt; // Maximum Number of Iterations
    private int HMS; // Harmony Memory Size
    private int nNew; // Number of New Harmonies
    private double HMCR; // Harmony Memory Consideration Rate
    private double PAR; // Pitch Adjustment Rate
    double FW; // Fret Width (Bandwidth)
    private double FW_damp; // Fret Width Damp Ratio
    private double cFW;
    
    private int stagnation;
    private double zeta; // harmony replacement percentage
    private double sigma; //allowable stagnation percentage
    
    LinkedList<Individual> HM; // Listado de poblacion POP
    
    static int expNum = 1;
    boolean EXPERIMENTATION;
    
    public HarmonySearchIOBL(Problem problem, int maxIt, int hms, int nNew, double hmcr, double par, double cFW, double FW_damp) {
        this.problem = problem;
        // Problem Definition
        nVar = problem.getnVars();
        varMin = problem.getLowerBound();
        varMax = problem.getUpperBound();
        nObj = problem.getnObj();
        
        this.MaxIt = maxIt;
        this.HMS = hms;
        this.nNew = nNew;
        this.HMCR = hmcr;
        this.PAR = par;
        this.cFW = cFW;
        this.FW = calculateFW(this.cFW,varMin,varMax);
        this.FW_damp = FW_damp;
        
        stagnation = 0;
        zeta = 0.3;
        sigma = 0.2;
        
        HM = new LinkedList<Individual>();
        
        EXPERIMENTATION = false;
    }
    
    public HarmonySearchIOBL(Problem problem, int maxIt, int hms, int nNew, double hmcr, double par, double cFW, double FW_damp, boolean experimentation) {
        this.problem = problem;
        // Problem Definition
        nVar = problem.getnVars();
        varMin = problem.getLowerBound();
        varMax = problem.getUpperBound();
        nObj = problem.getnObj();
        
        this.MaxIt = maxIt;
        this.HMS = hms;
        this.nNew = nNew;
        this.HMCR = hmcr;
        this.PAR = par;
        this.cFW = cFW;
        this.FW = calculateFW(this.cFW,varMin,varMax);
        this.FW_damp = FW_damp;
        
        stagnation = 0;
        zeta = 0.3;
        sigma = 0.2;
        
        HM = new LinkedList<Individual>();
        
        EXPERIMENTATION = experimentation;
    }
    
    public HarmonySearchIOBL() {
        problem = new Sphere();
        System.out.println("Selected problem: " + problem.getProblemName());
//        // Problem Definition
//        nVar = 5;
//        varMin = -10;
//        varMax = 10;
//        problem = new Sphere();
//        //problem = new Rosenbrock();
//        nObj = 1; // numero de objetivos
//        //Harmony Search Parameters
//        MaxIt = 5000; // Maximum Number of Iterations
//        HMS = 25; // Harmony Memory Size
//        nNew = 20; // Number of New Harmonies
//        HMCR = 0.9; // Harmony Memory Consideration Rate
//        PAR = 0.1; // Pitch Adjustment Rate
//        FW = 0.02 * (varMax - varMin); //bw -> Fret Width (Bandwidth) 
//        FW_damp = 0.995; // Fret Width Damp Ratio

        // Problem Definition
        nVar = problem.getnVars();
        varMin = problem.getLowerBound();
        varMax = problem.getUpperBound();
        nObj = problem.getnObj(); // numero de objetivos
        // *** Test functions for single-objective optimization ***
        //problem = new Rosenbrock();
        //problem = new Rastrigin();
        //problem = new Ackley();
        //problem = new Zakharov();
        //problem = new SumSquare();
        
        //Harmony Search Parameters
        MaxIt = 5000; // Maximum Number of Iterations
        HMS = 40; // Harmony Memory Size
        nNew = 20; // Number of New Harmonies
        HMCR = 0.95; // Harmony Memory Consideration Rate
        PAR = 0.7; // Pitch Adjustment Rate
        FW = calculateFW(0.2,varMin,varMax); //bw -> Fret Width (Bandwidth) 
        FW_damp = 0.995; // Fret Width Damp Ratio
        
        stagnation = 0;
        
        HM = new LinkedList<Individual>();
    }
    
    @Override
    public void execute() {
        
        long initialTime = System.currentTimeMillis();
        
        generateHarmonies();
        //generateHarmoniesPSO();
        //System.out.println("Harmonias iniciales...");
        //DoubleUtils.printList2HS(HM);
        
        //Sort
        DoubleUtils.sortHMListByCost(HM);
        

        // Update Best Solution Ever Found
        Individual bestSol = HM.get(0);
        
        double[][] bestCost = new double[this.MaxIt][this.nObj];
        
        LinkedList<Individual> NEW;
        // Harmony Search Main Loop
        for(int it=0; it<this.MaxIt; it++) {
            NEW = new LinkedList<Individual>();
            
            // Crear nuevas armonias
            for(int k=0; k<this.nNew; k++) {
                Individual newHarmony = new Individual();
                
                // Create New Harmony Position
                newHarmony.setPosition(DoubleUtils.getRandomPos(varMin, varMax, nVar));
                //NEW.add(k, newHarmony);
                for(int j=0; j<this.nVar; j++) {
                    /*if(Utils.getRandomDoubleNumber(0, 1) <= this.HMCR) {
                        // Use Harmony Memory
                        int i = Utils.getRandomIntNumberCeroToMax(this.HMS);
                        //System.out.println("i: " + i);
                        // newHarmony = NEW(k)
                        newHarmony.getPosition()[j] = HM.get(i).getPosition()[j];
                        
                        //Pitch Adjustment
                        if(Utils.getRandomDoubleNumber(0, 1) <= this.PAR) {
                            //double delta = this.FW * Utils.getRandomDoubleNumber(-1, 1); // Uniform
                            double delta = this.FW * Utils.getRandomGaussian(); // Gaussian (Normal)
                            newHarmony.getPosition()[j] = newHarmony.getPosition()[j] + delta;
                        }
                    } */
                    
                    if(DoubleUtils.getRandomDoubleNumber(0, 1) <= this.HMCR) {
                        // Use Harmony Memory
                        int i = DoubleUtils.getRandomIntNumberCeroToMax(this.HMS);
                        //System.out.println("i: " + i);
                        // newHarmony = NEW(k)
                        newHarmony.getPosition()[j] = HM.get(i).getPosition()[j];
                    }
                    
                    //Pitch Adjustment
                    if(DoubleUtils.getRandomDoubleNumber(0, 1) <= this.PAR) {
                        //double delta = this.FW * DoubleUtils.getRandomDoubleNumber(-1, 1); // Uniform
                        double delta = this.FW * DoubleUtils.getRandomGaussian(); // Gaussian (Normal)
                        newHarmony.getPosition()[j] = newHarmony.getPosition()[j] + delta;
                    }
                }
                
                // Apply Variable Limits
                DoubleUtils.max(newHarmony, this.varMin);
                DoubleUtils.min(newHarmony, this.varMax);
                
                // Evaluation
                newHarmony.setCost(new double[this.nObj]);
                problem.costFunction(newHarmony);
                
                ///*
                // OBL 1.0 
                double r = Math.random();
                Individual newHarmonyPrima = new Individual();
                //newHarmonyPrima.setTuning(zeros(nVar));
                newHarmonyPrima.clone(newHarmony);

                for(int ii=0; ii<newHarmony.getPosition().length; ii++) {
                    newHarmonyPrima.getPosition()[ii] = newHarmony.getPosition()[ii] * r;
                    Math.max(newHarmonyPrima.getPosition()[ii], problem.getLowerBound()[ii]);
                    Math.min(newHarmonyPrima.getPosition()[ii], problem.getUpperBound()[ii]);
                }

                problem.costFunction(newHarmonyPrima);
                
                if(newHarmonyPrima.getCost()[0] < newHarmony.getCost()[0]) {
                    NEW.add(newHarmonyPrima);
                } else {
                    NEW.add(newHarmony);
                }
                //*/
                
                /*
                NEW.add(newHarmony);
                */
            }
            
            /*
            System.out.println("");
            System.out.println("--------");
            System.out.println("HM");
            Utils.printList(HM);
            System.out.println("NEW");
            Utils.printList(NEW);
            */
            // Merge Harmony Memory and New Harmonies
            DoubleUtils.mergeList(HM, NEW);
            
            /*
            System.out.println("MERGE");
            Utils.printList(HM);
            */
            
            
            /*
            //OBL 2.0
            ////////
            // OBL 
            double r;// = Math.random();
            Individual newHarmonyPrima;
            
            //MutationOperator mutation = new PolinomialMutation(problem.getLowerBound(),problem.getUpperBound(),0.2,20.0);/////////
            for(int ihm=0; ihm<HM.size(); ihm++) {
                newHarmonyPrima = new Individual();
                newHarmonyPrima.clone(HM.get(ihm));
                
                r = Math.random();
                
                for (int ii = 0; ii < HM.get(ihm).getPosition().length; ii++) {
                    newHarmonyPrima.getPosition()[ii] = HM.get(ihm).getPosition()[ii] * r;
                    //newHarmonyPrima.getPosition()[ii] = ((problem.getUpperBound()[ii] + problem.getLowerBound()[ii]) - HM.get(ihm).getPosition()[ii]) * r; //(Upper + lower)- Xi
                    Math.max(newHarmonyPrima.getPosition()[ii], problem.getLowerBound()[ii]);
                    Math.min(newHarmonyPrima.getPosition()[ii], problem.getUpperBound()[ii]);
                }
                
                //Individual newHarmonyPrimaP = (Individual) mutation.execute(newHarmonyPrima);//////////
                
                problem.costFunction(newHarmonyPrima);
                //problem.costFunction(newHarmonyPrimaP);///////////
                
                //System.out.println("OBL");
                if (newHarmonyPrima.getCost()[0] < HM.get(ihm).getCost()[0]) {
                    //NEW.add(newHarmonyPrima);
                    HM.get(ihm).clone(newHarmonyPrima);
                }

            }
            //END OBL
            */
            

            

            
            ////////
            
            //Sort
            DoubleUtils.sortHMListByCost(HM);
            
            
            /*
            System.out.println("SORT");
            Utils.printList(HM);
            */
            
            // Truncate Extra Harmonies
            DoubleUtils.truncateList(HM, this.HMS);
            
            //////
            
            //////
            //DoubleUtils.sortHMListByCost(HM);
            
            /*
            System.out.println("HM TRUN");
            Utils.printList(HM);
            */
            
            /*
            // Estancamiento...
            if(it>0) {
                if(HM.get(0) == bestSol){
                    stagnation++;
                } else {
                    stagnation = 0;
                }
                if(stagnation > (int)(MaxIt * this.sigma)) {
                    restartHarmonies(); //////
                    stagnation = 0;
                }
            }
            */
            //if(Math.random() < 0.4 && it>(this.MaxIt/3)*2)
            //regenerateHarmonies(); //////
            
            
            // Update Best Solution Ever Found
            bestSol = HM.get(0);
            //BestInd.clone(bestSol);
            
            //Store Best Cost Ever Found
            //bestCost[it][0] = bestSol.getCost_0();
            
            // Damp Fret Width
            FW = FW*FW_damp;
        } // fin IT
        
        long endTime = System.currentTimeMillis();
         
        /*System.out.println("");
        System.out.println("Resultados: ");
        DoubleUtils.printList2HS(HM);
        System.out.println("\nBest Costs:");
        DoubleUtils.printMatrix(bestCost);
        Plot.plotMTXHS(bestCost);
        System.out.println("");*/
        
        ////
        if (EXPERIMENTATION) {
            DoubleUtils.createDirectory(Paths.get("").toAbsolutePath().toString() + "\\exp\\HS_IOBL1_\\");
            //DoubleUtils.createDirectory(Paths.get("").toAbsolutePath().toString() + "\\fronts\\DNSGA\\" + problem.getProblemName() + "\\F" + contW);
            
            DoubleUtils.saveIndividualToFileNTimeAppend(bestSol, (endTime - initialTime), "exp\\HS_IOBL1_\\" + problem.getProblemName() + "_.csv");
            //DoubleUtils.saveFrontObjectivesToFile(pop, "fronts\\DNSGA\\" + problem.getProblemName() + "\\F" + contW + "\\DNSGAII_A_" + problem.getProblemName() + "_F" + contW + "_C" + algConf + "_" + expNum + "_.txt");
            expNum++;
        } else {
        ////
            System.out.println("Best Sol: ");
            System.out.println(bestSol.getCost_0());
            System.out.println("Time: " + (endTime - initialTime));
        }
    }
    
    /*public void generateHarmoniesPSO() {
        int mitad = HMS/2;
        for(int i=0; i<this.HMS; i++) {
            // crea nuevo individuo
            Individual ind = new Individual();
            
            if(i<mitad) {
            
               // System.out.println("IIII");
            PSO algoritm1 = new PSO(
                    this.problem,// Problem
                    500, // Maximum Number of Iterations
                    100, // Pop Size
                    0.7298, // W
                    0.99, // W Damp
                    1.49618, // C1 1.5
                    1.49618, // C2 2.0
                    0.1 // Velocity const value
            );

            Particle particle = algoritm1.getBestParticle();
            //genera valores reales aletatorios(entre varMin y varMax) para el cromosoma de tamaño nVar
            ind.setPosition(particle.getPosition());
            
            // crea vector de costos de tamaño nObj para el individuo 
            ind.setCost(particle.getCost());
            } else {
                ind.setPosition(DoubleUtils.getRandomPos(varMin, varMax, nVar));
            
            // crea vector de costos de tamaño nObj para el individuo 
                ind.setCost(new double[this.nObj]);
            }
            // aplica la funcion de costo y obtiene los nObj costos
            problem.costFunction(ind);
            
            HM.add(ind);
        }
    }*/
    
    public void generateHarmonies() {
        for(int i=0; i<this.HMS; i++) {
            // crea nuevo individuo
            Individual ind = new Individual();
            
            //genera valores reales aletatorios(entre varMin y varMax) para el cromosoma de tamaño nVar
            ind.setPosition(DoubleUtils.getRandomPos(varMin, varMax, nVar));
            
            // crea vector de costos de tamaño nObj para el individuo 
            ind.setCost(new double[this.nObj]);
            
            // aplica la funcion de costo y obtiene los nObj costos
            problem.costFunction(ind);
            
            HM.add(ind);
        }
    }
    
    /*
    public void restartHarmonies() {
        int nRegen = (int)(this.zeta * HM.size());
        int mid = (int)(HM.size()/2);
        
        for(int i=0; i<nRegen; i++) {
            int index = DoubleUtils.getRandomIntNumber(0, HM.size());
             Individual ind = new Individual();
                     
            PSOIOBL algoritm1 = new PSOIOBL(
                    this.problem,// Problem
                    500, // Maximum Number of Iterations
                    100, // Pop Size
                    0.7298, // W
                    0.99, // W Damp
                    1.49618, // C1 1.5
                    1.49618, // C2 2.0
                    0.1 // Velocity const value
            );

            Particle particle = algoritm1.getBestParticle();
            //genera valores reales aletatorios(entre varMin y varMax) para el cromosoma de tamaño nVar
            ind.setPosition(particle.getPosition());
            
            // crea vector de costos de tamaño nObj para el individuo 
            ind.setCost(particle.getCost());
            
           if(ind.getCost()[0] < HM.get(index).getCost()[0]) {
               HM.get(index).clone(ind);
           }
            
        }
        
        
    }*/
    
    public double calculateFW(double v, double[] l, double [] u) {
        //0.02 * (varMax - varMin)
        double fw = 0;
        for(int i=0; i<l.length; i++) {
            fw += (varMax[i] - varMin[i]);
        }
        
        fw = fw/l.length;
        fw = fw * v;
        
        return fw;
    }

    /**
     * @return the problem
     */
    public Problem getProblem() {
        return problem;
    }

    /**
     * @param problem the problem to set
     */
    @Override
    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    /**
     * @return the MaxIt
     */
    public int getMaxIt() {
        return MaxIt;
    }

    /**
     * @param MaxIt the MaxIt to set
     */
    @Override
    public void setMaxIt(int MaxIt) {
        this.MaxIt = MaxIt;
    }

    /**
     * @return the HMS
     */
    public int getHMS() {
        return HMS;
    }

    /**
     * @param HMS the HMS to set
     */
    public void setHMS(int HMS) {
        this.HMS = HMS;
    }

    /**
     * @return the nNew
     */
    public int getnNew() {
        return nNew;
    }

    /**
     * @param nNew the nNew to set
     */
    public void setnNew(int nNew) {
        this.nNew = nNew;
    }

    /**
     * @return the HMCR
     */
    public double getHMCR() {
        return HMCR;
    }

    /**
     * @param HMCR the HMCR to set
     */
    public void setHMCR(double HMCR) {
        this.HMCR = HMCR;
    }

    /**
     * @return the PAR
     */
    public double getPAR() {
        return PAR;
    }

    /**
     * @param PAR the PAR to set
     */
    public void setPAR(double PAR) {
        this.PAR = PAR;
    }

    /**
     * @return the FW_damp
     */
    public double getFW_damp() {
        return FW_damp;
    }

    /**
     * @param FW_damp the FW_damp to set
     */
    public void setFW_damp(double FW_damp) {
        this.FW_damp = FW_damp;
    }
}
