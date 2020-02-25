package main.testSmellDetection.structuralRules;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import main.testSmellDetection.bean.PsiClassBean;
import main.testSmellDetection.bean.PsiMethodBean;
import main.utility.PsiTestSmellUtilities;

import java.util.List;
import java.util.stream.Collectors;

public abstract class IndirectTesting {

    public int  isIndirectTesting(PsiMethodBean pMethod, PsiClassBean productionClass) {
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

}
