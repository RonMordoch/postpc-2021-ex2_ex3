package android.exercise.mini.calculator.app;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private static final String calcBundleKey = "calculatorState";

    @VisibleForTesting
    public SimpleCalculator calculator;

    private View buttonEquals, buttonPlus, buttonMinus, buttonClear, buttonBackSpace,
    button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    private TextView calculatorOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (calculator == null) {
            calculator = new SimpleCalculatorImpl();
        }

        // find all views
        calculatorOutput = findViewById(R.id.textViewCalculatorOutput);
        buttonBackSpace = findViewById(R.id.buttonBackSpace);
        buttonEquals = findViewById(R.id.buttonEquals);
        buttonPlus = findViewById(R.id.buttonPlus);
        buttonMinus = findViewById(R.id.buttonMinus);
        buttonClear = findViewById(R.id.buttonClear);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);

        // initially update main text-view based on calculator's output
        calculatorOutput.setText(calculator.output());

        // set click listeners on all buttons to operate on the calculator and refresh main text-view
        buttonBackSpace.setOnClickListener(v -> {
            calculator.deleteLast();
            calculatorOutput.setText(calculator.output());
        });
        buttonEquals.setOnClickListener(v -> {
            calculator.insertEquals();
            calculatorOutput.setText(calculator.output());
        });
        buttonPlus.setOnClickListener(v -> {
            calculator.insertPlus();
            calculatorOutput.setText(calculator.output());
        });
        buttonMinus.setOnClickListener(v -> {
            calculator.insertMinus();
            calculatorOutput.setText(calculator.output());
        });
        buttonClear.setOnClickListener(v -> {
            calculator.clear();
            calculatorOutput.setText(calculator.output());
        });
        button0.setOnClickListener(v -> {
            calculator.insertDigit(0);
            calculatorOutput.setText(calculator.output());
        });
        button1.setOnClickListener(v -> {
            calculator.insertDigit(1);
            calculatorOutput.setText(calculator.output());
        });
        button2.setOnClickListener(v -> {
            calculator.insertDigit(2);
            calculatorOutput.setText(calculator.output());
        });
        button3.setOnClickListener(v -> {
            calculator.insertDigit(3);
            calculatorOutput.setText(calculator.output());
        });
        button4.setOnClickListener(v -> {
            calculator.insertDigit(4);
            calculatorOutput.setText(calculator.output());
        });
        button5.setOnClickListener(v -> {
            calculator.insertDigit(5);
            calculatorOutput.setText(calculator.output());
        });
        button6.setOnClickListener(v -> {
            calculator.insertDigit(6);
            calculatorOutput.setText(calculator.output());
        });
        button7.setOnClickListener(v -> {
            calculator.insertDigit(7);
            calculatorOutput.setText(calculator.output());
        });
        button8.setOnClickListener(v -> {
            calculator.insertDigit(8);
            calculatorOutput.setText(calculator.output());
        });
        button9.setOnClickListener(v -> {
            calculator.insertDigit(9);
            calculatorOutput.setText(calculator.output());
        });
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
        calculatorOutput.setText(calculator.output());
    }
}