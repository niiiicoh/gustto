# Plan de implementación

## Inspección inicial
Plantilla Android Java/XML, `com.example.gustto`. AGP 9.4, Gradle 9.6,
compile/target SDK 37, mínimo 24, Java fuente 11, JVM de Gradle 25.
Una Activity sin funcionalidad, sin credenciales, sin backend previo.
Se conserva el package y el toolchain existente. En Windows se habilita
`android.overridePathCheck` para trabajar en la ruta solicitada con acentos.

## Etapas
1. Tema, recursos XML, navegación inferior y Drawer.
2. Modelos Java, SQLiteOpenHelper, caché y adapters reutilizables.
3. Authentication, esquema relacional y operaciones SQL Connect autorizadas.
4. Repositorios e interoperabilidad con el SDK generado.
5. Explorar, filtros, detalle, favoritos e historial.
6. Maps, GPS opcional, selección administrativa y navegación externa.
7. Reseñas, perfil, administración y horarios.
8. Compilación, pruebas y documentación de configuración externa.

## Referencia visual aprobada
La imagen aportada manda sobre recomendaciones genéricas de skills.
No se genera otra identidad ni se incorporan patrones web/GSAP a Android.
Paleta: borgoña #802F3E, terracota #D86A47, crema #FBF7F1,
oliva #5E8064, carbón #352A2B, rosa #F7E7E6.
Títulos editoriales serif; texto y controles sans-serif. Márgenes 20dp,
ritmo 8dp, tarjetas blancas con radios 16dp y sombra suave.
Controles táctiles >=48dp; texto secundario más contrastado que el mockup.
Inicio: cabecera borgoña, búsqueda, categorías, mapa y locales.
Explorar/favoritos/historial: listas con imágenes y controles de guardado.
Detalle: fotografía amplia y contenido editorial. Formularios: labels visibles.
La app muestra estados reales de falta de configuración; no simula login o rol.
