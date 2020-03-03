package main.refactor.strategy;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.refactoring.extractMethod.ExtractMethodHandler;
import com.intellij.refactoring.extractMethod.ExtractMethodProcessor;
import com.intellij.refactoring.extractMethod.PrepareFailedException;
import com.intellij.refactoring.extractclass.ExtractClassProcessor;
import main.refactor.IRefactor;
import main.testSmellDetection.bean.PsiClassBean;
import main.testSmellDetection.bean.PsiMethodBean;
import main.testSmellDetection.testSmellInfo.indirectTesting.IndirectTestingInfo;
import main.testSmellDetection.testSmellInfo.indirectTesting.MethodWithIndirectTesting;

import java.util.ArrayList;
import java.util.List;

public class IndirectTestingStrategy implements IRefactor {
    private IndirectTestingInfo indirectTestingInfo; //contiene info sulla classe e metodi infetti
    private MethodWithIndirectTesting methodWithIndirectTesting; // Metodo infetto
    private Project project;
    private Editor editor;
    private ExtractClassProcessor processor2;

    public IndirectTestingStrategy(MethodWithIndirectTesting methodWithIndirectTesting,Project project,IndirectTestingInfo indirectTestingInfo){
        this.methodWithIndirectTesting = methodWithIndirectTesting;
        this.project = project;
        this.indirectTestingInfo = indirectTestingInfo;
    }

    /* Implementazione del metodo esposto dall'interfaccia, implementa la logica di eliminazione dello smell*/

    public void doRefactor () throws PrepareFailedException{

        PsiClassBean classeInfetta = indirectTestingInfo.getClassWithIndTest(); //classe smelly
        PsiClass classPsi = classeInfetta.getPsiClass();
        String packageName = classeInfetta.getPsiPackage().getName(); // Nome del package contenente la classe infetta
        String nomeMetodoDaRefattorizzare = methodWithIndirectTesting.getMethodWithIndTest().getName();
        boolean isEmptyFlag = true;  // Flag per verificare il contenimento dell' array di elementi da spostare

        PsiMethodBean psiMethodBean = methodWithIndirectTesting.getMethodWithIndTest();
        PsiMethod psiMethod = psiMethodBean.getPsiMethod();
        PsiType typeOfMethod = psiMethod.getReturnType(); // Mi restituisce il tipo di ritorno del metodo infetto

        ArrayList<PsiElement> elementiDaSpostare = new ArrayList<>(); // Elementi da spostare con refactoring tramite Extract Method
        ArrayList<PsiMethod> metodiDaMuovere = new ArrayList<>(); /*Per il move*/

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
        PsiElement [] elementiDaMuovere = elementiDaSpostare.toArray(new PsiElement[elementiDaSpostare.size()]);
        editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

        // Istanzio un processor per manipolare l'extract
        ExtractMethodProcessor processor = new ExtractMethodProcessor(project,editor,elementiDaMuovere,typeOfMethod,"ExtractMethodRefactoring","xx", null);

        // Controllo per inizializzare i parametri del processor

        if (processor.prepare()){
            processor.testPrepare();
            processor.testNullability();
            if(processor.showDialog()){
                ExtractMethodHandler.extractMethod(project,processor);
            }
        }

        // Creazione dell'annotazione @Test da aggiungere al metodo estratto
        PsiAnnotation annotazione = JavaPsiFacade.getElementFactory(project).createAnnotationFromText("@Test", processor.getExtractedMethod().getContext());

        // Aggiunta dell'annotazione al metodo
        WriteCommandAction.runWriteCommandAction(project, () -> {
            classPsi.addBefore(annotazione,processor.getExtractedMethod());
        });

        // Eliminazione della chiamata al metodo
        WriteCommandAction.runWriteCommandAction(project,()->{
            PsiStatement [] statements =  psiMethod.getBody().getStatements();
            String nomeMetodo = processor.getExtractedMethod().getName()+"();";
            for (PsiStatement statement: statements){
                if(statement.getText().equals(nomeMetodo)){
                    statement.delete();
                }
            }
        });

        /* Inizio della tecnica di Move*/

        metodiDaMuovere.add(processor.getExtractedMethod());
        List<PsiField> campiDaMuovere = new ArrayList<>();
        List<PsiClass> innerClasses = new ArrayList<>();
        List<PsiVariable> instances = psiMethodBean.getUsedInstanceVariables();

        if (instances.size()>0){
            for (int i= 0;i<instances.size();i++){
                campiDaMuovere.add(classPsi.findFieldByName(instances.get(i).getName(),true));
            }
        }

        for (PsiClass innerClass : classPsi.getInnerClasses()){
            innerClasses.add(innerClass);
        }

        String classNewName = classPsi.getName()+"s";
        processor2 = new ExtractClassProcessor(
                classPsi,
                campiDaMuovere,
                metodiDaMuovere,
                innerClasses,
                packageName,
                classNewName);
        processor2.setPreviewUsages(true);
        processor2.run();


    } // Fine doRefactor
} // Chiusura Classe
