# Bibliotecas y recursos externos

El proyecto no usa Maven ni descarga dependencias al ejecutarse. Los archivos necesarios están en `lib/`.

| Componente | Versión | Artefactos incluidos | Fuente |
| --- | --- | --- | --- |
| OpenCV JavaCPP | 4.14.0 | `opencv-4.14.0-1.5.14.jar` y nativo Windows x64 | [Maven Central](https://repo.maven.apache.org/maven2/org/bytedeco/opencv/4.14.0-1.5.14/) |
| JavaCPP | 1.5.14 | API Java y nativo Windows x64 | [Maven Central](https://repo.maven.apache.org/maven2/org/bytedeco/javacpp/1.5.14/) |
| OpenBLAS | 0.3.34 | API Java y nativo Windows x64 | [Maven Central](https://repo.maven.apache.org/maven2/org/bytedeco/openblas/0.3.34-1.5.14/) |
| Clasificador Haar frontal | Rama 4.x | `haarcascade_frontalface_default.xml` | [Repositorio oficial OpenCV](https://github.com/opencv/opencv/tree/4.x/data/haarcascades) |

Los tres artefactos JavaCPP son publicados por Bytedeco. Las versiones con sufijo `windows-x86_64` contienen los binarios nativos específicos de Windows 64 bits. El proyecto incluye también las capturas PNG de Memory Game como referencias visuales.