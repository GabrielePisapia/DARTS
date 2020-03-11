package main.refactor.strategy;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.refactoring.extractMethod.PrepareFailedException;
import main.refactor.IRefactor;
import main.testSmellDetection.bean.PsiClassBean;
import main.testSmellDetection.testSmellInfo.lazyTest.LazyTestInfo;
import main.testSmellDetection.testSmellInfo.lazyTest.MethodWithLazyTest;

public class LazyTestingStrategy implements IRefactor {

    private LazyTestInfo lazyTestInfo;
    private MethodWithLazyTest methodWithLazyTest;
    private Project project;
    private Editor editor;

    public LazyTestingStrategy(MethodWithLazyTest methodWithLazyTest,Project project,LazyTestInfo lazyTestInfo){
        this.methodWithLazyTest = methodWithLazyTest;
        this.project = project;
        this.lazyTestInfo = lazyTestInfo;
    }

    public void doRefactor() throws PrepareFailedException{
        PsiClassBean classBeanPsi = lazyTestInfo.getClassWithLazyTest();
        PsiClass classPsi = classBeanPsi.getPsiClass();
        String nomeMetooDaRefattorizzare = methodWithLazyTest.getMethodWithLazyTest().getName();


    }


}
