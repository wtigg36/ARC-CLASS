#include <stdio.h>

void hailstone(int n) {
    int length = 0; 
    printf("%d ", n); 

    while (n != 1) {
        if (n % 2 == 0) {
            n = n / 2;
        } else {
            n = 3 * n + 1;
        }

        printf("%d ", n); 
        length++; 
    }

    printf("\nLength of the sequence: %d\n", length); 
}

int main() {
    int initialNumber;

    printf("Enter a positive integer: ");
    scanf("%d", &initialNumber);

    hailstone(initialNumber);

    return 0;
}
