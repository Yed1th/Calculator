package hesapmakinesi;

/**
 *
 * @author Arda Atakan Uçan
 * @since 24.01.2021
 */
public class Calculator {
    String entry;     // It will be used to showing on the mainScreen.
    String textOp;    // It will be used to showing on the operationScreen.
    
    boolean afterFourOp;    // It becomes true after one of four op is selected.
    boolean afterEquals;    // It becomes true after equals op. is selected.
    
    Double result;         // it overides the results.
    Double number;         // last entered number.
    
    static enum OperationType { 
    // operation types
        Plus,
        Minus,
        Multiply,
        Division,
        Equals
    }
    
    OperationType operation = null;      // entered op type
    
    
    public Calculator() {
        entry = "0";
        textOp = "";
        result = null;
        number = null;
        afterEquals = false;
        afterFourOp = false;
    }
    
    public void enterNumber(char entry) {
        if (afterEquals) {
            this.clear();
        }
        
        else if (afterFourOp) {
            afterFourOp = false;
            this.entry = ""; 
        }
        
        else if (this.entry.equals("0")) {
            if (entry == '0')
                return;          
            this.entry = "";
        }
        
        this.entry += entry;
    }
    
    public void enterPoint() {
        if(!entry.contains("."))
            entry += '.';      
    }
    
    public void clearEntry() {
        entry = "0";
    }
    
    public void setSign() {
        if(entry.charAt(0) == '-')                  // if input is negative
            this.entry = entry.replace("-", "");        // now it is positive
        
        else if (!entry.equals("0"))    // if input is non-zero
            entry = "-" + entry;            // now it is negative
    }
    
    public void backspace() {
        if(entry.length() == 1)
            entry = "0";
        else if(entry.length() == 2 && entry.contains("-"))
            entry = "0";
        else
            entry = entry.substring(0, entry.length()-1);
    }
    
    public void enterOperation(OperationType newOp) {
        // if user enters a operation after the another one, previous op will be
        // replaced with the new one.
        if (afterFourOp) {
            textOp = this.textOp.substring(0, this.textOp.length()-3);
            record(newOp);
            operation = newOp;
            return;
        }
        if (afterEquals) {
            afterEquals = false;
            afterFourOp = true;
            
            this.operation = newOp;
            textOp = "" + fixNumber(result);
            record(newOp);
     
            return;
        }

        number = Double.parseDouble(entry);   //number is always last entry.
        
        // is this first step of calculation?
        if (result == null) {
            result = Double.parseDouble(entry);    // result will start with first entered number.
            this.operation = newOp;  //new operation will be executed next step.
            
            textOp += entry;
            record(newOp);
        }  
        else {
            textOp += entry;
            record(newOp);

            process(operation);     //calculation executes (previous operation).
            
            this.operation = newOp; // new operation will be executed next step.
        }
        
        this.afterFourOp = true;    
    }
    
    public void enterEquals() {
        if(!afterEquals) 
            number = Double.parseDouble(entry);
        
        if (this.operation != null) {
            textOp += entry;
            record(OperationType.Equals);
            
            process(this.operation);
        }
        else
            return;
        
        this.afterEquals = true;    // it will be change to true only here.
    }
    
    private void record(OperationType opType){
        switch (opType) {
            case Plus: textOp += " + "; break;
            case Minus: textOp += " - "; break;
            case Multiply: textOp += " * "; break;
            case Division: textOp += " / "; break;
            case Equals: textOp += " = "; break;
        }
    }
    
    private void process(OperationType opType){
        switch (opType) {
            case Plus: result += number; break;
            case Minus: result -= number; break;
            case Multiply: result *= number; break;
            case Division: result /= number; break;
        }
        entry = fixNumber(result);
    }
    
    public void clear() {
        entry = "";
        textOp = "";
        result = null;
        operation = null;
        afterEquals = false;
        afterFourOp = false;
    }
    
    // eğer noktadan sonra 0 dışında bir sayı yoksa noktadan sonrasını atar.
    public String fixNumber(double number) {
        if (Math.floor(number) == number)
            return "" + (int) number;
        else
            return "" + number;
    }
}
