Tarea 2 Seguridad Informática UBB
Contenido:
    - Servidor.java
    - Cliente.java
    - certificado.cer
    - certificadoServidor.cer
    - Informe.docx
    - Informe.pdf

Para compilar servidor:
    Revisar la linea 27, 28 y 29 para configurar la ruta de la keystore, 
    truststore y contraseña. (usan la misma clave)
    $ javac Servidor.java

Para compilar cliente:
    Revisar linea 26 para ver la ip donde se conectará
     IP servidor: 190.13.134.74
     IP loopback: 127.0.0.1
    Revisar la linea 28, 29 y 30 para configurar la ruta de la keystore,
    truststore y contraseña. (usan la misma clave)
    de esta.
    $ javac Cliente.java

Para ejecutar servidor:
    $ java Servidor

Para ejecutar cliente:
    $ java Cliente

///////////////////////////////////////////////////////

Se generan el almacén de llaves del servidor y del cliente:
    keytool -genkeypair -alias llaveServidor -keystore llavesServidor.jks
    keytool -genkeypair -alias llaveCliente -keystore llavesCliente.jks

Se generan el certificado del servidor y del cliente:
    keytool -export -alias llaveServidor -file certificadoServidor.cer -keystore llavesServidor.jks
    keytool -export -alias llaveCliente -file certificadoCliente.cer -keystore llavesCliente.jks

Finalmente se generaran los almacenes del cliente y del servidor (truststore) y adicionalmente 
Se importa
Certificado del servidor en el truststore del cliente 
    keytool -import -trustcacerts -file certificadoServidor.cer -alias certUBB -keystore truststore_cliente.jks
Certificado del cliente en el truststore del servidor
    keytool -import -trustcacerts -file certificadoCliente.cer -alias certCliente1 -keystore truststore_servidor.jks

