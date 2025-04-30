package org.example.多次元配列

/*
問題：偶数の位置一覧

整数の2次元配列が与えられます。
この配列の中から、値が「偶数」である要素の位置（行番号と列番号）をすべて出力するプログラムを作成してください。

【入力データ】
val matrix = arrayOf(
    arrayOf(3, 8, 7),
    arrayOf(6, 5, 2),
    arrayOf(9, 4, 11)
)

【出力例】
偶数の値: 8 → 位置: 行0 列1
偶数の値: 6 → 位置: 行1 列0
偶数の値: 2 → 位置: 行1 列2
偶数の値: 4 → 位置: 行2 列1

※ 行・列は0始まりとすること。
*/

fun main() {
    val matrix = arrayOf(
        arrayOf(3, 8, 7),
        arrayOf(6, 5, 2),
        arrayOf(9, 4, 11)
    )

    // ここに処理を実装
    data class EvenNumber(val number: Int, val row: Int, val col: Int) {
        override fun toString(): String {
            return "偶数の値: $number → 位置: 行$row 列$col"
        }
    }

    val evenNumberList = ArrayList<EvenNumber>()

    for ((indexRow, row) in matrix.withIndex()) {
        for ((indexCol, col) in row.withIndex()) {
            if (col % 2 == 0) {
                evenNumberList.add(
                    EvenNumber(
                        number = col,
                        row = indexRow,
                        col = indexCol
                    )
                )
            }
        }
    }
    evenNumberList.forEach(::println)
}


/*
問題：最大値の位置

整数の2次元配列が与えられます。
この中で「最大の値」と、その「位置（行番号と列番号）」を出力するプログラムを作成してください。

【入力データ】
以下のような配列が与えられるとします：

val matrix = arrayOf(
    arrayOf(3, 15, 7),
    arrayOf(22, 4, 9),
    arrayOf(6, 13, 17)
)

【出力例】
最大値: 22
位置: 行1 列0

※ 行・列は0始まりとすること。
*/

fun problem2() {
    val matrix = arrayOf(
        arrayOf(3, 15, 7),
        arrayOf(22, 4, 9),
        arrayOf(6, 13, 17)
    )

    var maxNum = 0
    val maxArrayInfo = Array(2) { 0 }

    for ((indexRow, row) in matrix.withIndex()) {
        for ((indexCol, col) in row.withIndex()) {
            if (maxNum < col) {
                maxNum = col
                maxArrayInfo[0] = indexRow
                maxArrayInfo[indexCol] = indexCol
            }
        }
    }

    println("最大値: $maxNum")
    println("位置: 行${maxArrayInfo[0]} 列${maxArrayInfo[1]}")
}


/*
問題：2次元配列の合計

3行4列の整数の2次元配列が与えられます。
各行の合計と、全体の合計を出力するプログラムを作成してください。

【入力データ】
以下のような配列が与えられているとします：

val matrix = arrayOf(
    arrayOf(3, 5, 1, 2),
    arrayOf(4, 8, 6, 0),
    arrayOf(7, 2, 9, 4)
)

//【出力例】
行1の合計: 11
行2の合計: 18
行3の合計: 22
全体の合計: 51
*/

fun problem1() {
    val matrix = arrayOf(
        arrayOf(3, 5, 1, 2),
        arrayOf(4, 8, 6, 0),
        arrayOf(7, 2, 9, 4)
    )

    // ここに処理を実装
    var totalSum = 0
    val arraySum = Array(matrix.size) { 0 }
    for ((index, row) in matrix.withIndex()) {
        for (col in row) {
            totalSum += col
            arraySum[index] += col
        }
    }
    arraySum.forEachIndexed { index, i ->
        println("行${index + 1}の合計: ${i}")
    }
    println("全体の合計: $totalSum")
}
