cat > README.md <<'EOF'
# CarlosCallagua_SMP-Pract1

## Servicios de las Plataformas Móviles - Práctica 1

Aplicación Android basada en Filmoteca, ampliada con autenticación mediante Google Sign-In y recepción de mensajes mediante Firebase Cloud Messaging.

## Funcionalidades implementadas

- Inicio de sesión con cuenta de Google.
- Acceso a datos del usuario autenticado.
- Pantalla "Acerca de" mostrando nombre y correo del usuario.
- Menú con opciones:
  - Close session
  - Disconnect account
- Integración con Firebase Cloud Messaging.
- Recepción de mensajes de datos.
- Alta de películas mediante FCM.
- Baja de películas mediante FCM.
- Notificación visible en el dispositivo/emulador.
- Apertura de la aplicación desde la notificación.
- Actualización de la fuente de datos `FilmDataSource`.

## Firebase Cloud Messaging

La aplicación recibe mensajes con datos personalizados.

### Alta de película

text
operation = alta
title = Matrix
director = Wachowski
year = 1999

Resultado esperado:

Si la película no existe, se añade.
Si ya existe, se actualizan sus datos.

Baja de Película
operation = baja
title = Matrix

Resultado esperado:

Si la película existe, se elimina.
Si no existe, no se realiza ninguna acción.

## Archivos principales

MainActivity.kt
LoginActivity.kt
AboutActivity.kt
MyFirebaseMessagingService.kt
Film.kt
FilmDataSource.kt
FilmListActivity.kt
FilmListComposeActivity.kt
AndroidManifest.xml

## Tecnologías utilizadas
Kotlin
Android SDK
Firebase Cloud Messaging
Google Sign-In
XML Views
Jetpack Compose
ViewBinding

## Evidencias de funcionamiento

Durante las pruebas se verificó:
Data: {title=Matrix, operation=baja}
Película eliminada: Matrix
Baja de película: Matrix

y:

Data: {title=Matrix, operation=baja}
Película eliminada: Matrix
Baja de película: Matrix

# Autor
Carlos Alfredo Callagua Llaque
EOF
