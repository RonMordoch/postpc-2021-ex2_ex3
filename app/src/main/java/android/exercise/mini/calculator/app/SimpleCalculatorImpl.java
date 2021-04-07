package android.exercise.mini.calculator.app;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleCalculatorImpl implements SimpleCalculator {

    private static final String PLUS = "+", MINUS = "-", ZERO = "0";
    private String currChar = "", // current char pressed on screen
            currNum = "", // current number being typed in calculator, stores the whole number string
            output = ZERO; // used for displaying string of history input
    private List<String> history = new ArrayList<>();  // saves the whole numbers typed and the operators

    private enum Modes {OPERATOR, DIGIT}  // possible states of calculator - typing number or operators

    private Modes mode = null; // null for zero output


    @Override
    public String output() {
        return output;
    }

    private boolean isValidDigit(int digit) {
        return (digit >= 0 && digit <= 9);
    }

    @Override
    public void insertDigit(int digit) throws IllegalArgumentException {
        if (!isValidDigit(digit)) {
            throw new IllegalArgumentException();
        }
        String digitStr = Integer.toString(digit);
        if (mode != Modes.DIGIT) {
            mode = Modes.DIGIT;
        }
        if (output.equals(ZERO)) {// current output is "0"
            output = currNum = currChar = digitStr; // save current digit to current number
            return;
        }
        currChar = digitStr;
        currNum += digitStr;
        output += digitStr;
    }

    private void insertOperator(String operator) {
        if (mode == Modes.OPERATOR) {
            return;  // ignore double operators
        }
        if (mode == Modes.DIGIT) { // add current number typed so far to history
            history.add(currNum);
            currNum = ""; // reset current number
        }
        mode = Modes.OPERATOR;
        currChar = operator;
        if (history.isEmpty()) { // input starts with an order, chain to zero
            output = ZERO;
            history.add(ZERO);
        }
        // regardless of history size, chain the operator to output and store it in history
        output += operator;
        history.add(operator);
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
        int prev = Integer.parseInt(history.get(0)), next = 0, i = 1; // get first number of history
        String op = "", res = "";
        while (i < history.size()) {
            op = history.get(i);
            next = Integer.parseInt(history.get(i + 1));
            prev = evalOp(prev, next, op);
            i += 2;
        }
        // finished eval, update all fields
        res = Integer.toString(prev);
        history.clear(); // don't insert into history yet to allow chaining more digits
        currChar = res.substring(res.length() - 1);
        currNum = output = res;
        mode = Modes.DIGIT;
    }

    @Override
    public void insertEquals() {
        if (history.size() <= 1) {  // size is 0,1 no operators to calculate
            return;
        }
        if (mode == Modes.DIGIT) { // save the current number typed so far into history and reset the current number
            history.add(currNum);
        } else { // remove the last operator, move into digit state
            deleteLast();
        }
        evalHistory();
    }

    private void deleteOperator() {
        output = output.substring(0, output.length() - 1); // remove operator from output
        history.remove(history.size() - 1); // remove operator from history
        mode = Modes.DIGIT;
        currChar = output.substring(output.length() - 1); // last char of output as curr char
        currNum = history.get(history.size() - 1); // set current num to be the last number
    }

    private void deleteDigit() {
        if (output.length() == 1) { // reset the output to zero output
            clear();
            return;
        }  // else, we have a number after previous entries, might be single digit or multiple digit number
        if (currNum.length() == 1) { // single digit, previous entries in output, not in history, delete and move to operator state
            mode = Modes.OPERATOR;
        }
        output = output.substring(0, output.length() - 1); // remove digit from output
        currChar = output.substring(output.length() - 1); // last char of output, operator or previous digit
        currNum = currNum.substring(0, currNum.length() - 1); // either reset current number or remove last digit
        if (!history.isEmpty()) {
            history.remove(history.size() - 1); // remove the previous number from history
        }
    }

    @Override
    public void deleteLast() {
        if (currChar.equals("")) {  // no input was given
            return;
        }
        if (mode == Modes.OPERATOR) { // necessarily move into digit state
            deleteOperator();
        } else {
            deleteDigit(); // else, digit state
        }
        if (output.equals(ZERO)) {
            clear();
        }
    }

    @Override
    public void clear() {
        currNum = currChar = "";
        output = ZERO;
        history.clear();
        mode = null;
    }

    @Override
    public Serializable saveState() {
        CalculatorState state = new CalculatorState();
        state.currChar = this.currChar;
        state.currNum = this.currNum;
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
        CalculatorState casted = (CalculatorState) prevState;
        this.currChar = casted.currChar;
        this.currNum = casted.currNum;
        this.output = casted.output;
        this.history = new ArrayList<>(casted.history);
        this.mode = casted.mode;
    }

    private static class CalculatorState implements Serializable {
        private String currChar, currNum, output;
        private List<String> history;
        private SimpleCalculatorImpl.Modes mode;
    }


}
