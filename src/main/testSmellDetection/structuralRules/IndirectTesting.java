package main.testSmellDetection.structuralRules;

import java.util.List;
import java.util.stream.Collectors;

public abstract class IndirectTesting {

    public int  isIndirectTesting(MethodBean pMethod) {
        int indirectTesting=0;
        List<String> productionClassMethods=productionClass.getMethods().stream().map(x -> x.getName()).collect(Collectors.toList());
        List<String> calls=pMethod.getMethodCalls().stream().map(x -> x.getName()).collect(Collectors.toList());
        for(String s : calls) {
            if (!productionClassMethods.contains(s)){
                indirectTesting++;
            }
        }
        return indirectTesting;
    }

}
