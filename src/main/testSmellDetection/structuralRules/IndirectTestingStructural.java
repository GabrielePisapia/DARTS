package main.testSmellDetection.structuralRules;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReference;
import main.testSmellDetection.bean.PsiClassBean;
import main.testSmellDetection.bean.PsiMethodBean;
import main.testSmellDetection.testSmellInfo.indirectTesting.MethodWithIndirectTesting;
import main.utility.PsiTestSmellUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class IndirectTestingStructural {

    public static int  isIndirectTesting(PsiMethodBean pMethod, PsiClassBean productionClass) {
        int indirectTesting=0;
        List<String> productionClassMethods=productionClass.getPsiMethodBeans().stream().map(x -> x.getName()).collect(Collectors.toList());
        List<PsiMethod> calls = PsiTestSmellUtilities.getPsiMethodCalledFromMethod((PsiMethod) pMethod);
        /*List<String> calls=pMethod.getMethodCalls().stream().map(x -> x.getName()).collect(Collectors.toList());*/
        for(PsiMethod s : calls) {
            if (!productionClassMethods.contains(s)){
                indirectTesting++;
            }
        }
        return indirectTesting;
    }

    public static ArrayList<MethodWithIndirectTesting> checkMethodsThatCauseIndTest(PsiClassBean testClass, PsiClassBean productionClass){
        ArrayList<MethodWithIndirectTesting> methodWithIndirectTestings = new ArrayList<>() ;
        for (PsiMethodBean psiMethodBeanInside : testClass.getPsiMethodBeans()){
            String methodName = psiMethodBeanInside.getPsiMethod().getName();
            if (!methodName.equals(testClass.getPsiClass().getName()) &&
                    !methodName.toLowerCase().equals("setup") &&
                    !methodName.toLowerCase().equals("teardown")) {
                boolean isWithIndTest = false;
                ArrayList<PsiMethodBean> methodCalledThatCauseIndTest = new ArrayList<>();
                ArrayList<PsiMethodCallExpression> methodCalls = psiMethodBeanInside.getMethodCalls();
                if (methodCalls.size()>1){
                    ArrayList<PsiMethodBean> methodsInProduction = productionClass.getPsiMethodBeans();
                    int count = 0;
                    for (PsiMethodCallExpression callExpression : methodCalls){
                        PsiReference reference = callExpression.getMethodExpression().getReference();
                        if (reference!= null){
                            PsiMethod calledMethod = (PsiMethod) reference.resolve();
                            if (! methodCalls.contains(calledMethod)){
                                count++;
                                methodCalledThatCauseIndTest.add((PsiMethodBean) calledMethod);
                            }
                        }
                    }

                    if (count>1){
                        isWithIndTest = true;
                    }
                    if(isWithIndTest){
                        MethodWithIndirectTesting methodWithIndirectTesting = new MethodWithIndirectTesting(psiMethodBeanInside,methodCalledThatCauseIndTest);
                        methodWithIndirectTestings.add(methodWithIndirectTesting);
                    }
                }
            }
        }
        if (methodWithIndirectTestings.isEmpty()){
            return null;
        }else{
            return methodWithIndirectTestings;
        }
    }

}
