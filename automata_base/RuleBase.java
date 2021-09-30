package automata_base;

import java.util.HashMap;

public class RuleBase {
    //alternatively a static init block could be used.


    public static final HashMap<String, CellularAutomata.RulesInterface> RULES_BASE = new HashMap<String, CellularAutomata.RulesInterface>(){{
        put("Game of Live", CellularAutomata.GAMEOFLIVE);
        put("Replicator", CellularAutomata.REPLICATOR);
        for(int i=0; i<=255; i++){
            put("Rule "+i, oneDrules(i));
        }
    }
    };

    public static final CellularAutomata.RulesInterface oneDrules(int rule){
        return (surroundings)->{
            //if this cell is a 1: dont change it...
            if(surroundings[1][1] == 1){
                return 1;
            }
            int[] upper_row = new int[3];
            for (int x=0; x<3; x++){
                upper_row[x] = surroundings[0][x];
            }
            
            /*
            boolean zero_check = java.util.Arrays.equals(new int[]{0, 0, 0}, upper_row);
            if (!zero_check){
                System.out.println("found pattern: "+upper_row[0]+""+upper_row[1]+""+upper_row[2]);
            }*/
            int index = (upper_row[0]<<2) | (upper_row[1]<<1) | (upper_row[2]);
            int evaluated_rule = rule & (1<<index);
            if (evaluated_rule != 0){
                return 1;
            }
            return 0;
        };
    }
}
