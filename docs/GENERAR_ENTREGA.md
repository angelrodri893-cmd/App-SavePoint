# Generación de APK y AAB

Los binarios y las credenciales de firma son artefactos locales. No deben confirmarse en Git.

## 1. Verificación previa

Desde la raíz del proyecto:

```powershell
.\gradlew.bat test
.\gradlew.bat assembleDebug
```

El APK de prueba se genera en:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## 2. Crear una clave de entrega

La opción recomendada para la entrega académica es Android Studio:

1. Abrir `Build > Generate Signed App Bundle or APK`.
2. Elegir `Android App Bundle`.
3. Seleccionar `Create new` y guardar el archivo dentro de `keystore/`.
4. Conservar las contraseñas fuera del repositorio.

También se puede crear con `keytool`:

```powershell
keytool -genkeypair -v -keystore keystore/savepoint-release.jks -alias savepoint -keyalg RSA -keysize 2048 -validity 10000
```

## 3. Configurar Gradle localmente

Copiar `keystore.properties.example` como `keystore.properties` y reemplazar sus valores:

```properties
storeFile=keystore/savepoint-release.jks
storePassword=CONTRASENA_LOCAL
keyAlias=savepoint
keyPassword=CONTRASENA_LOCAL
```

`.gitignore` excluye `keystore.properties`, `*.jks`, `*.keystore`, `*.apk` y `*.aab`.

## 4. Generar la entrega firmada

```powershell
.\gradlew.bat assembleRelease
.\gradlew.bat bundleRelease
```

Salidas esperadas:

```text
app/build/outputs/apk/release/app-release.apk
app/build/outputs/bundle/release/app-release.aab
```

Si `keystore.properties` no existe, Gradle permite validar R8 y producir un AAB sin firma, pero el APK se genera con el sufijo `-unsigned`. Ese resultado no es la entrega final.

## 5. Comprobación final

- Instalar el APK release en un dispositivo físico.
- Abrir Explorar, Biblioteca, Diario y Ajustes.
- Probar el catálogo con y sin conexión.
- Probar la cámara concediendo y rechazando el permiso.
- Confirmar que el color y el orden continúan después de reiniciar la app.
- Guardar una copia segura del keystore y sus contraseñas.
