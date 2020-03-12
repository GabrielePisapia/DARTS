package main.windowConstruction;

import com.intellij.openapi.project.Project;
import main.testSmellDetection.testSmellInfo.indirectTesting.IndirectTestingInfo;
import main.windowConstruction.testSmellPanel.ClassWithEagerTestPanel;
import main.windowConstruction.testSmellPanel.ClassWithIndirectTestPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public class IndirectTestingPanel extends JPanel {
    private ArrayList<IndirectTestingInfo> classWithIndTest;

    public IndirectTestingPanel(ArrayList<IndirectTestingInfo> classWithSmell, Project project){

        // Titolo + bordo del pannello
        TitledBorder border = new TitledBorder("INDIRECT TESTING");
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitlePosition(TitledBorder.TOP);

        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.setBorder(border);

        if(classWithSmell.isEmpty()){
            // Inizializzazione del pannello
            classWithIndTest = classWithSmell;

            // Altra parte del pannello
            JPanel topPanel = new JPanel(new GridLayout(1,3));
            topPanel.add(new JLabel("CLASS NAME"));
            topPanel.add(new JLabel("PRODUCTION CLASS NAME"));
            JLabel details = new JLabel("METHODS DETAILS");
            details.setHorizontalAlignment(SwingConstants.CENTER);
            topPanel.add(details);

            // IMPOSTAZIONI DIMENSIONI DEL PANNELLO

            topPanel.setMinimumSize(new Dimension(Integer.MAX_VALUE,1000));
            topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,1000));
            this.add(topPanel);

            // Creazione della parte dedicata alle info per ogni classe affetta
            for (IndirectTestingInfo iti : classWithIndTest){
                JPanel classPanel = new ClassWithIndirectTestPanel(iti,project);
                classPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,5000));
                this.add(classPanel);
            }
        }else{
            JPanel emptyPanel = new JPanel(new GridLayout(1,1));
            JLabel emptyLabel = new JLabel("No Smell Found!");
            emptyPanel.add(emptyLabel);
            this.add(emptyPanel);
        }
    }

    public ArrayList<IndirectTestingInfo> getClassesWithIndirectTesting(){
        return classWithIndTest;
    }

    public void setClassesWithIndirectTesting (ArrayList<IndirectTestingInfo> classWithIndirectTest){
        this.classWithIndTest = classWithIndirectTest;
    }
}
