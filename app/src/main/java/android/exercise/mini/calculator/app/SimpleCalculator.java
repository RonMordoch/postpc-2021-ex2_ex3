package android.exercise.mini.calculator.app;

import java.io.Serializable;

public interface SimpleCalculator extends Serializable {

  /**
   @return the current output that should be shown.
   The output is all the numbers and operations tapped until now, until an equals ("=") is given and the output is reset to calculate the value.
   Multiple times calling output() without new input should return the same value.

   Examples:
   With no input, the default output should be "0"
   After feeding the input "4", when calling output() answer should be  "4"
   After feeding the input "+", when calling output() answer should be "4+"
   After feeding the input "5", when calling output() answer should be "4+5"
   After feeding the input "=", when calling output() answer should be "9"
   After feeding the input "-", when calling output() answer should be "9-"
   And so on...
   */
  String output();

  /**
   Inserts a digit to the history.
   @param digit - number between 0 to 9.
   @throws IllegalArgumentException if any other int is given
   */
  void insertDigit(int digit);

  /**
   Adds a plus order ("+").
   Calculator should ignore if this call came after another order
   (e.g. given the input "9++" →
    Ignore the second plus.
    Calling output() will return "9+"
   )
   */
  void insertPlus();

  /**
   Adds a minus order ("-").
   Calculator should ignore if this call came after another order
   (e.g. given the input "54+7+-" →
    Ignore the last minus order
    Calling output() will return "54+7+"
   )
   */
  void insertMinus();

  /**
   Calculate the equation.
   If the equation ends with an order (e.g. "3-7-2+") →
    Remove the last order (e.g. calculate only "3-7-2")

   Calling output() should show the calculated value
    e.g. for input "3-5=" calling output() should return "-2"
   */
  void insertEquals();

  /**
   Delete the last digit/order.
   Ignore if no input was given yet.
   */
  void deleteLast();

  /**
   Delete everything.
   After calling this, calls to output() should return "0".
   */
  void clear();

  /**
   * @return some object that encapsulates the current state
   */
  Serializable saveState();

  /**
   Read the input state and load from it.
   Example:
   given the input "14+5-7",
   Calling saveState() will return some serializable object that stores all needed data for the state.
   If we will create a new Calculator instance and call loadState(), passing the serializable from earlier,
   and then call output() →
   the result should be "14+5-7" (i.e. successfully loaded the serializable state).

   On any error just reset the state back to zero.

   @param prevState the state to load
   */
  void loadState(Serializable prevState);
}
