package main.windowConstruction.testSmellPanel;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPackageStatement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.extractMethod.PrepareFailedException;
import main.refactor.IRefactor;
import main.refactor.strategy.IndirectTestingStrategy;
import main.testSmellDetection.bean.PsiMethodBean;
import main.testSmellDetection.testSmellInfo.indirectTesting.IndirectTestingInfo;
import main.testSmellDetection.testSmellInfo.indirectTesting.MethodWithIndirectTesting;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClassWithIndirectTestPanel extends JPanel {

    public ClassWithIndirectTestPanel(IndirectTestingInfo iti, Project project){

        // Contorno del pannello
        Border blackline = BorderFactory.createLineBorder(Color.BLACK);
        this.setBorder(blackline);

        //Costruzione struttura del pannello riguardante una specifica classe affetta dallo smell
        PsiFile parentFile = (PsiFile) PsiTreeUtil.getParentOfType(iti.getClassWithIndTest().getPsiClass(), PsiFile.class);
        PsiPackageStatement psiPackageStatement = PsiTreeUtil.getChildOfType(parentFile,PsiPackageStatement.class);
        String packageString = psiPackageStatement.getPackageName();
        JLabel classNameLabel = new JLabel("   "+ packageString + "." + iti.getClassWithIndTest().getPsiClass().getName());

        // Production Class
        parentFile = (PsiFile) PsiTreeUtil.getParentOfType(iti.getProductionClass().getPsiClass(),PsiFile.class);
        psiPackageStatement = PsiTreeUtil.getChildOfType(parentFile,PsiPackageStatement.class);
        packageString = psiPackageStatement.getPackageName();
        JLabel prodClassNameLabel = new JLabel(packageString + "."+ iti.getProductionClass().getPsiClass().getName());

        // Lista dei metodi
        JPanel listOfMethodsPanel = new JPanel();
        listOfMethodsPanel.setBorder(BorderFactory.createMatteBorder(0,1,0,1,Color.BLACK));
        listOfMethodsPanel.setLayout(new GridLayout(iti.getMethodThatCauseIndirectTest().size(),1));

        for(MethodWithIndirectTesting metodi : iti.getMethodThatCauseIndirectTest()){
            JPanel methodPanel = new JPanel(new GridLayout(1,3));
            JLabel methodName = new JLabel("   "+ metodi.getMethodWithIndTest().getPsiMethod().getName());
            JButton methodButton = new JButton("details");
            JButton refactoringButton = new JButton("Do Refactor");


            // Listener per i bottoni
            methodButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame detailsFrame = new JFrame(iti.getClassWithIndTest().getPsiClass().getName());
                    Container container = detailsFrame.getContentPane();
                    container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));

                    // Creazione della scritta che contiene le informazioni
                    String methodName = "Method: "+ metodi.getMethodWithIndTest().getPsiMethod().getName()+ " calls the following methods: ";
                    JLabel methodNameLabel = new JLabel(methodName);
                    methodNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    methodNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    methodNameLabel.setMinimumSize(new Dimension(500,35));
                    methodNameLabel.setPreferredSize(new Dimension(500,35));
                    methodNameLabel.setMaximumSize(new Dimension(500,35));
                    container.add(methodNameLabel);

                    for (PsiMethodBean methodCalled : metodi.getListOfMethodCalled()){
                        JLabel methodCalledName = new JLabel("   "+methodCalled.getPsiMethod().getName());
                        methodCalledName.setAlignmentX(Component.LEFT_ALIGNMENT);
                        methodCalledName.setMinimumSize(new Dimension(500,35));
                        methodCalledName.setPreferredSize(new Dimension(500,35));
                        methodCalledName.setMaximumSize(new Dimension(500,35));

                        container.add(methodCalledName);
                    }

                    // Refactoring Tips
                    JLabel refactoringLabel = new JLabel("It can be removed using this refactoring operations: ");
                    JLabel firstRefactoringLabel = new JLabel("Extract method: affected method can be splitted into smaller methods, each one testing a specific behavior of the tested object.");
                    JLabel secondRefactoringLabel = new JLabel ("Move method: create a new method in the class that uses the method the most, then move code from the old method to there. Turn the code of the original method into a reference to the new method in the other class or else remove it entirely.");
                    refactoringLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    firstRefactoringLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    secondRefactoringLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                    container.add(refactoringLabel);
                    container.add(firstRefactoringLabel);
                    container.add(secondRefactoringLabel);
                    detailsFrame.setSize(500,300);
                    detailsFrame.setVisible(true);


                }
            });

            // Listener per il bottone di refactoring automatico
            refactoringButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    IRefactor refactor = new IndirectTestingStrategy(metodi,project,iti);
                    try{
                        refactor.doRefactor();
                    }catch(PrepareFailedException ex1){
                        ex1.printStackTrace();
                    }
                }
            });

            methodPanel.add(methodName);
            methodPanel.add(methodButton);
            methodPanel.add(refactoringButton);
            listOfMethodsPanel.add(methodPanel);

        }
        this.setLayout(new GridLayout(1,3));
        this.add(classNameLabel);
        this.add(prodClassNameLabel);
        this.add(listOfMethodsPanel);
    }

}
