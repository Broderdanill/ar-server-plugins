# RestHttpPlugin

Dynamic REST API plugin for AR Server (BMC Helix Innovation Suite 25.1).  
Allows flexible HTTP calls (GET, POST, PUT, PATCH, DELETE) with support for headers, data, and various authentication types.

---

## 🧾 Input Parameters (9)

| Index | Description            | Type    | Example                     |
|-------|------------------------|---------|-----------------------------|
| 0     | HTTP Method            | String  | `"GET"`, `"POST"`           |
| 1     | URL                    | String  | `"https://api.example.com"` |
| 2     | Headers (JSON string)  | String  | `"{\"Content-Type\": \"application/json\"}"` |
| 3     | Body/data              | String  | `"{\"key\": \"value\"}"` |
| 4     | Auth type              | String  | `"none"`, `"basic"`, `"bearer"`, `"header"`, `"cert"` |
| 5     | Auth value 1           | String  | For `cert`: keystore path   |
| 6     | Auth value 2           | String  | For `cert`: keystore password |
| 7     | Truststore path        | String  | (optional) CA/truststore file |
| 8     | Truststore password    | String  | (optional) password for truststore |

---

## ✅ Return Format

The plugin returns 3 separate values mapped by result index:

| Index | Name          | Type    | Description                                  |
|-------|---------------|---------|----------------------------------------------|
| 0     | Status Code   | `int`   | HTTP status code (e.g., `200`, `401`, `500`) |
| 1     | Status Text   | `String`| Human-readable response message              |
| 2     | Result Body   | `String`| Raw response body (e.g., JSON, HTML, plain)  |


## ✅ Return Format

The plugin returns 3 separate values mapped by result index:

| Index | Name          | Type    | Description                                  |
|-------|---------------|---------|----------------------------------------------|
| 0     | Status Code   | `int`   | HTTP status code (e.g., `200`, `401`, `500`) |
| 1     | Status Text   | `String`| Human-readable response message              |
| 2     | Result Body   | `String`| Raw response body (e.g., JSON, HTML, plain)  |

### ✅ Example: Success

```text
Index 0 → 200
Index 1 → OK
Index 2 → {"data":"example","count":1}
```

### ❌ Example: Error

```text
Index 0 → 0
Index 1 → IOException
Index 2 → Exception: Connection timed out
```

> You can map these values to separate fields in your workflow for precise control and error handling.


## ⚙️ Compile

Requires `json-20250107.jar`.

```bash
javac --release 17 \
  -cp "../arapi/arapi251_build001.jar:../arapi/arutil251_build001.jar:../arapi/arpluginsvr251_build001.jar:./lib/json-20250107.jar" \
  -d classes \
  RestHttpPlugin.java
```

---

## 📦 Package

```bash
jar cf resthttpplugin.jar -C classes .
```

---

## 🧩 Plugin XML Configuration

```xml
<plugin>
  <name>RestHttpPlugin</name>
  <classname>com.example.RestHttpPlugin</classname>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/resthttpplugin.jar</pathelement>
  <pathelement type="location">/opt/bmc/ARSystem/pluginsvr/lib/json-20250107.jar</pathelement>
</plugin>
```

---

## 🧪 Examples by Auth Type

### 1. No Authentication

| Parameter | Value |
|----------|-------|
| 0 | `"GET"` |
| 1 | `"https://httpbin.org/get"` |
| 2 | `""` |
| 3 | `""` |
| 4 | `"none"` |
| 5 | `""` |
| 6 | `""` |

---

### 2. Basic Authentication

| Parameter | Value |
|----------|-------|
| 0 | `"POST"` |
| 1 | `"https://api.example.com/login"` |
| 2 | `"{\"Content-Type\": \"application/json\"}"` |
| 3 | `"{\"username\": \"admin\", \"password\": \"123\"}"` |
| 4 | `"basic"` |
| 5 | `"admin"` |
| 6 | `"123"` |

---

### 3. Bearer Token

| Parameter | Value |
|----------|-------|
| 0 | `"GET"` |
| 1 | `"https://httpbin.org/bearer"` |
| 2 | `""` |
| 3 | `""` |
| 4 | `"bearer"` |
| 5 | `"AR-JWT eyJhbGciOi..."` |
| 6 | `""` |

---

### 4. Custom Header Authentication

| Parameter | Value |
|----------|-------|
| 0 | `"GET"` |
| 1 | `"https://api.example.com/data"` |
| 2 | `""` |
| 3 | `""` |
| 4 | `"header"` |
| 5 | `"X-API-Key: my-secret"` |
| 6 | `""` |

---

### 5. Certificate Authentication (mutual TLS)

| Parameter | Value |
|----------|-------|
| 0 | `"GET"` |
| 1 | `"https://secure.example.com/securedata"` |
| 2 | `""` |
| 3 | `""` |
| 4 | `"cert"` |
| 5 | `"/opt/certs/client.p12"` |
| 6 | `"cert-password"` |

> ⚠️ Ensure the certificate file exists and is readable by the plugin server.

---

### 6. Certificate + Truststore (mutual TLS)

| Parameter | Example |
|----------|---------|
| 0 | `"GET"` |
| 1 | `"https://secure.example.com/securedata"` |
| 2 | `""` |
| 3 | `""` |
| 4 | `"cert"` |
| 5 | `"/opt/certs/client.p12"` |
| 6 | `"clientpass"` |
| 7 | `"/opt/certs/truststore.jks"` |
| 8 | `"trustpass"` |

> 🛡️ If truststore is not specified, JVM default is used.

---

### 7. `application/x-www-form-urlencoded` (POST form body)

| Parameter | Value |
|----------|-------|
| 0 | `"POST"` |
| 1 | `"https://api.example.com/login"` |
| 2 | `"{\"Content-Type\": \"application/x-www-form-urlencoded\"}"` |
| 3 | `"username=admin&password=123"` |
| 4 | `"none"` |
| 5 | `""` |
| 6 | `""` |

> 📝 Body must be formatted as key=value&key2=value2. This is commonly used for form submissions.