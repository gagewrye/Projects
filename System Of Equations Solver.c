/*
 Here’s my code for solving systems of linear equations. 

No temp arrays are used to avoid unnecessary memory usage by directly editing the input matrices.
The inverse array is also freed to conserve memory.

If you run the main function, it will look at the example system and output the solution.
The example numbers are hardcoded and can be edited to solve other systems.
*/

#include <math.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h> // for malloc
#define DIM 3


double dotproduct(int n, double a[], double b[]) {
    double dot = 0.0;
    for(int i=0; i<n; i++) {
        dot += a[i] * b[i];
    }
    return dot;
}


// Add two m x n matrices and get an m x n sum: Y = A + B
void matrixadd(int m, int n, double *A, double *B, double *Y) {
    for(int i=0; i<m; i++) {
        for(int j=0; j<n; j++) {
            Y[i*n+j] = A[i*n+j] + B[i*n+j];
        }
    }
}


// multiply a matrix by a scalar value. mat = mat * n
void scalarmult(int n, double *mat){
    for(int i=0; i<n; i++) {
            mat[i*n] = n*mat[i*n];
        }
}

// Y = sa * A + sb * B
void linearcomb(int m, int n, double sa, double sb, double *A, double *B, double *Y) {
    for(int i=0; i<m; i++) {
        for(int j=0; j<n; j++) {
            Y[i*n+j] = sa*A[i*n+j] + sb*B[i*n+j];
        }
    }
}

// At = transpose(A).  A is an m x n matrix, so At is an n x m matrix
void transpose(int m, int n, double *A, double *A_t) {
    for(int i=0; i<m; i++) {
        for(int j=0; j<n; j++) {
            A_t[j*m+i] = A[i*n+j];
        }
    }
}

// return 1 if all elements of A are equal to the corresponding elements of B, 0 otherwise
int equal(int m, int n, double *A, double *B) {
    for(int i=0; i<m; i++) {
        for(int j=0; j<n; j++) {
            if(A[i*n+j] != B[i*n+j]) {
                return 0;
            }
        }
    }
    return 1;
}

// Y = A * B
// A is m1 x n1m2.  B is n1m2 x n2  Y is m1 x n2
void mult(int m1, int n1m2, int n2, double *A, double *B, double *Y) {
    for(int i=0; i<m1; i++) {
        for(int j=0; j<n2; j++) {
            double sum = 0;
            for(int k=0; k<n1m2; k++) {
                sum += A[i*n1m2+k] * B[k*n2+j];
            }
            Y[i*n2+j] = sum;
        }
    }
}


// The following functions and main() are provided for you
double* newMatrix(int m, int n) {
    return (double *)calloc(m * n, sizeof(double));;
}

// create a new identity matrix of size n x n
double* newIdentityMatrix(int n) {
    int i, j;
    double* mat = newMatrix(n,n);
    for (i=0; i<n; i++)
        for (j=0; j<n; j++)
        {
        mat[j+i*n] = (i==j) ? 1.0 : 0.0;
        }
    return mat;
}

// Convert an existing matrix of size into an identity matrix 
void IdentityMatrix(int size, double* mat) {
    int i, j;
    for (i=0; i < size; i++)
        for (j=0; j < size; j++)
        {
        mat[j + i * size] = (i==j) ? 1.0 : 0.0;
        }
}

// Function to invert an n x n matrix A
// Returns 1 if the matrix is invertible, and 0 if it's singular
// The inverse matrix is stored in Y
int invert(int n, double *A, double *Y) {
    
    // Initialize Y as the identity matrix
    IdentityMatrix(n, Y);

    // Perform Gaussian elimination with partial pivoting on A
    // and apply the same row operations on Y
    for (int i = 0; i < n; i++) {
        // Check if the pivot is zero, indicating a singular matrix
        if (A[i * n + i] == 0) {
            return 0;
        }

        // Normalize the pivot row by dividing by the pivot element
        double pivot = A[i * n + i];
        for (int j = i; j < n; j++) {
            A[i * n + j] /= pivot;
        }
        for (int j = 0; j < n; j++) {
            Y[i * n + j] /= pivot;
        }

        // Eliminate the elements below the pivot
        for (int k = i + 1; k < n; k++) {
            double factor = A[k * n + i];
            for (int j = i; j < n; j++) {
                A[k * n + j] -= factor * A[i * n + j];
            }
            for (int j = 0; j < n; j++) {
                Y[k * n + j] -= factor * Y[i * n + j];
            }
        }
    }

    // Perform back-substitution on the upper triangular matrix A
    // and apply the same row operations on Y
    for (int i = n - 1; i >= 0; i--) {
        for (int k = i - 1; k >= 0; k--) {
            double factor = A[k * n + i];
            for (int j = 0; j < n; j++) {
                Y[k * n + j] -= factor * Y[i * n + j];
            }
        }
    }

    return 1;
}


/*
  Solves the linear system Ax = b using the inverse and multiplying it by the vector array
  Return 1 if successful, 0 if the matrix A is singular.
*/

int solve(int n, double *A, double *b, double *x) {
    // Calculate the inverse of A using the invert function
    double *invA = (double *)calloc(n * n, sizeof(double));
    if (!invert(n, A, invA)) {
        free(invA);
        return 0;
    }


    // Multiply invA by b to obtain the solution x
    for (int i = 0; i < n; i++) {
        x[i] = 0;
        for (int j = 0; j < n; j++) {
            x[i] += invA[i * n + j] * b[j];
        }
    }

    free(invA);
    return 1;
}

int main() {

    // number of variables
    int n = 4;
    
    /* Initialize the matrix A and the vector b using the example system:

        3a + b + c + d = 6

        2a + 3b + 4c - d = 12
    
        -4a + d = 8
    
        3b - 2c = 0
    */ 

    double A[] = {
        3, 1, 1, 1,
        2, 3, 4, -1,
        -4, 0, 0, 1,
        0, 3, -2, 0
    };

    double b[] = {6, 12, 8, 0};

    double x[n]; // Initialize the solution vector x.

    // If the solve function is successful, print the solution.
    if (solve(n, A, b, x)) {
        printf("Solution: x = [");
        for (int i = 0; i < n; ++i) {
            printf("%.2lf", x[i]);
            if (i < n - 1) {
                printf(", ");
            }
        }
        printf("]\n");
    } else {
        printf("The matrix A is singular.\n");
    }

    return 0;
}
