package org.orbisgis.mapuce.randomForest;


import java.io.FileOutputStream;
import org.slf4j.LoggerFactory;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * Use to build the file .model and do some Evaluation 
 * @author Melvin Le Gall
 */
public class BuildModel {
    
    //Path to acess the .model
    private String pathModel;
    
    //use to make Evaluation after build
    private RandomForest rf;
    
    //Instances create to build the Random Forest
    private Instances learning;
    

    
    /**
     * Build the File .model by using RandomForest algorithm
     * @param path path where are stored the data use to build the forest
     * @param pathModelFile path where you want to store the .model file
     * @param classIndex Collumun where the class is
     * @param options List of options (ref Weka.classifier.setOptions())
     * @throws Exception 
     */
    public BuildModel(String path,String pathModelFile, int classIndex,String[] options,boolean saveFile) throws Exception{
        
    
        learning = this.makeInstances(path, classIndex);
  
        Object[] write = new Object[2];
        
        rf = new RandomForest();
        rf.setOptions(options);  
        rf.buildClassifier(learning);
        
        write[0] = learning.classAttribute();
        write[1] = rf; 
        if(saveFile){
             weka.core.SerializationHelper.writeAll(new FileOutputStream(pathModelFile), write);
        }
        
        
        pathModel = pathModelFile;
    }
    
    /**
     * Build the File .model by using RandomForest algorithm
     * @param path path where are stored the data use to build the forest
     * @param pathModelFile path where you want to store the .model file
     * @param classIndex Collumun where the class is
     * @throws Exception 
     */
    public BuildModel(String path,String pathModelFile, int classIndex, boolean saveFile) throws Exception{
        
        learning = this.makeInstances(path, classIndex);
        Object[] write = new Object[2];
        
        rf = new RandomForest();
        rf.buildClassifier(learning);
        
        write[0] = learning.classAttribute();
        write[1] = rf;
        if(saveFile){
            weka.core.SerializationHelper.writeAll(new FileOutputStream(pathModelFile), write);
          
        } 
       
        pathModel = pathModelFile;
    }
    
 
    /**
     * Convert File in a data usable in weka (Instances)
     * @param path path for create Instances
     * @param classIndex Index of classe 
     * @return the Instances necessary to create Training data
     * @throws Exception 
     */
    private Instances makeInstances(String path, int classIndex) 
            throws Exception{
        
        ConverterUtils.DataSource sourceTestValue = new ConverterUtils.DataSource(path);
    	Instances value = sourceTestValue.getDataSet();
    	value.setClassIndex(classIndex);
        value.setRelationName("classification");
        
        return value;
    }
    
    
    /**
     * 
     * @return the Classifier Object in the file .model
     * @throws Exception 
     */
    public Object[] getClassifier() throws Exception{

        return (Object[]) weka.core.SerializationHelper.readAll(pathModel);
    }
    
    /**
     * Method use to evaluate the accuracy of the alogrithm
     * @throws Exception 
     */
    public void evaluate() throws Exception{
        
        Evaluation eval = new Evaluation(learning);
        eval.evaluateModel(rf,learning);
        LoggerFactory.getLogger(BuildModel.class)
                .info(eval.toSummaryString("\nResults\n======\n", false));
       
    }
    
}
