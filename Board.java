import java.util.Scanner;


public class Board {
  static Scanner in = new Scanner(System.in);
  public char [] squares = new char[26]; // element zero not used
  int moveCount = 0;
  static final char freeChar = '_';  // to indicate the square is available.
  static int skipMoveCount = 0;
  public Board() {
    for (int i = 1; i <= 25; i++) squares[i] = freeChar;  // all nine squares are initially available
  }
  public boolean moveToSquare(int square) {
	  //System.out.println("moveToSquare() " + square);
    if (squares[square] != freeChar) return false; // already and X or O at that location
    squares[square] = xturn() ? 'X' : 'O'; 
    moveCount++;
    //System.out.println("Movecount: " + moveCount);
    return true;
  }
  boolean xturn() { return moveCount % 2 == 0;}  // X's turn always follows an even number of previous moves

  boolean isFreeSquare(int square) { 
	  //System.out.println("isFreeSquare(" + square + "): " + (squares[square] == freeChar));
	  return squares[square] == freeChar; 
	 }

  void unDo(int square){
    moveCount--;
    squares[square] = freeChar;
  }
  boolean boardFull() { return moveCount >= 25; }

  int lineValue(int s1, int s2, int s3, int s4) {
	  	int count = 0;
	    if (squares[s1] == 'X')
	    {
	    	count++;
	    	if(squares[s2] == 'X')
	    	{
	    		count++;
	    		if(squares[s3] == 'X')
	    		{
	        		count++;
	    			if(squares[s4] == 'X')
	    			{
	    	    		count++;
	    			}
		    		else if(squares[s4] == 'O')
			    	{
	    				//this win has been ruined, throw it out
		    			return 999;
			    	}
	    		}
	    		else if(squares[s3] == 'O')
		    	{
    				//this win has been ruined, throw it out
	    			return 999;
		    	}
	    	}
	    	else if(squares[s2] == 'O')
	    	{
				//this win has been ruined, throw it out
    			return 999;
	    	}
	    }
	    
	    if (squares[s1] == 'O')
	    	count--;
	    if (squares[s2] == 'O')
	    	count--;
	    if (squares[s3] == 'O')
	    	count--;
	    if (squares[s4] == 'O')
	    	count--;
	    
	    if (squares[s1] == 'O')
	    {
	    	if(squares[s2] == 'O')
	    	{
	    		if(squares[s3] == 'O')
	    		{
	    			if(squares[s4] == 'O')
	    			{
	    			}
	    			else if(squares[s4] == 'X')
			    	{
	    				//this win has been ruined, throw it out
		    			return 999;
			    	}
	    		}
	    		else if(squares[s3] == 'X')
		    	{
    				//this win has been ruined, throw it out
	    			return 999;
		    	}
	    	}
	    	else if(squares[s2] == 'X')
	    	{
				//this win has been ruined, throw it out
    			return 999;
	    	}
	    }
   // System.out.println("Win Line: {" + s1 + ", " + s2 + ", " + s3 + ", " + s4 + "}, value = " + count);
    return count;
  }
  
  int boardValue(char player) {
    int[][] wins = {
    		//Horizontal wins
    		{1,2,3,4},
    		{2,3,4,5},
    		{6,7,8,9},
    		{7,8,9,10},
    		{11,12,13,14},
    		{12,13,14,15},
    		{16,17,18,19},
    		{17,18,19,20},
    		{21,22,23,24},
    		{22,23,24,25},
    		
    		//Vertical wins
    		{1,6,11,16},
    		{6,11,16,21},
    		{2,7,12,17},
    		{7,12,17,22},
    		{3,8,13,18},
    		{8,13,18,23},
    		{4,9,14,19},
    		{9,14,19,24},
    		{5,10,15,20},
    		{10,15,20,25},

    		//Diagonal wins
    		{1,7,13,19},
    		{7,13,19,25},    		
    		{6,12,18,24},
    		{2,8,14,20},
    		{5,9,13,17},
    		{9,13,17,21},
    		{4,8,12,16},
    		{10,14,18,22}
    };
    int best = 0;
    for (int i = 0; i < wins.length; i++) {
    	int v = lineValue(wins[i][0], wins[i][1], wins[i][2], wins[i][3]);
    	if(v == 999)
    	{
    		//this win has been ruined, throw it out of the array
    		continue;
    	}
	     if(player == 'X')
	     {
	    	 if(v > best)
	    		 best = v;
	    	 if(v == 4) //a win for cpu, should be considered before trying to block a human win
	    	 {
	    		 return v;
	    	 }
	    	 if(v == -3) //a win for the human
	    	 {
	    		 return v; //block their win
	    	 }
	     }
	     if(player == 'O')
	     {
	    	 if(v < best)
	    		 best = v;
	    	 if(v == -4) //a win for human, should be considered before trying to block a cpu
	    	 {
	    		 return v;
	    	 }
	    	 if(v == 3) //a win for the cpu
	    	 {
	    		 return v; //block their win
	    	 }
	     }
    }
    //System.out.println("boardValue(): Nobody has one so far");
    return best; 
  }
  
 // draw the board
public void draw() {
  for (int i = 1; i < 26; i++){
    if (isFreeSquare(i)) System.out.print(i);
    else System.out.print(squares[i]);
    System.out.print(" ");
    if (i % 5 == 0) System.out.println();
  }
}

// get next move from the user.
public boolean userMove(char team) {
	
	if(moveCount < skipMoveCount) //use cpu logic to fill in some initial moves
	{
		return computerMove(team);
	}
  boolean legalMove;
  int s;
  System.out.print("\nPlayer " + team + ", Enter a square number: ");
  do {
	  s = in.nextInt();
      legalMove = squares[s] == freeChar;
      if (!legalMove) System.out.println("Try again: ");
  } while (!legalMove);
  Move m = new Move(s,evaluateMove(team, s));
  moveToSquare(s);
  System.out.println("Human move: " + m);
  this.draw();
  if(team == 'O')
	  if (this.boardValue(team) == -4) return true; // a winning move
  if(team == 'X')
	  if (this.boardValue(team) == 4) return true; // a winning move
  return false;
}

public boolean computerMove(char team) {
	System.out.println("computerMove()");
  try {Thread.sleep(600);} catch (InterruptedException e) {}
  Move m = this.bestMove(team);
  moveToSquare(m.square);
  System.out.println("\nComputer move: " + m);;
  draw();
  if(team == 'X')
	  if (this.boardValue(team) == 4) return true; // a winning move
  if(team == 'O')
	  if (this.boardValue(team) == -4) return true; // a winning move
  return false;
}

// get a random number from min to max inclusive
static int rand(int min, int max) {
	return (int) (Math.random() * (max - min + 1) + min);
}


// randomize order of squares to look through
static void randomizeOrder(int[] squareList) {
	 // System.out.println("randomizeOrder()");
	for (int i = 1; i < 26; i++)
		squareList[i] = i;
	for (int i = 1; i < 26; i++) {
			int index1 = rand(1,25);
			int index2 = rand(1,25);
			int temp = squareList[index1];
			squareList[index1] = squareList[index2];
			squareList[index2] = temp;			
	}
}

/* 
 *  Return a Move object representing a best move for the current player.
 *  Use minimax strategy, meaning out of all possible moves, choose the one that is the worst for your opponent.
 *  Provisionally make a move, then recursively evaluate your opponent's possible responses. 
 *  Your best move is the one that "minimizes" the value of your opponent's best response.
*/
public Move bestMove(char player) {
	  //System.out.println("bestMove()");
	  Move bestSoFar = new Move();  // an impossibly "bad" move.
	  int [] squares = new int[26];
	  for (int i = 1; i < 26; i++)
		  squares[i] = i;
	  for (int i = 1; i < 25; i++)   // consider the possible moves in some random order
	  {
		    int s = squares[i];
		    if (isFreeSquare(s)) 
		    {
			      Move m = new Move(squares[i], evaluateMove(player, s)); 
			      //System.out.println("current move: " + m.toString());
			      if (m.betterThan(bestSoFar))  bestSoFar = m;
			      if(player == 'X' && m.value == 4)
			      {
			    	  bestSoFar = m;
			    	  break;
			      }
			      if(player == 'O' && m.value == -4)
			      {
			    	  bestSoFar = m;
			    	  break;
			      }
			}
	  }
	  //System.out.println("final best move: " + bestSoFar.toString());
	  return bestSoFar;
}


public int evaluateMove(char player, int square) {
	//System.out.println("evaluateMove(" + square + ")");
    moveToSquare(square);
    
    int val = boardValue(player); // if this is != 0 then it's a winning move
  
    unDo(square);
    //System.out.println("evaluateMove() done: " + val);
    return val;

    /*
     * The numerical value of my move is equal to the value of opponent's best response. 
     * For example, suppose I'm X and I want to evaluate a move to a certain square.
     * We determine that O's best response (to some other square) has value -1. 
     * That's a good number for O. (In fact, it means a win for O) but a bad number for me.  
     * When comparing moves, we prefer small numbers for O and big numbers for X.
     * The Move.betterThan() method makes this determination.
     */
}

  // Move is an inner class and allows us to wrap a square and a value together. 
  // It's an inner class so we have access to the xturn() method of Board.
  class Move {
    int square, value;
    public Move(int square, int value) {
      this.square = square;
      this.value = value;
    }
    public Move() {
      this(0, Board.this.xturn() ? 0 : 0);  // give this impossible move an impossibly bad value
    }
  
    boolean betterThan(Move m) {
      if (Board.this.xturn()) return this.value > m.value;
      else return this.value < m.value;
    }
    public String toString() {return "[ square=" + square + ", value=" + value + " ]";
    }
  }
  
  
public static void main(String [] args) {
	
  int random = rand(0,4);
  if(random == 0) skipMoveCount = 0;
  if(random == 1) skipMoveCount = 2;
  if(random == 2) skipMoveCount = 4;
  if(random == 3) skipMoveCount = 6;

  Board b = new Board();
  b.draw();
  char cpu = 'X';
  char player = 'O';
  if (Math.random() < 0.5) 
  {
	  cpu = 'O';
	  player = 'X';
  }
  if(cpu == 'X')
  {
	  b.computerMove(cpu);
	  
	  while (!b.boardFull()) {
	    if (b.userMove(player)) {
	      System.out.println("Congratulations! You win!");
	      return;
	    }
	    if (!b.boardFull() && b.computerMove(cpu)) {
	      System.out.println("Computer wins this one.");
	      return;
	    }
	  }
  }
  else
  {
	  b.userMove(player);
  
	  while (!b.boardFull()) {
	    if (!b.boardFull() && b.computerMove(cpu)) {
	      System.out.println("Computer wins this one.");
	      return;
	    }
	    if (b.userMove(player)) {
		      System.out.println("Congratulations! You win!");
		      return;
		    }
	  }
  }
  System.out.println("Tie!");
}
}
