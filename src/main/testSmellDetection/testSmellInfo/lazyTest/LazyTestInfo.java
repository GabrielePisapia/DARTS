package main.testSmellDetection.testSmellInfo.lazyTest;

import main.testSmellDetection.bean.PsiClassBean;
import main.testSmellDetection.testSmellInfo.TestSmellInfo;

import java.util.ArrayList;

public class LazyTestInfo extends TestSmellInfo {
    private PsiClassBean productionClass;
    private ArrayList<MethodWithLazyTest> metodiInfetti;

    public LazyTestInfo(PsiClassBean classWithLazyTest, PsiClassBean productionClass,ArrayList<MethodWithLazyTest> metodiInfetti){
        super(classWithLazyTest);
        this.productionClass = productionClass;
        this.metodiInfetti = metodiInfetti;
    }

    public String toString(){
        return "LazyTestInfo{"+
                "classWithLazyTest = "+ classWithSmell+
                ", productionClass = "+ productionClass+
                ", methodsThatCauseLazyTest = "+ metodiInfetti+
                " }";
    }

    /*Getters And Setters*/

    public PsiClassBean getClassWithLazyTest(){
        return classWithSmell;
    }

    public void setClassWithSmell(PsiClassBean classeInfetta){
        this.classWithSmell = classeInfetta;
    }

    public PsiClassBean getProductionClass(){
        return productionClass;
    }

    public void setProductionClass(PsiClassBean productionClass){
        this.productionClass = productionClass;
    }

    public ArrayList<MethodWithLazyTest> getMethodThatCauseLazyTest(){
        return metodiInfetti;
    }

    public void setMethodThatCauseLazyTest(ArrayList<MethodWithLazyTest> metodiInfetti){
        this.metodiInfetti = metodiInfetti;
    }
}
