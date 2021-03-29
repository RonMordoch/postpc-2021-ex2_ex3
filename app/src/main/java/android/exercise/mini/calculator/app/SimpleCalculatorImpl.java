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
    private enum States {OPERATOR, DIGIT}  // possible states of calculator - typing number or operators
    private States state = null; // current state of


    /******************** PRIVATE HELPER METHODS ********************/

    private boolean isValidDigit(int digit)
    {
        return (digit >= 0 && digit <= 9);
    }

    /**
     * Inserts the given operator (order) to the calculator.
     * Ignores double operator.
     */
    private void insertOperator(String operator) {
        if (state == States.OPERATOR)
        {
            return;  // ignore double operators
        }
        if (state == States.DIGIT) // add current number typed so far to history
        {
            history.add(currNum);
            currNum = ""; // reset current number
        }
        state = States.OPERATOR;
        currChar = operator;
        if (history.isEmpty()) // input starts with an order, chain to zero
        {
            output = ZERO;
            history.add(ZERO);
        }
        // regardless oh history size, chain the operator to output and store it in history
        output += operator;
        history.add(operator);
    }

    private int evalOp(int lhs, int rhs, String operator) {
        if (operator.equals(PLUS))
        {
            return lhs + rhs;
        }
        return lhs - rhs; // MINUS
    }


    private void evalHistory()
    {
        int prev = Integer.parseInt(history.get(0)), next = 0, i = 1; // get first number of history
        String op = "";
        while (i < history.size()) {
            op = history.get(i);
            next = Integer.parseInt(history.get(i + 1));
            prev = evalOp(prev, next, op);
            i += 2;
        }
        // finished eval, update all fields
        currChar = Integer.toString(prev);
        history.clear(); // dont insert into history yet to allow chaining more digits
        currNum = currChar;
        output = currChar;
        state = States.DIGIT;
    }


    private void deleteOperator()
    {
        output = output.substring(0, output.length() - 1); // remove operator from output
        history.remove(history.size() - 1); // remove operator from history
        state = States.DIGIT;
        currChar = output.substring(output.length() - 1); // last char of output as curr char
        currNum = history.get(history.size() - 1); // set current num to be the last number
    }

    private void deleteDigit()
    {
        if (output.length() == 1) // reset the output to zero output
        {
//            output = currChar = currNum = "0";
//            history.clear();
            clear();
        }
        else if (currNum.length() == 1)  // single digit number, not in history yet, delete and move to operator state
        {
            state = States.OPERATOR;
            output = output.substring(0, output.length() - 1); // remove digit from output
            currChar = output.substring(output.length() - 1); // last char of output, operator as curr char
            currNum = ""; // reset curr num
            history.remove(history.size() - 1); // remove the previous number from history
        }
        else // multiple digits number, stay in digit state
        {
            output = output.substring(0, output.length() - 1); // remove digit from output
            currChar = output.substring(output.length() - 1); // last char of output, the previous digit in number
            currNum = currNum.substring(0, currNum.length() - 1);
            history.remove(history.size() - 1); // remove the previous number from history
            history.add(currNum); // add the new current number to history
        }
    }

    /******************** PUBLIC API IMPLEMENTATION ********************/

    @Override
    public String output() {
        return output;
    }

    @Override
    public void insertDigit(int digit) throws IllegalArgumentException {
        if (!isValidDigit(digit))
        {
            throw new IllegalArgumentException();
        }
        state = States.DIGIT;
        currChar = Integer.toString(digit);
        if (history.isEmpty()) // current output is "0"
        {
            currNum = currChar; // save current digit to current number
            output = currChar; // override the zero output
        }
        else  // append to current digit or end of operators
        {
            currNum += currChar;
            output += currChar;
        }
    }


    @Override
    public void insertPlus()
    {
        insertOperator(PLUS);
    }

    @Override
    public void insertMinus()
    {
        insertOperator(MINUS);
    }

    @Override
    public void insertEquals() {
        if (history.size() <= 1)  // size is 0,1 no operators to calculate
        {
            return;
        }
        if (state == States.DIGIT) // save the current number typed so far into history and reset the current number
        {
            history.add(currNum);
            currNum = "";
        }
        else if (state == States.OPERATOR) // remove the last operator, move into digit state
        {
            deleteLast();
        }
        evalHistory();
    }

    @Override
    public void deleteLast()
    {
        if (currChar.equals(""))  // no input was given
        {
            return;
        }
        if (state == States.OPERATOR) // necessarily move into digit state
        {
            deleteOperator();
            return;
        }
        deleteDigit(); // else, digit state
    }

    @Override
    public void clear()
    {
        currNum = currChar = "";
        output = ZERO;
        history.clear();
        state = null;
    }

    @Override
    public Serializable saveState() {
        CalculatorState state = new CalculatorState();
        // todo: insert all data to the state, so in the future we can load from this state
        return state;
    }

    @Override
    public void loadState(Serializable prevState) {
        if (!(prevState instanceof CalculatorState)) {
            return; // ignore
        }
        CalculatorState casted = (CalculatorState) prevState;
        // todo: use the CalculatorState to load
    }

    private static class CalculatorState implements Serializable {
    /*
    TODO: add fields to this class that will store the calculator state
    all fields must only be from the types:
    - primitives (e.g. int, boolean, etc)
    - String
    - ArrayList<> where the type is a primitive or a String
    - HashMap<> where the types are primitives or a String
     */
    }

}
