# This is the makefile for Blackjack
JC = javac

.SUFFIXES: .java .class
.java.class:
	$(JC) $*.java

CLASSES = \
					Blackjack.java \
					Board.java \
					Player.java \
          Hand.java

Blackjack: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
