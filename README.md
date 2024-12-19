# Compilador
## Autores 
- Paladino Bravo Iñaqui
- Moran Alejandro

## Descripcion
Este proyecto fue desarrollado como parte de la cursada de Diseño de Compiladores I 2023 de la carrera de Ingeniería en Sistemas en la Universidad Nacional del Centro de la Provincia de Buenos Aires (UNICEN).

El compilador fue desarrollado en Java. Se utilizo la herramienta **YACC**, mas especificamente BYACC para Java, para la construccion del parser.
Como estructura de codigo intermedio se eligieron los tercetos y el codigo de salida es Assembler Pentium de 32 bits.

## Instrucciones de Uso
### Compilacion del proyecto
```bash
javac -d bin src/**/*.java
```

### Ejecucion del Compilador
```bash
java -cp bin Main <archivo_fuente> [<output.asm>]
```

## Salida del Compilador
El compilador genera las siguientes salidas:
- **Errores**: Listas de errores léxicos, sintácticos y semánticos detectados.
- **Estructuras**: Lista de estructuras reconocidas por el parser.
- **Código ensamblador**: Archivo .asm que puede ensamblarse y ejecutarse con MASM32.

## En caso de modificar la matriz de transicion:
El excel de la matriz tiene q estar en formato csv.

Cada elemento puede ser "int|asm", "int", o "null". Y deben estar separados por ";"

Remplazar todos los "\n" por ";"(sed -i "%s/\n/;/g" test.csv)

## Consideraciones
Las dimensiones de la matriz esta hardcodeada.

Hay dos ejecutable de byacc en la carpeta parser, uno para win32 y otro para aarch64.
