import java.util.Stack;

public class Tnode {

    private final String name;
    private Tnode firstChild;
    private Tnode nextSibling;

    public Tnode(String name, Tnode firstChild, Tnode nextSibling) {
        if (name == null) {
            throw new RuntimeException("Node name is null.");
        }
        if (name.isEmpty()) {
            throw new RuntimeException("Node name is empty.");
        }
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '(' || c == ')' || c == ',' || Character.isWhitespace(c)) {
                throw new RuntimeException("Illegal node name: \"" + name + "\".");
            }
        }
        this.name = name;
        this.firstChild = firstChild;
        this.nextSibling = nextSibling;
    }

    private static boolean isOperator(String token) {
        return "+".equals(token) || "-".equals(token)
                || "*".equals(token) || "/".equals(token);
    }

    private static boolean isInteger(String token) {
        return token != null && token.matches("-?\\d+");
    }

    public static Tnode buildFromRPN(String expression) {
        if (expression == null) {
            throw new RuntimeException("RPN expression is null.");
        }

        String trimmed = expression.trim();
        if (trimmed.isEmpty()) {
            throw new RuntimeException("RPN expression is empty: \"" + expression + "\".");
        }

        Stack<Tnode> stack = new Stack<>();
        String[] tokens = trimmed.split("\\s+");

        for (String token : tokens) {
            if (isInteger(token)) {
                stack.push(new Tnode(token, null, null));
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new RuntimeException(
                            "Too few operands in RPN expression \"" + expression
                                    + "\" for operator \"" + token + "\"."
                    );
                }

                Tnode right = stack.pop();
                Tnode left = stack.pop();
                left.nextSibling = right;
                right.nextSibling = null;

                stack.push(new Tnode(token, left, null));
            } else {
                throw new RuntimeException(
                        "Illegal token in RPN expression \"" + expression
                                + "\": \"" + token + "\"."
                );
            }
        }

        if (stack.size() != 1) {
            throw new RuntimeException(
                    "Too many operands in RPN expression \"" + expression + "\"."
            );
        }

        return stack.pop();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        buildLeftParenthetic(this, sb);
        return sb.toString();
    }

    private static void buildLeftParenthetic(Tnode node, StringBuilder sb) {
        sb.append(node.name);
        if (node.firstChild != null) {
            sb.append("(");
            Tnode child = node.firstChild;
            while (child != null) {
                buildLeftParenthetic(child, sb);
                if (child.nextSibling != null) {
                    sb.append(",");
                }
                child = child.nextSibling;
            }
            sb.append(")");
        }
    }
}