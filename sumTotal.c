#include <stdio.h>
#include <stdlib.h>

int main() {
    int n, total = 0;

    printf("Enter the initial count (n): ");
    scanf("%d", &n);

    if (n < 0) {
        printf("Invalid input. Initial count should be a non-negative integer.\n");
        return 1;
    }

    printf("Starting count: %d\n", n);

    while (n >= 0) {
        total += n;
        n--;
    }

    printf("Final total: %d\n", total);

    return 0;
}

