package automata_base;

public class CellularAutomata{
    int[][] grid;
    int width;
    int height;

    public static final  RulesInterface GAMEOFLIVE = (surroundings)->{
        int living_neighbor_count = 0;
        for (int y=0; y<3; y++){
            for (int x=0; x<3; x++){
                if (x==1 && y==1){
                    continue;
                }
                living_neighbor_count += surroundings[y][x];
            }
        }
        if (surroundings[1][1] == 1){
            if (living_neighbor_count < 2){
                return 0;
            }else if(living_neighbor_count < 4){
                return 1;
            }
        }else{
            //System.out.println("dead cell found + living_neighbor_count: "+ living_neighbor_count);
            if (living_neighbor_count == 3){
                //System.out.println("dead cell with 3 neighbors comes to live");
                return 1;
            }
        }
        return 0;
    };

    public static final RulesInterface REPLICATOR = (surroundings) -> {
        int living_neighbor_count = 0;
        for (int y=0; y<3; y++){
            for (int x=0; x<3; x++){
                if (x==1 && y==1){
                    continue;
                }
                living_neighbor_count += surroundings[y][x];
            }
        }
        return living_neighbor_count %2;
    };

    //creating lambdas: interfaces with only one function.
    public static interface RulesInterface{
        int rules(int[][] surroundings);
    }

    final public void set_int(int x, int y, int set){
        grid[y][x] = set;
    }

    public final int get_int(int x, int y){
        return grid[y][x];
    }

    

    public CellularAutomata(int widht, int height){
        grid = new int[height][widht];
        this.width = widht;
        this.height = height;
    }

    //applies rules to this automaton
    public int apply_rules(int outside, RulesInterface rules){
        int[][] surroundings = new int[3][3];
        int[][] newgrid = new int[height][width];
        int hamming_distance = 0;
        for (int y=0; y<height; y++){
            for(int x=0; x<width; x++){
                for (int yi=-1; yi<2; yi++){
                    for(int xi=-1; xi<2; xi++){
                        int local_x = xi+x;
                        int local_y = yi+y;
                        boolean out_of_bounds = local_x < 0 || local_x >= width || local_y < 0 || local_y >= height;
                        int current_cell = outside;
                        if(!out_of_bounds){
                            current_cell = this.grid[local_y][local_x];
                        }
                        surroundings[yi+1][xi+1] = current_cell;
                    }
                }
                int new_cell_state = rules.rules(surroundings);
                newgrid[y][x] = new_cell_state;
                if(newgrid[y][x] != new_cell_state){
                    hamming_distance += 1;
                }
            }
        }
        grid = newgrid;
        return hamming_distance;
    }

    public static void main(String[] args) {
        assert 5==3;
        CellularAutomata ca = new CellularAutomata(3, 3);
        ca.grid[2][0] = 1;
        ca.grid[2][1] = 1;
        ca.grid[2][2] = 1;
        ca.apply_rules(0, CellularAutomata.GAMEOFLIVE);
        assert ca.grid[1][1] == 0;
        System.out.println("a dead cell with 3 living neighbors comes to live: "+ ca.grid[1][1]);
        CellularAutomata ca2 = new CellularAutomata(3, 3);
        for (int y=0; y<3; y++){
            for(int x=0; x<3; x++){
                ca2.grid[y][x] = 1;
            }
        }
        ca2.apply_rules(0, CellularAutomata.GAMEOFLIVE);
        System.out.println("a living cell with more then 3 living neighbors dies: 0 = dead: "+ ca2.grid[1][1]);

        assert ca2.grid[1][1] == 1;
    }

}