package main.testSmellDetection.structuralRules;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

public abstract class LazyTest {



    public boolean isLazyTest(ClassBean pClassBean, ClassBean pProductionClass) {
        boolean lazyTest = false;
        Vector<MethodBean> lazyTestCheck = new Vector<MethodBean>();

        for (MethodBean mb : pClassBean.getMethods()) {

            if (!mb.getName().equals(pClassBean.getName())
                    && !mb.getName().toLowerCase()
                    .equals("setup")
                    && !mb.getName().toLowerCase()
                    .equals("teardown") && !lazyTest) {
                Vector<MethodBean> calledMethods = (Vector<MethodBean>) mb.getMethodCalls();
                Vector<MethodBean> calledMethodsNoDuplicate = new Vector<MethodBean>();
                for (MethodBean cm : calledMethods){
                    if (!calledMethodsNoDuplicate.contains(cm)){
                        calledMethodsNoDuplicate.add(cm);
                    }
                }

                if (calledMethodsNoDuplicate.size() > 0){
                    Vector<MethodBean> cbMethods = (Vector<MethodBean>) pProductionClass.getMethods();
                    for (MethodBean tmpMb : calledMethodsNoDuplicate) {
                        for (MethodBean cbMethod : cbMethods){
                            if (cbMethod.getName().toLowerCase().equals(tmpMb.getName().toLowerCase()) && lazyTestCheck.contains(tmpMb)) {
                                lazyTest = true;
                                break;
                            } else if (cbMethod.getName().toLowerCase().equals(tmpMb.getName().toLowerCase()) && !lazyTestCheck.contains(tmpMb)){
                                lazyTestCheck.add(tmpMb);
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

    public void calculateClones(File deckardPath, File projectPath) throws IOException, InterruptedException {
        this.setDeckardConfiguration(projectPath, deckardPath);
        ProcessBuilder pb = new ProcessBuilder("/bin/sh", "-c", deckardPath + "/scripts/clonedetect/deckard.sh >>" + deckardPath + "/log");
        pb.directory(new File(deckardPath + "/scripts/clonedetect/"));
        Process p = pb.start();
        p.waitFor();
    }

    public Collection<File> getProjectTests(File evosuiteSF110Path) {
        Collection<File> tests = new ArrayList<>();
        Collection<File> subdirectories = FileUtils.listFilesAndDirs(evosuiteSF110Path, new NotFileFilter(TrueFileFilter.INSTANCE), DirectoryFileFilter.DIRECTORY);
        subdirectories.stream().filter((f) -> (f.getName().contains("evosuite-tests"))).forEach((f) -> {
            tests.add(f);
        });
        return tests;
    }

    private void setDeckardConfiguration(File testsPath, File deckardPath) throws IOException {
        File sampleConfig = new File(deckardPath + "/scripts/clonedetect/config-sample");
        File newConfig = new File(deckardPath + "/scripts/clonedetect/config");
        FileUtils.copyFile(sampleConfig, newConfig);
        String content = FileUtils.readFileToString(newConfig);
        FileUtils.writeStringToFile(newConfig, content.replaceAll("SRC_DIR='src'", "SRC_DIR='" + testsPath + "'"), StandardCharsets.UTF_8);
        content = FileUtils.readFileToString(newConfig);
        FileUtils.writeStringToFile(newConfig, content.replaceAll("DECKARD_DIR='/home/deckard'", "DECKARD_DIR='" + deckardPath + "'"), StandardCharsets.UTF_8);
        content = FileUtils.readFileToString(newConfig);
        FileUtils.writeStringToFile(newConfig, content.replaceAll("VECTOR_DIR='vectors'", "VECTOR_DIR='" + testsPath + "/vectors'"), StandardCharsets.UTF_8);
        content = FileUtils.readFileToString(newConfig);
        FileUtils.writeStringToFile(newConfig, content.replaceAll("CLUSTER_DIR='clusters'", "CLUSTER_DIR='" + testsPath + "/clusters'"), StandardCharsets.UTF_8);
        content = FileUtils.readFileToString(newConfig);
        FileUtils.writeStringToFile(newConfig, content.replaceAll("TIME_DIR='times'", "TIME_DIR='" + deckardPath + "/times'"), StandardCharsets.UTF_8);
    }

}
}
