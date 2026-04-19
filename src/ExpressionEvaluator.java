import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

public class ExpressionEvaluator {

    public static double evaluate(String expression, double x) {
        try {
            String expr = expression.toLowerCase().trim();

            expr = expr.replaceAll("π", "Math.PI");
            expr = expr.replaceAll("pi", "Math.PI");

            expr = handleImplicitMultiplicationInFunctions(expr);

            expr = addImplicitMultiplication(expr);

            expr = replaceXWithValue(expr, x);

            expr = handleFunctions(expr);

            return evaluateExpression(expr);
        } catch (Exception e) {
            System.err.println("Error evaluating: " + e.getMessage());
            return Double.NaN;
        }
    }

    public static String handleImplicitMultiplicationInFunctions(String expr) {
        String[] trigFunctions = {"sin", "cos", "tan", "asin", "acos", "atan", "sinh", "cosh", "tanh"};

        for (String func : trigFunctions) {
            int index = 0;
            while ((index = expr.indexOf(func + "(", index)) != -1) {
                int startParam = index + func.length() + 1;
                int endParam = findMatchingParenthesis(expr, startParam - 1);
                String param = expr.substring(startParam, endParam);

                // Add * between number and x (e.g., 10x -> 10*x)
                String newParam = param.replaceAll("(\\d+)x", "$1*x");
                newParam = newParam.replaceAll("x(\\d+)", "x*$1");

                expr = expr.substring(0, startParam) + newParam + expr.substring(endParam);
                index = startParam + newParam.length();
            }
        }
        return expr;
    }

   public static String addImplicitMultiplication(String expr) {
        expr = expr.replaceAll("\\)\\(", ")*(");

        expr = expr.replaceAll("(\\d+)\\(", "$1*(");

        expr = expr.replaceAll("\\)(\\d+)", ")*$1");

        expr = expr.replaceAll("x\\(", "x*(");

        expr = expr.replaceAll("\\)x", ")*x");

        expr = expr.replaceAll("(\\d+)x", "$1*x");

        expr = expr.replaceAll("x(\\d+)", "x*$1");

        expr = expr.replaceAll("(\\))([a-zA-Z])", "$1*$2");

        expr = expr.replaceAll("(Math\\.PI)([a-zA-Z])", "$1*$2");

        return expr;
    }

    public static String replaceXWithValue(String expr, double x) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < expr.length()) {
            char c = expr.charAt(i);
            if (c == 'x' && (i == 0 || !Character.isLetterOrDigit(expr.charAt(i-1)))) {
                result.append(x);
            } else {
                result.append(c);
            }
            i++;
        }
        return result.toString();
    }

    public static String handleFunctions(String expr) {
        expr = expr.replaceAll("sin\\(", "Math.sin(");
        expr = expr.replaceAll("cos\\(", "Math.cos(");
        expr = expr.replaceAll("tan\\(", "Math.tan(");
        expr = expr.replaceAll("asin\\(", "Math.asin(");
        expr = expr.replaceAll("acos\\(", "Math.acos(");
        expr = expr.replaceAll("atan\\(", "Math.atan(");
        expr = expr.replaceAll("sinh\\(", "Math.sinh(");
        expr = expr.replaceAll("cosh\\(", "Math.cosh(");
        expr = expr.replaceAll("tanh\\(", "Math.tanh(");
        expr = expr.replaceAll("sqrt\\(", "Math.sqrt(");
        expr = expr.replaceAll("exp\\(", "Math.exp(");
        expr = expr.replaceAll("log\\(", "Math.log(");
        expr = expr.replaceAll("log10\\(", "Math.log10(");
        expr = expr.replaceAll("abs\\(", "Math.abs(");
        expr = expr.replaceAll("ceil\\(", "Math.ceil(");
        expr = expr.replaceAll("floor\\(", "Math.floor(");

        return expr;
    }

    public static double evaluateExpression(String expr) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        if (engine == null) {
            return evaluateSimple(expr);
        }

        try {
            Object result = engine.eval(expr);
            return ((Number) result).doubleValue();
        } catch (Exception e) {
            return evaluateSimple(expr);
        }
    }

    public static double evaluateSimple(String expr) {
        try {
            expr = expr.replaceAll("\\s+", "");
            return parseExpression(expr);
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    public static double parseExpression(String expr) {
        int index = findLowestPrecedenceOperator(expr, new char[]{'+', '-'});
        if (index >= 0) {
            char op = expr.charAt(index);
            double left = parseExpression(expr.substring(0, index));
            double right = parseExpression(expr.substring(index + 1));
            return op == '+' ? left + right : left - right;
        }
        return parseTerm(expr);
    }

    public static double parseTerm(String expr) {
        int index = findLowestPrecedenceOperator(expr, new char[]{'*', '/'});
        if (index >= 0) {
            char op = expr.charAt(index);
            double left = parseTerm(expr.substring(0, index));
            double right = parseTerm(expr.substring(index + 1));
            return op == '*' ? left * right : left / right;
        }
        return parsePower(expr);
    }

    public static double parsePower(String expr) {
        int index = findLowestPrecedenceOperator(expr, new char[]{'^'});
        if (index >= 0) {
            double left = parsePower(expr.substring(0, index));
            double right = parsePower(expr.substring(index + 1));
            return Math.pow(left, right);
        }
        return parseFactor(expr);
    }

    public static double parseFactor(String expr) {
        if (expr.startsWith("(") && expr.endsWith(")")) {
            return parseExpression(expr.substring(1, expr.length() - 1));
        }

        if (expr.startsWith("Math.sin(")) {
            double val = parseExpression(expr.substring(9, findMatchingParenthesis(expr, 8)));
            return Math.sin(val);
        }
        if (expr.startsWith("Math.cos(")) {
            double val = parseExpression(expr.substring(9, findMatchingParenthesis(expr, 8)));
            return Math.cos(val);
        }
        if (expr.startsWith("Math.tan(")) {
            double val = parseExpression(expr.substring(9, findMatchingParenthesis(expr, 8)));
            return Math.tan(val);
        }
        if (expr.startsWith("Math.asin(")) {
            double val = parseExpression(expr.substring(10, findMatchingParenthesis(expr, 9)));
            return Math.asin(val);
        }
        if (expr.startsWith("Math.acos(")) {
            double val = parseExpression(expr.substring(10, findMatchingParenthesis(expr, 9)));
            return Math.acos(val);
        }
        if (expr.startsWith("Math.atan(")) {
            double val = parseExpression(expr.substring(10, findMatchingParenthesis(expr, 9)));
            return Math.atan(val);
        }
        if (expr.startsWith("Math.sqrt(")) {
            double val = parseExpression(expr.substring(10, findMatchingParenthesis(expr, 9)));
            return Math.sqrt(val);
        }
        if (expr.startsWith("Math.exp(")) {
            double val = parseExpression(expr.substring(9, findMatchingParenthesis(expr, 8)));
            return Math.exp(val);
        }
        if (expr.startsWith("Math.log(")) {
            double val = parseExpression(expr.substring(9, findMatchingParenthesis(expr, 8)));
            return Math.log(val);
        }
        if (expr.startsWith("Math.abs(")) {
            double val = parseExpression(expr.substring(9, findMatchingParenthesis(expr, 8)));
            return Math.abs(val);
        }
        if (expr.startsWith("Math.PI")) {
            return Math.PI;
        }

        try {
            return Double.parseDouble(expr);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    public static int findMatchingParenthesis(String expr, int start) {
        int count = 1;
        for (int i = start + 1; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') count++;
            else if (c == ')') {
                count--;
                if (count == 0) return i;
            }
        }
        return expr.length();
    }

    public static int findLowestPrecedenceOperator(String expr, char[] operators) {
        int index = -1;
        int parenthesesCount = 0;

        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')') parenthesesCount++;
            else if (c == '(') parenthesesCount--;
            else if (parenthesesCount == 0) {
                for (char op : operators) {
                    if (c == op) {
                        return i;
                    }
                }
            }
        }
        return index;
    }
}
