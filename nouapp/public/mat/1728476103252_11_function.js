function one(x,y){
    return Math.round((1+(x+y)/2))
}

const sum= (p,q)=>{
    return p+q
}
let a=1;
let b=2;
let c=3;

console.log("one plus Average of a and b is:", one(a,b)); 
console.log("one plus Average of a and c is:", one(a,c));
console.log("one plus Average of b and c is:", one(b,c));

console.log(sum(9,7))