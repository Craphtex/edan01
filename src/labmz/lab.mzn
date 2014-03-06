%Ex2

include "globals.mzn";

int: nCities = 10;

var {2,3}: C1;
var {2,3,4}: C2;
var {2,3,5,6}: C3;
var {2,4,5,6,7}: C4;
var {3,4,5,7}: C5;
var {3,4,6,8}: C6;
var {4,5,7,8,9}: C7;
var {6,7,8,9,10}: C8;
var {7,8,9,10}: C9;
var {1}: C10;



array[1..nCities] of var int: cities = [C1,C2,C3,C4,C5,C6,C7,C8,C9,C10];

constraint subcircuit(cities);

solve satisfy;


output [show(cities)];