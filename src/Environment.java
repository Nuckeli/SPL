import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Deque<Map<String, Object>> scopeStack;

    public Environment() {
        scopeStack = new ArrayDeque<>();
        pushScope();
    }

    public void pushScope() {
        scopeStack.push(new HashMap<>());
    }

    public void popScope() {
        scopeStack.pop();
    }

    public void declareVariable(String name, Object value) {
        scopeStack.peek().put(name, value);
    }

    public void assignVariable(String name, Object value) {
        for (Map<String, Object> scope : scopeStack) {
            if (scope.containsKey(name)) {
                scope.put(name, value);
                return;
            }
        }
        throw new RuntimeException("Variable " + name + " not declared.");
    }

    public Object getVariableValue(String name) {
        for (Map<String, Object> scope : scopeStack) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        throw new RuntimeException("Variable " + name + " not declared.");
    }

    public boolean isVariableDeclared(String name) {
        for (Map<String, Object> scope : scopeStack) {
            if (scope.containsKey(name)) {
                return true;
            }
        }
        return false;
    }
}
