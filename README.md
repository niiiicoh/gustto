# Gustto 🍽️

Gustto es una aplicación móvil Android orientada al descubrimiento y localización de establecimientos gastronómicos de La Serena y Coquimbo.

La aplicación permite explorar restaurantes, cafeterías, pastelerías y otros locales mediante Google Maps, realizar búsquedas y filtros por gastronomía o tipo de establecimiento, guardar favoritos, consultar establecimientos visitados recientemente y visualizar información detallada como ubicación, horarios, reseñas y calificaciones.

Los usuarios registrados pueden publicar y administrar sus propias reseñas, mientras que los administradores cuentan con herramientas para gestionar establecimientos y moderar contenido.

## Tecnologías

- Java
- XML
- Android Studio
- SQLite
- Firebase Authentication
- Firebase SQL Connect
- PostgreSQL
- Google Maps SDK for Android

## Configurar otro equipo

1. Abre el proyecto en Android Studio e instala el SDK Android 37. Las pruebas requieren JDK 21.
2. Descarga `google-services.json` de la app Android `com.example.gustto` del proyecto Firebase `gustto-18263` y colócalo en `app/`.
3. Android Studio configura `sdk.dir` en `local.properties`. Añade `MAPS_API_KEY=TU_CLAVE`, sin comillas.
4. Habilita Maps SDK for Android y restringe la clave al paquete y al SHA-1 del certificado de firma. Obtén el SHA-1 de cada equipo con `./gradlew.bat :app:signingReport` en PowerShell.
5. Sincroniza Gradle y ejecuta la app.

Estos archivos locales están excluidos de Git y deben configurarse en cada equipo. No subas claves de firma ni credenciales administrativas. Tampoco se versionan compilaciones, cachés ni `.idea/`.

Firebase funciona en la nube: ejecutar la app no requiere iniciar sesión en Firebase CLI ni volver a desplegar. Todos los equipos configurados con este proyecto acceden al mismo backend. Los datos SQLite permanecen en cada dispositivo.

## Backend Firebase

SQL Connect usa el servicio `gustto`, región `southamerica-west1`, conector `app` y base `gustto` de la instancia `gustto-postgres`. El esquema y las operaciones están en `dataconnect/`. El SDK Kotlin generado en `app/src/main/java/com/example/gustto/generated/` sí se versiona porque la app lo necesita para compilar.

Para administrar o desplegar cambios con una cuenta autorizada:

```powershell
npx --yes firebase-tools login
npx --yes firebase-tools deploy --only dataconnect --project gustto-18263
npx --yes firebase-tools dataconnect:sdk:generate --project gustto-18263
```

Revisa las migraciones antes de confirmarlas. Si la CLI devuelve `404 Method not found` para `schemas/main:migrate` tras anunciar una migración experimental, desactiva esa ruta con `npx --yes firebase-tools experiments:disable fdcapimigration` y reintenta. Esta preferencia es local a cada equipo.

El despliegue no carga catálogos ni restaurantes. También hay que habilitar correo/contraseña en Firebase Authentication y asignar el rol de administrador desde una herramienta administrativa a la cuenta correspondiente.
