package org.example.ハッシュテーブル

/*
【問題】
以下の要件を満たすKotlinプログラムを作成してください。

1. ユーザーの名前とスコア（Int）を記録するシステムを作成してください。
2. 初期状態では以下の3人のデータが登録されています：
     - Alice: 80
     - Bob: 75
     - Charlie: 90
3. 標準入力から追加のユーザー名とスコアを1件ずつ受け取り、既に登録されているユーザー名なら
   スコアを上書きせず、無視してください。
4. 最終的に、全てのユーザーとそのスコアを**登録順**で出力してください。

【制約】
- 登録済みユーザー名の重複チェックは、`Map` の機能を利用して行うこと。
- 登録順の出力を保証すること。
*/

fun main() {
    val scoreRecord = LinkedHashMap<String, Int>()
    scoreRecord["Alice"] = 80
    scoreRecord["Bob"] = 75
    scoreRecord["Charlie"] = 90

    val inputName = readln()

    val inputScore = readln().toInt()
    scoreRecord.putIfAbsent(inputName, inputScore)

    for ((key, value) in scoreRecord) {
        println("登録名: $key スコア: $value")
    }
}