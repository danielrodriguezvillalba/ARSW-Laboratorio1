# Laboratorio 1


## Nombres:
```
Nicolas Cardenas Chaparro

Daniel Felipe Rodriguez Villalba
```
## Compile and run instructions:

Ubicarse sobre el directorio en el cual se va a trabajar
* Compilar: Use el comando `mvn package`
* Ejecutar Pruebas: Use el comando `mvn test`


## BBP Formula
### Part I - Introduction to Java Threads

1. Change the beginning with start() to run(). How does the output change? Why?.

`R: Si, la salida cambia ya que al usar el start se ejecutan los hilos de manera desordenada, por lo que no se puede ver claramente cual esta ejecutando, sin embargo al iniciarlo con el metodo run, estos se ejecutan de manera seguida, es decir, ejecuta el metodo run de el primer hilo, lo termina y despues el metodo run del Segundo hilo y asi sucesivamente hasta terminar con el hilo 3.`


### Part II - BBP Formula Exercise

1. Create a Thread type class that represents the life cycle of a thread that calculates a portion of the required digits. 
````
package edu.eci.arsw.math;

/* this class is used to calculate a portion of the nth digit of pi */

public class BBpFormulaThread extends Thread{
	
	private String res;
	private String nameTh;
	private ThreadGroup tg;
	private  int start, end;
	private PiDigits gDigits;
//	private Main aHexa;
	
	public BBpFormulaThread(String name, ThreadGroup tg, int start, int end) {
		super(tg, name);
		this.nameTh = name;
		this.tg = tg;
		this.start = start;
		this.end = end;
		gDigits = new PiDigits();
//		aHexa = new Main();
		
	}	
	@Override
	public void run() {	
//		res = nameTh+"////////"+start+"//"+end+"/////////"+ Main.bytesToHex(PiDigits.getDigits(start, end));
		res = Main.bytesToHex(PiDigits.getDigits(start, end));
	}

	public String getRes() {return res;}
	public String getNameTh() {return nameTh;}
	public ThreadGroup getTg() {return tg;}	

}
````
2. Have the PiDigits.getDigits() function receive as an additional parameter an N value, corresponding to the number of threads between which the solution is to be parallelized. Have that function wait until the N threads finish solving the problem to combine the answers and then return the result. For this, review the join method of the Java concurrency API. 

````
//This method divides the load of calculate the nth digit of pi into n lightweight loads, then concatenates the result.
	public static String getDigits(int start, int count, int nThreads) throws InterruptedException {
		int interval = count / nThreads;
		int residuo = count % nThreads;
		int ini = start;
		int fin = interval;
		BBpFormulaThread thr;

		ThreadGroup tg = new ThreadGroup("pidigits");

		int ap = Runtime.getRuntime().availableProcessors();

		List<BBpFormulaThread> threadsList = new ArrayList<BBpFormulaThread>();

		for (int i = 1; i <= nThreads; i++) {
			if (i == 1) {
				thr = new BBpFormulaThread("piDigits" + i, tg, ini, fin);
				threadsList.add(thr);
				thr.start();
			} else if (i == nThreads) {
				ini += fin;
				thr = new BBpFormulaThread("piDigits" + i, tg, ini, fin + residuo);
				threadsList.add(thr);
				thr.start();
			} else {
				ini += fin;
				thr = new BBpFormulaThread("piDigits" + i, tg, ini, fin);
				threadsList.add(thr);
				thr.start();
			}
		}
		//Have the main thread to wait to the other threads
		for (int i = 0; i < threadsList.size(); i++) {
			threadsList.get(i).join();
		}

		/* finally concatenate the results */
		StringBuilder sb = new StringBuilder();
		for (int f = 0; f < threadsList.size(); f++) {
			sb.append(threadsList.get(f).getRes());
			respuesta = sb.toString();
		}
		return respuesta;
	}
````

3. Adjust the JUnit tests, considering the cases of using 1, 2 or 3 threads (the last one to consider an odd number of threads!)

`Las pruebas se realizaron con el 20-milesimo digito de pi ya que la prueba de 1 millon en el codigo secuencial(El original con el que se compara) se tarda demasiado`

````
@Test
    public void ResSecuencialDebeSerIgualResParalela1thread() throws InterruptedException {
    	String s1 = new String(Main.bytesToHex(PiDigits.getDigits(1, 20000)));
    	String s2 = new String(PiDigits.getDigits(1, 20000, 1));
    	assertTrue(s1.equals(s2));
    }
    
    @Test
    public void ResSecuencialDebeSerIgualResParalela2threads() throws InterruptedException {
    	String s1 = new String(Main.bytesToHex(PiDigits.getDigits(1, 20000)));
    	String s2 = new String(PiDigits.getDigits(1, 20000, 2));
    	assertTrue(s1.equals(s2));
    }
    
    @Test
    public void ResSecuencialDebeSerIgualResParalela3threads() throws InterruptedException {
    	String s1 = new String(Main.bytesToHex(PiDigits.getDigits(1, 6666)) + Main.bytesToHex(PiDigits.getDigits(6667, 6666)) + Main.bytesToHex(PiDigits.getDigits(13333, 6668)));
    	String s2 = new String(PiDigits.getDigits(1, 20000, 3));
    	assertTrue(s1.equals(s2));
    }
````

### Part III - Performance Evaluation
From the above, implement the following sequence of experiments to calculate the million digits (hex) of PI, taking their execution times (be sure to do them on the same machine):

  * Single thread. 
  * As many threads as processing cores (have the program determine this using the Runtime API). 
  * So many threads as double processing cores. 
  * 200 threads.
  ![Imagenes](https://github.com/danielrodriguezvillalba/ARSW-Lab1/blob/master/Imagenes/200Hilos1millon.PNG)
  * 500 threads. 
  
  
When starting the program, run the jVisualVM monitor, and as the tests run, check and record the CPU and memory consumption in each case.




---
With the above, and with the execution times given, graph solution time vs. Number of threads. Analyze and propose hypotheses with your partner for the following questions (you can take into account what is reported by jVisualVM):

 1. According to Amdahls law, where S (n) is the theoretical performance improvement, P the parallel fraction of the algorithm, and          n the number of threads, the greater n, the greater the improvement should be. Why is the best performance not achieved with            the 500 threads? How does this performance compare when 200 are used?. 
      
 2. How does the solution behave using as many processing threads as cores compared to the result of using twice as much?

 3. According to the above, if for this problem instead of 500 threads on a single CPU, 1 wire could be used on each of 500                  hypothetical machines, would Amdahls's law be better applied? If, instead, c threads were used in 500 / c distributed machines          (where c is the number of cores of said machines), would it be improved? Explain your answer.



## Dogs Race case

### Part I

#### Punto 1: 

Como se puede observar en la imagen este es el rendimiento mostrado del programa con 1 hilo (hay 2 pero se debe tener en cuenta que el main es un hilo).

![Imagenes](https://github.com/danielrodriguezvillalba/ARSW-Lab1/blob/master/Imagenes/Verificando1Hilo.PNG)

#### Punto 2:

El programa se dividio en las tres partes pedidas y el resultado en el codigo fue el siguiente:

![Imagenes](https://github.com/danielrodriguezvillalba/ARSW-Lab1/blob/master/Imagenes/TresHilosMain.PNG)


Seguido esto se procedio a medir el procesamiento de los nucleos por medio de JavaVisualVM y este fue el resultado


![Imagenes](https://github.com/danielrodriguezvillalba/ARSW-Lab1/blob/master/Imagenes/TresHilos.PNG)

#### Punto 3:

Al modificar el programa con lo requerido por este punto, el codigo resultante fue el siguiente, en el cual se hace uso de buffer para la lectura de teclado y de la clase TimeTask para calcular los 5 segundos


![Imagenes](https://github.com/danielrodriguezvillalba/ARSW-Lab1/blob/master/Imagenes/Parte2Punto1Completp.PNG)

### Part III

#### Punto 1:

Se hizo uso como se sugeria en el punto del metodo .join() de los hilos para que esto mostrara el numero del ganador, despues de acabados todos los hilos, el resultado fue este:

![Imagenes](https://github.com/danielrodriguezvillalba/ARSW-Lab1/blob/master/Imagenes/Join.PNG)

#### Punto 2:

Al momento de correr la aplicación pudimos observar que esta al momento de mostrar las posiciones de los perros, habian unas que se repetian, por lo que identificamos al momento de acceder a las posiciones como zona critica.

#### Punto 3:

Despues de identificar la zona critica se procedio a hacer uso de lo visto en clase con los bloques de Synchronized, en la clase Galgo en el metodo corra(), el codigo quedo de la siguiente manera:


![Imagenes](https://github.com/danielrodriguezvillalba/ARSW-Lab1/blob/master/Imagenes/SynchronyzedCode.PNG)


El resultado obtenido respecto a las posiciones, al ejecutar la aplicación con este codigo implementado, fue el siguiente:


![Imagenes](https://github.com/danielrodriguezvillalba/ARSW-Lab1/blob/master/Imagenes/Results.PNG)

#### Punto 4:

En la clase MainCanodromo, en los metodos que implementaban estas funcionalidades se realizaron unos cambios, los cuales finalmente quedaron asi:


![Imagenes](https://github.com/danielrodriguezvillalba/ARSW-Lab1/blob/master/Imagenes/Funci.PNG)


Adicionalmente se realizaron cambios en la clase Galgo, para poder tratar con los temas que del monitor que manejaba el wait, estos cambios se muestran en dicha clase.

