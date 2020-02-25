package main.testSmellDetection.testSmellInfo.indirectTesting;

import main.testSmellDetection.bean.PsiMethodBean;

import java.util.ArrayList;

public class MethodWithIndirectTesting {
    private PsiMethodBean methodWithIndTest;
    private ArrayList<PsiMethodBean> listOfMethodCalled;

    public MethodWithIndirectTesting(PsiMethodBean methodWithIndTest,ArrayList<PsiMethodBean> listOfMethodCalled){
        this.methodWithIndTest = methodWithIndTest;
        this.listOfMethodCalled = listOfMethodCalled;
    }

    /*Getters and Setters*/
    public PsiMethodBean getMethodWithIndTest(){
        return methodWithIndTest;
    }

    public void setMethodWithIndTest(PsiMethodBean methodWithIndTest){
        this.methodWithIndTest = methodWithIndTest;
    }

    public ArrayList<PsiMethodBean> getListOfMethodCalled (){
        return listOfMethodCalled;
    }

    public void setListOfMethodCalled(ArrayList<PsiMethodBean> metodiChiamati){
        this.listOfMethodCalled = metodiChiamati;
    }
}
