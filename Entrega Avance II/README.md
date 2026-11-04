# Proyecto DS-II Avance I

Proyecto Java Swing para IntelliJ IDEA, sin Maven. El punto de entrada es `src/Main.java`.

## Abrir y ejecutar

1. Extrae el ZIP y abre la carpeta `Proyecto DS-II Avance I` en IntelliJ IDEA.
2. Configura el JDK 26 definido en el proyecto, o un JDK compatible con las bibliotecas incluidas.
3. IntelliJ carga las bibliotecas locales desde `.idea/libraries`. Si tu versión no las importa automáticamente, agrega todos los JAR de `lib` a las dependencias del módulo.
4. Ejecuta `Main` y autoriza la cámara para aplicaciones de escritorio en la configuración de privacidad de Windows.

Se incluyen binarios nativos de OpenCV/JavaCPP para Windows de 64 bits. Para otra plataforma se deben reemplazar los JAR con clasificador `windows-x86_64` por los nativos correspondientes.

## Flujo de autenticación

- El registro valida nombre de usuario, correo, contraseña y confirmación. La cuenta se guarda después de capturar un rostro con la cámara.
- La pantalla facial muestra video en vivo, detecta el rostro con Haar y captura el área detectada. El acceso facial compara un descriptor local LBP con las cuentas registradas.
- El inicio de sesión está disponible con contraseña o con el rostro capturado en vivo.
- `Recordarme` crea un token aleatorio para iniciar automáticamente en la siguiente sesión. El token queda en el perfil local de Windows (`%LOCALAPPDATA%\\MemoryGame`); en el archivo de cuentas solo se guarda el hash del token. No se conserva la contraseña.
- Las cuentas y las plantillas faciales se guardan localmente en `data/usuarios.csv`. La contraseña se almacena con PBKDF2-HMAC-SHA256 y una sal individual; la carpeta `data/` se excluye de Git.
- La cámara se usa para detectar y calcular la plantilla; el fotograma y la imagen del rostro se mantienen en memoria y no se guardan como archivos.

La comparación LBP es una solución académica local; no equivale a un sistema biométrico de producción y no incorpora prueba de vida contra fotografías o videos.

## Estructura MVC

- `model`: usuario, persistencia local, validación, captura/detección OpenCV, reglas de Patrones y Memoria, consulta del API SDDE del BCCR y música WAV en bucle mediante un hilo dedicado.
- `view`: cada pantalla y componente está en su propio archivo, incluidos bienvenida, selector de modo, tableros y premios.
- En inicio de sesión, registro y confirmación, la contraseña empieza oculta; el usuario puede pulsar el icono de ojo para mostrarla u ocultarla.
- `controller`: validación de datos, coordinación de autenticación y lógica de los modos de juego.
- `resources/referencias`: recursos visuales actualizados, logo, fuentes e iconos.
- `resources/recursos`: clasificador Haar, recursos del Memory Game y `cartas/` con el reverso (`1.png`) y las 18 caras (`2.png` a `19.png`).
- `lib`: bibliotecas Java y nativas OpenCV/JavaCPP para Windows x64.

## Dependencias

- OpenCV JavaCPP 4.14.0, JavaCPP 1.5.14 y OpenBLAS 0.3.34, descargados de Maven Central para integrarlos directamente en IntelliJ sin Maven.
- `haarcascade_frontalface_default.xml`, del repositorio oficial de OpenCV.
- Más detalles y enlaces en `DEPENDENCIAS.md`.
## Modos de juego y premios

- **Patrones:** muestra una secuencia de tres cartas en un tablero 6×6 y agrega una por nivel; el jugador debe repetirla. Cada carta se voltea con animación y muestra una imagen aleatoria del mazo.
- **Memoria:** muestra dos matrices independientes de 6×6, una por jugador. Cada carta es un botón cuadrado de hasta 128×128 px en pantalla grande (se reduce proporcionalmente en ventanas con menos espacio). Cada mazo se baraja al iniciar; acertar una pareja suma un punto y conserva el turno, fallar lo cede. Gana quien complete primero las 18 parejas de su matriz y aparece una celebración animada.
- **Premios:** consulta el indicador de compra del dólar (317) en el API SDDE del BCCR, muestra la fecha de la cotización y permite convertir montos entre USD y CRC.
- **Música:** `resources/recursos/audio/Meditation.wav` inicia en el selector de modos y continúa en bucle por las pantallas del juego. Los controles de sonido de Memoria y Patrones la silencian y reanudan; al cerrar la aplicación se detiene el hilo.
- **Token BCCR:** por seguridad no se guarda un token en el código. Configura la variable de entorno `MEMORYGAME_BCCR_TOKEN` con el token de suscripción del BCCR antes de abrir Premios. En IntelliJ se agrega en **Run > Edit Configurations > Environment variables**; desde PowerShell se puede establecer para la sesión con `$env:MEMORYGAME_BCCR_TOKEN="tu-token"`.

## Ejecutar sin el compilador interno de IntelliJ

Abre la terminal de IntelliJ en la carpeta del proyecto y ejecuta .ejecutar.bat. El script detecta JAVA_HOME o el JDK incluido con IntelliJ, compila src/ con los JAR locales de lib/ y lanza Main. También puedes abrir jecutar.bat desde el Explorador de Windows.

## Interfaz visual

La ventana conserva los controles nativos de Windows y se abre maximizada. Las vistas de autenticación, registro, captura facial, bienvenida y juego se ajustan al área de la ventana; la previsualización de cámara adapta su tamaño a la resolución. Los campos de correo y contraseña comparten un ancho fijo para mantener la alineación. El logo se recorta automáticamente para quitar márgenes transparentes. Los títulos y botones usan Poppins, y las etiquetas y textos de apoyo usan Inter desde los recursos del proyecto, sin instalar fuentes en Windows. Se incluyen sus archivos de licencia OFL.

## Ejecutar desde IntelliJ sin avisos de acceso nativo

Selecciona la configuración compartida **Memory Game (OpenCV)** en el menú junto al botón Run. Incluye el parámetro `--enable-native-access=ALL-UNNAMED` que OpenCV necesita en JDK recientes. Si ejecutas una configuración local llamada `Main` y siguen apareciendo los avisos, abre **Run > Edit Configurations** y agrega ese parámetro en **VM options**. `ejecutar.bat` ya lo configura automáticamente.

## Transición a las pantallas del juego

Las pantallas de bienvenida, selección, patrones, memoria y premios están construidas con componentes Swing. Las capturas PNG se conservan como referencias visuales; ya no se usan como fondos de pantalla.
