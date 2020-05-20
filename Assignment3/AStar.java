import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AStar {
    private final int[][] Valid = {{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
    //private final int[][] Valid = {{1,0},{0,1},{-1,0},{0,-1}};
    
    private class Site{
        char val;
        int x,y;
        double h,c;
        Site prev;
        public Site(char val, int x, int y){
            this.val = val;
            this.x = x; this.y = y;
            if (val == 'G'){ goal = this; }
            else if (val == 'S') { start = this; frontier.add(this); }
        }
        public void calc_distance() { h = !"X+-|".contains(val+"")? Math.sqrt(Math.pow(x-goal.x, 2) + Math.pow(y-goal.y, 2)) : -1; }
    }

    Site[][] map;
    Site goal, start, pos;
    ArrayList<Site> frontier, path;
    public AStar(ArrayList<String> _map) {
        frontier = new ArrayList<Site>();
        this.map = new Site[_map.size()][_map.get(0).length()];
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) { this.map[i][j] = new Site(_map.get(i).charAt(j), i, j); }
        }
        printMap();
        for (Site[] row : map) { for (Site cell : row) { cell.calc_distance(); } }
        expand();
        while (pos != goal) { expand(); }
        while (pos.prev != start) { pos = pos.prev; pos.val = '.'; }
        printMap();
    }

    public void expand() {
        pos = frontier.get(0);
        for (Site site : frontier) { if (site.h + site.c < pos.h + pos.c) { pos = site; }}
        if (pos == goal) { return; }
        Site temp;
        double cost;
        for (int[] coord : Valid) {
            temp = map[pos.x + coord[0]][pos.y + coord[1]];
            if(temp.h < 0){ continue; }
            cost = coord[0] == 0 || coord[1] == 0? 1 : Math.sqrt(2);
            if (temp.prev == null) {
                temp.prev = pos; temp.c = pos.c+cost;
                frontier.add(temp);
            }
            else if (pos.c+cost < temp.c) { temp.prev = pos; temp.c = pos.c+cost; }
        }
        frontier.remove(pos);
    }

    public void printMap() {
        for (Site[] row : map) {
            for (Site cell : row) { System.out.print(cell.val); }
            System.out.println();
        }
        //System.out.printf("Start: (%d,%d)\t Goal: (%d,%d)\n", start.x, start.y, goal.x, goal.y);
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0){
            System.out.println("Usage: java AStar <filepath>");
            args = new String[]{"map1.txt"};
            //return;
        }
        Scanner scanner = new Scanner(new File((args[0])));
        ArrayList<String> map = new ArrayList<String>();
        while (scanner.hasNext()) { map.add(scanner.nextLine()); }
        scanner.close();
        AStar aStar = new AStar(map);
    }
}
