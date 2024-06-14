# 1. Non-SSL-Client と Non-SSL-Server

これらの通信を lo0 で Wireshark から覗くと、[PSH ACK]と書かれているところで情報が漏洩していることがわかる
なにも気にせずに Socket 通信をすると、外部から情報が漏れる

# 2. SSL 鍵の作り方

JDK の機能のひとつ

## 2.1. keystore の作成（サーバー）

```
keytool -genkey -keyalg RSA -keystore keystore.jks -keysize 2048
```

## 2.2. 生成された証明書をファイルにエクスポートし、クライアントにコピー

```
eytool -export -keystore keystore.jks -file server.crt && cp ./server.crt  ../client/
```

## 2.3. truststore を作成し、そこに server.crt を追加（クライアント）

クライアント側ではサーバーから提供された crt を使う（本来は第三者の証明書を利用する）

```
keytool -import -file server.crt -keystore truststore.jks
```

keystore.jks と truststore.jks が作成される。keystore.jks はサーバーの秘密鍵や証明書を保持し、truststore.jks は信頼できる証明書を保持。

その上で、ソースコードを変更すればできる。

# 3. crt の交換どうする？

普通はブラウザから提供される
