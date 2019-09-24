# Blackjack

This is a console-based interactive Blackjack card game (non-gambling version).
It allows multiplayer where you switch off the person at the computer when
instructed to do so.

This was created for the 2020 Kleiner Perkins Engineering Fellow coding
challenge. This README.md file serves as the Design Document.


## Table of Contents

- [Installation](#Installation)
- [Building the Code](#BuildCode)
- [Running the Code](#RunCode)
- [Rules of Blackjack](#BlackjackRules)
- [Design Choices](#DesignChoices)
  

## Installation

The following instructions will get you a copy of the project up and running on
your local machine for testing purposes.

### Prerequisites

You will need a unix-compatible terminal that can run Java programs/files. You
may need to install JDK and JRE if your terminal cannot run Java programs.

### Option 1: Clone

To clone, your terminal will need to have git and allow git commands. If your
terminal does not have git, please go to Option 2: Download.
1. Clone this repo to your local machine by typing `git clone 
https://github.com/AimeeShao/Blackjack.git` in your terminal in your desired
folder.

### Option 2: Download

Download only if you were not able to copy the files using git.
1. At `https://github.com/AimeeShao/Blackjack`, click the `Clone or download`
button near the top right and click `Download ZIP`.
2. Extract the source files from the ZIP to your desired folder location.
  

<a name="BuildCode"></a>
## Building the Code
1. In your terminal, navigate to the folder/directory containing all the files
with the source code.
- You should have the `Blackjack.java`, `Board.java`, `Player.java`, and
`Hand.java` files in this folder.
2. Compile the java source files.
- If you have the Makefile, type `make Blackjack` in your terminal.
- Otherwise, type `javac Blackjack.class` in your terminal.
  

<a name="RunCode"></a>
## Running the Code

1. In your terminal, navigate to the folder/directory where you compiled the
source code.
- You should have the `Blackjack.class`, `Board.class`, `Player.class`, and
`Hand.class` files in this folder.
2. Run the program according to the usage below.

### Usage

<pre>
Usage: java Blackjack [numPlayers]
  numPlayers: equal to the number of players aside from the dealer
    -- optional: default is 1 player
    -- must be an integer
</pre>
  

<a name="BlackjackRules"></a>
## Rules of Blackjack

There are many versions of Blackjack, so here are the rules of this version of
Blackjack.

### Game End

The goal of the game is to have a hand with a sum greater than the one of the
dealer (the computer), but not to go over 21.

If your hand's sum is:
- Over 21 = lose
- Tied with dealer (& under 21) = tie
- Under 21 and dealer is over 21 = win
- Greater than dealer's sum (both you and dealer under 21) = win

### Card Rules

- "A" can be either 1 or 11.
  - Ex: Hand = A,2. Total = 3 OR 13.
- Face cards (J,Q,K) are worth 10.

### Gameplay

- Every player including the dealer (the computer) is initially dealt 1 face up
card and 1 face down card.
- Player 1 starts, then Player 2 and so on. Dealer goes last.
- During a player's turn, say "hit" to get a random card until you wish to stop
receiving cards by saying "stay". Saying "stay" ends the player's turn.
- After all players have gone, dealer deals himself. The dealer must
continue drawing cards until the dealer's hand has at least 17, or until it goes
bust by going over 21.
- The game starts with one deck (4 cards for each number A,2-10,J,Q,K). If the
deck runs out, then we add another deck to the game.
  

<a name="DesignChoices"></a>
## Design Choices

### Language

I chose to use Java because I am most familiar with it and its common libraries
and it is portable.

### Libraries

I used java.util because it is one of the most common libraries in Java and
contains all the classes and data structures I needed.

Functionalities needed:
- ArrayList
- Random number generator
- Read input from terminal

### Classes

I broke down each layer of the game into classes to create abstractions at the
level of the problem domain.
- Blackjack: Used as the interface that interacts with players.
- Board: The black box abstraction around the a board. It is the table that
Blackjack is normally played on. This includes the deck of cards and the players.
- Player: The player class consists of methods that a player would do. This
includes calling "hit", calculating the total in the player's hand, and card
counting.
- Hand: The hand is the set of cards that a player has. This class helps manage
the cards that a player has.

### Data Structures

For the board, I chose to use an array for the deck because card numbers are
in sequential order and start from 1, or A, and all I needed to track were
counts for each card number. Thus, the index served as card numbers and the
elements serve as the counts.

For a player's hand, I chose to use an ArrayList because all I needed was to add
cards to the hand and retreive a card at a specific position. ArrayList has the
best optimization for doing these two. An array would require a fixed size and a
LinkedList has O(n) for retreiving a card at a specific position. Thus, I did
not use either of those and chose to use ArrayList.

### Game Decisions

1. How would I enable multiplayer on a single computer?
  - Clear the console so the next person sitting at the computer can't see the
	previous person's hand.
  - Require a response when switching players by printing "Please press enter"
	and waiting for a response.

2. Why can the player still "hit" after reaching blackjack (21)?
  - In the card game, the player may mis-calculate or simply want to lose and
	say "hit" even when he has 21. Thus, this program does not automatically
	end a player's turn when reaching 21.

3. Why are suits not included?
  - Including suits would take up a lot more memory because we have to track
	each individual card (52 of them).
  - Suits are not needed in Blackjack. They serve no purpose with the main set
	of rules.

4. Why does the program print other players' visible card?
  - In the card game, the visible card can be seen by everyone. This may be a
	way to card count if the player really wanted to.

5. Why does the code need a deck if we just need random numbers from 1-13?
  - The original game is played with a deck or decks. This limits the number of
	A's (and other cards) that can be on the board. This allows the card counting
	technique.

### Corner Cases Considered and Addressed

1. User tries to enter more than one argument.
  - Error appears and program exits. 
  - Another solution would be to ignore all the arguments except the first one,
	numPlayers.

2. User tries to enter a non-integer for numPlayers argument.
  - Error appears and program exits.

3. A lot of players or one player keeps hitting, so the deck may run out of
cards.
  - Refill the deck (simulating adding a new deck) when it runs out to continue
	the game.
  - Another solution would be to get rid of decks totally, making the code
	design a little simpler. However, that would steer from the idea of card
	games where	card counting is an option.

### About Hint Function

1. What's the hint?
  - The hint is the probability of the player busting based on what he
	knows. This basically does the card counting for you.

2. Why is this included?
  - I incorporated the hint function to make the program a little special. I
	imagined it as a phone app and there are usually "hints" in phone app games at
	the cost of watching an ad. So I thought this would be an appropriate add to
	the program.

3. How is the probability calculated?
  - Simply explained:
  goal = number till player reaches 21.
	count = total # of cards - # of cards the player knows about
	probability = # of cards greater than goal / count

  - The player knows that the visible cards and his own hand cannot be in
	the deck. The cards that previous players received during their turns are up
	in the air, so we consider those cards as still in the deck.

#### Example
1 dealer, 5 players have a 2, 3, 4, 5, 6, 7 in that respective order.
Player 5's turn. He has a 7 and a 10.

- goal = 21 - 17 = 4. If Player 5 gets a card greater than 4, he busts.
- Player 5 knows 2, 3, 4, 5, 6, 7, and 10 are not in the deck and they only used
1 deck of 52 cards. 
- Player 5 knows nothing about the face down cards and the cards Players 1-4
were hit with. 
- So count = 52 - 7 = 45.
- In those 45 cards, 3 (Card 5s) + 3 (6s) + 3 (7s) + 4 (8s) + 4 (9s) + 
3 (10s) + 4 * 3 (Js, Qs, Ks) = 32 cards are greater than 4.
- So probability = 32 / 45 = 0.7111 = 71.11%
