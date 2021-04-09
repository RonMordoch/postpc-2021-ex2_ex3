I pledge the highest level of ethical principles in support of academic excellence.
I ensure that all of my work reflects my own abilities and not those of someone else.


# Question:
Saying we want to add a cool feature - button "x" to run multiplication.
What code do we need to change/add/remove to support this feature?
Which tests can we run on the calculator? On the activity? On the app?

# Answer
First, we will need to add to our logic implementation a method called 'insertMult()', which inserts the
multiplication symbol into the calculator, and update our insertEquals implementation so we can support
evaluating multiplication between two numbers. We will also need to implement someway to instill an
order of operations between the '+','-' operators and the 'x' operator.
After that we will need to update our MainActivity code, add view for the button and set onClick listener to it.

As to testing, we can supply input which checks how the calculator responds to associative/commutative input,
such as '1+3x2', input which tests the validity of zero multiplication such as '0x5', '0x', '0x0', and more logical
validation tests.
To test the activity we can create a mock calculator and verify that the activity indeed called
the 'insertMult' method on the calculator instance.
To test the app we will run flow tests with the new method we impelemented for multiplication.