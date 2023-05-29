#include <stdio.h>

void hailstone(int n) {
    int length = 0; // Initialize the length of the sequence
    printf("%d ", n); // Print the initial number

    while (n != 1) {
        if (n % 2 == 0) {
            n = n / 2;
        } else {
            n = 3 * n + 1;
        }

        printf("%d ", n); // Print the next number in the sequence
        length++; // Increment the sequence length
    }

    printf("\nLength of the sequence: %d\n", length); // Print the length of the sequence
}

int main() {
    int initialNumber;

    printf("Enter a positive integer: ");
    scanf("%d", &initialNumber);

    hailstone(initialNumber);

    return 0;
}
