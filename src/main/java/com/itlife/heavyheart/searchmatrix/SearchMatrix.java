package com.itlife.heavyheart.searchmatrix;

public class SearchMatrix {

    /**
     * 搜索二维矩阵
     * z字搜索
     *
     * @param g 二维数组
     * @param t t
     * @return boolean
     */
    public boolean searchMatrix(int[][] g, int t) {
        int i = g.length - 1, j = 0;
        while (i >= 0 && j < g[0].length) {
            if (g[i][j] > t) {
                i--;
            } else if (g[i][j] < t) {
                j++;
            } else {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[][] g = new int[2][2];
        g[0][0] = 1;
        g[0][1] = 2;
        g[1][0] = 3;
        g[1][1] = 4;
        SearchMatrix searchMatrix = new SearchMatrix();
        System.out.println(searchMatrix.searchMatrix(g, 5));
        System.out.println(searchMatrix.searchMatrix(g, 0));
        System.out.println(searchMatrix.searchMatrix(g, 1));
        System.out.println(searchMatrix.searchMatrix(g, 4));
    }
}
