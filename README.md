# Informe Detallado de la Aplicación de Gestión de Mascotas

## 1. Introducción y Flujo General

Esta aplicación móvil Android permite a los usuarios registrarse, iniciar sesión y gestionar información sobre sus mascotas. Utiliza Firebase Authentication para la autenticación de usuarios y Firebase Firestore como base de datos para almacenar los datos de las mascotas. La interfaz de usuario ha sido modernizada recientemente con un tema verde predominante y elementos visuales mejorados.

El flujo principal de la aplicación es el siguiente:

1.  **Pantalla de Splash (`SplashActivity`):** Se muestra brevemente al iniciar la aplicación.
2.  **Pantalla de Login/Registro (`Login`):** Permite a los usuarios existentes iniciar sesión o a los nuevos usuarios registrarse. La autenticación se realiza con Firebase, incluyendo verificación por correo electrónico.
3.  **Pantalla de Bienvenida (`Bienvenida`):** Una vez autenticado, el usuario accede a esta pantalla que sirve como menú principal, ofreciendo opciones como registrar una nueva mascota o ver las mascotas existentes.
4.  **Gestión de Mascotas:**
    - **Formulario de Mascotas (`FormPets`):** Para registrar los detalles de una nueva mascota.
    - **Visualización de Mascotas (`PetsTar` Activity conteniendo `PetsFragment`):** Muestra una lista de las mascotas registradas por el usuario.
    - **Detalle y Edición de Mascota (`PetsClinic` Fragment):** Permite ver, modificar o eliminar los datos de una mascota seleccionada.

## 2. Componentes Principales de la Aplicación

A continuación, se detallan las principales Activities, Fragments, Layouts y otros componentes clave.

### 2.1. Activities

#### 2.1.1. `SplashActivity.kt`

- **Layout Asociado:** `layout_splash.xml`
- **Propósito:** Es la primera pantalla que ve el usuario al abrir la aplicación. Muestra una imagen o logo de bienvenida durante un breve período (2 segundos).
- **Funcionamiento:**
  - Infla `layout_splash.xml`.
  - Utiliza un `Handler` para introducir un retraso.
  - Después del retraso, navega automáticamente a `Login` Activity y finaliza `SplashActivity` para que el usuario no pueda volver a ella.
- **Layout (`layout_splash.xml`):**
  - Contiene una imagen de fondo (`@drawable/login` o similar) que ocupa toda la pantalla, proporcionando una bienvenida visual.

#### 2.1.2. `Login.kt`

- **Layout Asociado:** `layout_login.xml`
- **Propósito:** Gestiona la autenticación de usuarios, permitiendo el inicio de sesión para usuarios existentes y el registro para nuevos usuarios.
- **Funcionamiento:**
  - Infla `layout_login.xml`.
  - Inicializa los campos de texto para email (`TextEmail`) y contraseña (`TextPassword`), y los botones de login (`Btnlogin`) y registro (`Btnregistrar`).
  - **Registro:**
    - Al pulsar "Registrar", obtiene el email y la contraseña.
    - Valida que la contraseña tenga al menos 6 caracteres.
    - Llama a `auth.createUserWithEmailAndPassword()` de Firebase.
    - Si el registro es exitoso, envía un correo de verificación (`user.sendEmailVerification()`) y muestra un mensaje al usuario.
  - **Inicio de Sesión:**
    - Al pulsar "Login", obtiene el email y la contraseña.
    - Valida que los campos no estén vacíos.
    - Llama a `auth.signInWithEmailAndPassword()` de Firebase.
    - Si el inicio de sesión es exitoso y el email del usuario está verificado (`user.isEmailVerified`), navega a la `Bienvenida` Activity, pasando el correo del usuario como extra.
    - Muestra mensajes de error o informativos al usuario mediante `Toast`.
- **Layout (`layout_login.xml`):**
  - Utiliza `ConstraintLayout` con un fondo verde lima (`#32CD32`).
  - Un `ImageView` (`imageView`) en la parte superior, configurado para ser responsivo.
  - Dos `EditText` (`TextEmail`, `TextPassword`) para la entrada de credenciales, estilizados con fondo blanco redondeado (`@drawable/bg_edittext_rounded_white`), texto negro y hints grises. Tienen márgenes para separación.
  - Un `TextView` (`textCorreo3`) con el texto "¿No tienes cuenta? Regístrate", con un efecto de sombreado para resaltar sobre un fondo claro.
  - Dos `Button` (`Btnlogin`, `Btnregistrar`) estilizados con fondo verde oscuro redondeado (`@drawable/bg_button_rounded_corners_green`) y texto blanco. Están distribuidos horizontalmente usando una `Guideline`.

#### 2.1.3. `Bienvenida.kt`

- **Layout Asociado:** `layout_bienvenida.xml`
- **Propósito:** Pantalla principal después de un inicio de sesión exitoso. Proporciona opciones de navegación a otras funcionalidades de la aplicación.
- **Funcionamiento:**
  - Infla `layout_bienvenida.xml`.
  - Recibe el correo del usuario desde `Login` Activity (a través de `intent.getStringExtra("Correo")`).
  - Configura listeners para los botones:
    - `ImageButton` (`bntFormPetsPrinc`): Navega a `FormPets` Activity para registrar una nueva mascota.
    - `Button` (`bntFormPets`): (Su funcionalidad exacta podría variar, pero por nombre sugiere ir al formulario de mascotas o a la lista).
    - `Button` (`bntFormPets2`): Navega a `PetsTar` Activity para ver la lista de mascotas registradas.
- **Layout (`layout_bienvenida.xml`):**
  - Usa `ConstraintLayout` con un fondo de imagen (`@drawable/Bienvenida`).
  - Un `TextView` (`textBienvenida`) que muestra un mensaje de bienvenida, con texto blanco.
  - Un `ImageButton` (`bntFormPetsPrinc`) redondo con fondo verde oscuro (`@drawable/bg_button_round_green`) y una imagen (ej. `@drawable/iconpets`), probablemente para añadir mascotas.
  - Dos `Button` (`bntFormPets`, `bntFormPets2`) estilizados con fondo verde oscuro y esquinas redondeadas (`@drawable/bg_button_rounded_corners_green`) y texto blanco. Estos botones permiten acceder a diferentes secciones.
  - Otros `TextViews` (`textNamePrinc`, `textNamePrinc2`) con texto blanco para describir las acciones de los botones.

#### 2.1.4. `FormPets.kt`

- **Layout Asociado:** `layout_formpets.xml`
- **Propósito:** Permite al usuario registrar los detalles de una nueva mascota.
- **Funcionamiento:**
  - Infla `layout_formpets.xml`.
  - Inicializa los `EditText` para nombre, edad, vacunas, dueño y veterinario, y los botones de guardar y atrás.
  - **Guardar Mascota:**
    - Al pulsar "Guardar" (`btnGuardar`), obtiene los datos de los `EditTexts`.
    - Realiza una validación básica (ej. el nombre de la mascota es obligatorio).
    - Obtiene el ID del usuario actual de Firebase Auth (`FirebaseAuth.getInstance().currentUser?.uid`).
    - Crea un objeto `HashMap` con los datos de la mascota, incluyendo el `userId` y un `mascotaId` generado por Firestore.
    - Guarda la mascota en la colección "mascotas" de Firestore usando `db.collection("mascotas").document().set(mascota)`.
    - Muestra mensajes de éxito o error y finaliza la actividad si el guardado es exitoso.
  - **Botón Atrás (`btnAtras`):** Navega de vuelta a `Bienvenida` Activity.
- **Layout (`layout_formpets.xml`):**
  - Utiliza `ConstraintLayout` con una imagen de fondo (`@drawable/nuevamascota`) que ocupa toda la pantalla.
  - Un `ImageButton` (`btnAtras`) redondo con fondo verde oscuro (`@drawable/bg_button_round_green`) e icono de flecha, ubicado en la esquina superior derecha para volver.
  - Cinco `EditText` (`editNombre`, `editEdad`, `editVacunas`, `editDueno`, `editVeterinario`) para ingresar los datos de la mascota. Están estilizados con fondo blanco redondeado (`@drawable/bg_edittext_rounded_white`) y tienen márgenes de `16dp`. Están encadenados verticalmente y distribuidos.
  - Un `Button` (`btnGuardar`) estilizado con fondo verde oscuro redondeado (`@drawable/bg_button_rounded_corners_green`), texto blanco y ancho `match_parent` con márgenes, ubicado en la parte inferior.

#### 2.1.5. `PetsTar.kt`

- **Layout Asociado:** `layout_petstar.xml`
- **Propósito:** Actúa como una actividad contenedora para los fragmentos relacionados con la visualización y gestión de mascotas (`PetsFragment` y `PetsClinic`).
- **Funcionamiento:**
  - Infla `layout_petstar.xml`.
  - En su método `onCreate`, si `savedInstanceState` es nulo (es decir, la actividad se crea por primera vez), inicia una transacción de fragmentos para cargar `PetsFragment` dentro de un contenedor (probablemente un `FrameLayout` con ID `R.id.fragment_mascotas`).
- **Layout (`layout_petstar.xml`):**
  - Su contenido principal es un `FrameLayout` (con ID `R.id.fragment_mascotas`) que sirve como anfitrión para los fragmentos `PetsFragment` y `PetsClinic`.

### 2.2. Fragments

#### 2.2.1. `PetsFragment.kt`

- **Layout Asociado:** `fragment_pets.xml`
- **Propósito:** Muestra una lista de todas las mascotas registradas por el usuario.
- **Funcionamiento:**
  - Infla `fragment_pets.xml`.
  - Inicializa un `RecyclerView` (`recyclerViewMascotas`) y su `LinearLayoutManager`.
  - Llama a `cargarMascotas()`:
    - Obtiene los documentos de la colección "mascotas" de Firestore.
    - Para cada documento, lo convierte a un objeto `Pet` y asigna el `document.id` al campo `mascotaId` del objeto.
    - Crea una instancia de `PetsAdapter` con la lista de mascotas obtenida.
    - Configura un listener de clic para el adaptador: cuando se selecciona una mascota, crea una instancia de `PetsClinic.newInstance(mascota.mascotaId)` y la reemplaza en el contenedor de fragmentos (ID `R.id.fragment_mascotas`), añadiendo la transacción al `backStack`.
    - Asigna el adaptador al `RecyclerView`.
- **Layout (`fragment_pets.xml`):**
  - Usa un `FrameLayout` como raíz con fondo verde lima (`#32CD32`).
  - Contiene un `RecyclerView` (`recyclerViewMascotas`) que ocupa todo el espacio, configurado con `android:clipToPadding="false"` para un mejor efecto visual con el padding.

#### 2.2.2. `PetsClinic.kt`

- **Layout Asociado:** `fragment_pets_clinic.xml`
- **Propósito:** Permite ver, editar y eliminar los detalles de una mascota específica.
- **Funcionamiento:**
  - Utiliza un método factoría `newInstance(mascotaId: String)` para recibir el ID de la mascota a través de los argumentos del fragmento.
  - En `onCreateView`:
    - Infla `fragment_pets_clinic.xml`.
    - Obtiene el `mascotaId` de los argumentos.
    - Inicializa los `EditText` (nombre, edad, vacunas, dueño, veterinario) y los botones (guardar, volver, eliminar).
    - Si `mascotaId` no está vacío, llama a `cargarMascota()`.
  - `cargarMascota()`:
    - Obtiene el documento de la mascota desde Firestore usando `mascotaId`.
    - Si se encuentra, mapea los datos a los `EditText` correspondientes.
  - `actualizarMascota()`:
    - Al pulsar "Guardar" (`btnGuardar`), obtiene los datos de los `EditTexts`.
    - Realiza validaciones básicas (nombre y edad no vacíos).
    - Crea un `HashMap` con los datos actualizados.
    - Actualiza el documento en Firestore usando `db.collection("mascotas").document(mascotaId).update(updatedMascota)`.
    - Muestra un `Toast` y navega hacia atrás (`parentFragmentManager.popBackStack()`).
  - `eliminarMascota()`:
    - Al pulsar "Eliminar" (`btnEliminar`), elimina el documento de Firestore usando `db.collection("mascotas").document(mascotaId).delete()`.
    - Muestra un `Toast` y navega hacia atrás.
  - Botón Volver (`btnVolver`): Navega hacia atrás en la pila de fragmentos (`parentFragmentManager.popBackStack()`).
- **Layout (`fragment_pets_clinic.xml`):**
  - Usa `ConstraintLayout` con una imagen de fondo (`@drawable/nuevamascota`).
  - Un `ImageButton` (`btnVolver`) redondo con fondo verde oscuro, ubicado en la esquina superior derecha.
  - Cinco `EditText` (`editNombre`, `editEdad`, `editVacunas`, `editDueno`, `editVeterinario`) estilizados con fondo blanco redondeado, con márgenes, y encadenados verticalmente.
  - Dos `Button` (`btnGuardar`, `btnEliminar`) estilizados con fondo verde oscuro redondeado y texto blanco, distribuidos horizontalmente en la parte inferior mediante una `Guideline`.

### 2.3. Adapters

#### 2.3.1. `PetsAdapter.kt`

- **Layout de Ítem Asociado:** `layout_tarjetamascota.xml`
- **Propósito:** Adapta una lista de objetos `Pet` para ser mostrada en un `RecyclerView`.
- **Funcionamiento:**
  - Recibe una lista de `Pet` y una lambda `onClick` en su constructor.
  - **`MascotaViewHolder` (clase interna):**
    - Mantiene las referencias a los `ImageView` y `TextViews` dentro de `layout_tarjetamascota.xml` (para imagen, nombre, edad, vacunas, dueño, veterinario).
  - **`onCreateViewHolder()`:** Infla `layout_tarjetamascota.xml` para crear la vista de cada ítem.
  - **`onBindViewHolder()`:**
    - Obtiene el objeto `Pet` para la posición actual.
    - Asigna los datos de la mascota (nombre, edad, vacunas, etc.) a los `TextViews` del `holder`.
    - Utiliza la librería `Glide` para cargar la imagen de la mascota (`mascota.fotoUrl`) en el `ImageView`. Si `fotoUrl` está vacía o hay un error, muestra una imagen por defecto (`R.drawable.mascota_default1`).
    - Configura un `OnClickListener` en el `itemView` del `holder` que ejecuta la lambda `onClick` pasada al constructor, proveyendo el objeto `Pet` seleccionado.
  - **`getItemCount()`:** Devuelve el tamaño de la `listaMascotas`.
- **Layout (`layout_tarjetamascota.xml`):**
  - Utiliza un `CardView` como elemento raíz, con un color de fondo verde oscuro (`#228B22`) y esquinas redondeadas.
  - Dentro del `CardView`, un `ConstraintLayout` organiza los elementos.
  - Un `ImageView` (`imgMascota`) para mostrar la foto de la mascota.
  - Varios `TextView` (`txtNombre`, `txtEdad`, `txtVacunas`, `txtDueno`, `txtVeterinario`) para mostrar los detalles de la mascota, con texto de color blanco para mejorar la legibilidad sobre el fondo oscuro. Se han ajustado paddings y tamaños de fuente para una mejor presentación.

### 2.4. Modelos de Datos (Data Classes)

#### 2.4.1. `Pet.kt` (Suposición basada en el uso)

- **Propósito:** Clase de datos (presumiblemente un `data class` en Kotlin) que representa la estructura de una mascota.
- **Campos esperados (basado en el uso en adaptadores y fragmentos):**
  - `mascotaId: String` (ID del documento en Firestore)
  - `nombreMascota: String`
  - `edadMascota: Int` (o `String` si no se convierte estrictamente)
  - `vacunas: String`
  - `nombreDueno: String`
  - `nombreVeterinario: String`
  - `fotoUrl: String` (URL de la imagen de la mascota)
  - `userId: String` (ID del usuario dueño de la mascota)

### 2.5. Recursos Gráficos (Drawables)

- **`bg_button_round_green.xml`:** Define un fondo de forma ovalada (círculo si ancho y alto son iguales) con color verde oscuro (`#228B22`). Usado para `ImageButton` redondos.
- **`bg_button_rounded_corners_green.xml`:** Define un fondo rectangular con esquinas redondeadas y color verde oscuro (`#228B22`). Usado para botones estándar.
- **`bg_edittext_rounded_white.xml`:** Define un fondo rectangular con esquinas redondeadas y color blanco. Usado para los campos `EditText`.
- **Imágenes de fondo:**
  - `@drawable/Bienvenida`: Usada en `layout_bienvenida.xml`.
  - `@drawable/nuevamascota`: Usada en `layout_formpets.xml` y `fragment_pets_clinic.xml`.
  - `@drawable/login`: Usada en `layout_splash.xml`.
- **Iconos:**
  - `@drawable/iconpets` (o similar): Usado en `ImageButton` en `layout_bienvenida.xml`.
  - Icono de flecha para el botón de "atrás".
- **Imágenes por defecto:**
  - `@drawable/mascota_default1`: Usada en `PetsAdapter` cuando no hay `fotoUrl` o hay un error de carga.

## 3. Interacción con Firebase

- **Firebase Authentication:**
  - Utilizada en `Login.kt` para el registro de nuevos usuarios (`createUserWithEmailAndPassword`) y el inicio de sesión de usuarios existentes (`signInWithEmailAndPassword`).
  - Se gestiona la verificación por correo electrónico.
- **Firebase Firestore:**
  - Utilizada como base de datos NoSQL para almacenar la información de las mascotas.
  - **Colección `mascotas`:** Todos los datos de las mascotas se guardan en documentos dentro de esta colección.
  - **`FormPets.kt`:** Crea nuevos documentos en la colección `mascotas` al registrar una nueva mascota.
  - **`PetsFragment.kt`:** Lee todos los documentos de la colección `mascotas` para mostrarlos en una lista.
  - **`PetsClinic.kt`:**
    - Lee un documento específico de la colección `mascotas` usando su `mascotaId`.
    - Actualiza un documento existente usando `update()`.
    - Elimina un documento existente usando `delete()`.
