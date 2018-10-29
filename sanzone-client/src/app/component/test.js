function sum(a) {
    var s = 0;
    if (a.length > 1) for (var i = 0; i < a.length; i++) s += a[i];
    else for (var i = 0; i < arguments.length; i++) s += arguments[i];
    return s;
}
//console.log(sum(1,2));
//console.log(sum([1,2]));

var hash = { a: 1, b: 2 };
console.log(hash.a + hash.b); //3
console.log(hash['a'] + hash['b']); //3

try {
    var a = bbbbb;
}
catch (ex) {
    console.error('an exception occurred!');
}
var arr=[1,2];
const[aa,bb]=arr;
console.log(aa);
console.log(bb);