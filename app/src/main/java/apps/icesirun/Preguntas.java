package apps.icesirun;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Preguntas extends Activity {

    public final static String RESULTADO = "apps.icesirun.result";

    private ArrayList<Integer> preguntasHechas1;
    private int preguntasCorrectas, numeroVidas, preguntasRealizadas;
    private TextView tvPregunta;
    private String fuente;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_preguntas);
        Intent intent = getIntent();
        String nivel = intent.getStringExtra(Opciones.NIVEL);
        fuente = "theunseen.ttf";
        preguntasCorrectas = 0;
        numeroVidas = 3;
        preguntasHechas1 = new ArrayList<Integer>();

        if (nivel.equals(getResources().getString(R.string.nivel1))) {
            leerPreguntas(getResources().getString(R.string.nivel1));
        }

        Typeface font = Typeface.createFromAsset(getAssets(), fuente);
        tvPregunta.setTypeface(font);

        hiloTiempo();
    }

    public void leerPreguntas(String nivel) {

        if (nivel.equals(getResources().getString(R.string.nivel1))) {
            Random rnd = new Random();
            int numeroPregunta = rnd.nextInt(37 - 1 + 1) + 1;
            System.out.println("Numero Pregunta" + numeroPregunta);

            boolean hecha = false;
            if (preguntasHechas1.contains(numeroPregunta)) {
                hecha = true;
            }
            System.out.println(hecha);

            if (hecha) {
                leerPreguntas(getResources().getString(R.string.nivel1));
            } else {
                try {
                    InputStream fraw = getResources().openRawResource(R.raw.listapreguntas);
                    System.out.println(fraw != null);
                    BufferedReader brin = new BufferedReader(new InputStreamReader(fraw));
                    int contadorLinea = 0;
                    String linea = "";
                    while (contadorLinea != numeroPregunta) {
                        linea = brin.readLine();
                        contadorLinea++;
                    }
                    fraw.close();
                    preguntasHechas1.add(numeroPregunta);
                    System.out.println("Numero linea " + contadorLinea);
                    System.out.println("La linea es " + linea);
                    String[] infoPregunta = linea.split(",");

                    String textoPregunta = infoPregunta[0];

                    Random reordenar = new Random();
                    int[] numeros = {1, 2, 3, 4};

                    for (int i = 0; i < numeros.length; i++) {
                        numeros[i] = i + 1;
                    }
                    for (int i = 0; i < numeros.length; i++) {
                        int posicionAleatoria = reordenar.nextInt(numeros.length);
                        int temp = numeros[i];
                        numeros[i] = numeros[posicionAleatoria];
                        numeros[posicionAleatoria] = temp;
                    }

                    String textoOpcion1 = infoPregunta[numeros[0]];
                    String textoOpcion2 = infoPregunta[numeros[1]];
                    String textoOpcion3 = infoPregunta[numeros[2]];
                    String textoOpcion4 = infoPregunta[numeros[3]];

                    tvPregunta = (TextView) findViewById(R.id.textoPregunta);
                    tvPregunta.setText(textoPregunta);
                    final String textoOpcionCorrecta = infoPregunta[5];
                    System.out.println("Opcion correcta es " + textoOpcionCorrecta);
                    final Button btOpcion1 = (Button) findViewById(R.id.botonOpcion1);
                    btOpcion1.setTypeface(Typeface.createFromAsset(getAssets(), fuente));
                    btOpcion1.setText(textoOpcion1);
                    btOpcion1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            seleccionarRespuesta(btOpcion1,textoOpcionCorrecta);
                        }
                    });
                    final Button btOpcion2 = (Button) findViewById(R.id.botonOpcion2);
                    btOpcion2.setTypeface(Typeface.createFromAsset(getAssets(), fuente));
                    btOpcion2.setText(textoOpcion2);
                    btOpcion2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            seleccionarRespuesta(btOpcion2, textoOpcionCorrecta);
                        }
                    });
                    final Button btOpcion3 = (Button) findViewById(R.id.botonOpcion3);
                    btOpcion3.setTypeface(Typeface.createFromAsset(getAssets(), fuente));
                    btOpcion3.setText(textoOpcion3);
                    btOpcion3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            seleccionarRespuesta(btOpcion3, textoOpcionCorrecta);
                        }
                    });
                    final Button btOpcion4 = (Button) findViewById(R.id.botonOpcion4);
                    btOpcion4.setTypeface(Typeface.createFromAsset(getAssets(), fuente));
                    btOpcion4.setText(textoOpcion4);
                    btOpcion4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            seleccionarRespuesta(btOpcion4, textoOpcionCorrecta);
                        }
                    });
                } catch (Exception ex) {
                    new AlertDialog.Builder(Preguntas.this).setTitle(getResources().getString(R.string.error_ejecucion))
                            .setMessage(getResources().getString(R.string.error_pregunta)).show();
                }
            }
        }
    }

    public void seleccionarRespuesta(Button arg0, String opcionCorrecta) {
        TextView correcta = (TextView) findViewById(R.id.resultado);
        correcta.setTypeface(Typeface.createFromAsset(getAssets(), fuente));
        switch (arg0.getId()) {
            case R.id.botonOpcion1:
                Button btOpcion1 = (Button) findViewById(R.id.botonOpcion1);
                if (opcionCorrecta.equals(btOpcion1.getText())) {
                    correcta.setTextColor(getResources().getColor(R.color.negro));
                    correcta.setText(getResources().getString(R.string.correcta));
                    preguntasCorrectas++;
                    // TextView preguntasCorrec = (TextView) findViewById(R.id.numeroPreguntasCorrectas);
                    //preguntasCorrec.setText("" + preguntasCorrectas);
                    leerPreguntas(getResources().getString(R.string.nivel1));
                    refrescarPregunta();
                } else {
                    correcta.setTextColor(getResources().getColor(R.color.rojo));
                    correcta.setText(getResources().getString(R.string.incorrecta));
                    numeroVidas--;
                    //TextView vidas = (TextView) findViewById(R.id.vidas);
                    //vidas.setText("" + numeroVidas);
                    refrescarVidas();
                    if (numeroVidas == 0) {
                        correcta.setText(getResources().getString(R.string.muerto));
                        Intent ultimo = new Intent(this,Final.class);
                        ultimo.putExtra(RESULTADO, getResources().getString(R.string.muerto));
                        startActivity(ultimo);
                    } else {
                        leerPreguntas(getResources().getString(R.string.nivel1));
                    }
                }
                break;
            case R.id.botonOpcion2:
                Button btOpcion2 = (Button) findViewById(R.id.botonOpcion2);
                if (opcionCorrecta.equals(btOpcion2.getText())) {
                    correcta.setTextColor(getResources().getColor(R.color.negro));
                    correcta.setText(getResources().getString(R.string.correcta));
                    preguntasCorrectas++;
                    //TextView preguntasCorrec = (TextView) findViewById(R.id.numeroPreguntasCorrectas);
                    //preguntasCorrec.setText("" + preguntasCorrectas);
                    refrescarPregunta();
                    leerPreguntas(getResources().getString(R.string.nivel1));
                } else {
                    correcta.setTextColor(getResources().getColor(R.color.rojo));
                    correcta.setText(getResources().getString(R.string.incorrecta));
                    numeroVidas--;
                    //TextView vidas = (TextView) findViewById(R.id.vidas);
                    //vidas.setText("" + numeroVidas);
                    refrescarVidas();
                    if (numeroVidas == 0) {
                        Intent ultimo = new Intent(this,Final.class);
                        ultimo.putExtra(RESULTADO, getResources().getString(R.string.muerto));
                        startActivity(ultimo);
                    } else {
                        leerPreguntas(getResources().getString(R.string.nivel1));
                    }
                }
                break;
            case R.id.botonOpcion3:
                Button btOpcion3 = (Button) findViewById(R.id.botonOpcion3);
                if (opcionCorrecta.equals(btOpcion3.getText())) {
                    correcta.setTextColor(getResources().getColor(R.color.negro));
                    correcta.setText(getResources().getString(R.string.correcta));
                    preguntasCorrectas++;
//                    TextView preguntasCorrec = (TextView) findViewById(R.id.numeroPreguntasCorrectas);
                    //                  preguntasCorrec.setText("" + preguntasCorrectas);
                    refrescarPregunta();
                    leerPreguntas(getResources().getString(R.string.nivel1));
                } else {
                    correcta.setTextColor(getResources().getColor(R.color.rojo));
                    correcta.setText(getResources().getString(R.string.incorrecta));
                    numeroVidas--;
                    //TextView vidas = (TextView) findViewById(R.id.vidas);
                    //vidas.setText("" + numeroVidas);
                    refrescarVidas();
                    if (numeroVidas == 0) {
                        Intent ultimo = new Intent(this,Final.class);
                        ultimo.putExtra(RESULTADO, getResources().getString(R.string.muerto));
                        startActivity(ultimo);
                    } else {
                        leerPreguntas(getResources().getString(R.string.nivel1));
                    }
                }
                break;
            case R.id.botonOpcion4:
                Button btOpcion4 = (Button) findViewById(R.id.botonOpcion4);
                if (opcionCorrecta.equals(btOpcion4.getText())) {
                    correcta.setTextColor(getResources().getColor(R.color.negro));
                    correcta.setText(getResources().getString(R.string.correcta));
                    preguntasCorrectas++;
                    //TextView preguntasCorrec = (TextView) findViewById(R.id.numeroPreguntasCorrectas);
                    //preguntasCorrec.setText("" + preguntasCorrectas);
                    refrescarPregunta();
                    leerPreguntas(getResources().getString(R.string.nivel1));
                } else {
                    correcta.setTextColor(getResources().getColor(R.color.rojo));
                    correcta.setText(getResources().getString(R.string.incorrecta));
                    numeroVidas--;
                    //          TextView vidas = (TextView) findViewById(R.id.vidas);
                    //        vidas.setText("" + numeroVidas);
                    refrescarVidas();
                    if (numeroVidas == 0) {
                        correcta.setText(getResources().getString(R.string.muerto));
                        Intent ultimo = new Intent(this,Final.class);
                        ultimo.putExtra(RESULTADO, getResources().getString(R.string.muerto));
                        startActivity(ultimo);
                    } else {
                        leerPreguntas(getResources().getString(R.string.nivel1));
                    }
                }
                break;


        }

        preguntasRealizadas++;

        if (preguntasCorrectas == 5) {
            correcta.setText(getResources().getString(R.string.ganador));
            Intent ultimo = new Intent(this,Final.class);
            ultimo.putExtra(RESULTADO, getResources().getString(R.string.ganador));
            startActivity(ultimo);
        }

        System.out.println("Preguntas correctas " + preguntasCorrectas);
        System.out.println("Preguntas realizadas " + preguntasRealizadas);
        System.out.println("Vidas " + numeroVidas);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preguntas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refrescarVidas(){
        ImageView vidas = (ImageView)findViewById(R.id.imagenVidas);
        if (numeroVidas==3){
            vidas.setBackgroundResource(R.drawable.tresvidas);
        }else  if (numeroVidas==2){
            vidas.setBackgroundResource(R.drawable.dosvidas);
        }else if (numeroVidas==1){
            vidas.setBackgroundResource(R.drawable.unavida);
        }
    }

    public void refrescarPregunta(){
        ImageView numeroPreg =(ImageView)findViewById(R.id.imagenPreguntasCorrectas);
        if (preguntasCorrectas==1){
            numeroPreg.setBackgroundResource(R.mipmap.pgtauno);
        }else if (preguntasCorrectas==2){
            numeroPreg.setBackgroundResource(R.mipmap.pgtados);
        }else if (preguntasCorrectas==3){
            numeroPreg.setBackgroundResource(R.mipmap.pgtatres);
        }else if (preguntasCorrectas==4){
            numeroPreg.setBackgroundResource(R.mipmap.pgtacuatro);
        }else if (preguntasCorrectas==5){
            numeroPreg.setBackgroundResource(R.mipmap.pgtacinco);
        }
    }

    public void hiloTiempo() {

        thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    runOnUiThread(new Runnable() {
                        TextView segundos = (TextView) findViewById(R.id.segundos);
                        int contador = 30;

                        @Override
                        public void run() {
                            while (!segundos.getText().equals("0")) {

                                try {
                                    segundos.setText("" + contador--);
                                    Thread.sleep(500);

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        };

       /* runOnUiThread(new Runnable() {
            TextView segundos = (TextView) findViewById(R.id.segundos);
            int contador = 30;

            @Override
            public void run() {

                while(!segundos.getText().equals("0")) {
                    try {
                        segundos.setText("" + contador--);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/
    }

    protected void onStop() {
        super.onStop();
        finish();
    }
}
