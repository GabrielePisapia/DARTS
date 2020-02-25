package main.testSmellDetection.testSmellInfo.indirectTesting;

import com.intellij.psi.PsiClass;
import main.testSmellDetection.bean.PsiClassBean;
import main.testSmellDetection.testSmellInfo.TestSmellInfo;

import java.util.ArrayList;

public class IndirectTestingInfo extends TestSmellInfo {
    private PsiClassBean productionClass;
    private ArrayList<MethodWithIndirectTesting> metodiInfetto;


    public IndirectTestingInfo(PsiClassBean classWithIndTest, PsiClassBean productionClass,ArrayList<MethodWithIndirectTesting> metodiInfetto){
        super(classWithIndTest);
        this.productionClass = productionClass;
        this.metodiInfetto = metodiInfetto;
    }

    public String toString(){
        return "IndirectTestingInfo{"+
                "classWithIndirectTesting = "+ classWithSmell+
                ", productionClass = "+ productionClass+
                ", methodsThatCauseIndirectTesting = " +metodiInfetto+
                "}";
    }

    /*Getters and Setters*/

    public PsiClassBean getClassWithIndTest(){
        return classWithSmell;
    }

    public void setClassWithIndTest(PsiClassBean classeInfetta){
        this.classWithSmell = classeInfetta;
    }

    public PsiClassBean getProductionClass(){
        return productionClass;
    }

    public void setProductionClass(PsiClassBean productionClass){
        this.productionClass = productionClass;
    }

    public ArrayList<MethodWithIndirectTesting> getMethodThatCauseIndirectTest(){
        return metodiInfetto;
    }

    public void setMethodsThatCauseIndirectTest (ArrayList<MethodWithIndirectTesting> metodiInfetti){
        this.metodiInfetto = metodiInfetti;
    }
}
