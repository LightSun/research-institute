package algorithm;

import static java.lang.Math.max;

public class Knapsack {
    public static final int N = 3;
    public static final int C = 5;

    public static void main(String[] args) {
        main();
    }

    static int main() {

        int value[] = {0, 60, 100, 120}; // 价值
        int weight[] = {0, 1, 2, 3};     // 重量
        int[][] f = new int[N + 1][C + 1];   // f[i][j]表示在背包容量为j的情况下，前i件宝贝的最大价值

        int i = 1;
        int j = 1;
        for (i = 1; i <= N; i++)        //外循环控制物品数量，确保每个物品都会被遍历到
        {
        /*for (j = weight[i]; j <= C; j++)      //内循环控制物品的重量，确保能够遍历出“以前每个物品放入时的最大价值f[i][j]”
        {
            int x = f[i - 1][j];        //不放第i件物品
            int y = f[i - 1][j - weight[i]] + value[i];      //放入第i件物品
            f[i][j] = max(x, y);
        }*/

            for (j = 1; j <= C; j++) {
                // 递推关系式
                if (j < weight[i]) {
                    f[i][j] = f[i - 1][j];
                } else {
                    int x = f[i - 1][j];
                    int y = f[i - 1][j - weight[i]] + value[i];
                    f[i][j] = max(x, y);
                }
            }
        }

        for (i = 0; i <= N; i++) {
            for (j = 0; j <= C; j++) {
                System.out.print(String.format("%4d ", f[i][j]));
            }
            System.out.println();
        }

        System.out.println("选取的最大价值是：" +  f[N][C]);
        i = N;
        j = C;
        while (i > 0 && j > 0) {
            if (f[i][j] == (f[i - 1][j - weight[i]] + value[i])) {
                System.out.printf("%d : weight = %d, value = %d", i, weight[i], value[i]);
                j -= weight[i];
            }
            i--;
            System.out.println();
        }
        return 0;
    }
}
