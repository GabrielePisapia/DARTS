package main.refactor.strategy;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.refactoring.extractMethod.PrepareFailedException;
import main.refactor.IRefactor;
import main.testSmellDetection.bean.PsiClassBean;
import main.testSmellDetection.bean.PsiMethodBean;
import main.testSmellDetection.testSmellInfo.indirectTesting.IndirectTestingInfo;
import main.testSmellDetection.testSmellInfo.indirectTesting.MethodWithIndirectTesting;

import java.util.ArrayList;

public class IndirectTestingStrategy implements IRefactor {
    private IndirectTestingInfo indirectTestingInfo; //contiene info sulla classe e metodi infetti
    private MethodWithIndirectTesting methodWithIndirectTesting; // Metodo infetto
    private Project project;
    private Editor editor;

    public IndirectTestingStrategy(MethodWithIndirectTesting methodWithIndirectTesting,Project project,IndirectTestingInfo indirectTestingInfo){
        this.methodWithIndirectTesting = methodWithIndirectTesting;
        this.project = project;
        this.indirectTestingInfo = indirectTestingInfo;
    }

    /* Implementazione del metodo esposto dall'interfaccia, implementa la logica di eliminazione dello smell*/

    public void doRefactor () throws PrepareFailedException{

        PsiClassBean classeInfetta = indirectTestingInfo.getClassWithIndTest(); //classe smelly
        PsiClass classPsi = classeInfetta.getPsiClass();
        String nomeMetodoDaRefattorizzare = methodWithIndirectTesting.getMethodWithIndTest().getName();
        boolean isEmptyFlag = true;  // Flag per verificare il contenimento dell' array di elementi da spostare
        PsiMethodBean psiMethodBean = methodWithIndirectTesting.getMethodWithIndTest();
        PsiMethod psiMethod = psiMethodBean.getPsiMethod();
        PsiType typeOfMethod = psiMethod.getReturnType(); // Mi restituisce il tipo di ritorno del metodo infetto

        ArrayList<PsiElement> elementiDaSpostare = new ArrayList<>(); // Elementi da spostare con refactoring tramite Extract Method

        /*Ricerca del metodo infetto fra tutti i metodi della classe infetta*/

        for (int i = 0; i<classPsi.getAllMethods().length;i++){
            if(classPsi.getAllMethods()[i].getName().equals(nomeMetodoDaRefattorizzare)){

                for (int j = 0; j<classPsi.getAllMethods()[i].getBody().getStatements().length;j++){

                    PsiElement statement = classPsi.getAllMethods()[i].getBody().getStatements()[j].getFirstChild();

                    if (statement instanceof PsiMethodCallExpression && statement.getFirstChild() instanceof PsiReferenceExpression){
                        PsiReferenceExpression referenceExpression = (PsiReferenceExpression) statement.getFirstChild();
                        String identifier = referenceExpression.getReferenceName();

                        if (isEmptyFlag){

                            /*Riempiamo l'array di elementi da spostare con occorrenze di statement da spsotare*/

                            elementiDaSpostare.add(classPsi.getAllMethods()[i].getBody().getStatements()[j]);
                            isEmptyFlag = false;
                        }else{

                            PsiReferenceExpression psiReferenceExpression = (PsiReferenceExpression) elementiDaSpostare.get(0).getFirstChild().getFirstChild();
                            String referenceName = psiReferenceExpression.getReferenceName();

                            // Check per vedere se lo statement che vogliamo spostare è lo stesso di quello presente nell'array
                            if (identifier.equals(referenceName)){
                                elementiDaSpostare.add(classPsi.getAllMethods()[i].getBody().getStatements()[j]);
                            }
                        } // Fine else
                    } // Fine If per controllare il tipo di istanza dello statement
                } // Fine Secondo for
            } // Fine if per verificare se il metodo scandito è quello da rifattorizzare
        } // Fine Primo For

        /* Abbiamo identificato i metodi da spostare, da qui in poi si implementa la logica per la refattorizzazione*/

    } // Fine doRefactor
} // Chiusura Classe
