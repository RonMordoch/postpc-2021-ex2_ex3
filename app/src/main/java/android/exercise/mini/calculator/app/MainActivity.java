package android.exercise.mini.calculator.app;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final String calcBundleKey = "calculatorState";

    @VisibleForTesting
    public SimpleCalculator calculator;

    private View buttonEquals, buttonPlus, buttonMinus, buttonClear, buttonBackSpace;
    private final List<View> buttons = new ArrayList<>();
    private TextView textViewCalculatorOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (calculator == null) {
            calculator = new SimpleCalculatorImpl();
        }

        // find all views
        textViewCalculatorOutput = findViewById(R.id.textViewCalculatorOutput);
        buttonBackSpace = findViewById(R.id.buttonBackSpace);
        buttonEquals = findViewById(R.id.buttonEquals);
        buttonPlus = findViewById(R.id.buttonPlus);
        buttonMinus = findViewById(R.id.buttonMinus);
        buttonClear = findViewById(R.id.buttonClear);
        // add digit buttons to buttons array
        buttons.add(findViewById(R.id.button0));
        buttons.add(findViewById(R.id.button1));
        buttons.add(findViewById(R.id.button2));
        buttons.add(findViewById(R.id.button3));
        buttons.add(findViewById(R.id.button4));
        buttons.add(findViewById(R.id.button5));
        buttons.add(findViewById(R.id.button6));
        buttons.add(findViewById(R.id.button7));
        buttons.add(findViewById(R.id.button8));
        buttons.add(findViewById(R.id.button9));


        // initially update main text-view based on calculator's output
        textViewCalculatorOutput.setText(calculator.output());

        // set click listeners on all buttons to operate on the calculator and refresh main text-view
        buttonBackSpace.setOnClickListener(v -> {
            calculator.deleteLast();
            textViewCalculatorOutput.setText(calculator.output());
        });
        buttonEquals.setOnClickListener(v -> {
            calculator.insertEquals();
            textViewCalculatorOutput.setText(calculator.output());
        });
        buttonPlus.setOnClickListener(v -> {
            calculator.insertPlus();
            textViewCalculatorOutput.setText(calculator.output());
        });
        buttonMinus.setOnClickListener(v -> {
            calculator.insertMinus();
            textViewCalculatorOutput.setText(calculator.output());
        });
        buttonClear.setOnClickListener(v -> {
            calculator.clear();
            textViewCalculatorOutput.setText(calculator.output());
        });

        for (int i = 0; i < 10; i++) {
            setDigitButtonClickListener(buttons.get(i), i);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // save calculator state into the bundle
        outState.putSerializable(calcBundleKey, calculator.saveState());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // restore calculator state from the bundle, refresh main text-view from calculator's output
        calculator.loadState(savedInstanceState.getSerializable(calcBundleKey));
        textViewCalculatorOutput.setText(calculator.output());
    }

    /**
     * Associates the given view ( button ) With the digit to insert to calculator)
     */
    private void setDigitButtonClickListener(View view, int digit) {
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                calculator.insertDigit(digit);
                textViewCalculatorOutput.setText(calculator.output());
            }
        });
    }
}