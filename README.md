shaked.hayek
shirlevy

## UML explanation


## Endless world
In order to make the world endless and have a minimum of calculations to do, we chose to have only few blocks
in each direction beyond the screen shown - a buffer. When the avatar moves a distance larger than half the
buffer, we create the world on one side, and remove it on the other side, such that each side will have the
same number of blocks. We had to deal with deleting and creating again the same view of terrain and trees: 
* For the terrain, for creating again in same way we used a noise function that isn't random based. For 
deleting, in order to know which blocks we need to delete we created a hash map that maps x coordinate to
list of blocks.
* For the trees, we created a hash map, mapping each x to the tree stem blocks we created. If the list is null
we know there is no tree in that coordinate. If the list exists but the first object in the list is null we 
know that the tree is deleted and we can recreate it, in the size of the list. If the x coordinate doesn't 
exist in map, we create tree randomly and add it to the map.
To deal with the leaves we had a separate hash map that maps x coordinate to list of leaves. Because the size
of the leaves block is determined by the size of the stem, we can recreate it easily.

## Tree choices


## Design choices
* Avatar's energy reduction - We felt that subtracting energy only when the flying keys are pressed seemed 
less optimal. We wanted to reduce energy as long as the avatar is in the sky and not jumping - because this
is the time he is actually flying. So we decided to regard flying time as all time after the flying keys were
pressed and avatar didn't collide with anything and energy is still not 0.
* 

## BONUS
* We created the avatar as a penguin, and added different animations for flying, jumping, walking and 
standing.
* Because the avatar is a penguin, we made the ground in a snow color, and added snowflakes falling 
randomly from the sky and fades out when touching the ground, just like snow.
* We added an energy text bar, with different colors for different energy values.
