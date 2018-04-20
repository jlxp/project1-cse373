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
     * - syntax for cond is gr for greater than (>), sm for smaller than (<), eq for equal (=), nq for not equal (!=)
     * - e.g.) if(sm(x,0), -x, x) gives -x when x is smaller than 0, x if elsewhere. 
     *
     * Postcondition:
     *
     * - If 'cond' evaluates to any non-zero number, interpret the "body" AST node and ignore the
     *   "else" node completely.
     * - Otherwise, evaluate the "else" node.
     * - In either case, return the result of whatever AST node you ended up interpreting.
     */
    public static AstNode handleIf(Environment env, AstNode wrapper) {

        Interpreter interp = env.getInterpreter();
        
        AstNode cond = wrapper.getChildren().get(0);
        AstNode body = wrapper.getChildren().get(1);
        AstNode other = wrapper.getChildren().get(2);
        
        // cond does not need simplify syntax        
        cond = checkCond(env, cond.getChildren().get(0)); 
        
        if (cond.getNumericValue() != 0.0) {
            return interp.evaluate(env, body);
        } else { // when the number that condition is equal to 0
            return interp.evaluate(env, other);
        }
    }
    
    /*
     * return the node representing of comparing in given situation of smaller, greater, equal to, not equal to
     * non-zero means the comparing turns out to be true and zero means otherwise
     */
    private static AstNode checkCond(Environment env, AstNode cond) {
        Interpreter interp = env.getInterpreter();
        
        String condName = cond.getName();
        
        AstNode left = cond.getChildren().get(0); 
        IList<AstNode> leftList = new DoubleLinkedList<>();
        leftList.add(left);
        left = new AstNode("toDouble", leftList); 
        
        AstNode right = cond.getChildren().get(1); 
        IList<AstNode> rightList = new DoubleLinkedList<>();
        rightList.add(right);
        right = new AstNode("toDouble", rightList); 
        
        double condLeft = interp.evaluate(env, left).getNumericValue();
        double condRight = interp.evaluate(env, right).getNumericValue();
        
        if (condName.equals("gr")) {
            if (condLeft > condRight) {
                return new AstNode(condLeft - condRight);
            } else {
                return new AstNode(0);
            }            
        } else if (condName.equals("sm")) {
            if (condLeft < condRight) {
                return new AstNode(condRight - condLeft);
            } else {
                return new AstNode(0);
            }  
        } else if (condName.equals("eq")) {
            if (condLeft == condRight) {
                return new AstNode(1);
            } else {
                return new AstNode(0);
            }
        } else { // for not equal
            return new AstNode(condLeft - condRight);
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
        Interpreter interp = env.getInterpreter();
        int loopNum = (int) interp.evaluate(env, times).getNumericValue();
        if (loopNum < 1.0) {
            throw new EvaluationError("invalid repitition");
        }
        AstNode body = wrapper.getChildren().get(1);
        AstNode result = body; 
        String var = findVar(env, body.getChildren().get(0)).getName(); 
        
        for (int i = 0; i < loopNum; i++) {
            result = interp.evaluate(env, body);
            env.getVariables().put(var, result);
            System.out.println(result.getNumericValue());           
        }
        return interp.evaluate(env, result);
    }
    /*
     * the leftest variable is the main variable that is returned
     */
    private static AstNode findVar(Environment env, AstNode body) {
        if(body.isOperation()) {
            AstNode left = body.getChildren().get(0);
            AstNode right = body.getChildren().get(1);
            
            if(left.isVariable()) {
                return left; 
            } else if (right.isVariable()) {
                return right;
            } else if (left.isNumber() && right.isNumber()) {
                return null; // do nothing
            } else {
                left = findVar(env, left);
                right = findVar(env, right);
                
                return null; //fail to find
            }
        }
        return null;
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
        
        AstNode currentCond = checkCond(env, cond.getChildren().get(0));
        
        double lim = interp.evaluate(env, wrapper.getChildren().get(0)).getNumericValue();
        
        int times = 0; 
        AstNode result = body;
        while (currentCond.getNumericValue() != 0.0) {
            result = interp.evaluate(env, body); 
            if (times > (int) lim) {
                throw new EvaluationError("never ending loop");
            }
            currentCond = checkCond(env, cond.getChildren().get(0));
            times++;
        }
        return (AstNode) interp.evaluate(env, result);
    }
    
    
}
