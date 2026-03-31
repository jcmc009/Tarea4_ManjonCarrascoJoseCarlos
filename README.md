# 🐉 Spyro the Dragon - Guía Interactiva

## 📝 Introducción
Esta aplicación es una guía interactiva y visual sobre el universo de **Spyro the Dragon**. Su propósito principal es enseñar a los usuarios a navegar por la interfaz de la aplicación mediante un tutorial inmersivo y guiado, mientras exploran a los personajes, los mundos mágicos y los coleccionables del juego. Además, esconde secretos interactivos para los más curiosos.

## ✨ Características principales
* **🗺️ Tutorial Interactivo Animado:** Una guía paso a paso que se muestra la primera vez que se abre la app. Enseña a usar la navegación mediante bocadillos informativos con diferentes animaciones (latido, flotación, zoom y balanceo).
* **🎵 Efectos de Sonido:** Integración de efectos sonoros temáticos al avanzar por las pantallas de la guía, omitir el tutorial o finalizarlo con éxito.
* **💾 Memoria de Usuario (Mostrado Único):** La aplicación recuerda si el usuario ya ha completado u omitido la guía utilizando almacenamiento persistente, saltándosela en futuros inicios.
* **🧭 Navegación Fluida:** Uso de un `BottomNavigationView` para cambiar entre las secciones de Personajes, Mundos y Coleccionables de forma automática e intuitiva.
* **🎁 Easter Eggs (Secretos Ocultos):**
  * **Video Cinematográfico:** Al pulsar 3 veces rápidas sobre un mundo específico, se reproduce un vídeo a pantalla completa.
  * **Animación Mágica Personalizada:** Al mantener pulsado sobre el villano Ripto en la pestaña de personajes, se desata una animación gráfica generada por código que simula la magia de su cetro.

## 🛠️ Tecnologías utilizadas
El proyecto ha sido desarrollado íntegramente en el ecosistema Android moderno:
* **Lenguaje:** Kotlin
* **Interfaz y Vistas:** XML, `ConstraintLayout`, y `ViewBinding` para una conexión segura entre el código y el diseño.
* **Navegación:** `Navigation Component` (`NavHostFragment`) para la gestión ágil de los fragmentos.
* **Almacenamiento:** `SharedPreferences` para guardar el estado del tutorial.
* **Multimedia:** `MediaPlayer` para la gestión de efectos de sonido cortos y `VideoView` para la reproducción del Easter Egg.
* **Gráficos Avanzados:** Creación de una *Custom View* dibujando directamente sobre el `Canvas` de Android y animándola con `ValueAnimator` (matemáticas y trigonometría para el efecto de Ripto).
* **Animaciones de Interfaz:** Uso de clases de animación de Android (`AlphaAnimation`, `TranslateAnimation`, `ScaleAnimation`, `RotateAnimation`).

## 🚀 Instrucciones de uso
Para probar este proyecto en tu entorno local, sigue estos pasos:

1. **Requisitos previos:** Debes tener instalado [Android Studio](https://developer.android.com/studio) (versión Hedgehog o superior recomendada).
2. **Clonar el repositorio:**
   Abre tu terminal y ejecuta:
   ```bash
   git clone [https://github.com/jcmc009/Tarea4_ManjonCarrascoJoseCarlos.git]
