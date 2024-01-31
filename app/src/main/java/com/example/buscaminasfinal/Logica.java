package com.example.buscaminasfinal;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.gridlayout.widget.GridLayout;
import java.util.ArrayList;
import java.util.Collections;

public class Logica implements View.OnLongClickListener, View.OnClickListener{

    //Arrays para guardar casillas, minas
    private ArrayList<Button> botones = new ArrayList<>();
    private ArrayList<Integer> minas = new ArrayList<>();
    private ArrayList<Casilla> casillas = new ArrayList<>();
    private ArrayList<Integer> casillasConBadera = new ArrayList<Integer>();
    private Casilla[][] casilla;

    //Objetos para reproducir sonidos
    MediaPlayer mp;
    MediaPlayer mpGanar;

    //Widgets
    GridLayout tablero;
    int x, y;
    Button  btnBackToPast, btnFacil, btnDificil;
    TextView mostrarNumFlags;

    private Context context;

    //Variables globales
    private int numAzar;
    private int numMinas;
    private int numBanderas;
    private int minasAlrededor;
    private int identificador;
    private int casillasActivadas = 0;
    private boolean facil;
    private boolean daPaso;
    private boolean ponerBandera;

    public Logica(Context context, GridLayout tablero, TextView mostrarNumFlags){
        this.context = context;
        this.tablero = tablero;
        this.mostrarNumFlags = mostrarNumFlags;
    }

    // Método que reincia la partida actual sin generar una nueva lista de minas.
    // Esta pensado para gente que quiera reintentar el tablero de nuevo sin cambiar
    // la posición de las minas.
    public void reiniciar(){
        dificultad();
        botones.clear();
        casillas.clear();
        casillasConBadera.clear();
        casillasActivadas = 0;
        generaTablero();
        setContadorReinicio();
    }

    // El método llama a los métodos necesarios para generar el tablero y reiniciarlo,
    // así se puede empezar una partida nueva. El método es llamado cuando se ele¡ige una dificultad.

    public void start() {
        dificultad();
        generarListaMinas();
        numBanderas = minas.size();
        generaTablero();
        setContador();
    }

    // Funciones que se encargan de settear todos los elementos del juego
    // a sus valores iniciales para poder empezar una nueva partida.
    // Dependiendo de qué método se active, el boolean de dificultad se activa
    // o desactiva para decirdir tamaño y número de minas.

    public void empiezaJuegoFacil() {
        facil = true;
        botones.clear();
        minas.clear();
        casillas.clear();
        casillasConBadera.clear();
        casillasActivadas = 0;
        start();
    }

    public void empiezaJuegoDificil() {
        facil = false;
        botones.clear();
        minas.clear();
        casillas.clear();
        casillasConBadera.clear();
        casillasActivadas = 0;
        start();
    }

    // Este método simplemente activa o desactiva la flag de dificultad
    // dándole valores a las variables encargadas de la creación del tablero.

    private void dificultad() {
        if (facil) {
            x = 8;
            y = 8;
            numMinas= 10;
            casilla = new Casilla[x][y];

        } else {
            x = 16;
            y = 16;
            numMinas = 60;
            casilla = new Casilla[x][y];
        }

    }

    // Genera una lista de minas que usaré para settear las minas en el tablero por ID.
    // La lista está ordenada para comparar el ID de las minas con las casillas que han
    // sido marcadas con una bandera y así establecer una condición de victoria.

    private void generarListaMinas() {
        dificultad();
        for (int j = 0; j < numMinas; j++) {
            if (facil)
                numAzar = (int) (Math.random() * 54) + 1;
            else
                numAzar = (int) (Math.random() * 100) + 1;
            if (minas.contains(numAzar)) {
                j--;
            } else {
                minas.add(numAzar);
                Collections.sort(minas);
                System.out.println(minas);
            }
        }
    }

    // Método que se encarga del número de minas en el contador cuando se empieza otra partida.

    protected void setContador() {
        mostrarNumFlags.setText("" + numBanderas);
    }

    // Método que se encarga de settear las minas si se reinicia la misma partida.
    // Como para reiniciar la prtida no se borra la lista de minas para que la partida sea idéntica,
    // he tenido que crear este método porque al reiniciar con el otro se setteaba el contador
    // con el número de banderas que quedaban en la partida anterior, ya que al no settear la
    // lista de minas a 0 para volver a crearlas, el contador era igual al número de banderas restante
    // y se quedaba con el número de banderas de la partida anterior.

    public void setContadorReinicio(){
        if (facil)
            numBanderas = 8;
        else
            numBanderas = 18;
        mostrarNumFlags.setText("" + numBanderas);
    }

    // Este método es el encargado de crear el tablero. Los botones son de tipo
    // Casilla, clase que hereda de Button para poder crear un campo de botones
    // con una serie de métodos de la clase Casilla y así poder poner banderas
    // que indiquen las minas, casillas marcdas, banderitas, etc...
    // También nos permite obtener los valores que queremos con los getter.
    // Los botones se guardan en una array de objetos del tipo Casilla y en una
    // ArrayList para hacer comparaciones y poder recorrer el tablero por coordenadas.


    protected void generaTablero() {
        tablero.removeAllViews();
        GridLayout g = tablero.findViewById(R.id.tablero);
        g.setRowCount(y);
        g.setColumnCount(x);
        dificultad();
        identificador = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                Casilla b = new Casilla(this.context, i, j);
                b.setLayoutParams(new ViewGroup.LayoutParams((g.getLayoutParams().width / (g.getColumnCount())),
                        g.getLayoutParams().height / (g.getRowCount())));
                b.setId(identificador);
                b.setActiva();
                b.setBanderita();
                b.setTag("");
                System.out.println("Tag en: "+b.getTag());
                if (minas.contains(b.getId())) {
                    System.out.println("Hola, soy una mina.");
                    b.setMina(true);
                }
                b.setBackgroundColor(Color.rgb(100, 100, 100));
                b.setBackgroundResource(R.drawable.pngegg);
                System.out.println("ID: " + identificador);
                System.out.println("Es mina: " + b.isMina());
                System.out.println("Es activa: " + b.isActiva());
                b.setOnClickListener(this);
                casilla[i][j] = b;
                botones.add(b);
                if (b.isActiva())
                    casillasActivadas++;
                g.addView(b, j);
                identificador++;
                casilla[i][j].setOnLongClickListener(this);
            }
            System.out.println("Casillas activas: " + casillasActivadas);
        }

    }

    // El método onClick comprueba si la casilla es mina y si está pulsada y
    // si ninguna de las dos opciones es cierta, entonces comprueba el número de minas
    // alrededor de cada casilla del tablero, recoge los índices correspondientes a la
    // casilla pulsada y se los pasa al método recursivo. Pone el botón en estado
    // desactivado y settea el número de minas colindantes en la variable de Clase
    // correspondiente.
    // Si el botón es una mina, settea una imagen de mina y llama al método que
    // destapa todas las minas y settea enabled false todos los botones.

    public void onClick(View view) {
        if (view.getClass().getSimpleName().equals("Casilla")) {
            Casilla b = (Casilla) view;
            b.setOnClickListener((View.OnClickListener) this);
            if (!b.isMina() && b.isActiva() && b.getTag().equals("")) {
                contarMinasAlrededor(b);
                int i = b.getCoordenadaFila();
                int j = b.getCoordenadaColumna();
                System.out.println("Tag en: "+b.getTag());
                if (casilla[i][j].isActiva() && b.getTag().equals("")) {
                    recursividad(i, j, b);
                }
                b.setEnabled(false);
                b.setminasAlrededor(minasAlrededor);

            } else {

                // Si el botón es una mina se llama al método start() de la clase MediaPlayer
                // con el fin de que al pulsar una mina suene una explosión.
                // Se settea las bombas con una imagen de una mina y se destapan todas
                // llamando al método destaparMinas().

                if (b.isActiva() && b.getTag().equals("")) {
                    if (b.isMina())
                        mp.start();
                    b.setBackgroundResource(R.drawable.bomba);
                    destaparMinas(view);
                    for (int i = 0; i <= tablero.getRowCount(); i++) {
                        for (int j = 0; j <= tablero.getColumnCount(); j++) {
                            if (i > -1 && i < x && j > -1 && j < y)
                                if (casilla[i][j].isMina())
                                    b.setBackgroundResource(R.drawable.bomba);
                        }
                    }
                    b.setEnabled(false);
                    Toast.makeText(this.context, "¡BOOOOOOOM!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this.context, "Perdiste, más suerte la próxima vez.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this.context, "Si quieres volver a jugar, ¡elige una dificultad!", Toast.LENGTH_SHORT).show();
                }
            }
            // Estos sout son para depurar y saber si las propiedades se han añadido correctamente
                    /*
                    System.out.println("Botón: " + b.getId());
                    System.out.println("isMina"+ b.isMina());
                    System.out.println("Activa " + b.isActiva());
                    System.out.println("x:" + b.getCoordenadaFila());
                    System.out.println("y:" + b.getCoordenadaColumna());
                    System.out.println("Fila: "+ fila +" Columna: " + columna);
                    System.out.println("Casillas activas: " + casillasActivadas);
                    System.out.println("Casilla tapada: " + b.isDestapada());
                    */
        }

    }

    // Implementación del OnLongClickListener para los botones.
    // En este método he tenido que usar el método setTag() porque la variable
    // de la clase Casilla isBandera() y setBandera() se quedan siempre en false,
    // cosa que con las etiquetas está solucionado.
    // Si la etiqueta está vacía, entonces pone el ícono de bandera, settea el contador
    // con un -1 y pone la etiqueta "Bandera" en el botón.
    // Por el contrario, si la etiqueta es bandera realiza la operación inversa.
    // El método llama al método ganar para comprobar si todas las minas han sido tapadas,
    // si es así, settea todas las casillas enabled(false) y finaliza la patida con victoria.

    @Override
    public boolean onLongClick(View view) {
        if (view.getClass().getSimpleName().equals("Casilla")) {
            Casilla b = (Casilla) view;
            b.setOnLongClickListener((View.OnLongClickListener) this);
            System.out.println(b.getCoordenadaFila());
            System.out.println(b.getCoordenadaColumna());
            int i = b.getCoordenadaFila();
            int j = b.getCoordenadaColumna();
            int pos = casilla[i][j].getId();
            System.out.println("Botón " + b.getId() + " En las coordenadas [" + i + "][" + j + "]");
            if (b.getTag().equals("Bandera")) {
                b.setBackgroundResource(R.drawable.pngegg);
                casilla[i][j].setSinBandera();
                b.setTag("");
                System.out.println("Tag en: "+b.getTag());
                numBanderas = numBanderas + 1;
                setContador();
                casillasConBadera.remove((Integer)pos);
                ponerBandera = false;
                System.out.println("botón bandera true: " + casilla[i][j].isBanderirta());
            } else if (b.getTag().equals("") && casilla[i][j].isActiva() && numBanderas > 0 && numBanderas <= numMinas && !casillasConBadera.contains(casilla[i][j].getId()) && casilla[i][j].getText().equals("")) {
                ponerBandera = true;
                casilla[i][j].setBackgroundResource(R.drawable.hipotenocha2);
                casilla[i][j].setBanderita();
                System.out.println("Is any flag here? " + casilla[i][j].isBanderirta());
                int id = casilla[i][j].getId();
                numBanderas = numBanderas - 1;
                setContador();
                b.setTag("Bandera");
                System.out.println("Tag en: "+b.getTag());
                casillasConBadera.add(id);
                Collections.sort(casillasConBadera);
                System.out.println("Lista de casillas con bandera: " + casillasConBadera);
                System.out.println("botón " + casilla[i][j].getId());
                System.out.println("botón " + casilla[i][j]);
                System.out.println("botón es bandera: " + casilla[i][j].isBanderirta());
            }
            if (minas.equals(casillasConBadera)){
                ganar();
                partidaGanada(b);
            }

        }
        return true;
    }


    // Método que establece las condiciones de victoria.

    private void ganar() {
        if (minas.equals(casillasConBadera)){
            Toast.makeText(this.context, "¡Todas las minas han sido cubiertas, has ganado la partida, enhorabuena!", Toast.LENGTH_SHORT).show();
            mpGanar.start();
        }
    }

    // Recorre el grid comparando los índices de los botones del grid con los de la lista de minas.
    // Cuando encuentra una coincidencia en la lista pone al botón un ícono de mina y desactiva los
    // botones para que no se pueda hacer ningún click más. Este es el método de pérdida de la partida.

    private void destaparMinas(View view) {
        View v;
        GridLayout g = tablero.findViewById(R.id.tablero);
        for (int i = 0; i < g.getChildCount(); i++) {
            v = g.getChildAt(i);
            //Log.e("Objeto: ",v.getClass().getSimpleName()+ " <---> "+ v);
            Casilla b;
            if ((v.getClass().getSimpleName().contains("Casilla"))) {
                b = (Casilla) v;
                if (minas.contains(b.getId()))
                    b.setBackgroundResource(R.drawable.bomba);
                botones.get(b.getId()).setEnabled(false);
            }
        }
    }



    private void partidaGanada(View view){
        View v;
        GridLayout g = tablero.findViewById(R.id.tablero);
        for (int i = 0; i < g.getChildCount(); i++) {
            v = g.getChildAt(i);
            Casilla b;
            if ((v.getClass().getSimpleName().contains("Casilla"))) {
                b = (Casilla) v;
                if (minas.equals(casillasConBadera))
                    botones.get(b.getId()).setEnabled(false);
            }
        }
    }

    // Se rcorre la matriz buscando botones que no sean mina, de cada botón que no es mina,
    // se recogen las coordenadas y se pasan al método contarMinas, añadiendo un +1 al contador de minas
    // si tiene una mina en una de las 8 casillas colindantes a sí misma en la variable contador de la
    // clase Casilla.

    private void contarMinasAlrededor(Casilla b) {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (!casilla[i][j].isMina()) {
                    casilla[i][j].setminasAlrededor(contarMinas(i, j));
                    if (casilla[i][j].getMinasAlrededor() != 0)
                        casilla[i][j].setTieneMinaAlrededor();
                    //sout de depuración para ver por dónde va el bucle y los resultados de todo el tablero
                    //System.out.println("Tiene minas alrededor: " + casilla[i][j].getTieneMinaAlrededor());
                    //System.out.println("Minas alrededor: " + casilla[i][j].getMinasAlrededor() + " en la casilla [" + casilla[i][j].getCoordenadaFila() + "][" + casilla[i][j].getCoordenadaColumna() + "]");
                }
            }

        }
    }

    // Método recursivo que recibe las coordenadas del botón, comprueba si el botón está dentro del grid y si está
    // comprueba que la casilla no sea una mina, esté activa y no sea una bandera, después distingue si es una casilla
    // vacía o si tiene minas colindates, si la casilla no tiene minas alrededor, se recorren recursivamente 4 coordenadas
    // alrededor de la casilla y una vez finalicen de rcorrer en esas coordenadas, sse activa un flag que da paso
    // a las otras 4 coordenadas. Lo he hecho así porque con dos coordenadas opuestas a la vez, daba error stackOverFlow.
    // Una vez acaba la segunda recursión, la flag pasa a false para que vuelva a entrar en la primera la siguenta vez.
    // Si la recusión encuentra mina, o getMinasAlrededor() > 0 se hace un return para salir del bucle infinito, si se produce.

    private void recursividad(int i, int j, Casilla b) {

        if (i >= 0 && i < x && j >= 0 && j < y) {

            //System.out.println("Ha entrado el método en la casilla: " + casilla[i][j].getMinasAlrededor() + " en la casilla [" + casilla[i][j].getCoordenadaFila() + "][" + casilla[i][j].getCoordenadaColumna() + "]" + contador + " veces.");

            if (casilla[i][j].isActiva() && !casilla[i][j].isMina() && casilla[i][j].getTag().equals("")) {
                // Esta parte del click que settea en el botón un texto correspondiente al número
                // de minas adyacentes de la casilla.
                switch (casilla[i][j].getMinasAlrededor()) {
                    case 0:
                        casilla[i][j].setBackgroundColor(Color.rgb(155, 155, 155));
                        b.setEnabled(false);
                        System.out.println("Casillas activadas: " + casillasActivadas);
                        break;
                    case 1:
                        casilla[i][j].setBackgroundColor(Color.rgb(155, 155, 155));
                        casilla[i][j].setText("1");
                        casilla[i][j].setTextColor(Color.YELLOW);
                        b.setEnabled(false);
                        System.out.println("Casillas activadas: " + casillasActivadas);
                        break;
                    case 2:
                        casilla[i][j].setBackgroundColor(Color.rgb(155, 155, 155));
                        casilla[i][j].setText("2");
                        casilla[i][j].setTextColor(Color.GREEN);
                        b.setEnabled(false);
                        System.out.println("Casillas activadas: " + casillasActivadas);
                        break;
                    case 3:
                        casilla[i][j].setBackgroundColor(Color.rgb(155, 155, 155));
                        casilla[i][j].setText("3");
                        casilla[i][j].setTextColor(Color.BLUE);
                        b.setEnabled(false);
                        System.out.println("Casillas activadas: " + casillasActivadas);
                        break;
                    case 4:
                        casilla[i][j].setBackgroundColor(Color.rgb(155, 155, 155));
                        casilla[i][j].setText("4");
                        casilla[i][j].setTextColor(Color.RED);
                        b.setEnabled(false);
                        System.out.println("Casillas activadas: " + casillasActivadas);
                        break;
                    case 5:
                        casilla[i][j].setBackgroundColor(Color.rgb(155, 155, 155));
                        casilla[i][j].setText("5");
                        casilla[i][j].setTextColor(Color.WHITE);
                        b.setEnabled(false);
                        System.out.println("Casillas activadas: " + casillasActivadas);
                        break;
                    case 6:
                        casilla[i][j].setBackgroundColor(Color.rgb(155, 155, 155));
                        casilla[i][j].setText("6");
                        casilla[i][j].setTextColor(Color.CYAN);
                        b.setEnabled(false);
                        break;
                    case 7:
                        casilla[i][j].setBackgroundColor(Color.rgb(155, 155, 155));
                        casilla[i][j].setText("7");
                        casilla[i][j].setTextColor(Color.MAGENTA);
                        b.setEnabled(false);
                        break;
                    case 8:
                        casilla[i][j].setBackgroundColor(Color.rgb(155, 155, 155));
                        casilla[i][j].setText("8");
                        casilla[i][j].setTextColor(Color.BLACK);
                        b.setEnabled(false);
                        break;
                }
            }

            if (casilla[i][j].getMinasAlrededor() == 0 && casilla[i][j].isActiva()) {
                recursividad(i - 1, j - 1, b); //Abajo-izquierda
                recursividad(i - 1, j, b);       //Detrás
                recursividad(i - 1, j + 1, b);
                recursividad(i, j + 1, b);
                casilla[i][j].setInactiva();
                daPaso = true;
                if (daPaso == true) {
                    recursividad(i + 1, j - 1, b);
                    recursividad(i + 1, j + 1, b); //Arriba-derecha
                    recursividad(i, j - 1, b);
                    recursividad(i + 1, j, b);
                    daPaso = false;
                }
            } else {
                return;
            }
        }


    }


    // Contador de minas colindantes a la casilla.
    // Recibe las coordenadas de cada casilla del tablero que no es mina,
    // si la casilla con la que estamos comparando está dentro del tablero,
    // se busca en cada una de las 8 posiciones, restando o sumando al índice
    // que le corresponde un 1 para desplazarnos hasta la casilla que queremos.
    // Cuando termina, retorna el contador de minas que usaremos para decirle a
    // cada casilla cuántas minas tiene a su alrededo con el método de su clase
    // setMinasAlrededor().

    private int contarMinas(int i, int j) {
        int numMinasColindantes = 0;

        if (i - 1 >= 0 && j - 1 >= 0) {
            if (casilla[i - 1][j - 1].isMina()) {
                numMinasColindantes++;
            }
        }

        if (i - 1 >= 0) {
            if (casilla[i - 1][j].isMina()) {
                numMinasColindantes++;
            }
        }

        if (i - 1 >= 0 && j + 1 < y) {
            if (casilla[i - 1][j + 1].isMina()) {
                numMinasColindantes++;
            }
        }

        if (j + 1 < y) {
            if (casilla[i][j + 1].isMina()) {
                numMinasColindantes++;
            }
        }

        if (i + 1 < x && j + 1 < y) {
            if (casilla[i + 1][j + 1].isMina()) {
                numMinasColindantes++;
            }
        }

        if (i + 1 < x) {
            if (casilla[i + 1][j].isMina()) {
                numMinasColindantes++;
            }
        }

        if (i + 1 < x && j - 1 >= 0) {
            if (casilla[i + 1][j - 1].isMina()) {
                numMinasColindantes++;
            }
        }

        if (j - 1 >= 0) {
            if (casilla[i][j - 1].isMina()) {
                numMinasColindantes++;
            }
        }

        return numMinasColindantes;
    }


}
