# APPCrafters
Proyecto de MemoryCrafters con uso de RXJava y Android

Este proyecto es un minijuego de memory que pretende experimentar el entorno Android junto con el uso de Java, también existe la posibilidad de hacerlo en Kotlin

## Flujo de Trabajo

### 1. Clonar el Repositorio

Primero, clona el repositorio desde la rama principal (Main) a tu máquina local.

``` bash
git clone https://github.com/Warrio111/MemoryCrafters.git
cd MemoryCrafters
```
### 2. Actualizar la Rama Main
Asegúrate de tener la última versión de la rama Main antes de empezar a trabajar en tu desarrollo.

```bash

git checkout main
git pull origin main
```
### 3. Crear una Rama de Desarrollo
Para cualquier nuevo desarrollo en la aplicación, crea una nueva rama de desarrollo desde Main. Asegúrate de darle un nombre descriptivo, por ejemplo, dev/nueva-funcionalidad.

```bash

git checkout -b dev/nueva-funcionalidad
```
### 4. Trabajar en tu Desarrollo
Realiza tus cambios y desarrollos en esta nueva rama. Asegúrate de escribir pruebas unitarias asociadas y verificar que todo funcione correctamente.

### 5. Actualizar desde Main
Antes de finalizar tu desarrollo, asegúrate de estar al día con la rama Main.

```bash
git checkout main
git pull origin main
```
### 6. Hacer un Merge de Main a tu Rama de Desarrollo
Integra cualquier cambio nuevo de Main en tu rama de desarrollo antes de finalizar.

```bash

git checkout dev/nueva-funcionalidad
git merge main
```
Resuelve cualquier conflicto si es necesario.

### 7. Enviar una Solicitud de Extracción (Pull Request)
Cuando tu desarrollo esté completo y funcionando correctamente, sube tu rama de desarrollo a GitHub y crea una Solicitud de Extracción (Pull Request) hacia la rama Main. Asigna a Robert Benavides como revisor.

### 8. Revisión y Aprobación
Robert Benavides revisará tu código y, una vez aprobado, se fusionará con la rama Main.

# Contacto
Si tienes alguna pregunta o necesitas ayuda, no dudes en ponerte en contacto con el equipo de desarrollo.

Este flujo de trabajo te ayudará a mantener un proyecto ordenado y colaborativo, asegurando que todos los cambios se integren correctamente y que la rama Main siempre esté en buen estado.

# Resources
https://github.com/firebase/snippets-android/blob/36ead7df6890f5888b44097b61ac6eb5f95e5f2f/auth/app/src/main/res/layout/activity_firebase_ui.xml
https://github.com/gradle/wrapper-validation-action
https://medium.com/empathyco/applying-ci-cd-using-github-actions-for-android-1231e40cc52f
https://github.com/MGamalE/Android-CICD/tree/master
https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md
https://medium.com/realm/startactivityforresult-is-deprecated-82888d149f5d
https://stackoverflow.com/questions/45977847/make-sure-to-call-firebaseapp-initializeappcontext-first-in-android
https://www.youtube.com/watch?v=troDCp2t2fQ
https://github.com/firebase/firebase-android-sdk/issues/4693
https://stackoverflow.com/questions/33866061/error-file-google-services-json-is-missing-from-module-root-folder-the-google
https://stackoverflow.com/questions/78103881/firebase-auth-failed-resolution-of-lcom-google-android-gms-auth-api-credential
https://www.youtube.com/watch?v=Jt-Cv-s9AR0
https://www.youtube.com/watch?v=YQ0fJUiOYbY
