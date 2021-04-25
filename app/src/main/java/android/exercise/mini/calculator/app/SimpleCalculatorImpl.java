package android.exercise.mini.calculator.app;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleCalculatorImpl implements SimpleCalculator
{
    private static final String PLUS = "+", MINUS = "-", ZERO = "0";

    private enum Modes
    {OPERATOR, DIGIT}  // possible states of calculator - typing number or operators

    private String output = ZERO; // used for displaying string of history input
    private List<String> history = new ArrayList<>();  // saves the whole numbers typed and the operators
    private SimpleCalculatorImpl.Modes mode = Modes.DIGIT;


    @Override
    public String output() {
        return output;
    }

    private boolean isValidDigit(int digit) {
        return (digit >= 0 && digit <= 9);
    }

    private boolean isOperator(String op) {
        return (op.equals(PLUS) || op.equals(MINUS));
    }

    @Override
    public void insertDigit(int digit) throws IllegalArgumentException {
        if (!isValidDigit(digit)) {
            throw new IllegalArgumentException();
        }
        mode = Modes.DIGIT;
        String digitStr = Integer.toString(digit);
        history.add(digitStr);
        if (output.equals(ZERO)) {// current output is "0"
            output = digitStr; // save current digit to current number
            return;
        }
        output += digitStr;
    }

    private void insertOperator(String operator) {
        if (mode == Modes.OPERATOR) {
            return;  // ignore double operators
        }
        mode = Modes.OPERATOR;
        if (history.isEmpty()) { // input starts with an order, chain to zero
            output = ZERO + operator;
            history.add("0");
            history.add(operator);
            return;
        }
        // else , chain the operator to output and add to history
        history.add(operator);
        output += operator;
    }

    @Override
    public void insertPlus() {
        insertOperator(PLUS);
    }

    @Override
    public void insertMinus() {
        insertOperator(MINUS);
    }

    private int evalOp(int lhs, int rhs, String operator) {
        if (operator.equals(PLUS)) {
            return lhs + rhs;
        }
        return lhs - rhs; // MINUS
    }

    private void evalHistory() {
        int lhs = 0, rhs = 0, i = 0, currDigit = 0;
        String op = "", currChar = "";
        boolean opFound = false;

        while (i < history.size()) {
            currChar = history.get(i);

            if (isOperator(currChar)) {
                if (opFound) {
                    lhs = evalOp(lhs, rhs, op);
                    rhs = 0;
                    op = currChar;
                }
                else {
                    opFound = true;
                    op = currChar;
                }
            }
            else // digit
            {
                currDigit = Integer.parseInt(currChar);
                if (opFound) // add to rhs
                {
                    rhs *= 10;
                    if (rhs >= 0) {
                        rhs += currDigit;
                    }
                    else {
                        rhs -= currDigit;
                    }
                }
                else // add to lhs
                { // also for subsequent calls on insertEquals¶
                    lhs *= 10;
                    if (lhs >= 0) {
                        lhs += currDigit;
                    }
                    else {
                        lhs -= currDigit;
                    }
                }
            }
            i++;
        }
        if (opFound) { // for history with only one operator between 2 numbers, else evaluates with 0
            lhs = evalOp(lhs, rhs, op);
        }
        output = Integer.toString(lhs);
        history.clear();
        history.add(output);
    }


    @Override
    public void insertEquals() {
        if (history.size() <= 1) {  // size is 0,1 no operators to calculate
            return;
        }
        if (mode == Modes.OPERATOR) // delete last operator
        {
            deleteLast();
        }
        // eval
        evalHistory();
    }

    private void deleteOperator() {
        output = output.substring(0, output.length() - 1); // remove operator from output
        history.remove(history.size() - 1); // remove operator from history
        mode = Modes.DIGIT;
        if (history.size() == 1 && output.equals(ZERO)) {
            clear();
        }
    }

    private void deleteDigit() {
        if (output.length() == 1) { // reset the output to zero output
            clear();
            return;
        }
        if (history.size() == 1 && history.get(0).length() > 1) // multi digit number in lhs
        {
            output = output.substring(0, output.length() - 1);
            history.clear();
            history.add(output);
        }
        else {
            // else, we have a number after previous entries, might be single digit or multiple digit number
            output = output.substring(0, output.length() - 1); // remove digit from output
            if (!history.isEmpty()) {
                history.remove(history.size() - 1); // remove the previous number from history
            }
        }
        if (isOperator(history.get(history.size() - 1))) {
            mode = Modes.OPERATOR;
        }

    }

    @Override
    public void deleteLast() {
        if (output.equals(ZERO)) {
            clear();
        }
        if (mode == Modes.OPERATOR) { // necessarily move into digit state
            deleteOperator();
        }
        else {
            deleteDigit(); // else, digit state
        }

    }

    @Override
    public void clear() {
        output = ZERO;
        history.clear();
        mode = Modes.DIGIT;
    }

    @Override
    public Serializable saveState() {

        CalculatorState state = new CalculatorState();
        state.output = this.output;
        state.history = new ArrayList<>(this.history);
        state.mode = this.mode;
        return state;
    }

    @Override
    public void loadState(Serializable prevState) {
        if (!(prevState instanceof CalculatorState)) {
            return; // ignore
        }
        SimpleCalculatorImpl.CalculatorState casted = (CalculatorState) prevState;
        this.output = casted.output;
        this.history = new ArrayList<>(casted.history);
        this.mode = casted.mode;
    }

    private static class CalculatorState implements Serializable
    {
        private String output;
        private List<String> history;
        private Modes mode;
    }

}
