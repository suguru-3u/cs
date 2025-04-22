package org.example.再帰関数

/*
 問題：フィボナッチ数列（末尾再帰バージョン）

 自然数 n を受け取り、n 番目のフィボナッチ数を求める関数を
 末尾再帰（tailrec）を使って実装してください。

 フィボナッチ数列は以下のように定義されます：
   F(0) = 0
   F(1) = 1
   F(n) = F(n-1) + F(n-2)  （n ≧ 2）

 例：
   fibonacci(6) → 8

 条件：
 - tailrec 修飾子を使うこと
 - 再帰呼び出しは関数の末尾で行うこと
 - 関数名は fibonacci とする
 - メイン関数から呼び出して println で出力する
*/


fun main() {
    val result = fibonacci(5)
    println(result)
}

fun fibonacci(num: Int): Int {
    return fibonacciHelper(num, 0, 1)
}

tailrec fun fibonacciHelper(n: Int, a: Int, b: Int): Int {
    if (n == 0) return a
    return fibonacciHelper(n - 1, b + a, a)
}
