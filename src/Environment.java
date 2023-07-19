import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, SPLValue> variables;

    public Environment() {
        variables = new HashMap<>();
    }

    public void declareVariable(String name) {
        if (variables.containsKey(name)) {
            throw new RuntimeException("Variable " + name + " already declared.");
        }
        variables.put(name, null);
    }

    public void assignVariable(String name, SPLValue value) {
        if (!variables.containsKey(name)) {
            throw new RuntimeException("Variable " + name + " not declared.");
        }
        variables.put(name, value);
    }

    public SPLValue getVariableValue(String name) {
        if (!variables.containsKey(name)) {
            throw new RuntimeException("Variable " + name + " not declared.");
        }
        return variables.get(name);
    }

    public boolean isVariableDeclared(String name) {
        return variables.containsKey(name);
    }
}
