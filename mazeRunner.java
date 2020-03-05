import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;



public class mazeRunner {
	
	public static void main(String[] args){
		int N = 5;
		double p = 0.2;
		System.out.println("Number separated by ,(comma).");
		System.out.println("Example: 0,0");
		System.out.println("===========================");
		System.out.println("Give the starting location.");
		Location start = scanLocation(N);
		System.out.println("===========================");
		Location[] end = new Location[2];
		System.out.println("===========================");
		System.out.println("Give 1st E-Cell's location.");
		end[0] = scanLocation(N);
		System.out.println("===========================");
		System.out.println("Give 2nd E-Cell's location.");
		end[1] = scanLocation(N);
		System.out.println("===========================");
		
		Field field1 =  new Field(N, p, start,end);
		
		field1.printField();
		field1.doUniformCostSearch();
		System.out.println("\nUniform Cost Search: ");
		field1.printField();
		field1.printCounter();
		field1.doAStarSearch();
		System.out.println("\nA* search:");
		field1.printField();
		field1.printCounter();
		System.out.println("==============");
		System.out.println("Start:(S) ");
		System.out.println("End:{E} ");
		System.out.println("Path:[+]");
		System.out.println("Obsatcles:[#]");
		System.out.println("==============");
		
	}
	//============================================================================	
	private static Location scanLocation(int N) {
		Scanner sss = new Scanner(System.in);
		while (true) {
			System.out.print("Numbers between:{0.."+(N-1)+"},{0.."+(N-1)+"}: \n");
			System.out.print("Your input: ");
			String input = sss.nextLine();
			try{
				String[] xy = input.split(",");
				int x = Integer.parseInt(xy[0]);
				int y = Integer.parseInt(xy[1]);
				return new Location(x,y);
			} catch(Exception e) {
				System.out.println("WRONG INPUT!");
				System.out.println("TRY AGAIN!!!");
			}
		}
	}
}
//============================================================================
class Location {
	int x,y;
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
//============================================================================
class Cell {
	int H_value, G_value;
	boolean ending, obastacle, path;
	Location parent;
	Cell() {
		this.ending = false;
		this.path = false;
		this. obastacle = false;
	}
	public void setEnd() {
		this.ending = true;
	}
	
	public boolean isEnd() {
		return ending;
	}
	
	public void setBrick() {
		this.obastacle = true;
	}
	
	public boolean isObastacle() {
		return obastacle;
	}
	
	public void setPath() {
		path = true;
	}
	
	public boolean isPath() {
		return path;
	}
	
	public void setH(int H) {
		H_value = H;
	}
	
	public int getH() {
		return H_value;
	}
	
	public void setG(int G) {
		G_value = G;
	}
	
	public int getG() {
		return G_value;
	}
	
	public int getF() {
		return G_value + H_value;
	}
	
	public void setParent(Location parent) {
		this.parent = parent;
	}
	
	public Location getParent() {
		return parent;
	}
	
	public void resetPath1() {
		this.path = false;
	}
	
}
//============================================================================
class Field {
	int N;
	int  counter;
	Cell[][] cell;
	Location start;
	Location[] end;
	
	public Field(int N, double p, Location start, Location[] end) {
		this.N = N;
		this.start = start;	
		this.cell = new Cell[N][N];
		this.end = end;
		Random rand = new Random();
		//here i put  the blocks
		for (int i=0;i<N;i++) {
			for (int j=0;j<N;j++) {
				cell[i][j] = new Cell();
			}
		}
		//here i put the destinations(end)
		for (Location loc : end) {
			cell[loc.getX()][loc.getY()].setEnd();
		}
		//here i put the obstacles 
		for (int i=0;i<N;i++) 
		{
			for (int j=0;j<N;j++) 
			{
				if ((!(i==start.getX()&&j==start.getY()))&&(!cell[i][j].isEnd()))  
				{
					int temp = rand.nextInt(101);
					if (temp<p*100) 
					{
						cell[i][j].setBrick();
					}
				}
			}
		}
		///////////
		cell[start.getX()][start.getY()].setG(0);
	}
	//============================================================================
	public void printField() 
	{
		int cost=0;
		for (int i=0;i<N;i++) 
		{
			for (int j=0;j<N;j++) 
			{
				if (cell[i][j].isObastacle()) 
				{					
					System.out.print("[#]");
				}
				else if (cell[i][j].isEnd())
				{
					System.out.print("{E}");
				}
				else if (cell[i][j].isPath()) 
				{				
					System.out.print("[+]");
					cost++;
				}
				else if	(start.getX()==i&&start.getY()==j)	
					System.out.print("(S)");
				else 										
					System.out.print("[ ]");
			}
			System.out.println();
		}
		System.out.println();
		if(cost>=1) { 
			
			System.out.println("COST:"+ (cost+1));
			
		}
		else 
		{
			System.out.println("COST:0");
		}
		
	}
	//===========================================================================
	public boolean doUniformCostSearch() {
		this.counter=0;
		setupGValue();
		resetPath();
		Location fin = uniformCostSearch();
		return findPath(fin);
	}
	
	
	private Location uniformCostSearch() {
		ArrayList<Location> open = new ArrayList<Location>();
		ArrayList<Location> closed = new ArrayList<Location>();
		Location current = start;
		open.add(start);
		while (true) {
			if (open.isEmpty())	return current;
			current = open.get(0);
			int currentG = cell[open.get(0).getX()][open.get(0).getY()].getG();
			int currentIndex = 0;
			for (int k=0; k<open.size();k++) {
				if (cell[open.get(k).getX()][open.get(k).getY()].getG()< currentG) {
					currentG = cell[open.get(k).getX()][open.get(k).getY()].getG();
					current = open.get(k);
					currentIndex = k;
				}
			}
			closed.add(open.get(currentIndex));
			open.remove(currentIndex);
			counter++;
				
			if (cell[current.getX()][current.getY()].isEnd()) {
				return current;
			}
			
			Location[] neighbours = findNeighbours(current);
			for (Location neighbour : neighbours) {
				if (isValidLocation(neighbour)&&!closed.contains(neighbour)) 
				{
					int newG = cell[current.getX()][current.getY()].getG()+Math.abs(neighbour
							.getX()-current.getX())+Math.abs(neighbour.getY()-current.getY());
					
					if (!open.contains(neighbour)&&cell[neighbour.getX()][neighbour.getY()]
							.getG()>newG)
					{
						
							cell[neighbour.getX()][neighbour.getY()].setG(newG);
							cell[neighbour.getX()][neighbour.getY()].setParent(current);
					if (!open.contains(neighbour)) 
					{	
						open.add(neighbour);
					}
					}
				}
			}
		}
	}
	//=========================================================================================
	public boolean doAStarSearch() {
		this.counter=0;
		setupGValue();
		setupHValue(end);
		resetPath();
		Location fin = aStarSearch();
		return findPath(fin);
	}
	//============================================================================
	public Location aStarSearch() {
		ArrayList<Location> open = new ArrayList<Location>();
		ArrayList<Location> closed = new ArrayList<Location>();
		Location current = start;
		open.add(start);
		while (true) {
			if (open.isEmpty()) return current;
			current = open.get(0);
			int currentF = cell[open.get(0).getX()][open.get(0).getY()].getF();
			int currentIndex = 0;
			for (int k=0; k<open.size();k++) {
				if (cell[open.get(k).getX()][open.get(k).getY()].getF()< currentF) {
					currentF = cell[open.get(k).getX()][open.get(k).getY()].getF();
					current = open.get(k);
					currentIndex = k;
				}
			}
			
			closed.add(open.get(currentIndex));
			open.remove(currentIndex);
			counter++;
				
			if (cell[current.getX()][current.getY()].isEnd()) {
				return current;
			}
			
			Location[] neighbours = findNeighbours(current);
			for (Location neighbour : neighbours) {
				if (isValidLocation(neighbour)&&!closed.contains(neighbour)) {
					int newG = cell[current.getX()][current.getY()].getG()+Math.abs(neighbour.
							getX()-current.getX())+Math.abs(neighbour.getY()-current.getY());
					if (!open.contains(neighbour)&&cell[neighbour.getX()][neighbour.getY()].getG()>newG) {
							cell[neighbour.getX()][neighbour.getY()].setG(newG);
							cell[neighbour.getX()][neighbour.getY()].setParent(current);
					if (!open.contains(neighbour))	open.add(neighbour);
					}
				}
			}
		}
	}
	//========================================================================
	public void setupGValue() {
		for (int i=0;i<N;i++) {
			for (int j=0;j<N;j++) {
				cell[i][j].setG(2147483647);
			}
		}
	}
	//============================================================
	public void setupHValue(Location[] ends)
	{
		int dx,dy;
		for (int i=0;i<N;i++) 
		{
			for (int j=0;j<N;j++) 
			{
				if (!cell[i][j].isObastacle())
				{
					dx=Math.abs(i-ends[0].getX());
					dy=Math.abs(j-ends[0].getY());
					cell[i][j].setH(dy+dx-Math.min(dx,dy));
				}
			}
		}
		
		for (int i=0;i<N;i++) 
		{
			for (int j=0;j<N;j++) 
			{
				if (!cell[i][j].isObastacle()) 
				{
					dx=Math.abs(i-ends[1].getX());
					dy=Math.abs(j-ends[1].getY());
					int currentH = dy + dx - Math.min(dx,dy);
					if (cell[i][j].getH() > currentH) 
					{
						cell[i][j].setH(currentH);
					}
				}
			}
		}
	}
	//====================================================================
	private void resetPath() {
		for (int i=0;i<N;i++) {
			for (int j=0;j<N;j++) {
				cell[i][j].resetPath1();
			}
		}
	}
	//===========================================================================
	
	//======================================================================
	private Location[] findNeighbours(Location current) {
		int x = current.getX();
		int y = current.getY();
		Location[] locations = {new Location(x,y+1),new Location(x,y-1)
				, new Location(x+1,y), new Location(x-1,y),
				new Location(x-1,y-1),new Location(x+1,y+1)
				};
		return locations;
	}
	//==============================================================================================================================
	private boolean isValidLocation(Location loc) {
		if (loc.getX()>=N || loc.getX()<0 || loc.getY()>=N || loc.getY()<0) 
		{
			return false;
		}
		if (cell[loc.getX()][loc.getY()].isObastacle()) 
		{
			return false;
		}
		return true;
	}
	//==================================================================
	public boolean findPath(Location fin) 
	{
		Location current = fin;
		boolean reachedEnd = cell[fin.getX()][fin.getY()].isEnd();
		if (!reachedEnd) return false;
		while (start!=current) 
		{
			cell[current.getX()][current.getY()].setPath();
			current = cell[current.getX()][current.getY()].getParent();
		}
		return true;
	}
	public void printCounter() 
	{
		System.out.println("Blocks extended: "+ counter);
	}
}	
	//============================================================================
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


