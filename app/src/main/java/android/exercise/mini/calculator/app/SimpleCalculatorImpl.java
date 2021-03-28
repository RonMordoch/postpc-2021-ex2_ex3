package android.exercise.mini.calculator.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleCalculatorImpl implements SimpleCalculator
{

    // todo: add fields as needed
    String output = "0";
    private static final String PLUS = "+", MINUS = "-", ZERO = "0";
    // output is used for displaying string of history of inputs, history saves operator and the whole numbers typed
    private String currChar = "", currNum = "";
    List<String> history = new ArrayList<>();

    private enum States
    {OPERATOR, DIGIT}

    private States state = null;

    @Override
    public String output()
    {
        // todo: return output based on the current state
        return output;
    }

    private boolean isValidDigit(int digit)
    {
        return (digit >= 0 && digit <= 9);
    }

    @Override
    public void insertDigit(int digit) throws IllegalArgumentException
    {
        // todo: insert a digit
        if (!isValidDigit(digit))
        {
            throw new IllegalArgumentException();
        }
        state = States.DIGIT;
        currChar = Integer.toString(digit);
//        if (history.isEmpty())  // current output is "0"
        if (output.equals(ZERO))
        {
            currNum = currChar; // save current digit to current number
            output = currChar;
        }
        else  // append to current digit or end of operators
        {
            currNum += currChar;
            output += currChar;
        }
    }

    private void insertOperator(String operator)
    {
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
        if (history.isEmpty()) //  input starts with an order, chain to zero
        {
            output = ZERO;
            history.add(ZERO);
        }
        // regardless, chain the operator to output and store it in history
        output += operator;
        history.add(operator);
    }

    @Override
    public void insertPlus()
    {
        // todo: insert a plus
        insertOperator(PLUS);
    }

    @Override
    public void insertMinus()
    {
        // todo: insert a minus
        insertOperator(MINUS);
    }

    @Override
    public void insertEquals()
    {
        // todo: calculate the equation. after calling `insertEquals()`, the output should be the result
        //  e.g. given input "14+3", calling `insertEquals()`, and calling `output()`, output should be "17"
        if (history.size() <= 1)  // size is 0,1 no operators to calculate
        {
            return; // TODO anything else?
        }
        if (state == States.DIGIT)
        {
            history.add(currNum);
            currNum = ""; // reset current number
        }
        if (state == States.OPERATOR)
        {
            history = history.subList(0, history.size() - 1);
            state = States.DIGIT;
        }
        int prev = Integer.parseInt(history.get(0)), next = 0, i = 1; // get first number of history
        String op = "";
        while (i < history.size())
        {
            op = history.get(i);
            next = Integer.parseInt(history.get(i + 1));
            prev = eval(prev, next, op);
            i += 2;
        }
        // finished eval
        currChar = Integer.toString(prev);
        history.clear(); // dont insert into history yet to allow chaining more digits
        currNum = currChar;
        output = currChar;
        state = States.DIGIT;

    }

    private int eval(int lhs, int rhs, String operator)
    {
        if (operator.equals(PLUS))
        {
            return lhs + rhs;
        }
        return lhs - rhs; // MINUS
    }

    @Override
    public void deleteLast()
    {
        // todo: delete the last input (digit, plus or minus)
        //  e.g.
        //  if input was "12+3" and called `deleteLast()`, then delete the "3"
        //  if input was "12+" and called `deleteLast()`, then delete the "+"
        //  if no input was given, then there is nothing to do here
        if (currChar.equals(""))  // no input was given
        {
            return;
        }
        if (state == States.OPERATOR) // necessarily move into digit state
        {
            output = output.substring(0, output.length() - 1); // remove operator from output
            history.remove(history.size() - 1); // remove operator from history

            state = States.DIGIT;
            currChar = output.substring(output.length() - 1); // last char of output as curr char
            currNum = history.get(history.size() - 1); // set current num to be the last number
        }
        else if (state == States.DIGIT)
        {
            // TODO
            if (output.length() == 1)
            {
                output = currChar = currNum = "0";
                history.clear();
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
    }

    @Override
    public void clear()
    {
        // todo: clear everything (same as no-input was never given)
    }

    @Override
    public Serializable saveState()
    {
        CalculatorState state = new CalculatorState();
        // todo: insert all data to the state, so in the future we can load from this state
        return state;
    }

    @Override
    public void loadState(Serializable prevState)
    {
        if (!(prevState instanceof CalculatorState))
        {
            return; // ignore
        }
        CalculatorState casted = (CalculatorState) prevState;
        // todo: use the CalculatorState to load
    }

    private static class CalculatorState implements Serializable
    {
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