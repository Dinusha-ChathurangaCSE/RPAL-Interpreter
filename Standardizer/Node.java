package Standardizer;

import java.util.ArrayList;

public class Node {
	//Not like other nodes in parser
	//This node is uded for Standardizing purposes
	
    private String data;
    private int depth;
    private Node parent;
    public ArrayList<Node> children;    
    public boolean isStandardized = false;
    
    public Node() {
        
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    public String getData() {
        return this.data;
    }
    
    public int getDegree() {
        return children.size();
    }
    
    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    public int getDepth() {
        return this.depth;
    }
    
    public void setParent(Node parent) {
        this.parent = parent;
    }
    
    public Node getParent() {
        return this.parent;
    }
    
    public void standardize() {
        if (!this.isStandardized) {
            for (Node child: this.children) {
                child.standardize();
            }
            switch (this.getData()) {
                // let node standardize
                case "let":
                    Node temp1 = this.children.get(0).children.get(1);
                    temp1.setParent(this);
                    temp1.setDepth(this.depth+1);
                    Node temp2 = this.children.get(1);                    
                    temp2.setParent(this.children.get(0));
                    temp2.setDepth(this.depth+2);
                    this.children.set(1, temp1);
                    this.children.get(0).setData("lambda");
                    this.children.get(0).children.set(1, temp2);
                    this.setData("gamma");
                    break;
                // where node standardize
                case "where":
                    Node temp = this.children.get(0);
                    this.children.set(0, this.children.get(1));
                    this.children.set(1, temp);
                    this.setData("let");
                    this.standardize();
                    break;
                // function_form node standardize
                case "function_form":
                    Node Ex = this.children.get(this.children.size()-1);                    
                    Node currentLambda = NodeFactory.getNode("lambda", this.depth+1, this, new ArrayList<Node>(), true);
                    this.children.add(1, currentLambda);
                    while (!this.children.get(2).equals(Ex)) {
                        Node V = this.children.get(2);
                        this.children.remove(2);
                        V.setDepth(currentLambda.depth+1);
                        V.setParent(currentLambda);
                        currentLambda.children.add(V);
                        if (this.children.size() > 3) {
                            currentLambda = NodeFactory.getNode("lambda", currentLambda.depth+1, currentLambda, new ArrayList<Node>(), true);
                            currentLambda.getParent().children.add(currentLambda);
                        }
                    }
                    currentLambda.children.add(Ex);
                    this.children.remove(2);                    
                    this.setData("=");
                    break;
                // lambda node standardize
                case "lambda":
                    if (this.children.size() > 2) {
                        Node Ey = this.children.get(this.children.size()-1);
                        Node currentLambdax = NodeFactory.getNode("lambda", this.depth+1, this, new ArrayList<Node>(), true);
                        this.children.add(1, currentLambdax);
                        while (!this.children.get(2).equals(Ey)) {
                            Node V = this.children.get(2);
                            this.children.remove(2);
                            V.setDepth(currentLambdax.depth+1);
                            V.setParent(currentLambdax);
                            currentLambdax.children.add(V);
                            if (this.children.size() > 3) {
                                currentLambdax = NodeFactory.getNode("lambda", currentLambdax.depth+1, currentLambdax, new ArrayList<Node>(), true);
                                currentLambdax.getParent().children.add(currentLambdax);
                            }
                        }
                        currentLambdax.children.add(Ey);
                        this.children.remove(2);
                    }
                    break;
                // within node standardize
                case "within":
                    Node X1 = this.children.get(0).children.get(0);                    
                    Node X2 = this.children.get(1).children.get(0);
                    Node E1 = this.children.get(0).children.get(1);
                    Node E2 = this.children.get(1).children.get(1);
                    Node gamma = NodeFactory.getNode("gamma", this.depth+1, this, new ArrayList<Node>(), true);
                    Node lambda = NodeFactory.getNode("lambda", this.depth+2, gamma, new ArrayList<Node>(), true);                    
                    X1.setDepth(X1.depth+1);
                    X1.setParent(lambda);                    
                    X2.setDepth(X1.depth-1);
                    X2.setParent(this);                    
                    E1.setDepth(E1.depth);
                    E1.setParent(gamma);                    
                    E2.setDepth(E2.depth+1);
                    E2.setParent(lambda);                    
                    lambda.children.add(X1);
                    lambda.children.add(E2);
                    gamma.children.add(lambda);
                    gamma.children.add(E1);
                    this.children.clear();
                    this.children.add(X2);
                    this.children.add(gamma);
                    this.setData("=");
                    break;
                // @ node standardize
                case "@":
                    Node gamma1 = NodeFactory.getNode("gamma", this.depth+1, this, new ArrayList<Node>(), true);
                    Node e1 = this.children.get(0);
                    e1.setDepth(e1.getDepth()+1);
                    e1.setParent(gamma1);
                    Node n = this.children.get(1);
                    n.setDepth(n.getDepth()+1);
                    n.setParent(gamma1);
                    gamma1.children.add(n);
                    gamma1.children.add(e1);
                    this.children.remove(0);
                    this.children.remove(0);
                    this.children.add(0, gamma1);                    
                    this.setData("gamma");
                    break;
                // and (simultaneous definitions) node standardize
                case "and":
                    Node comma = NodeFactory.getNode(",", this.depth+1, this, new ArrayList<Node>(), true);
                    Node tau = NodeFactory.getNode("tau",this.depth+1, this, new ArrayList<Node>(), true);
                    for (Node equal: this.children) {
                        equal.children.get(0).setParent(comma);
                        equal.children.get(1).setParent(tau);
                        comma.children.add(equal.children.get(0));
                        tau.children.add(equal.children.get(1));
                    }
                    this.children.clear();
                    this.children.add(comma);
                    this.children.add(tau);
                    this.setData("=");
                    break;                
                // rec node standardize
                case "rec":
                    Node X = this.children.get(0).children.get(0);
                    Node E = this.children.get(0).children.get(1);
                    Node F = NodeFactory.getNode(X.getData(), this.depth+1, this, X.children, true);
                    Node G = NodeFactory.getNode("gamma", this.depth+1, this, new ArrayList<Node>(), true);
                    Node Y = NodeFactory.getNode("<Y*>", this.depth+2, G, new ArrayList<Node>(), true);
                    Node L = NodeFactory.getNode("lambda", this.depth+2, G, new ArrayList<Node>(), true);                    
                    X.setDepth(L.depth+1);
                    X.setParent(L);
                    E.setDepth(L.depth+1);
                    E.setParent(L);
                    L.children.add(X);
                    L.children.add(E);
                    G.children.add(Y);
                    G.children.add(L);
                    this.children.clear();
                    this.children.add(F);
                    this.children.add(G);
                    this.setData("=");
                    break; 
                // unop, binop, tuples, conditionals and commas are not standardized due to the cse rules 6-13
                default:
                    break;                         
            }
        }
        this.isStandardized = true;
    }
}
