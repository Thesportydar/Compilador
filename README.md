# Compilador
## Paladino Bravo Iñaqui y Moran Alejandro

Ejecutar el Main dentro del parser con el nombre de archivo de codigo como parametro
Ejemplo: java -cp "./target/classes" src/main/java/compi/Parser/Parser.java testNums.txt

# En caso de modificar la matriz de transicion:
El excel de la matriz tiene q estar en formato csv.
Cada elemento puede ser "int|asm", "int", o "null". Y deben estar separados por ";"
Remplazar todos los "\n" por ";"(sed -i "%s/\n/;/g" test.csv)

# Consideraciones
Las dimensiones de la matriz esta hardcodeada.
El salto de linea es por defecto <crlf>, debe cambiarse dentro de la clase de LexicalAnalizer.
Hay dos ejecutable de byacc en la carpeta parser, uno para win32 y otro para aarch64.
