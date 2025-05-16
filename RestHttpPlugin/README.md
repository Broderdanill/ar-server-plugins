# RestHttpPlugin

Dynamic REST API plugin for AR Server (BMC Helix Innovation Suite 25.1).  
Allows flexible HTTP calls (GET, POST, PUT, PATCH, DELETE) with support for headers, data, and various authentication types.

---

## ✅ Return Format

### On success:
```json
{
  "success": true,
  "status": 200,
  "body": "..."
}
```

### On error:
```json
{
  "success": false,
  "error": "HTTP request failed: ..."
}
```

---

## 🧾 Input Parameters (7)

| Index | Description            | Type    | Example                     |
|-------|------------------------|---------|-----------------------------|
| 0     | HTTP Method            | String  | `"GET"`, `"POST"`           |
| 1     | URL                    | String  | `"https://api.example.com"` |
| 2     | Headers (JSON string)  | String  | `"{\"Content-Type\": \"application/json\"}"` |
| 3     | Body/data              | String  | `"{\"key\": \"value\"}"` |
| 4     | Auth type              | String  | `"none"`, `"basic"`, `"bearer"`, `"header"`, `"cert"` |
| 5     | Auth value 1           | String  | Varies by auth type         |
| 6     | Auth value 2           | String  | Varies by auth type         |

---

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
| 5 | `"eyJhbGciOi...token"` |
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

> ⚠️ Ensure the certificate path is accessible by the plugin server and the file has correct permissions.