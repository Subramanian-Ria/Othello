--------------------------------------------------------------------------------------------
CIS 120 README
--------------------------------------------------------------------------------------------
Game: Chess
Rules: Standard Chess rules as per FIDE
Features:
	Implementation of 2 player chess with an updating boardstate
	Implementation of check, checkmate, castling, and enpassant
	Implementation of board rotation depending on which side's turn it is
		- Remark: the rotation gets old and disorienting quickly so I'd recommend switching it off
	Implementation of move preview which highlights which squares a selected piece can move to/capture (compatible with check/castle/enpassant)
	Implementation of scoreboard that displays piece value taken by each side (Pawn-1, Bishop/Knight - 3, Rook -5, Queen - 9)
	Implementation of rudimentary AI that checks 3-5 moves ahead (depth of the function changes as fewer pieces exist to maintain speed)
		-AI uses a minmax function with AB pruning for efficiency
		-Combined with an evaluator heuristic
	Settings Panel to disable/enable movepreview and rotation or AI
	Checkmate Panel that allows for reset or quit
	Readme Panel with basic instructions
---------------------------------------------------------------------------------------------
Core Concepts:

1. 2D Array -
	The boardstate is represented through a 2D array. The Board class stores an 8*8 array of objects that implement the Piece
	interface. The Board class implements a variety of methods to manipulate this array. The most relevant are listed:
		
		UpdateBoardState - moves pieces, ensures consistancy with specific piece value (toggle for movement, castling, etc)
				   and ensures that the pieces maintain internal location consistency (pieces store their own loc
				   as a point to correctly generate moves prior to check tests). Uses movefilter to prevent moves
				   that would result in check. Finally, stores move data into an object called UndoInfoStorage
				   which is eventually pushed into movehistory (see core concept - collections).
		
		Undo - 		   **SEE** Collections concept
		Transform - 	   Implements pawn promotion
		toggleturn -       Toggles the turn and checks for checkmate/pushes UndoMoveInfo to moveHistory
	
	The benefits of a 2D array are evident - it is most analagous to a chess board and thus translates well to GUI implementation
	(the JPanels used to represent board squares are also stored in a 2D array, allowing for easy and intuitive linking
	of the GUI board square and the backend's info about it). It also allows for easy check/checkmate implementation through 
	for loops to traverse the array. Finally, storing the pieces location info within the array and a point within the piece seems redundant.
	But, it allows move calculation to be mostly done on the piece end at the cost of 32 points, which makes the code much more readable.
	This is especially relevant for complicated calculations like enpassant. Finally, this acts as a second check to prevent invalid moves.

2. Complex Logic - 

	The concept is preapproved for chess. Enpassant and Castling are implemented through a variety of checks ensuring the conditions are met
	They are then added to the respective piece's moveset and the additional rules (movement of the rook as well, removal of the enpassanted pawn)
	are dealt with by the Board class. Check and Checkmate tie in to the movepreview and movefilter features described above. movefilter checks moves
	for discovered check and prevents it. Checkmate checks every piece's movepreview and returns true if no piece can move and the king is in check.

3. Inheritence -
	
	There is a interface, Piece, which each individual piece class (Knight, Bishop, etc.) implements. Moreover, they all extend pieceClass, which defines
	common methods like returning the name, color, and certain types of moves (Bishops, Rooks, and the Queen have the same move logic in different directions)
	Dynamic dispatch is used frequently - often only the piece interface is necessary (any regular movement, piece color, etc.). In special moves/cases (e.g.
	castling), the piece is cast to the relevant piece class and specific methods are called (toggle the boolean that checks if the king has castled, etc.).
	Finally, the differences between the pieces are distinct enough to justify different classes. The movement of a pawn, especially given enpassant, is 
	substantially distinct from that of a queen. The king has to implement check. Additionally, though small, each piece must have a different image and glow
	image associated with. Despite this, the similarities mentioned above justify a superclass and interface. Most of piece movement does NOT require specific
	piece class functions and thus it is substantially more efficient to view the relevant object as a piece. The superclass allows for bishop, rook, and queen 
	movement to only take up a handful of lines.

4. Collections -
	
	The Board class stores move history through a linked list collection. The linked list move history stores undomoveinfostorage objects and adds to it after each
	turn. Calling the undo function removes the last element of the list and uses that element to reset the boardstate. Linked lists are the best representation of the
	move state as we only need to call the head/next element. A set wouldn't be able to get the last element added and any sorted map would be unecessary (sorting on last added
	is just extra steps to implement what a linkedlist can do. Obviously an array is insufficient as we need to increase and decrease the size of the list.
---------------------------------------------------------------------------------------------------------
AI Related Core Concepts/Notes

5. AI -

	I've implemented a minmax algorithm with alpha beta pruning to model AI. The algorithm uses a heuristic that asseses the value of a board state by counting the piece value
	checking which pieces threaten other pieces, and assesing piece mobility (encourages the AI to develop and defend its pieces). The eval function assigns a positive value
	to White pieces/mobility/threat and a negative to Black. Then the minmax algorithim asseses each possible move by projecting 3-5 moves ahead and assuming that each player/ai
	will try to maximize board val if they are white and minimize if they are black. The function basically calculates the min(max(min(max(x))) where x is the board evaluation after
	the min and max moves are made. Based on this, the AI picks the most optimal move. I've also implemented alpha beta pruning to speed up the function which relies on the fact that 
	the max(x, min (y,z)) where y<x is always x irrespective of z. This allows us to eliminate certain branches and speed up minmax drastically. This is done through an upper and lower
	variable that are arguments for the function where upper is always the max of the previously assessed scores for white and lower is the min for black. If lower >= upper then we can
	exit the branch.

	***NOTE***
	I used the wikipedia articles for the two algorithms (minmax and AB pruning) to understand the algorithms and after implementation, ended up with an answer very similar to 
	the pseudo-code provided on wikipedia. For the heuristic function, I only used the standard piece values of chess. If these sources are too revalatory, default to the first
	four core concepts described above
	**********
6. Recursion -
	
	The minmax algorithim I used recursivly traverses "move-trees" to assess the minimum and maximum values throughout the tree. This is the obvious way to implement the minmax function.
	An iterative solution would be overly verbose and inefficient (I am not even sure of a good way to implement this other than hardcoding the depth). A recursive function also allows for
	adjustment of move depth to improve the AI upon increases in efficiency.
	***NOTE***
	The same note regarding wikipedia applies, no tutorial was used but the article is fairly indepth regarding the algorithm. There is also pseudocode that is very similar to my solution.
	**********
-------------------------------------------------------------------------------------------------------------

Sources

https://en.wikipedia.org/wiki/Minimax
https://en.wikipedia.org/wiki/Alpha–beta_pruning
Java Swing Documentation