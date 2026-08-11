# Phase 4「DBと分散システム」ロードマップ

> Phase 2・Phase 3 と同じ **3部構成・全9章**。第1〜6章で「1台のDBを深く」、第7〜9章で「複数台に広げる」。
> 分散から入ると足場がないので、後半に置く構成にしています。

- **期間目安**: 2〜3ヶ月(週5時間未満 / 各章5時間前後 = 計45時間程度)
- **DBMS**: **MySQL 8.4(InnoDB)** — 実務に合わせる。要所で PostgreSQL との違いを注記
- **実測環境**: **Docker**(第7章以降は source / replica の2コンテナ構成)
- **アプリ側**: **Kotlin + MyBatis(アノテーション方式・XMLなし)+ HikariCP + JDBC**

---

## 前提環境(第1章で用意する)

```yaml
# docker-compose.yml(第1〜6章はこれだけでOK)
services:
  mysql:
    image: mysql:8.4
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: cs_study
    ports: ["3306:3306"]
    command: >
      --slow_query_log=ON
      --long_query_time=0.1
      --log_bin=binlog
```

第7章でこれに `replica` サービスを足してレプリケーションを組みます。

---

## 第1部 リレーショナルDBの土台

### 第1章 なぜファイルではダメなのか — DBの存在理由とリレーショナルモデル
**目安 4h**

- 永続化・並行アクセス・検索・整合性を「自分で作ると地獄」から入る(Phase 2 のファイルI/O・排他の回収)
- リレーショナルモデル = **集合と関係**(Phase 0 の直接回収)、キー、正規化と非正規化
- MySQL のアーキテクチャ:接続層 → パーサ → オプティマイザ → **ストレージエンジン(InnoDB)** の分離
- 環境構築:Docker で MySQL を立てて 100万行のテストデータを投入(以降の章で使い回す)
- **Phase 3 接続**: `mysql://localhost:3306` への接続は結局ソケット。クライアント/サーバモデル
- **実務**: なぜ CSV 運用が破綻するか / 正規化崩れが障害になるパターン

### 第2章 SQL はどう実行されるのか — オプティマイザと EXPLAIN
**目安 5h**

- 宣言的な SQL → 手続き的な実行計画への変換、統計情報とコスト見積り
- **`EXPLAIN` の読み方**(`type`, `key`, `rows`, `filtered`, `Extra`)ここが実務で一番効く
- `EXPLAIN ANALYZE`(実測付き)、`FORMAT=JSON`、`optimizer_trace`
- 結合アルゴリズム:Nested Loop / **Hash Join(8.0.18+)** / Block Nested Loop
- **実測**: 100万行で full table scan と index scan の実行時間を比較(何倍違うか自分の目で)
- **実務**: 「昨日まで速かったクエリが突然遅い」= 統計情報の陳腐化とプラン変化 / `ANALYZE TABLE`

### 第3章 インデックスとストレージ — B+木・クラスタインデックス・バッファプール
**目安 6h**

- B+木の構造と**段数の計算**(16KBページで数百万行がなぜ3〜4段で届くのか、実際に手計算する)
- **InnoDB のクラスタインデックス**: PRIMARY KEY がデータ本体。セカンダリインデックスは PK を持つ → **二度引き(テーブルアクセス)**
- カバリングインデックス(`Using index`)、複合インデックスの**列順**、左端一致の原則
- インデックスが効かないケース(関数適用、暗黙の型変換、前方一致でない LIKE、低カーディナリティ)
- バッファプール(`innodb_buffer_pool_size`)= Phase 2 のページキャッシュの回収
- LSM木にも軽く触れる(第7章 NoSQL への布石)
- **実測**: カバリングインデックス有/無で同じクエリの時間差 / バッファプールのヒット率を見る
- **実務**: インデックス設計の勘所、増やしすぎの副作用(書き込みコスト・容量)

> **PostgreSQL との差分メモ**: PostgreSQL はヒープ+全インデックスが間接参照なので「クラスタインデックス」の概念がない。MySQL は PK の設計がそのまま性能に直結する(採番方式が重要)。

---

## 第2部 トランザクションと Kotlin/MyBatis からの実践

### 第4章 トランザクションと ACID — redo/undo ログとクラッシュリカバリ
**目安 5h**

- ACID を一つずつ分解し、「原子性はどう実装されているのか」に踏み込む
- **redo ログ(WAL)/ undo ログ / doublewrite buffer / チェックポイント**
- `innodb_flush_log_at_trx_commit` と **fsync のコスト**、group commit
- binlog と redo ログの**内部2相コミット**(第8章の 2PC の伏線)
- **実測**: 1件ずつコミット vs バッチコミット vs `innodb_flush_log_at_trx_commit=2` で INSERT 速度比較
- **実測**: トランザクション中に `docker kill` → 再起動でロールバックされることを確認(クラッシュリカバリを体感)
- **実務**: 大量 INSERT が遅い理由、電源断でもデータが残る仕組み

### 第5章 並行制御 — 分離レベル・MVCC・ネクストキーロック・デッドロック
**目安 6h**

- 3つのアノマリー(Dirty Read / Non-repeatable Read / Phantom)と4つの分離レベル
- **MySQL のデフォルトは REPEATABLE READ**(PostgreSQL は READ COMMITTED)— この差が実務で刺さる
- MVCC と consistent read(なぜ読みがブロックされないか)、undo ログとの関係
- **ロックの種類**: レコードロック / **ギャップロック / ネクストキーロック**(MySQL 固有の落とし穴)、意図ロック
- 悲観ロック(`FOR UPDATE`)と楽観ロック(バージョン列)、`FOR UPDATE SKIP LOCKED`
- **実測**: mysql クライアント2セッションを並走させて、アノマリーとデッドロックを**実際に再現**
- **実測**: `SHOW ENGINE INNODB STATUS` のデッドロックログ、`performance_schema.data_locks` で待ちを観察
- **実務**: ロック待ちタイムアウト、デッドロックの読み方、トランザクションを短く保つ理由

### 第6章 Kotlin/MyBatis から MySQL を触る — JDBC・HikariCP・トランザクション境界
**目安 6h**

- JDBC の裏側:`Connection` = ソケット、`Statement` / `PreparedStatement`、サーバサイドプリペア
- **MyBatis(アノテーション方式)**: `@Select` / `@Insert` / `@Options(useGeneratedKeys)` / `@Param`
  - **Kotlin data class(`val`)のマッピング**: setter が無いので**コンストラクタマッピング**になる。`mapUnderscoreToCamelCase`、`useActualParamName` と `-java-parameters` コンパイラオプション、`@ConstructorArgs` の使い分け(ハマりどころとしてコラム化)
  - `@Results` / `@One` / `@Many` と **N+1 問題**、`fetchType = LAZY`、JOIN 一発に書き換える判断基準
  - SQL 文字列組み立ての注意点(`${}` と `#{}` の違い = **SQLインジェクション**、Phase 5 の伏線)
- **HikariCP**: プールサイズ設計(Phase 3 第8章の直接応用)、`maxLifetime` と MySQL の `wait_timeout` の関係、`connectionTimeout`
- 接続 URL の重要パラメータ:`rewriteBatchedStatements=true`、`cachePrepStmts`、`useServerPrepStmts`
- トランザクション境界:`SqlSession` / `@Transactional` の伝播、`autoCommit` の罠、**トランザクション内で外部API呼び出しをしない**
- **実測**: プールサイズを 1→5→20→50 と振ってスループット計測(増やせば速い、は嘘)
- **実測**: `rewriteBatchedStatements` 有/無でバッチ INSERT の時間差(桁が変わる)
- **実測**: 全接続を長時間トランザクションで占有して**プール枯渇を意図的に再現**
- **実務**: 「アプリが固まった、DB は元気そう」→ プール枯渇の切り分け手順

---

## 第3部 分散システム

### 第7章 スケールさせる — レプリケーション・シャーディング・NoSQL
**目安 6h**

- binlog ベースのレプリケーション(ROW フォーマット)、GTID、非同期 / 準同期(semi-sync)
- **レプリケーション遅延**:「書いた直後に読んだら無い」を Docker の source/replica で**実際に再現**
- フェイルオーバー、read replica への参照分離、`SHOW REPLICA STATUS` の見方
- パーティショニングとシャーディング、シャードキー選定、ホットスポット、Vitess の紹介
- RDB と NoSQL(KVS / ドキュメント / ワイドカラム)の使い分け、第3章の LSM木が効いてくる
- **実測**: replica に負荷や遅延を与えて遅延秒数の増加を観察、read-your-writes の破れを確認
- **実務**: 参照系をレプリカに逃がす設計と落とし穴(遅延を許容できない画面の見極め)

### 第8章 分散システムの現実 — CAP・結果整合性・合意・べき等性
**目安 6h**

- Phase 3 第8章「**ネットワークは失敗する前提**」がここで主役に。**「成功したか分からない」問題**
- CAP と PACELC、強整合性 vs 結果整合性、線形化可能性のイメージ
- 分散トランザクション:**2PC** とその弱点(コーディネータ障害でブロック)、**Saga** と補償トランザクション
- 合意アルゴリズム:**Raft のリーダー選出とログ複製**を図で追う、クォーラム、split brain とフェンシング
- **べき等性**と Exactly-once の幻想(at-least-once + べき等 = 実質1回)、べき等キーの設計
- 分散キャッシュと無効化(Phase 3 の DNS TTL / HTTP キャッシュの回収)、キャッシュスタンピード
- 時間の問題:物理時計は信頼できない、論理時計 / Lamport clock(Phase 0 の順序関係の回収)
- **実測**: リトライで二重登録が起きるコードを書いて再現 → べき等キーで防ぐ(Kotlin)
- **実務**: 二重課金を防ぐ設計、リトライ・タイムアウト・サーキットブレーカの組み合わせ

### 第9章 「遅い・止まった」の調べ方 — DBと分散システムのトラブル対応
**目安 5h**

Phase 3 第9章の DB 版。実務直結の総まとめ章。

- **観測ツールの棚卸し**: スロークエリログ、`performance_schema`、`sys` スキーマ、`SHOW PROCESSLIST`、pt-query-digest
- レイテンシを層で分解:アプリ → コネクションプール → ネットワーク → MySQL → InnoDB → ディスク
- **症状別の切り分けフローチャート**
  - 特定クエリだけ遅い → 実行計画・インデックス
  - 全体が遅い → バッファプール / ディスク / 接続数上限
  - 一部だけ極端に遅い(p99) → ロック待ち / チェックポイント / GC(Phase 2 回収)
  - 書けるが読めない・古い → レプリカ遅延
  - アプリだけ固まる → プール枯渇 / スレッド枯渇
- **障害シナリオ演習**:「深夜バッチ後、一部 API だけ p99 が悪化」を与えられたログから切り分ける
- Phase 3 第9章との対応表を作って、ネットワークとDBの調査手順を統合する

---

## Phase 5(セキュリティと暗号)への橋

第9章の最後で次につなげます。

- `${}` による SQL インジェクション(第6章)→ 入力検証と原理
- DB 接続情報・認証情報の管理、最小権限
- 保存データの暗号化、通信の TLS(Phase 3 第6章の回収)
- 監査ログ、個人情報の扱い

---

## 進め方のルール(Phase 2・3 から継続)

1. コード例は **Kotlin**、DB は **MySQL/InnoDB の挙動**と結びつける
2. 身近な例から入って段階的に抽象化
3. 各章で**実務との接続**(障害対応・性能調査)を明示
4. 演習は解答・解説を `<details>` 折りたたみで付ける
5. 可能な限り **Docker で実測**して数値を見る
6. **7割理解で次へ進む**。章末に「7割理解チェック」と「次章の予告」
7. 前の章・前の Phase との接続を明示(伏線回収)

## 進捗チェックリスト

- [ ] 第1章 なぜファイルではダメなのか
- [ ] 第2章 SQL はどう実行されるのか
- [ ] 第3章 インデックスとストレージ
- [ ] 第4章 トランザクションと ACID
- [ ] 第5章 並行制御
- [ ] 第6章 Kotlin/MyBatis から MySQL を触る
- [ ] 第7章 スケールさせる
- [ ] 第8章 分散システムの現実
- [ ] 第9章 「遅い・止まった」の調べ方
