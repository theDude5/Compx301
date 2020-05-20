import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AStar {
    private class Site{
        char val;
        int x, y, h, c=0;
        Site prev;
        public Site(char val, int x, int y){
            this.val = val;
            this.x = x; this.y = y;
            if (val == 'G'){ goal = this; }
            else if (val == 'S') { start = this; frontier.add(this); }
        }
        public void calc_distance() { h = !"X+-|".contains(val+"")? (int) Math.sqrt(Math.pow(x-goal.x, 2) + Math.pow(y-goal.y, 2) ) : -1; }
        public int getf(){ return h+c; }
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
        for (Site[] row : map) {
            for (Site cell : row) { 
                cell.calc_distance(); 
                //System.out.printf("%3d",cell.h);
            }
            //System.out.println();
        }
        expand();
        while (pos != goal) { expand(); }
        while (pos.prev != start) { pos = pos.prev; pos.val = '.'; }
        printMap();
    }

    public void expand() {
        pos = frontier.get(0);
        Site temp;
        for (Site site : frontier) { if (site.getf() < pos.getf()) { pos = site; } }
        if (pos == goal) { return; }
        for (int[] coord : new int[][] {{1,0},{0,1},{-1,0},{0,-1}}) {
            temp = map[pos.x + coord[0]][pos.y + coord[1]];
            if (temp.h < 0) { continue;}
            else if (temp.prev == null) {
                temp.prev = pos; temp.c = pos.c+1; 
                frontier.add(temp); 
            }
            else if (pos.c+1 < temp.c) { temp.prev = pos; temp.c = pos.c+1; }
        }
        frontier.remove(pos);
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
            args = new String[]{"map3.txt"};
            //return;
        }
        Scanner scanner = new Scanner(new File((args[0])));
        ArrayList<String> map = new ArrayList<String>();
        while (scanner.hasNext()) { map.add(scanner.nextLine()); }
        scanner.close();
        AStar aStar = new AStar(map);
        //aStar.printMap();
    }
}
