package automata_base;

import java.util.HashMap;

public class RuleBase {
    public static final HashMap<String, CellularAutomata.RulesInterface> RULES_BASE = new HashMap<String, CellularAutomata.RulesInterface>(){{
        put("Game of Live", CellularAutomata.GAMEOFLIVE);
        put("Replicator", CellularAutomata.REPLICATOR);
    }
    };
}
