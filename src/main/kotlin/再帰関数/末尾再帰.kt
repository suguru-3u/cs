package org.example.再帰関数

import com.sun.jdi.IntegerType

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

fun fibonacci(num: Int): Int {
    return fibonacciHelper(num, 0, 1)
}

tailrec fun fibonacciHelper(n: Int, a: Int, b: Int): Int {
    if (n == 0) return a
    return fibonacciHelper(n - 1, b + a, a)
}

/*
 問題：リストの反転（末尾再帰）

 Int型のListを受け取り、そのリストを反転した新しいListを返す関数を、
 末尾再帰（tailrec）を使って実装してください。

 例：
   reverseList(listOf(1, 2, 3, 4)) → [4, 3, 2, 1]

 条件：
 - tailrec 修飾子を使うこと
 - 再帰呼び出しは関数の末尾で行うこと
 - 関数名は reverseList とすること
 - 元のリストを変更せず、新しいリストを返すこと
*/

fun reverseList(intList: List<Int>): List<Int> {
    return reverseListHelper(intList, emptyList())
}

tailrec fun reverseListHelper(
    currentList: List<Int>,
    revertList: List<Int>
): List<Int> {
    if (currentList.isEmpty()) return revertList
    val firstValue = currentList.first()
    val lastValue = currentList.drop(1)
    return reverseListHelper(lastValue, listOf(firstValue) + revertList)
}

/*
 問題：自然数の桁の合計（末尾再帰）

 正の整数 n を受け取り、その各桁の数字の合計を返す関数を、
 末尾再帰（tailrec）を使って実装してください。

 例：
   digitSum(1234) → 10  （1 + 2 + 3 + 4）

 条件：
 - tailrec 修飾子を使うこと
 - 再帰呼び出しは関数の末尾で行うこと
 - 関数名は digitSum とすること
 - 累積変数（accumulator）を使って実装すること
*/

fun digitSum(num: Int): Int {
    val chsngeNum = num.toString()
    return digitSumHelper(chsngeNum.length - 1, chsngeNum, 0)
}

tailrec fun digitSumHelper(length: Int, str: String, sum: Int): Int {
    if (length == -1) return sum
    val char = str[length]
    return digitSumHelper(
        length - 1,
        str,
        sum + Integer.parseInt(char.toString())
    )
}

/*
 問題：最大公約数（GCD）の計算（再帰）

 2つの正の整数 a, b を受け取り、その最大公約数（GCD）を返す関数を
 再帰を使って実装してください。

 例：
   gcd(48, 18) → 6

 条件：
 - tailrec を使っても使わなくてもOK（ただし tailrec を使えるなら使ってみてください）
 - 関数名は gcd とすること
 - ユークリッドの互除法を使うこと
*/

fun main() {
    val result1 = fibonacci(5)
    println(result1)

    val result2 = reverseList(listOf(1, 2, 3, 4))
    println(result2)

    val n = 1234
    println(digitSum(n)) // 出力: 10
}

