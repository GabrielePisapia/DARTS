package main.testSmellDetection.structuralRules;

import java.io.File;
import java.io.IOException;

public abstract class CodeDuplication {

    public boolean isCodeDuplication(ClassBean pClassBean, String testsPath) throws IOException {
        File result = new File(testsPath + "/clusters/post_cluster_vdb_100_2_allg_1.0_50");
        if (result.exists()) {
            String content = FileUtils.readFileToString(result);
            String classPath = pClassBean.getBelongingPackage().replace(".", "/") + "/" + pClassBean.getName() + ".java";
            return content.contains(classPath);
        }
        return false;
    }

}
