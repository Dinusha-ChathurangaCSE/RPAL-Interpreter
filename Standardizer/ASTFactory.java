package Standardizer;


import java.util.ArrayList;

public class ASTFactory {
    
    public ASTFactory() {
        
    }
    
    public AST getAbstractSyntaxTree(ArrayList<String> data) {
        Node root = NodeFactory.getNode(data.get(0), 0);
        Node previous_node = root;
        int current_depth = 0;
        
        for (String s: data.subList(1, data.size())) {
            int i = 0;                                                          // index
            int d = 0;                                                          // depth
            
            while (s.charAt(i) == '.') { 
                d++; 
                i++; 
            }            
            
            Node current_node = NodeFactory.getNode(s.substring(i), d); 
            
            if (current_depth < d) {
                previous_node.children.add(current_node);
                current_node.setParent(previous_node);               
            } else {
                while (previous_node.getDepth() != d) {
                    previous_node = previous_node.getParent();
                }
                previous_node.getParent().children.add(current_node);
                current_node.setParent(previous_node.getParent());
            }
            
            previous_node = current_node;
            current_depth = d;
        }        
        return new AST(root);
    }
}
