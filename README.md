# Distributed Programming Project

# TCSMP - Time-Cost Stamped Mail Protocol

TCSMP is a client-server protocol meant to transfer mail reliably and to make transfers time consuming.
While this can sound surprising, the time consumption is to be considered as a cost, ie. sending a mail actually costs time. The idea is to prevent spamming: sending a mail to one recipient could take less than 1 sec, while sending a mail to 50 could take 30 seconds!

Now in order to do so, TCSMP relies on puzzle solving. More precisely, it uses Eternity puzzles which complexity evolves exponentially. The server sends a puzzle to the client, which has to solve it in order for its mail to be accepted by the server.
