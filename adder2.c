#include <stdio.h>

int adder() {
    int a;
    return a * 100;
}

int assign() {
    int y = 450 * 56;
    return y;
}

int main() {
    int x;
    assign();
    x = adder();
    printf("x is: %d\n", x);
    return 0;
}