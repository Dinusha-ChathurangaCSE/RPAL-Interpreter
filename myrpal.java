import Engine.Evaluvator;

public class myrpal {
    
    public static void main(String[] args) {  
        String fn;
        boolean isPrintAST=false,isPrintST=false; 
        if(args.length==0){
            fn = "input.txt"; 
            isPrintAST = true;
            isPrintST = true;
           // System.out.println(Evaluvator.evaluvate(fn,isPrintAST,isPrintST));             

        }
  
        else if(args.length==2){
            fn=args[1];
            if(args[0].equalsIgnoreCase("-ast")){
                isPrintAST=true;
                Evaluvator.evaluvate(fn,isPrintAST,isPrintST); 
                return;
            }
            else if(args[0].equalsIgnoreCase("-st")){
                isPrintST=true;
                Evaluvator.evaluvate(fn,isPrintAST,isPrintST);  
                return;

            }
            else{
                System.out.println("Invalid Arguments Passing!");
                return;
            }
        }
        else if(args.length==1){
            fn = args[0];
        }
        else{
            System.out.println("Invalid Arguments Passing!");
            return;     
        }

        System.out.println(Evaluvator.evaluvate(fn,isPrintAST,isPrintST));                                  

                                                                 
    }
}
