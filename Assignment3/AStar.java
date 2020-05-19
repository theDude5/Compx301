import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AStar {
    private class Site{
        char val;
        int x, y, h, c=0;
        Site prev;
        boolean visited;
        public Site(char val, int x, int y){
            this.val = val;
            this.x = x; this.y = y;
            if (val == 'G'){ goal = this; }
            else if (val == 'S') { start = this; frontier.add(this); }
        }
        public void calc_distance() { h = !"X+-|".contains(val+"")? (int) Math.pow(x-goal.x, 2)+ (int) Math.pow(y-goal.y, 2) : -1; }
    }

    Site[][] map;
    Site goal, start, pos;
    ArrayList<Site> frontier, path;
    public AStar(ArrayList<String> _map) {
        frontier = new ArrayList<Site>();
        this.map = new Site[_map.size()][_map.get(0).length()];
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) { 
                this.map[i][j] = new Site(_map.get(i).charAt(j), i, j); 
            }
        }
        printMap();
        for (Site[] row : map) {
            for (Site cell : row) { 
                cell.calc_distance(); 
                //System.out.printf("%3d",cell.h);
            }
            //System.out.println();
        }
        expand();
        while (pos != goal) { expand(); }
    }

    public void expand() {
        Site temp;
        pos = frontier.get(0);
        for (Site site : frontier) { if (site.h < pos.h) { pos = site; } }
        if (pos == goal) { return; }
        for (int[] coord : new int[][] { {1,0}, {-1,0}, {0,1}, {0,-1}, {-1,-1}, {1,1}, {1,-1},{-1,1}}) {
            temp = map[pos.x + coord[0]][pos.y + coord[1]];
            if (!temp.visited && temp.h > -1) {
                frontier.add(temp);
                temp.visited= true;
                if (temp != start && temp != goal) { temp.val = '.'; }
            }
        }
        //if(pos != start && pos != goal) { pos.val = '\0'; }
        frontier.remove(pos);
        //printMap();
    }

    public void printMap() {
        for (Site[] row : map) {
            for (Site cell : row) { System.out.print(cell.val); }
            System.out.println();
        }
        System.out.printf("Start: (%d,%d)\t Goal: (%d,%d)\n", start.x, start.y, goal.x, goal.y);
    }
    
    public static void main(String[] args) throws FileNotFoundException{
        if (args.length == 0){
            System.out.println("Usage: java AStar <filepath>");
            args = new String[]{"map2.txt"};
            //return;
        }
        Scanner scanner = new Scanner(new File((args[0])));
        ArrayList<String> map = new ArrayList<String>();
        while (scanner.hasNext()) { map.add(scanner.nextLine()); }
        scanner.close();
        AStar aStar = new AStar(map);
        aStar.printMap();
    }
}
