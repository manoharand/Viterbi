# Viterbi
Implement the Viterbi and Forward-Backward algorithms for CWRU's EECS 458:

The Hidden Markov Model (HMM) above is used to describe the dishonest casino mentioned in class. The dealer has two coins “Fair” and “Biased”. Each coin has a “Tail” and a “Head”, but with different distributions. The dealer can switch the two coins using the specified probabilities without being noticed by anyone. The first time the dealer chooses one of the coins with probability 0.5 each. Now 1) decode the following sequence of coin tosses HHHHHTTTTT using Viterbi algorithm. 2) What is the posterior probability that the "T" at the seventh position is generated by the biased coin? 