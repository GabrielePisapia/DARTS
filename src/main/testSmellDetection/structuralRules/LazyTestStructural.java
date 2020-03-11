package main.testSmellDetection.structuralRules;

import com.intellij.psi.PsiMethod;
import main.testSmellDetection.bean.PsiClassBean;
import main.testSmellDetection.bean.PsiMethodBean;
import main.utility.PsiTestSmellUtilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

public abstract class LazyTestStructural {



    public boolean isLazyTest(PsiClassBean pClassBean, PsiClassBean pProductionClass) {
        boolean lazyTest = false;
        Vector<PsiMethodBean> lazyTestCheck = new Vector<PsiMethodBean>();

        for (PsiMethodBean mb : pClassBean.getPsiMethodBeans()) {

            if (!mb.getName().equals(pClassBean.getName())
                    && !mb.getName().toLowerCase()
                    .equals("setup")
                    && !mb.getName().toLowerCase()
                    .equals("teardown") && !lazyTest) {
                List<PsiMethod> calledMethods = PsiTestSmellUtilities.getPsiMethodCalledFromMethod((PsiMethod)mb);
                /*Vector<MethodBean> calledMethods = (Vector<MethodBean>) mb.getMethodCalls();*/
                List<PsiMethod> calledMethodsNoDuplicate = new Vector<PsiMethod>();
                for (PsiMethod cm : calledMethods){
                    if (!calledMethodsNoDuplicate.contains(cm)){
                        calledMethodsNoDuplicate.add(cm);
                    }
                }

                if (calledMethodsNoDuplicate.size() > 0){
                    List<PsiMethodBean> cbMethods = pProductionClass.getPsiMethodBeans();
                    for (PsiMethod tmpMb : calledMethodsNoDuplicate) {
                        for (PsiMethodBean cbMethod : cbMethods){
                            if (cbMethod.getName().toLowerCase().equals(tmpMb.getName().toLowerCase()) && lazyTestCheck.contains(tmpMb)) {
                                lazyTest = true;
                                break;
                            } else if (cbMethod.getName().toLowerCase().equals(tmpMb.getName().toLowerCase()) && !lazyTestCheck.contains(tmpMb)){
                                lazyTestCheck.add((PsiMethodBean) tmpMb);
                                break;
                            }
                        }
                        if (lazyTest)
                            break;
                    }
                    if (lazyTest)
                        break;
                }
            }
        }

        return false;
    }


}

