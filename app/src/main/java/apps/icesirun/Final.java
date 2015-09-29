package apps.icesirun;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Final extends Activity implements OnClickListener {

    public final static String NIVEL = "apps.icesirun.nivel";

    private String resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_final);
        Intent intent = getIntent();
        resultado = intent.getStringExtra(Preguntas.RESULTADO);

        Button botonVolverJugar = (Button)findViewById(R.id.btnVolverJugar);
        botonVolverJugar.setVisibility(View.INVISIBLE);

        if (resultado.equals(getResources().getString(R.string.ganador))) {
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
            layout.setBackgroundResource(R.mipmap.ganaste);
            botonVolverJugar.setVisibility(View.VISIBLE);
        }else {
            TextView texto = (TextView)findViewById(R.id.textoFinal);
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
            layout.setBackgroundResource(R.mipmap.perdiste);
            botonVolverJugar.setVisibility(View.VISIBLE);
        }
        botonVolverJugar.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_final, menu);
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

    @Override
    public void onClick(View v) {

        Intent opciones = new Intent(this,Opciones.class);
        startActivity(opciones);
    }

    protected void onStop() {
        super.onStop();
        finish();
    }
}
