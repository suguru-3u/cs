package org.example.非末尾再帰

/*
 問題：数値の桁の合計を再帰で求めよう（非末尾再帰）

 与えられた正の整数 n の各桁の合計を求める関数を、
 非末尾再帰を使って実装してください。

 例:
 digitSum(1234) → 1 + 2 + 3 + 4 = 10
 digitSum(501)  → 5 + 0 + 1 = 6

 制約:
 - 関数名は digitSum
 - パラメータは n: Int
 - 非末尾再帰を使うこと（再帰呼び出しの後に処理がある）
 - tailrec は使わない
*/

fun digitSum(n: Int): Int {
    if (n < 10) return n
    return n % 10 + digitSum(n / 10)
}

fun main() {
    println(digitSum(501))
}