package main.testSmellDetection.testSmellInfo.lazyTest;

import main.testSmellDetection.bean.PsiMethodBean;

import java.util.ArrayList;

public class MethodWithLazyTest {
    private PsiMethodBean methodWithLazyTest;
    private ArrayList<PsiMethodBean> listOfMethodCalled;

    public MethodWithLazyTest(PsiMethodBean methodWithLazyTest,ArrayList<PsiMethodBean> listOfMethodCalled){
        this.methodWithLazyTest = methodWithLazyTest;
        this.listOfMethodCalled = listOfMethodCalled;
    }

    /*Getter e Setter*/

    public PsiMethodBean getMethodWithLazyTest(){
        return methodWithLazyTest;
    }

    public void setMethodWithLazyTest(PsiMethodBean method){
        this.methodWithLazyTest = method;
    }

    public ArrayList<PsiMethodBean> getListOfMethodCalled(){
        return listOfMethodCalled;
    }

    public void setListOfMethodCalled(ArrayList<PsiMethodBean> metodiChiamati){
        this.listOfMethodCalled = metodiChiamati;
    }
}
