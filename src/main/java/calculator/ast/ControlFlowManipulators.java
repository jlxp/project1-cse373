package calculator.ast;

import calculator.errors.EvaluationError;
import calculator.interpreter.Environment;
import calculator.interpreter.Interpreter;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;


/**
 * Note: this file is meant for the extra credit portion of this assignment
 * focused around adding a programming language to our calculator.
 *
 * If you choose to work on this extra credit, feel free to add additional
 * control flow handlers beyond the two listed here. Be sure to register
 * each new function inside the 'Calculator' class -- see line 59.
 */
public class ControlFlowManipulators {
    
    /**
     * Handles AST nodes corresponding to "randomlyPick(body1, body2)"
     *
     * Preconditions:
     *
     * - Receives an operation node with the name "randomlyPick" and two (arbitrary) children
     *
     * Postcondition:
     *
     * - This method will randomly decide to evaluate and return the result of either body1 or
     *   body2 with 50% probability. If body1 is interpreted, body2 is ignored completely and vice versa.
     */
    public static AstNode handleRandomlyPick(Environment env, AstNode wrapper) {
        AstNode body1 = wrapper.getChildren().get(0);
        AstNode body2 = wrapper.getChildren().get(1);

        Interpreter interp = env.getInterpreter();
        if (Math.random() < 0.5) {
            // Note: when implementing this method, we do NOT want to
            // manually recurse down either child: we instead want the calculator
            // to take back full control and evaluate whatever the body1 or body2
            // AST nodes might be. To do so, we use the 'Interpreter' object
            // available to us within the environment.
            return interp.evaluate(env, body1);
        } else {
            return interp.evaluate(env, body2);
        }
    }

    /**
     * Handles AST nodes corresponding to "if(cond, body, else)"
     *
     * Preconditions:
     *
     * - Receives an operation node with the name "if" and three children
     * - e.g.) if(cond, -x, x), return -x if cond is non-zero number, return x if cond is zero.
     *
     * Postcondition:
     *
     * - If 'cond' evaluates to any non-zero number, interpret the "body" AST node and ignore the
     *   "else" node completely.
     * - Otherwise, evaluate the "else" node.
     * - In either case, return the result of whatever AST node you ended up interpreting.
     */
    public static AstNode handleIf(Environment env, AstNode wrapper) {
        
        AstNode cond = wrapper.getChildren().get(0);
        AstNode body = wrapper.getChildren().get(1);
        AstNode other = wrapper.getChildren().get(2);
        Interpreter interp = env.getInterpreter();
        
        if (interp.evaluate(env,  cond).getNumericValue() != 0.0) {
            return interp.evaluate(env, body);
        } else { // when the number that condition is equal to 0
            return interp.evaluate(env,other);
        }
    }


    /**
     * Handles AST nodes corresponding to "repeat(times, body)"
     *
     * Preconditions:
     *
     * - Receives an operation node with the name "repeat" and two children
     * - The 'times' AST node is assumed to be some arbitrary AST node that,
     *   when interpreted, will also produce an integer result.
     *   -e.g) repeat(3, x + 3); when x is defined as 0 at first, it will return 9
     *
     * Postcondition:
     *
     * - Repeatedly evaluates the given body the specified number of times.
     * - Returns the result of interpreting 'body' for the final time.
     */
    public static AstNode handleRepeat(Environment env, AstNode wrapper) {        
        AstNode times = wrapper.getChildren().get(0);
        if (times.getNumericValue() < 1.0) {
            throw new EvaluationError("invalid repitition");
        }
        AstNode body = wrapper.getChildren().get(1);
        Interpreter interp = env.getInterpreter();
        
        AstNode result = body; 
        int loopNum = (int) interp.evaluate(env,times).getNumericValue();
        for (int i = 0; i < loopNum; i++) {
            result = interp.evaluate(env, body); 
        }
        return (AstNode) interp.evaluate(env, result);
    }
    
    /**
     * Handles AST nodes corresponding to while(cond, body, lim)
     * 
     * Precondition
     * 
     * - Receive an operation node with the name while and two children
     *   
     * Postcondition
     * 
     * - keep evaluating the given body until the given cond turns to be zero
     * - if the loop goes over number that is given by lim, it breaks and throw an Evaluation Error
     * - returns the result of interpreting body after the cond becomes false. 
     */
    public static AstNode handleWhile(Environment env, AstNode wrapper) {
        AstNode cond = wrapper.getChildren().get(0);
        AstNode body = wrapper.getChildren().get(1);
        Interpreter interp = env.getInterpreter();
        
        double lim = interp.evaluate(env, wrapper.getChildren().get(0)).getNumericValue();
        
        int times = 0; 
        AstNode result = body;
        while (interp.evaluate(env, cond).getNumericValue() != 0) {
            result = interp.evaluate(env, body); 
            if (times > (int) lim) {
                throw new EvaluationError("never ending loop");
            }
            times++;
        }
        return (AstNode) interp.evaluate(env, result);
    }
    
    
}
