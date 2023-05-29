#include <stdio.h>
#include <stdlib.h>

int main(int n, char* args[]) {
    int count;
    int total = 0; // Initialize total
    
    if ((n != 2) || ((count = atoi(args[1])) < 1)) {
        printf("Usage: ./sumTotal posInt\n");
        return 1;
    }
    
    printf("Starting count: %d\n", count);
    
    while (count > 0) {
        total = total + count;
        count = count - 1;
    }
    
    printf("Final total: %d\n", total);
    
    return 0; // Add return statement
}
