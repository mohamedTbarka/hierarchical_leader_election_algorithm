# Hierarchical Leader Election Algorithm with Remoteness Constraint
A hierarchical algorithm for electing a leaders’ hierarchy in an asynchronous network with dynami-
cally changing communication topology is presented including a remoteness’s constraint towards
each leader. The algorithm ensures that, no matter what pattern of topology changes occur, if topology
changes cease, then eventually every connected component contains a unique leaders’ hierarchy.
The algorithm combines ideas from the Temporally Ordered Routing Algorithm (TORA) for mobile
ad hoc networks with a wave algorithm, all within the framework of a height-based mechanism
for reversing the logical direction of communication links. Moreover, an improvement from the
algorithm in is the introduction of logical clocks as the nodes measure of time, instead of requiring
them to have access to a common global time. This new feature makes the algorithm much more
flexible and applicable to real situations, while still providing a correctness proof. It is also proved
that in certain well behaved situations, a new leader is not elected unnecessarily.
