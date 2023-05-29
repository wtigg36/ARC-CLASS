#include <stdio.h>
#include <stdlib.h>
// ./addTwo int int
int main(int n, char* args[]) {
int num1, num2, max;
if (n != 3) {
printf("Usage: ./addTwo int int\n");
return 1;
}
num1 = atoi(args[1]);
num2 = atoi(args[2]);
printf("addTwo of %d and %d is: %d\n", num1, num2, num1 + num2);
}
