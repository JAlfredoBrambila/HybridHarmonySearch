/**
 * HarmonySearch.java
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

package algorithm.singleobjective.hs;

import algorithm.singleobjective.SingleobjectiveAlgorithm;
import java.util.LinkedList;
import problem.singleobjective.Problem;
import problem.singleobjective.testFunctions.Sphere;
import solution.real.Individual;
import utils.DoubleUtils;
import utils.Plot;


/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class HarmonySearch implements SingleobjectiveAlgorithm<Individual> {
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
    
    LinkedList<Individual> HM; // Listado de poblacion POP
    
    public HarmonySearch(Problem problem, int maxIt, int hms, int nNew, double hmcr, double par, double cFW, double FW_damp) {
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
        
        HM = new LinkedList<Individual>();
    }
    
    public HarmonySearch() {
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
        
        HM = new LinkedList<Individual>();
    }
    
    @Override
    public void execute() {
        generateHarmonies();
        //System.out.println("Harmonias iniciales...");
        //Utils.printList2(HM);
        
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
                        //double delta = this.FW * Utils.getRandomDoubleNumber(-1, 1); // Uniform
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
                
                NEW.add(newHarmony);
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
            
            //Sort
            DoubleUtils.sortHMListByCost(HM);
            
            /*
            System.out.println("SORT");
            Utils.printList(HM);
            */
            
            // Truncate Extra Harmonies
            DoubleUtils.truncateList(HM, this.HMS);
            
            /*
            System.out.println("HM TRUN");
            Utils.printList(HM);
            */
            
            
            // Update Best Solution Ever Found
            bestSol = HM.get(0);
            
            //Store Best Cost Ever Found
            bestCost[it][0] = bestSol.getCost_0();
            
            // Damp Fret Width
            FW = FW*FW_damp;
        }
        
         
        System.out.println("");
        System.out.println("Resultados: ");
        DoubleUtils.printList2HS(HM);
        System.out.println("\nBest Costs:");
        DoubleUtils.printMatrix(bestCost);
        Plot.plotMTXHS(bestCost);
        System.out.println("");
        System.out.println("Best Sol: ");
         
        System.out.println(bestSol.getCost_0());
    }
    
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
