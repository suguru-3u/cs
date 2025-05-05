package org.example.ハッシュテーブル

/*
【問題】
テストの得点記録プログラムを作成してください。

1. プログラム開始時、次の受験者と点数が登録されています（順序を保持すること）：
   - Ken: 85
   - Yui: 92
   - Sota: 78

2. 標準入力から、任意の件数だけ名前と点数のペアを受け取ります。
   - 入力を終えるときは、名前として「end」と入力してください。

3. すでに登録されている名前の場合は、スコアを更新せずスキップしてください。

4. 最終的に、スコアの高い順に並び替えて「名前: スコア」の形式で出力してください。

【制約】
- 点数は整数。
- 登録順は保持するが、最終出力は点数の降順。
- Mapの機能（putIfAbsentやcontainsKey）を積極的に使うこと。
*/

fun main() {
    val scoreRecord = LinkedHashMap<String, Int>()
    scoreRecord["Ken"] = 85
    scoreRecord["Yui"] = 92
    scoreRecord["Sota"] = 78

    var inputName: String
    while (true) {
        println("名前を入力してください（endで終了）:")
        inputName = readln()
        if (inputName == "end") break

        println("スコアを入力してください:")
        val inputScore = readln().toInt()
        scoreRecord.putIfAbsent(inputName, inputScore)
    }

    println("===== スコア順に並び替えた結果 =====")
    scoreRecord.entries
        .sortedByDescending { it.value }
        .forEach { (name, score) ->
            println("$name: $score")
        }
}

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

fun problem1() {
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

