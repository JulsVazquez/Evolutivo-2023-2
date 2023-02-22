# Optimización continua

## Descripción y características de las funciones

### Sphere
[Sphere-Function](https://www.sfu.ca/~ssurjano/spheref.html).

Nuestra función contiene un único óptimo global, ya que es una función *unimodal*.

Se ha leído[[1]](https://xloptimizer.com/projects/toy-problems/sphere-function-pure-excel#:~:text=The%20sphere%20function%20is%20one,proposed%20modification%20of%20an%20algorithm.) *que nos ayuda a poder probar el rendimiento de un algoritmo o una propuesta de un algoritmo
modificado.*

También sabemos que la función es convexa, ya que si trazamos un segmento a través de ella, ningún punto por debajo del segmento podrá
sobre pasarlo.

Consideramos que al tener un **único óptimo global**, es muy difícil de optimizar, ya que aunque aumentemos de dimensión nuestra función,
siempre tendrá un óptimo único.

La imagen de la función asemeja a una sábana tomada en cada punta (extremo), por una persona.

### Ackley
[Ackley-Function](https://www.sfu.ca/~ssurjano/ackley.html)

Esta función fue presentada en las sesiones con el profesor y la ayudante. Podemos notar que tiene muchísimo mínimos locales.

También encontramos que la función es no convexa[[2]](https://en.wikipedia.org/wiki/Ackley_function), por lo que nos difículta poder encontrar
los mínimos locales dada una superficie de trabajo.

Lo más curioso de está función es que su **único óptimo global** se encuentra en el punto $f(0,0)=0$.

Creemos que aumentar o disminuir la dimensión de la función mantiene su alta dificultad, ya que siempre tendremos múltiples mínimos locales,
incluso si trabajaramos en un *espacio bidimensional* tendríamos complicaciones con encontrar el mínimo local.

### Griewank
[Griewank-Function](https://www.sfu.ca/~ssurjano/griewank.html)

Esta función también fue presentada en la ayudantía con Malinali, pudimos observar que también tiene varios mínimos locales, pero
estos son generalizados y se distribuyen de manera regular.

La dimensión nos ayuda a alterar su dificultad, ya que entre menor sea dicha dimensión, mucho más fácil es de analizar la función y de
esta manera podemos encontrar la distribución de sus mínimos.

Su **óptimo global** depende de su distribución, pero al ser regulares siempre se mantiene con múltiples óptimos a lo largo de la función.

### Tenth Power 

Esta función es una modificación presentada por la *función Sphere*, su diferencia es que nuestra variable está elevada a la potencia 10.

Esto quiere decir que mantendrá las mismas propiedades que la *función Sphere*.

### Rastrigin
[Rastrigin-Function](https://www.sfu.ca/~ssurjano/rastr.html)

La función es presentada con muchísimos minimos locales, por lo que es altamente modal, pero algo que la caracteriza es que sus localidades
mínimas son distribuidas de manera regular.

La función será dificil de optimizar si buscamos justamente estos mínimos, pero altamente fácil si queremos encontrar los máximos.

La dimensión nos ayuda a alterar su dificultad si buscamos máximos, pero se nos dificultará si buscamos los mínimos por sus propiedades.

Al igual que la función **Ackley** su óptimo local se encuentra en el cuadrante 0.

### Rosenbrock
[Rosenbrock-Function](https://www.sfu.ca/~ssurjano/rosen.html)

La función de Rosenbrock es unimodal, por lo que el mínimo global residirá muy cerca del valle parábolico.

Es muy fácil encontrar este el mínimo local en esta función y hemos encontrado que es muy popular para las pruebas
de *optimización por gradientes*.

## Esquema de codificación

### Tamaño de búsqueda

## Búsqueda aleatoria

## Tabla 

<br>
<center>

| **Funcion**  | **Dimension**  |  **Mejor valor f(x)** | **Valor promedio f(x)** | **Peor valor f(x)** |
|---|---|---|---|---|
|  Sphere | --- |---|---|---|
|  Sphere | --- |---|---|---|
|  Sphere | --- |---|---|---|
|  Ackley | --- |---|---|---|
|  Ackley | --- |---|---|---|
|  Ackley | --- |---|---|---|
|  Griewank | --- |---|---|---|
|  Griewank | --- |---|---|---|
|  Griewank | --- |---|---|---|
|  TenthPowerFunction | --- |---|---|---|
|  TenthPowerFunction | --- |---|---|---|
|  TenthPowerFunction | --- |---|---|---|
|  Rastrigin | --- |---|---|---|
|  Rastrigin | --- |---|---|---|
|  Rastrigin | --- |---|---|---|
|  Rosenbrock | --- |---|---|---|
|  Rosenbrock | --- |---|---|---|
|  Rosenbrock | --- |---|---|---|

</center>
<br>

# Optimización combinatoria

Cómo primer ejemplo de problema de combinación optimataría, lo que 
haremos será proponer el problema de *Gráfica bipartita*.

El cuál describimos de la siguiente manera:

**Llamamos *gráfica bipartita* o sólo *bigráfica* es un conjunto de 
vertices de una gráfica descompuestos en dos conjuntos disjuntos, del
modo que no hay dos vértices de una gráfica adyacente dentro del 
mismo conjunto.**

Podemos mostrar un ejemplo donde la *k-partición* de una grafica con
*k=2*.

![](./img/bigrafica.png)

## Codificación binaria

Nuestra codificación para poder representar una gráfica será 
sencilla, por lo cuál usaremos una matriz de adyacencias, esto en
particular es excelente para nuestro problema, debido a que 
justamente buscamos conjuntos disjuntos dentro de *G*.

Por lo que la codificación será de la siguiente manera:

Sea *G* una gráfica. Diremos que por cada par de vértices, estarán
conectados tal que cada par de vértices $$(a,b) \in G$$ si su 
evaluación en la matriz de adyacencias es 1.

Es decir:

$$(a,b) \in G$$ estarán conectados syss $$(a,b)=1$$.

También agregamos la restricción de que un vértice no puede estar
conectado consigo mismo, en otras palabras:

$$(a,a)=0$$

### Espacio de la búsqueda

Nuestro espacio de búsqueda lo vamos a definir como los vértices 
pares e impares de nuestra grafica, donde cada uno representará
las distintas particiones de $$G$$.

### Función objetivo

Definiremos nuestra función objetivo como la suma de las aristas con
elementos de cada clase.

Es decir, tendremos dos tipos de clase.

La clase del $$0$$ y la clase del $$1$$.

Ya que ellas definirán el vector de $$n$$ localidades con $$n$$ 
siendo el número de nodos.

Donde si $$f(E) = 0$$, tendremos una gráfica que se puede 
bi-particionar. 

### Tamaño del espacio de búsqueda

El tamaño del espacio de búsqueda será toda la gráfica $$G$$, pero
siempre teniendo un vértice como inicial y el cuál nunca cambiará.

### Ejemplar concreto del problema

Sea *G*, la siguiente gráfica con el vértice $$v_1$$ marcado como 
fijo:

![](img/graphExample.png)

### Ejemplo de solución

Elegiremos arbitraríamente dos conjuntos de nuestra gráfica y 
obtendremos dos vectores que nos indicarán las posiciones en donde se
encuentran dichos elemento.

Si tomo como mi primer conjunto $$G_1$$ marcado con verde y mi 
segundo conjunto a $$G_2$$ marcado con amarillo obtendría la 
siguiente gráfica:

![](img/biPartitionGraph.png)

En la cuaĺ tengo los vértices etiquetados de la siguiente manera:

$$[00000,00001,00100],[00010, 00011]$$

Donde el primer véctor representa a los vértices: $$[0,1,4]$$ y el
segundo véctor representa a los vértices $$[2,3]$$

Teniendo esto, lo siguiente es hacer la función objetivo, en  donde 
nos vamos a fijar a que clase pertenecen, si a la del 0 o a la del 1.

Verificaremos cada uno de los vértices de cada subgráfica y si tienen
vértices inconexos entre ellos pertenecerán a la clase de 
equivalencia del 0, si tienen vértices conexos pertenecerán a la 
clase de equivalencia del 1.

Por lo que nuestrá gráfica de ejemplo tanto los vértices:

$$v_1,v_2,v_5 \in G_1$$ y

$$v_3,v_4 \in G_2$$ no tienen vértices conexos entre su gráfica.

$$\therefore$$ la gráfica es bipartita, ya que la función objetivo

se evalua a 0.

$$f(E) = 0 + 0 = 0$$

## Vector de valores discretos

### Espacio de la búsqueda

### Función objetivo

### Tamaño del espacio de búsqueda

### Ejemplar concreto del problema

### Ejemplo de solución

## Permutaciones 

### Espacio de la búsqueda

### Función objetivo

### Tamaño del espacio de búsqueda

### Ejemplar concreto del problema

### Ejemplo de solución